package com.sunuhelp.media.service.impl;

import com.sunuhelp.media.dto.response.MediaFileResponse;
import com.sunuhelp.media.entity.MediaFile;
import com.sunuhelp.media.enums.MediaOwnerType;
import com.sunuhelp.media.exception.FileAccessDeniedException;
import com.sunuhelp.media.exception.FileNotFoundBusinessException;
import com.sunuhelp.media.exception.FileTooLargeException;
import com.sunuhelp.media.exception.FileTypeNotAllowedException;
import com.sunuhelp.media.repository.MediaFileRepository;
import com.sunuhelp.media.service.MediaFileService;
import com.sunuhelp.media.storage.MediaStorageClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MediaFileServiceImpl implements MediaFileService {

    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "application/pdf");

    private final MediaFileRepository mediaFileRepository;
    private final MediaStorageClient storageClient;

    public MediaFileServiceImpl(MediaFileRepository mediaFileRepository, MediaStorageClient storageClient) {
        this.mediaFileRepository = mediaFileRepository;
        this.storageClient = storageClient;
    }

    @Override
    @Transactional
    public MediaFileResponse upload(MultipartFile file, MediaOwnerType ownerType, UUID ownerId,
                                     UUID uploaderAccountId) {
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new FileTooLargeException();
        }
        if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
            throw new FileTypeNotAllowedException();
        }

        MediaFile mediaFile = MediaFile.create(ownerType, ownerId, "", file.getContentType(),
                file.getSize(), uploaderAccountId);
        mediaFileRepository.saveAndFlush(mediaFile);

        mediaFile.markScanning();
        String storedFileName = storageClient.store(file, mediaFile.getId());
        mediaFile.setFileUrl(storedFileName);
        mediaFile.markReady(null);

        mediaFileRepository.save(mediaFile);

        return toResponse(mediaFile, storedFileName);
    }

    @Override
    public List<MediaFileResponse> findByOwner(MediaOwnerType ownerType, UUID ownerId) {
        return mediaFileRepository.findByOwnerTypeAndOwnerIdAndActiveTrueOrderByDisplayOrder(ownerType, ownerId)
                .stream()
                .map(f -> toResponse(f, f.getFileUrl()))
                .toList();
    }

    @Override
    public byte[] download(UUID mediaFileId, UUID requesterAccountId, boolean isAdmin) {
        MediaFile mediaFile = mediaFileRepository.findById(mediaFileId)
                .orElseThrow(FileNotFoundBusinessException::new);

        if (mediaFile.requiresSignedUrl()
                && !isAdmin
                && !mediaFile.getUploadedByAccountId().equals(requesterAccountId)) {
            throw new FileAccessDeniedException();
        }

        // Delegue entierement a MediaStorageClient - jamais de chemin
        // reconstruit ici (bug corrige : l'ancienne version dupliquait
        // "./media-storage" en dur, divergent du chemin absolu reellement
        // utilise par store()).
        return storageClient.read(mediaFile.getFileUrl());
    }

    private MediaFileResponse toResponse(MediaFile file, String actualFileUrl) {
        return MediaFileResponse.builder()
                .id(file.getId())
                .ownerType(file.getOwnerType())
                .ownerId(file.getOwnerId())
                .fileUrl(actualFileUrl)
                .thumbnailUrl(file.getThumbnailUrl())
                .mimeType(file.getMimeType())
                .fileSizeBytes(file.getFileSizeBytes())
                .processingStatus(file.getProcessingStatus())
                .isPublic(file.isPublic())
                .build();
    }
}

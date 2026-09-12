package com.sunuhelp.media.service;

import com.sunuhelp.media.dto.response.MediaFileResponse;
import com.sunuhelp.media.enums.MediaOwnerType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MediaFileService {

    MediaFileResponse upload(MultipartFile file, MediaOwnerType ownerType, UUID ownerId, UUID uploaderAccountId);

    List<MediaFileResponse> findByOwner(MediaOwnerType ownerType, UUID ownerId);

    /** Verifie l'acces (public, ou proprietaire/Admin pour un fichier prive) avant de renvoyer les octets. */
    byte[] download(UUID mediaFileId, UUID requesterAccountId, boolean isAdmin);
}

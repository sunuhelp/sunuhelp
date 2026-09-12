package com.sunuhelp.media.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.media.enums.MediaOwnerType;
import com.sunuhelp.media.enums.ProcessingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Reference d'un fichier uploade - jamais le contenu binaire lui-meme,
 * qui vit sur le stockage physique (MediaStorageClient). Une seule table
 * polymorphe pour les 5 usages (logo, photo, document, avatar) plutot que
 * 5 tables identiques dans leur structure.
 */
@Entity
@Table(name = "media_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaFile extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private MediaOwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "file_size_bytes", nullable = false)
    private long fileSizeBytes;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false)
    private ProcessingStatus processingStatus;

    /**
     * Securite explicite en donnee, pas en logique dispersee : un document
     * justificatif n'est jamais public, un logo l'est toujours - fixe a la
     * creation selon ownerType, verifiable directement en base.
     */
    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "uploaded_by_account_id", nullable = false)
    private UUID uploadedByAccountId;

    public static MediaFile create(MediaOwnerType ownerType, UUID ownerId, String fileUrl,
                                    String mimeType, long fileSizeBytes, UUID uploadedByAccountId) {
        MediaFile file = new MediaFile();
        file.ownerType = ownerType;
        file.ownerId = ownerId;
        file.fileUrl = fileUrl;
        file.mimeType = mimeType;
        file.fileSizeBytes = fileSizeBytes;
        file.displayOrder = 0;
        file.processingStatus = ProcessingStatus.PENDING;
        file.isPublic = ownerType != MediaOwnerType.VERIFICATION_DOCUMENT;
        file.uploadedByAccountId = uploadedByAccountId;
        return file;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void markScanning() {
        this.processingStatus = ProcessingStatus.SCANNING;
    }

    public void markReady(String thumbnailUrl) {
        this.processingStatus = ProcessingStatus.READY;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void markRejected() {
        this.processingStatus = ProcessingStatus.REJECTED;
    }

    public void markFailed() {
        this.processingStatus = ProcessingStatus.FAILED;
    }

    public boolean isImage() {
        return mimeType != null && mimeType.startsWith("image/");
    }

    /** Un document prive necessite une URL signee/temporaire, jamais un lien direct permanent. */
    public boolean requiresSignedUrl() {
        return !isPublic;
    }
}

package com.sunuhelp.media.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Abstraction du stockage physique des fichiers - le reste du service ne
 * connait jamais l'implementation reelle (disque local aujourd'hui, cloud
 * potentiellement demain). Seule LocalDiskStorageClient sait ou et comment
 * les fichiers sont physiquement ecrits/lus - jamais reconstruit ailleurs.
 */
public interface MediaStorageClient {

    String store(MultipartFile file, UUID mediaFileId);

    byte[] read(String fileUrl);

    void delete(String fileUrl);
}

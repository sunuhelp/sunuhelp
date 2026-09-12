package com.sunuhelp.media.repository;

import com.sunuhelp.media.entity.MediaFile;
import com.sunuhelp.media.enums.MediaOwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaFileRepository extends JpaRepository<MediaFile, UUID> {
    List<MediaFile> findByOwnerTypeAndOwnerIdAndActiveTrueOrderByDisplayOrder(
            MediaOwnerType ownerType, UUID ownerId);
}

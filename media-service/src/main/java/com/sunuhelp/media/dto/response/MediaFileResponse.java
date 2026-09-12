package com.sunuhelp.media.dto.response;

import com.sunuhelp.media.enums.MediaOwnerType;
import com.sunuhelp.media.enums.ProcessingStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MediaFileResponse {
    private UUID id;
    private MediaOwnerType ownerType;
    private UUID ownerId;
    private String fileUrl;
    private String thumbnailUrl;
    private String mimeType;
    private long fileSizeBytes;
    private ProcessingStatus processingStatus;
    private boolean isPublic;
}

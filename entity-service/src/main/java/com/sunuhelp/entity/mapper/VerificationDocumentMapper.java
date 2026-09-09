package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.dto.response.VerificationDocumentResponse;
import com.sunuhelp.entity.entity.VerificationDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VerificationDocumentMapper {
    VerificationDocumentResponse toResponse(VerificationDocument document);
}

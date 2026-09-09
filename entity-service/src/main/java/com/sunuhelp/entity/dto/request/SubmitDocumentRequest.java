package com.sunuhelp.entity.dto.request;

import com.sunuhelp.entity.enums.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitDocumentRequest {

    @NotNull
    private DocumentType documentType;

    /** Facultatif meme si le type est choisi. */
    private String documentNumber;
}

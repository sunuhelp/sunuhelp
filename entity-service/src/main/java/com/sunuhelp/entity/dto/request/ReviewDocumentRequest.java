package com.sunuhelp.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** approved=true ignore rejectionReason ; approved=false l'exige cote service. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDocumentRequest {

    @NotNull
    private boolean approved;

    private String rejectionReason;
}

package com.sunuhelp.entity.dto.request;

import com.sunuhelp.entity.enums.Availability;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/** price facultatif - certains services n'ont pas de tarif fixe (ex. "sur devis"). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferRequest {

    private BigDecimal price;

    private Availability availability;

    @NotEmpty
    @Valid
    private List<OfferTranslationRequest> translations;
}

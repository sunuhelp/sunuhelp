package com.sunuhelp.geo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Mapping partiel de la reponse Nominatim - seuls les champs utilises.
 * place_rank (pas importance) sert de signal de fiabilite : plus il est
 * eleve, plus le resultat est precis (batiment ~30) plutot que large
 * (pays ~4, ville ~16) - importance mesure la notoriete, pas la precision
 * geographique, ce qui etait notre erreur initiale.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimResult {
    private String lat;
    private String lon;

    @JsonProperty("place_rank")
    private Integer placeRank;
}

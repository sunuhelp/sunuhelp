package com.sunuhelp.auth.mapper;

import com.sunuhelp.auth.dto.response.AccountResponse;
import com.sunuhelp.auth.entity.Account;
import org.mapstruct.Mapper;

/**
 * Conversion Account (entite) vers AccountResponse (DTO). MapStruct genere
 * l'implementation a la compilation par correspondance de noms de champs -
 * aucun mapping manuel a ecrire ici tant que les noms correspondent.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
}

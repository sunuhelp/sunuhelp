package com.sunuhelp.auth.repository;

import com.sunuhelp.auth.entity.OtpCode;
import com.sunuhelp.auth.enums.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    /**
     * Recupere le dernier code non utilise pour un compte et un usage
     * donnes - c'est celui-la qu'on compare a la saisie de l'utilisateur.
     */
    Optional<OtpCode> findFirstByAccountIdAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(
            UUID accountId, OtpType otpType);

    /** Nombre de codes generes recemment pour un compte - detection d'abus. */
    long countByAccountIdAndOtpTypeAndCreatedAtAfter(
            UUID accountId, OtpType otpType, java.time.LocalDateTime since);
}

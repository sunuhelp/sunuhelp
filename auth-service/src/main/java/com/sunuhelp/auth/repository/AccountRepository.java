package com.sunuhelp.auth.repository;

import com.sunuhelp.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    /** Retrouve un compte a partir de son identifiant de connexion. */
    Optional<Account> findByPhoneNumber(String phoneNumber);

    /** Verification rapide avant inscription, sans charger l'entite entiere. */
    boolean existsByPhoneNumber(String phoneNumber);
}

package com.sunuhelp.auth.config;

import com.sunuhelp.auth.entity.Account;
import com.sunuhelp.auth.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cree des comptes de demonstration (1 Admin, 3 Usagers) au demarrage -
 * uniquement quand le profil "dev" est actif, jamais en production.
 * Idempotent : verifie l'existence avant de creer, relancer le service
 * plusieurs fois ne cree jamais de doublons.
 */
@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataInitializer.class);
    private static final String DEV_PASSWORD = "DevPass123";

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createIfMissing("+221770000010", "admin@sunuhelp.sn", true);
        createIfMissing("+221770000011", "usager1@sunuhelp.sn", false);
        createIfMissing("+221770000012", "usager2@sunuhelp.sn", false);
        createIfMissing("+221770000013", "usager3@sunuhelp.sn", false);

        log.warn("=== Comptes de demonstration prets (mot de passe pour tous : {}) ===", DEV_PASSWORD);
        log.warn("Admin   : +221770000010");
        log.warn("Usager1 : +221770000011");
        log.warn("Usager2 : +221770000012");
        log.warn("Usager3 : +221770000013");
    }

    private void createIfMissing(String phoneNumber, String email, boolean isAdmin) {
        if (accountRepository.existsByPhoneNumber(phoneNumber)) {
            return;
        }

        Account account = Account.register(phoneNumber, email, passwordEncoder.encode(DEV_PASSWORD));
        account.activate();
        if (isAdmin) {
            account.promoteToAdmin();
        }
        accountRepository.save(account);
    }
}

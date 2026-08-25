package com.sunuhelp.auth.service.impl;

import com.sunuhelp.auth.config.OtpProperties;
import com.sunuhelp.auth.entity.Account;
import com.sunuhelp.auth.entity.OtpCode;
import com.sunuhelp.auth.enums.OtpType;
import com.sunuhelp.auth.event.EventProducer;
import com.sunuhelp.auth.event.OtpRequestedEvent;
import com.sunuhelp.auth.exception.InvalidOtpException;
import com.sunuhelp.auth.exception.OtpExpiredException;
import com.sunuhelp.auth.exception.OtpMaxAttemptsReachedException;
import com.sunuhelp.auth.repository.OtpCodeRepository;
import com.sunuhelp.auth.service.OtpService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {


    private final OtpCodeRepository otpCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventProducer eventProducer;
    private final OtpProperties otpProperties;

    public OtpServiceImpl(OtpCodeRepository otpCodeRepository, PasswordEncoder passwordEncoder,
                           EventProducer eventProducer, OtpProperties otpProperties) {
        this.otpCodeRepository = otpCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventProducer = eventProducer;
        this.otpProperties = otpProperties;
    }

    @Override
    @Transactional
    public void generateAndSend(Account account, OtpType otpType) {
        String rawCode = generateSixDigitCode();
        String codeHash = passwordEncoder.encode(rawCode);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(otpProperties.getExpirationMinutes());

        OtpCode otp = OtpCode.generate(account.getId(), codeHash, otpType, expiresAt);
        otpCodeRepository.save(otp);

        eventProducer.publish(OtpRequestedEvent.of(
                account.getId(), account.getPhoneNumber(), otpType, rawCode));
    }

    @Override
    @Transactional
    public void verify(Account account, OtpType otpType, String rawCode) {
        OtpCode otp = otpCodeRepository
                .findFirstByAccountIdAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(account.getId(), otpType)
                .orElseThrow(InvalidOtpException::new);

        if (otp.isExpired()) {
            throw new OtpExpiredException();
        }
        if (otp.getAttempts() >= otpProperties.getMaxAttempts()) {
            throw new OtpMaxAttemptsReachedException();
        }
        if (!passwordEncoder.matches(rawCode, otp.getCodeHash())) {
            otp.incrementAttempts();
            otpCodeRepository.save(otp);
            throw new InvalidOtpException();
        }

        otp.markUsed();
        otpCodeRepository.save(otp);
    }

    /** Code a 6 chiffres, genere de facon cryptographiquement sure. */
    private String generateSixDigitCode() {
        int code = new SecureRandom().nextInt(1_000_000);
        return String.format("%06d", code);
    }
}

package com.sunuhelp.auth.service.impl;

import com.sunuhelp.common.security.JwtProperties;
import com.sunuhelp.auth.config.JwtIssuanceProperties;
import com.sunuhelp.auth.dto.request.LoginRequest;
import com.sunuhelp.auth.dto.request.RefreshTokenRequest;
import com.sunuhelp.auth.dto.request.RegisterRequest;
import com.sunuhelp.auth.dto.request.VerifyOtpRequest;
import com.sunuhelp.auth.dto.response.AccountResponse;
import com.sunuhelp.auth.dto.response.TokenResponse;
import com.sunuhelp.auth.entity.Account;
import com.sunuhelp.auth.entity.LoginAttempt;
import com.sunuhelp.auth.entity.RefreshToken;
import com.sunuhelp.auth.enums.OtpType;
import com.sunuhelp.auth.exception.AccountAlreadyExistsException;
import com.sunuhelp.auth.exception.AccountNotVerifiedException;
import com.sunuhelp.auth.exception.AccountSuspendedException;
import com.sunuhelp.auth.exception.InvalidCredentialsException;
import com.sunuhelp.auth.exception.InvalidRefreshTokenException;
import com.sunuhelp.auth.mapper.AccountMapper;
import com.sunuhelp.auth.repository.AccountRepository;
import com.sunuhelp.auth.repository.LoginAttemptRepository;
import com.sunuhelp.auth.repository.RefreshTokenRepository;
import com.sunuhelp.auth.security.jwt.JwtTokenProvider;
import com.sunuhelp.auth.service.AuthService;
import com.sunuhelp.auth.security.jwt.TokenHasher;
import com.sunuhelp.auth.service.OtpService;
import com.sunuhelp.common.exception.ResourceNotFoundException;
import com.sunuhelp.auth.i18n.MessageKeys;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtIssuanceProperties jwtIssuanceProperties;
    private final AccountMapper accountMapper;
    private final OtpService otpService;
    private final TokenHasher tokenHasher;

    public AuthServiceImpl(AccountRepository accountRepository,
                            RefreshTokenRepository refreshTokenRepository,
                            LoginAttemptRepository loginAttemptRepository,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider,
                            JwtIssuanceProperties jwtIssuanceProperties,
                            AccountMapper accountMapper,
                            OtpService otpService,
                            TokenHasher tokenHasher) {
        this.accountRepository = accountRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtIssuanceProperties = jwtIssuanceProperties;
        this.accountMapper = accountMapper;
        this.otpService = otpService;
        this.tokenHasher = tokenHasher;
    }

    @Override
    @Transactional
    public AccountResponse register(RegisterRequest request) {
        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AccountAlreadyExistsException();
        }

        Account account = Account.register(
                request.getPhoneNumber(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        accountRepository.saveAndFlush(account);

        otpService.generateAndSend(account, OtpType.REGISTRATION);

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional
    public void verifyOtp(VerifyOtpRequest request) {
        Account account = accountRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.ACCOUNT_NOT_FOUND));

        otpService.verify(account, request.getOtpType(), request.getCode());

        if (request.getOtpType() == OtpType.REGISTRATION) {
            account.activate();
            accountRepository.save(account);
        }
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request, String ipAddress, String userAgent) {
        Account account = accountRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);

        boolean success = account != null
                && passwordEncoder.matches(request.getPassword(), account.getPasswordHash());

        loginAttemptRepository.save(LoginAttempt.record(
                account != null ? account.getId() : null,
                request.getPhoneNumber(), success, ipAddress, userAgent));

        if (account == null || !success) {
            throw new InvalidCredentialsException();
        }
        if (!account.isPhoneVerified()) {
            throw new AccountNotVerifiedException();
        }
        if (account.getAccountStatus() == com.sunuhelp.auth.enums.AccountStatus.SUSPENDED) {
            throw new AccountSuspendedException();
        }

        return issueTokens(account, userAgent);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String hash = tokenHasher.hash(request.getRefreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHash(hash)
                .filter(RefreshToken::isValid)
                .orElseThrow(InvalidRefreshTokenException::new);

        Account account = accountRepository.findById(token.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.ACCOUNT_NOT_FOUND));

        token.revoke();
        refreshTokenRepository.save(token);

        return issueTokens(account, null);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        String hash = tokenHasher.hash(refreshToken);
        refreshTokenRepository.findByTokenHash(hash).ifPresent(token -> {
            token.revoke();
            refreshTokenRepository.save(token);
        });
    }

    /** Emet un nouveau couple access/refresh token pour un compte deja authentifie. */
    private TokenResponse issueTokens(Account account, String userAgent) {
        String accessToken = jwtTokenProvider.generateAccessToken(account.getId(), account.getRole());
        String rawRefreshToken = jwtTokenProvider.generateOpaqueRefreshToken();

        RefreshToken refreshToken = RefreshToken.issue(
                account.getId(),
                tokenHasher.hash(rawRefreshToken),
                LocalDateTime.now().plusDays(jwtIssuanceProperties.getRefreshTokenExpirationDays()),
                userAgent);
        refreshTokenRepository.save(refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(rawRefreshToken)
                .expiresIn(jwtIssuanceProperties.getAccessTokenExpirationMinutes() * 60)
                .build();
    }
}

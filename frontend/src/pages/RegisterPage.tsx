import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useTranslation } from 'react-i18next'
import { useNavigate, Link } from 'react-router-dom'
import { isAxiosError } from 'axios'
import { registerSchema, type RegisterFormData } from '../schemas/auth'
import { register as registerAccount, verifyOtp, login } from '../api/auth'
import { useAuthStore } from '../stores/authStore'
import { OtpInput } from '../components/ui/OtpInput'
import { useCountdown } from '../hooks/useCountdown'
import { notify } from '../lib/toast'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'

type Step = 'form' | 'otp'

export default function RegisterPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const setTokens = useAuthStore((s) => s.setTokens)

  const [step, setStep] = useState<Step>('form')
  const [otpCode, setOtpCode] = useState('')
  const [otpError, setOtpError] = useState(false)
  const [isVerifying, setIsVerifying] = useState(false)
  const [isResending, setIsResending] = useState(false)
  const countdown = useCountdown(60)

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    mode: 'onBlur',
    reValidateMode: 'onChange',
  })

  const phoneNumber = watch('phoneNumber')
  const password = watch('password')

  const onSubmit = async (data: RegisterFormData) => {
    try {
      await registerAccount({
        phoneNumber: data.phoneNumber,
        email: data.email || undefined,
        password: data.password,
      })
      notify.success(t('auth.register_success'))
      setStep('otp')
      countdown.reset()
    } catch (err) {
      if (isAxiosError(err) && err.response?.status === 409) {
        notify.error(t('errors.phone_already_exists'))
      } else {
        notify.error(t('errors.generic'))
      }
    }
  }

  const handleVerify = async () => {
    if (otpCode.length !== 6) {
      setOtpError(true)
      return
    }
    setIsVerifying(true)
    setOtpError(false)
    try {
      await verifyOtp(phoneNumber, otpCode)
      const tokens = await login(phoneNumber, password)
      setTokens(tokens.accessToken, tokens.refreshToken)
      notify.success(t('auth.welcome_back'))
      navigate('/')
    } catch {
      setOtpError(true)
      setOtpCode('')
      notify.error(t('errors.generic'))
    } finally {
      setIsVerifying(false)
    }
  }

  const handleResend = async () => {
    setIsResending(true)
    try {
      await registerAccount({ phoneNumber, password })
      notify.info(t('auth.otp_resent'))
      countdown.reset()
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsResending(false)
    }
  }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 flex items-center justify-center px-6 py-10">
        <div className="w-full max-w-md bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl shadow-sm p-8">
          {step === 'form' ? (
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
              <div className="text-center mb-2">
                <h1 className="text-xl font-medium">{t('auth.register')}</h1>
                <p className="text-sm text-[var(--color-ink-muted)] mt-1">{t('auth.register_tagline')}</p>
              </div>

              <div>
                <label className="text-sm font-medium block mb-1.5">{t('auth.phone_label')}</label>
                <div className="relative">
                  <svg className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--color-ink-muted)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                  </svg>
                  <input
                    {...register('phoneNumber')}
                    type="tel"
                    placeholder="+221 77 123 45 67"
                    className={`w-full h-12 pl-10 pr-4 rounded-lg border bg-[var(--color-bg)] outline-none transition-colors ${
                      errors.phoneNumber ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'
                    }`}
                  />
                </div>
                {errors.phoneNumber && (
                  <p className="text-sm text-[var(--color-danger)] mt-1.5">{t(errors.phoneNumber.message!)}</p>
                )}
              </div>

              <div>
                <label className="text-sm font-medium block mb-1.5">{t('auth.email_label')}</label>
                <div className="relative">
                  <svg className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--color-ink-muted)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                  </svg>
                  <input
                    {...register('email')}
                    type="email"
                    className={`w-full h-12 pl-10 pr-4 rounded-lg border bg-[var(--color-bg)] outline-none transition-colors ${
                      errors.email ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'
                    }`}
                  />
                </div>
                {errors.email && (
                  <p className="text-sm text-[var(--color-danger)] mt-1.5">{t(errors.email.message!)}</p>
                )}
              </div>

              <div>
                <label className="text-sm font-medium block mb-1.5">{t('auth.password_label')}</label>
                <div className="relative">
                  <svg className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--color-ink-muted)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                  <input
                    {...register('password')}
                    type="password"
                    className={`w-full h-12 pl-10 pr-4 rounded-lg border bg-[var(--color-bg)] outline-none transition-colors ${
                      errors.password ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'
                    }`}
                  />
                </div>
                {errors.password && (
                  <p className="text-sm text-[var(--color-danger)] mt-1.5">{t(errors.password.message!)}</p>
                )}
              </div>

              <button
                type="submit"
                disabled={isSubmitting}
                className="w-full h-12 rounded-lg bg-[var(--color-accent)] text-white font-medium disabled:opacity-60 transition-opacity flex items-center justify-center gap-2"
              >
                {isSubmitting && (
                  <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                  </svg>
                )}
                {isSubmitting ? t('auth.register_submitting') : t('auth.register_submit')}
              </button>

              <p className="text-sm text-center text-[var(--color-ink-muted)]">
                {t('auth.already_have_account')}{' '}
                <Link to="/connexion" className="text-[var(--color-accent)] font-medium">
                  {t('auth.login')}
                </Link>
              </p>
            </form>
          ) : (
            <div className="text-center">
              <h1 className="text-xl font-medium mb-2">{t('auth.otp_title')}</h1>
              <p className="text-sm text-[var(--color-ink-muted)] mb-6">
                {t('auth.otp_subtitle', { phone: phoneNumber })}
              </p>

              <OtpInput value={otpCode} onChange={(v) => { setOtpCode(v); setOtpError(false) }} error={otpError} />

              <button
                onClick={handleVerify}
                disabled={isVerifying}
                className="w-full h-12 mt-6 rounded-lg bg-[var(--color-accent)] text-white font-medium disabled:opacity-60 transition-opacity"
              >
                {isVerifying ? t('auth.otp_verifying') : t('auth.otp_verify')}
              </button>

              <button
                onClick={handleResend}
                disabled={countdown.isActive || isResending}
                className="text-sm text-[var(--color-accent)] mt-4 disabled:text-[var(--color-ink-muted)]"
              >
                {countdown.isActive
                  ? t('auth.otp_resend_countdown', { time: countdown.format() })
                  : t('auth.otp_resend')}
              </button>
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  )
}

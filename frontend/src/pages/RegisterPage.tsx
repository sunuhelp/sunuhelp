import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useTranslation } from 'react-i18next'
import { useNavigate, Link } from 'react-router-dom'
import { isAxiosError } from 'axios'
import { Mail, Lock, Eye, EyeOff, Check } from 'lucide-react'
import { registerSchema, type RegisterFormData } from '../schemas/auth'
import { register as registerAccount, verifyOtp, login } from '../api/auth'
import { useAuthStore } from '../stores/authStore'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { PhoneInput } from '../components/PhoneInput'
import { OtpInput } from '../components/OtpInput'
import { useCountdown } from '../hooks/useCountdown'
import { notify } from '../lib/toast'

type Step = 'form' | 'otp'

export default function RegisterPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const setTokens = useAuthStore((s) => s.setTokens)

  const [step, setStep] = useState<Step>('form')
  const [showPassword, setShowPassword] = useState(false)
  const [otpCode, setOtpCode] = useState('')
  const [otpError, setOtpError] = useState(false)
  const [isVerifying, setIsVerifying] = useState(false)
  const [isResending, setIsResending] = useState(false)
  const countdown = useCountdown(60)

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    mode: 'onBlur',
    reValidateMode: 'onChange',
    defaultValues: { phoneDigits: '' },
  })

  const phoneDigits = watch('phoneDigits')
  const password = watch('password') ?? ''
  const passwordValid = password.length >= 8
  const fullPhoneNumber = `+221${phoneDigits}`

  const onSubmit = async (data: RegisterFormData) => {
    try {
      await registerAccount({ phoneNumber: `+221${data.phoneDigits}`, email: data.email || undefined, password: data.password })
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
    if (otpCode.length !== 6) { setOtpError(true); return }
    setIsVerifying(true)
    setOtpError(false)
    try {
      await verifyOtp(fullPhoneNumber, otpCode)
      const tokens = await login(fullPhoneNumber, password)
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
      await registerAccount({ phoneNumber: fullPhoneNumber, password })
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

      <main className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-xl bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl p-8 sm:p-10">
          {step === 'form' ? (
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              <div className="text-center mb-2">
                <h1 className="text-xl font-medium">{t('auth.register')}</h1>
                <p className="text-sm text-[var(--color-ink-muted)] mt-1">{t('auth.register_tagline')}</p>
              </div>

              <div>
                <label className="text-sm font-medium block mb-1.5">{t('auth.phone_label')}</label>
                <PhoneInput value={phoneDigits} onChange={(v) => setValue('phoneDigits', v, { shouldValidate: true })} error={!!errors.phoneDigits} />
                <p className={`text-xs mt-1.5 ${phoneDigits.length === 9 ? 'text-[var(--color-success)]' : 'text-[var(--color-ink-muted)]'}`}>
                  {phoneDigits.length}/9 {t('auth.digits')}
                </p>
              </div>

              <div>
                <label className="text-sm font-medium block mb-1.5">{t('auth.email_label')}</label>
                <div className={`flex items-center gap-2.5 h-12 rounded-lg border px-3.5 transition-colors ${errors.email ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus-within:border-[var(--color-accent)]'}`}>
                  <Mail size={16} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
                  <input {...register('email')} type="email" placeholder="nom@exemple.com" className="flex-1 bg-transparent outline-none text-sm" />
                </div>
                {errors.email && <p className="text-sm text-[var(--color-danger)] mt-1.5">{t(errors.email.message!)}</p>}
              </div>

              <div>
                <label className="text-sm font-medium block mb-1.5">{t('auth.password_label')}</label>
                <div className={`flex items-center gap-2.5 h-12 rounded-lg border px-3.5 transition-colors ${errors.password ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus-within:border-[var(--color-accent)]'}`}>
                  <Lock size={16} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
                  <input {...register('password')} type={showPassword ? 'text' : 'password'} className="flex-1 bg-transparent outline-none text-sm" />
                  <button type="button" onClick={() => setShowPassword((v) => !v)} aria-label={showPassword ? 'Masquer' : 'Afficher'}>
                    {showPassword ? <EyeOff size={16} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} /> : <Eye size={16} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} />}
                  </button>
                </div>
                <div className="flex items-center gap-1.5 mt-1.5">
                  {passwordValid && <Check size={13} className="text-[var(--color-success)]" strokeWidth={2} />}
                  <span className={`text-xs ${passwordValid ? 'text-[var(--color-success)]' : 'text-[var(--color-ink-muted)]'}`}>
                    {t('auth.password_requirement')}
                  </span>
                </div>
              </div>

              <button
                type="submit"
                disabled={isSubmitting}
                className="w-full h-12 rounded-lg bg-[var(--color-accent)] text-white font-medium disabled:opacity-60 transition-opacity"
              >
                {isSubmitting ? t('auth.register_submitting') : t('auth.register_submit')}
              </button>

              <p className="text-sm text-center text-[var(--color-ink-muted)]">
                {t('auth.already_have_account')} <Link to="/connexion" className="text-[var(--color-accent)] font-medium">{t('auth.login')}</Link>
              </p>
            </form>
          ) : (
            <div className="text-center">
              <h1 className="text-xl font-medium mb-2">{t('auth.otp_title')}</h1>
              <p className="text-sm text-[var(--color-ink-muted)] mb-6">{t('auth.otp_subtitle', { phone: fullPhoneNumber })}</p>

              <OtpInput value={otpCode} onChange={(v) => { setOtpCode(v); setOtpError(false) }} error={otpError} />

              <button
                onClick={handleVerify}
                disabled={isVerifying}
                className="w-full h-12 mt-6 rounded-lg bg-[var(--color-accent)] text-white font-medium disabled:opacity-60"
              >
                {isVerifying ? t('auth.otp_verifying') : t('auth.otp_verify')}
              </button>

              <button
                onClick={handleResend}
                disabled={countdown.isActive || isResending}
                className="text-sm text-[var(--color-accent)] mt-4 disabled:text-[var(--color-ink-muted)]"
              >
                {countdown.isActive ? t('auth.otp_resend_countdown', { time: countdown.format() }) : t('auth.otp_resend')}
              </button>
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  )
}

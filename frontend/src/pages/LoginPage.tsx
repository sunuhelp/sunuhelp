import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useTranslation } from 'react-i18next'
import { useNavigate, Link } from 'react-router-dom'
import { isAxiosError } from 'axios'
import { login } from '../api/auth'
import { useAuthStore } from '../stores/authStore'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { notify } from '../lib/toast'

const loginSchema = z.object({
  phoneNumber: z.string().min(1, 'validation.phone_required'),
  password: z.string().min(1, 'validation.password_required'),
})
type LoginFormData = z.infer<typeof loginSchema>

export default function LoginPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const setTokens = useAuthStore((s) => s.setTokens)

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({ resolver: zodResolver(loginSchema), mode: 'onBlur' })

  const onSubmit = async (data: LoginFormData) => {
    try {
      const tokens = await login(data.phoneNumber, data.password)
      setTokens(tokens.accessToken, tokens.refreshToken)
      notify.success(t('auth.welcome_back'))
      navigate('/')
    } catch (err) {
      if (isAxiosError(err) && (err.response?.status === 401 || err.response?.status === 403)) {
        notify.error(t('errors.invalid_credentials'))
      } else {
        notify.error(t('errors.generic'))
      }
    }
  }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 flex items-center justify-center px-6 py-10">
        <div className="w-full max-w-md bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl shadow-sm p-8">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            <div className="text-center mb-2">
              <h1 className="text-xl font-medium">{t('auth.login')}</h1>
              <p className="text-sm text-[var(--color-ink-muted)] mt-1">{t('auth.login_tagline')}</p>
            </div>

            <div>
              <label className="text-sm font-medium block mb-1.5">{t('auth.phone_label')}</label>
              <input
                {...register('phoneNumber')}
                type="tel"
                placeholder="+221 77 123 45 67"
                className={`w-full h-12 px-4 rounded-lg border bg-[var(--color-bg)] outline-none transition-colors ${
                  errors.phoneNumber ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'
                }`}
              />
              {errors.phoneNumber && (
                <p className="text-sm text-[var(--color-danger)] mt-1.5">{t(errors.phoneNumber.message!)}</p>
              )}
            </div>

            <div>
              <label className="text-sm font-medium block mb-1.5">{t('auth.password_label')}</label>
              <input
                {...register('password')}
                type="password"
                className={`w-full h-12 px-4 rounded-lg border bg-[var(--color-bg)] outline-none transition-colors ${
                  errors.password ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'
                }`}
              />
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
              {isSubmitting ? t('auth.login_submitting') : t('auth.login')}
            </button>

            <p className="text-sm text-center text-[var(--color-ink-muted)]">
              {t('auth.no_account')}{' '}
              <Link to="/inscription" className="text-[var(--color-accent)] font-medium">
                {t('auth.register')}
              </Link>
            </p>
          </form>
        </div>
      </main>

      <Footer />
    </div>
  )
}

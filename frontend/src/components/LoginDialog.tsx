import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useTranslation } from 'react-i18next'
import { isAxiosError } from 'axios'
import { Lock, Eye, EyeOff } from 'lucide-react'
import { login } from '../api/auth'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../stores/authStore'
import { notify } from '../lib/toast'
import { Dialog } from './Dialog'
import { PhoneInput } from './PhoneInput'

const loginSchema = z.object({
  phoneDigits: z.string().length(9, 'validation.phone_incomplete'),
  password: z.string().min(1, 'validation.password_required'),
})
type LoginFormData = z.infer<typeof loginSchema>

interface LoginDialogProps {
  open: boolean
  onClose: () => void
  onSwitchToRegister: () => void
}

export function LoginDialog({ open, onClose, onSwitchToRegister }: LoginDialogProps) {
  const { t } = useTranslation()
  const setTokens = useAuthStore((s) => s.setTokens)
  const navigate = useNavigate()
  const [showPassword, setShowPassword] = useState(false)

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({ resolver: zodResolver(loginSchema), defaultValues: { phoneDigits: '' } })

  const phoneDigits = watch('phoneDigits')

  const onSubmit = async (data: LoginFormData) => {
    try {
      const tokens = await login(`+221${data.phoneDigits}`, data.password)
      setTokens(tokens.accessToken, tokens.refreshToken, `+221${data.phoneDigits}`)
      notify.success(t('auth.welcome_back'))
      reset()
      onClose()
      navigate('/dashboard')
    } catch (err) {
      if (isAxiosError(err) && (err.response?.status === 401 || err.response?.status === 403)) {
        notify.error(t('errors.invalid_credentials'))
      } else {
        notify.error(t('errors.generic'))
      }
    }
  }

  return (
    <Dialog open={open} onClose={onClose} title={t('auth.login')}>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="text-sm font-medium block mb-1.5">{t('auth.phone_label')}</label>
          <PhoneInput value={phoneDigits} onChange={(v) => setValue('phoneDigits', v, { shouldValidate: true })} error={!!errors.phoneDigits} />
        </div>

        <div>
          <label className="text-sm font-medium block mb-1.5">{t('auth.password_label')}</label>
          <div className={`flex items-center gap-2.5 h-11 rounded-lg border px-3.5 transition-colors ${errors.password ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus-within:border-[var(--color-accent)]'}`}>
            <Lock size={16} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
            <input {...register('password')} type={showPassword ? 'text' : 'password'} className="flex-1 bg-transparent outline-none text-sm" />
            <button type="button" onClick={() => setShowPassword((v) => !v)} aria-label={showPassword ? 'Masquer' : 'Afficher'}>
              {showPassword ? <EyeOff size={16} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} /> : <Eye size={16} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} />}
            </button>
          </div>
        </div>

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full h-11 rounded-lg bg-[var(--color-accent)] text-white font-medium disabled:opacity-60 transition-opacity"
        >
          {isSubmitting ? t('auth.login_submitting') : t('auth.login')}
        </button>

        <p className="text-sm text-center text-[var(--color-ink-muted)]">
          {t('auth.no_account')}{' '}
          <button type="button" onClick={onSwitchToRegister} className="text-[var(--color-accent)] font-medium">
            {t('auth.register')}
          </button>
        </p>
      </form>
    </Dialog>
  )
}

import { useTranslation } from 'react-i18next'
import { Link } from 'react-router-dom'
import { Logo } from './ui/Logo'
import { LanguageSwitcher } from './LanguageSwitcher'
import { AccountMenu } from './AccountMenu'
import { useAuthStore } from '../stores/authStore'

export function Header() {
  const { t } = useTranslation()
  const accessToken = useAuthStore((s) => s.accessToken)

  return (
    <header className="sticky top-0 z-40 bg-[var(--color-bg)]/85 backdrop-blur-md border-b border-[var(--color-border)]">
      <div className="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Logo size={34} />
          <div className="hidden sm:block leading-tight">
            <p className="text-sm font-medium">{t('brand.title')}</p>
            <p className="text-xs text-[var(--color-ink-muted)]">{t('brand.tagline')}</p>
          </div>
        </div>

        <nav className="flex items-center gap-2">
          <LanguageSwitcher />
          {accessToken ? (
            <AccountMenu />
          ) : (
            <>
              <Link
                to="/inscription"
                className="hidden sm:block text-sm px-3 py-2 rounded-lg hover:bg-[var(--color-surface)] transition-colors"
              >
                {t('auth.register')}
              </Link>
              <Link
                to="/connexion"
                className="text-sm px-4 py-2 rounded-lg bg-[var(--color-accent)] text-white font-medium hover:opacity-90 transition-opacity"
              >
                {t('auth.login')}
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  )
}

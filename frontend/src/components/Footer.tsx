import { useTranslation } from 'react-i18next'
import { Link } from 'react-router-dom'
import { Logo } from './ui/Logo'

export function Footer() {
  const { t } = useTranslation()

  return (
    <footer className="border-t border-[var(--color-border)] bg-[var(--color-surface)] mt-auto">
      <div className="max-w-6xl mx-auto px-6 py-12 grid sm:grid-cols-3 gap-8">
        <div>
          <div className="flex items-center gap-3 mb-3">
            <Logo size={32} />
            <span className="text-sm font-medium">{t('brand.title')}</span>
          </div>
          <p className="text-sm text-[var(--color-ink-muted)] max-w-xs">{t('brand.tagline')}</p>
        </div>

        <div>
          <p className="text-sm font-medium mb-3">{t('footer.discover')}</p>
          <ul className="space-y-2 text-sm text-[var(--color-ink-muted)]">
            <li><Link to="/" className="hover:text-[var(--color-accent)] transition-colors">{t('footer.home')}</Link></li>
            <li><Link to="/categories" className="hover:text-[var(--color-accent)] transition-colors">{t('footer.categories')}</Link></li>
          </ul>
        </div>

        <div>
          <p className="text-sm font-medium mb-3">{t('footer.account')}</p>
          <ul className="space-y-2 text-sm text-[var(--color-ink-muted)]">
            <li><Link to="/inscription" className="hover:text-[var(--color-accent)] transition-colors">{t('auth.register')}</Link></li>
            <li><Link to="/connexion" className="hover:text-[var(--color-accent)] transition-colors">{t('auth.login')}</Link></li>
          </ul>
        </div>
      </div>

      <div className="border-t border-[var(--color-border)]">
        <div className="max-w-6xl mx-auto px-6 py-4 text-xs text-[var(--color-ink-muted)] text-center">
          © {new Date().getFullYear()} {t('brand.title')}
        </div>
      </div>
    </footer>
  )
}

import { useTranslation } from 'react-i18next'
import { MapPin } from 'lucide-react'
import { ThemeSwitcher } from './ThemeSwitcher'

const NAV_ITEMS = ['home', 'categories', 'how_it_works', 'for_business', 'help'] as const

export function Header() {
  const { t, i18n } = useTranslation()

  return (
    <header className="sticky top-0 z-40 bg-[var(--color-bg)]/90 backdrop-blur-md border-b border-[var(--color-border)]">
      <div className="max-w-6xl mx-auto px-7 h-16 flex items-center justify-between">
        <div className="flex items-center gap-8">
          <div className="w-8 h-8 rounded-lg bg-[var(--color-accent)] flex items-center justify-center shrink-0">
            <MapPin size={16} className="text-white" strokeWidth={2} aria-hidden="true" />
          </div>
          <nav className="hidden lg:flex items-center gap-6 text-sm">
            {NAV_ITEMS.map((key, i) => (
              <span
                key={key}
                className={i === 0 ? 'font-medium text-[var(--color-ink)]' : 'text-[var(--color-ink-muted)] hover:text-[var(--color-ink)] transition-colors cursor-pointer'}
              >
                {t(`nav.${key}`)}
              </span>
            ))}
          </nav>
        </div>

        <div className="flex items-center gap-2.5">
          <button
            onClick={() => i18n.changeLanguage(i18n.language === 'fr' ? 'en' : 'fr')}
            className="text-sm px-2 text-[var(--color-ink-muted)] hover:text-[var(--color-ink)] transition-colors"
          >
            {i18n.language === 'fr' ? 'En' : 'Fr'}
          </button>
          <ThemeSwitcher />
          <button className="text-sm px-4 py-2.5 rounded-lg border border-[var(--color-border-strong)] font-medium hover:bg-[var(--color-surface)] transition-colors">
            {t('auth.register')}
          </button>
          <button className="text-sm px-4 py-2.5 rounded-lg bg-[var(--color-accent)] text-white font-medium hover:opacity-90 transition-opacity">
            {t('auth.login')}
          </button>
        </div>
      </div>
    </header>
  )
}

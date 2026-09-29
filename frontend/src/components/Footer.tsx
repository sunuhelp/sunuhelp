import { useTranslation } from 'react-i18next'

export function Footer() {
  const { t } = useTranslation()

  return (
    <footer className="border-t border-[var(--color-border)]">
      <div className="max-w-6xl mx-auto px-7 py-6 flex items-center justify-between text-xs text-[var(--color-ink-muted)] flex-wrap gap-2">
        <span>{t('footer.discover')} · {t('nav.categories')} · {t('nav.how_it_works')}</span>
        <span>{t('footer.account')} · {t('auth.register')} · {t('auth.login')}</span>
      </div>
    </footer>
  )
}

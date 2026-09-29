import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'

export function CtaBand() {
  const { t } = useTranslation()
  const navigate = useNavigate()

  return (
    <div className="bg-[var(--color-surface)] border-y border-[var(--color-border)]">
      <div className="max-w-6xl mx-auto px-7 py-8 flex items-center justify-between gap-4 flex-wrap">
        <div>
          <p className="text-base font-medium mb-1">{t('home.cta_title')}</p>
          <p className="text-sm text-[var(--color-ink-muted)]">{t('home.cta_body')}</p>
        </div>
        <button
          onClick={() => navigate('/mon-etablissement')}
          className="px-5 py-2.5 rounded-lg bg-[var(--color-accent)] text-white text-sm font-medium hover:opacity-90 transition-opacity whitespace-nowrap"
        >
          {t('home.cta_button')}
        </button>
      </div>
    </div>
  )
}

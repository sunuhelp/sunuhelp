import { useTranslation } from 'react-i18next'
import { useLocation, useNavigate } from 'react-router-dom'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'

export default function BusinessCreatedPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { state } = useLocation() as { state?: { name?: string } }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 flex items-center justify-center px-6 py-10">
        <div className="w-full max-w-md text-center">
          <div className="w-16 h-16 mx-auto mb-5 rounded-full bg-[var(--color-accent)]/10 flex items-center justify-center animate-in zoom-in duration-300">
            <svg className="w-8 h-8 text-[var(--color-accent)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          </div>

          <h1 className="text-xl font-medium mb-2">{t('add_business.created_title')}</h1>
          <p className="text-sm text-[var(--color-ink-muted)] mb-8">
            {t('add_business.created_subtitle', { name: state?.name ?? '' })}
          </p>

          <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-xl p-5 mb-6 text-left">
            <p className="text-sm font-medium mb-1">{t('add_business.verification_prompt_title')}</p>
            <p className="text-xs text-[var(--color-ink-muted)]">{t('add_business.verification_prompt_body_soon')}</p>
          </div>

          <button
            onClick={() => navigate('/')}
            className="w-full h-12 rounded-lg bg-[var(--color-accent)] text-white font-medium"
          >
            {t('add_business.back_to_home')}
          </button>
        </div>
      </main>

      <Footer />
    </div>
  )
}

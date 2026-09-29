import { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { Search, Store } from 'lucide-react'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { useAuthStore } from '../stores/authStore'
import { useGreeting } from '../hooks/useGreeting'

export default function DashboardPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const greeting = useGreeting()
  const phoneNumber = useAuthStore((s) => s.phoneNumber)
  const [query, setQuery] = useState('')

  const lastTwoDigits = phoneNumber?.slice(-2) ?? '··'

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    navigate(`/resultats?query=${encodeURIComponent(query)}`)
  }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-xl mx-auto w-full px-6 py-10">
        <div className="flex items-center gap-3.5 bg-[var(--color-surface)] rounded-2xl p-5 mb-6">
          <div className="w-12 h-12 rounded-full bg-[var(--color-accent)] text-white flex items-center justify-center text-sm font-medium shrink-0">
            {lastTwoDigits}
          </div>
          <div>
            <p className="text-base font-medium">{greeting}</p>
            {phoneNumber && <p className="text-sm text-[var(--color-ink-muted)] mt-0.5">{phoneNumber}</p>}
          </div>
        </div>

        <form onSubmit={handleSearch} className="flex items-center gap-2.5 h-12 rounded-lg border border-[var(--color-border)] px-4 mb-8">
          <Search size={17} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder={t('dashboard.search_placeholder')}
            className="flex-1 bg-transparent outline-none text-sm placeholder:text-[var(--color-ink-muted)]"
          />
        </form>

        <p className="text-sm font-medium text-[var(--color-ink-muted)] mb-2.5">{t('dashboard.recent_searches')}</p>
        <div className="bg-[var(--color-surface)] rounded-lg p-4 text-center mb-6">
          <p className="text-xs text-[var(--color-ink-muted)]">{t('dashboard.recent_searches_empty')}</p>
        </div>

        <p className="text-sm font-medium text-[var(--color-ink-muted)] mb-2.5">{t('account.favorites')}</p>
        <div className="bg-[var(--color-surface)] rounded-lg p-4 text-center mb-8">
          <p className="text-xs text-[var(--color-ink-muted)]">{t('dashboard.favorites_empty')}</p>
        </div>

        <button
          onClick={() => navigate('/mon-etablissement')}
          className="w-full flex items-center justify-center gap-2.5 h-14 rounded-xl bg-[var(--color-accent)] text-white font-medium text-base hover:opacity-90 transition-opacity"
        >
          <Store size={19} strokeWidth={1.75} aria-hidden="true" />
          {t('dashboard.add_business_link')}
        </button>
      </main>

      <Footer />
    </div>
  )
}

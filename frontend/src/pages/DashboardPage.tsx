import { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate, Link } from 'react-router-dom'
import { Search, Store, ChevronRight } from 'lucide-react'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { useAuthStore } from '../stores/authStore'
import { useMyEntities } from '../hooks/useMyEntities'

export default function DashboardPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const phoneNumber = useAuthStore((s) => s.phoneNumber)
  const lastEntity = useAuthStore((s) => s.lastEntity)
  const { data: myEntities } = useMyEntities()
  const [query, setQuery] = useState('')

  const entityCount = myEntities?.totalElements ?? 0

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    navigate(`/resultats?query=${encodeURIComponent(query)}`)
  }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-xl mx-auto w-full px-6 py-12">
        <div className="flex items-center gap-2 mb-1.5">
          <h1 className="text-[26px] font-medium">{t('dashboard.greeting')}</h1>
          {phoneNumber && <span className="text-sm text-[var(--color-ink-muted)]">· {phoneNumber}</span>}
        </div>
        <p className="text-sm text-[var(--color-ink-muted)] mb-7">{t('dashboard.subtitle')}</p>

        <form onSubmit={handleSearch} className="flex items-center gap-2.5 h-[54px] rounded-lg border border-[var(--color-border)] px-4.5 mb-5">
          <Search size={19} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder={t('dashboard.search_placeholder')}
            className="flex-1 bg-transparent outline-none text-[15px] placeholder:text-[var(--color-ink-muted)]"
          />
        </form>

        {lastEntity && (
          <Link
            to={`/mes-etablissements/${lastEntity.id}`}
            className="flex items-center gap-3.5 p-4.5 rounded-2xl border-[1.5px] border-[var(--color-accent)] bg-[var(--color-accent)]/5 mb-3.5 hover:bg-[var(--color-accent)]/10 transition-colors"
          >
            <div className="w-11 h-11 rounded-xl bg-[var(--color-accent)] flex items-center justify-center shrink-0">
              <Store size={21} className="text-white" strokeWidth={1.75} aria-hidden="true" />
            </div>
            <div className="flex-1">
              <p className="text-xs text-[var(--color-accent)] font-medium mb-0.5">{t('dashboard.resume_hint')}</p>
              <p className="text-[15px] font-medium">{lastEntity.name}</p>
            </div>
            <ChevronRight size={19} className="text-[var(--color-accent)]" strokeWidth={1.75} aria-hidden="true" />
          </Link>
        )}

        {entityCount > 0 ? (
          <Link
            to="/mes-etablissements"
            className="flex items-center gap-3.5 p-4 rounded-2xl bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors"
          >
            <Store size={20} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
            <span className="flex-1 text-sm">{t('dashboard.entity_count', { count: entityCount })}</span>
            <span className="text-sm text-[var(--color-accent)] font-medium flex items-center gap-1 shrink-0">
              {t('dashboard.see_all')} <ChevronRight size={15} strokeWidth={1.75} aria-hidden="true" />
            </span>
          </Link>
        ) : (
          <button
            onClick={() => navigate('/mon-etablissement')}
            className="w-full flex items-center justify-center gap-2.5 h-14 rounded-xl bg-[var(--color-accent)] text-white font-medium text-base hover:opacity-90 transition-opacity"
          >
            <Store size={19} strokeWidth={1.75} aria-hidden="true" />
            {t('dashboard.add_business_link')}
          </button>
        )}
      </main>

      <Footer />
    </div>
  )
}

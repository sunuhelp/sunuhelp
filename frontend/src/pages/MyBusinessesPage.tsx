import { useTranslation } from 'react-i18next'
import { Link } from 'react-router-dom'
import { Plus, Store } from 'lucide-react'
import { useMyEntities } from '../hooks/useMyEntities'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { EntityCard } from '../components/EntityCard'

export default function MyBusinessesPage() {
  const { t } = useTranslation()
  const { data, isLoading } = useMyEntities()

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-4xl mx-auto w-full px-7 py-10">
        <h1 className="text-2xl sm:text-[26px] font-medium mb-2">{t('account.my_businesses')}</h1>
        <p className="text-sm text-[var(--color-ink-muted)] leading-relaxed max-w-xl mb-8">
          {t('my_businesses.intro')}
        </p>

        {isLoading ? (
          <div className="space-y-3.5">
            {Array.from({ length: 2 }).map((_, i) => <div key={i} className="h-28 rounded-2xl bg-[var(--color-surface)] animate-pulse" />)}
          </div>
        ) : !data?.content.length ? (
          <div className="bg-[var(--color-surface)] rounded-2xl p-12 text-center mb-6">
            <Store size={34} className="text-[var(--color-ink-muted)] mx-auto mb-3" strokeWidth={1.5} aria-hidden="true" />
            <p className="text-sm text-[var(--color-ink-muted)]">{t('my_businesses.empty')}</p>
          </div>
        ) : (
          <div className="space-y-3.5 mb-7">
            {data.content.map((entity) => (
              <EntityCard key={entity.id} id={entity.id} name={entity.name} categoryId={entity.categoryId} />
            ))}
          </div>
        )}

        <Link
          to="/mon-etablissement"
          className="flex items-center justify-center gap-2.5 p-5 rounded-2xl border-2 border-dashed border-[var(--color-border-strong)] text-[var(--color-ink-muted)] hover:border-[var(--color-accent)]/50 hover:text-[var(--color-accent)] transition-colors font-medium text-sm"
        >
          <Plus size={18} strokeWidth={1.75} aria-hidden="true" /> {t('my_businesses.add_another')}
        </Link>
      </main>

      <Footer />
    </div>
  )
}

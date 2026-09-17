import { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'

const RADIUS_OPTIONS = [5, 10, 15, 20]

export function SearchBar() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const [query, setQuery] = useState('')
  const [radiusKm, setRadiusKm] = useState(5)

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    const params = new URLSearchParams({ query, radiusKm: String(radiusKm) })
    navigate(`/resultats?${params.toString()}`)
  }

  return (
    <form onSubmit={handleSearch} className="w-full max-w-xl mx-auto">
      <div className="flex items-center gap-3 bg-[var(--color-surface)] border border-[var(--color-border)] rounded-lg px-4 h-12">
        <svg className="w-5 h-5 text-[var(--color-ink-muted)] shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-4.35-4.35M17 10.5a6.5 6.5 0 11-13 0 6.5 6.5 0 0113 0z" />
        </svg>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder={t('search.placeholder')}
          className="flex-1 bg-transparent outline-none text-sm placeholder:text-[var(--color-ink-muted)]"
        />
      </div>

      <div className="flex items-center gap-2 mt-3 flex-wrap">
        <span className="text-sm text-[var(--color-ink-muted)]">{t('search.radius')}</span>
        {RADIUS_OPTIONS.map((km) => (
          <button
            key={km}
            type="button"
            onClick={() => setRadiusKm(km)}
            className={`text-sm px-3 py-1.5 rounded-md border transition-colors ${
              radiusKm === km
                ? 'bg-[var(--color-accent)] text-white border-[var(--color-accent)]'
                : 'border-[var(--color-border)] text-[var(--color-ink)]'
            }`}
          >
            {km} km
          </button>
        ))}
      </div>
    </form>
  )
}

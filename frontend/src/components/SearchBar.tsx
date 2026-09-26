import { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { RadiusSlider } from './RadiusSlider'

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
    <form
      onSubmit={handleSearch}
      className="w-full max-w-2xl mx-auto bg-[var(--color-bg)] border border-[var(--color-border)] rounded-2xl shadow-sm p-5 sm:p-6"
    >
      <div className="flex items-center gap-3 bg-[var(--color-surface)] rounded-xl px-4 h-14">
        <svg className="w-5 h-5 text-[var(--color-ink-muted)] shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-4.35-4.35M17 10.5a6.5 6.5 0 11-13 0 6.5 6.5 0 0113 0z" />
        </svg>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder={t('search.placeholder')}
          className="flex-1 bg-transparent outline-none text-base placeholder:text-[var(--color-ink-muted)]"
        />
        <button
          type="submit"
          className="hidden sm:flex items-center px-5 h-10 rounded-lg bg-[var(--color-accent)] text-white text-sm font-medium hover:opacity-90 transition-opacity shrink-0"
        >
          {t('search.submit')}
        </button>
      </div>

      <div className="mt-5">
        <RadiusSlider value={radiusKm} onChange={setRadiusKm} />
      </div>

      <button
        type="submit"
        className="sm:hidden w-full mt-4 h-11 rounded-lg bg-[var(--color-accent)] text-white text-sm font-medium hover:opacity-90 transition-opacity"
      >
        {t('search.submit')}
      </button>
    </form>
  )
}

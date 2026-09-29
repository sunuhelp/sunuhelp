import { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { Search } from 'lucide-react'

export function SearchBar() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const [query, setQuery] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    navigate(`/resultats?query=${encodeURIComponent(query)}`)
  }

  return (
    <form onSubmit={handleSubmit} className="max-w-[420px]">
      <div className="flex items-center gap-2.5 bg-[var(--color-surface)] border border-[var(--color-border)] rounded-lg px-4 h-12">
        <Search size={18} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder={t('search.placeholder')}
          className="flex-1 bg-transparent outline-none text-sm placeholder:text-[var(--color-ink-muted)]"
        />
      </div>
      <p className="text-xs text-[var(--color-ink-muted)] mt-2">{t('home.search_hint')}</p>
    </form>
  )
}

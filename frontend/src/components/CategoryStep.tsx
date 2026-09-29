import { useMemo, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { Search, ChevronLeft } from 'lucide-react'
import { useAllCategories, type FlatCategory } from '../hooks/useAllCategories'

interface CategoryStepProps {
  onSelect: (categoryId: string, label: string) => void
}

export function CategoryStep({ onSelect }: CategoryStepProps) {
  const { t } = useTranslation()
  const { data, isLoading } = useAllCategories()
  const [query, setQuery] = useState('')
  const [expandedRoot, setExpandedRoot] = useState<FlatCategory | null>(null)

  const searchResults = useMemo(() => {
    if (!query.trim() || !data) return null
    const q = query.toLowerCase()

    // 1. Cherche d'abord au niveau racine.
    const rootMatches = data.roots.filter((r) => r.name.toLowerCase().includes(q))
    if (rootMatches.length > 0) return { type: 'root' as const, items: rootMatches }

    // 2. Sinon, descend dans les sous-categories.
    const leafMatches = data.flat.filter((c) => c.parentId !== null && c.name.toLowerCase().includes(q))
    return { type: 'leaf' as const, items: leafMatches }
  }, [query, data])

  const children = expandedRoot ? data?.flat.filter((c) => c.parentId === expandedRoot.id) : null

  if (isLoading) {
    return <div className="h-48 rounded-xl bg-[var(--color-surface)] animate-pulse" />
  }

  if (expandedRoot && !query) {
    return (
      <div>
        <button onClick={() => setExpandedRoot(null)} className="flex items-center gap-1 text-sm text-[var(--color-ink-muted)] mb-4 hover:text-[var(--color-accent)] transition-colors">
          <ChevronLeft size={15} strokeWidth={1.75} aria-hidden="true" /> {expandedRoot.name}
        </button>
        <div className="grid grid-cols-3 gap-2.5 max-h-72 overflow-y-auto pr-1">
          {children?.map((c) => (
            <button
              key={c.id}
              onClick={() => onSelect(c.id, `${expandedRoot.name} · ${c.name}`)}
              className="flex flex-col items-center gap-2 py-4 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors"
            >
              <span className="text-2xl" aria-hidden="true">{c.icon}</span>
              <span className="text-xs text-center">{c.name}</span>
            </button>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div>
      <div className="flex items-center gap-2.5 h-11 rounded-lg border border-[var(--color-border)] px-3.5 mb-4 focus-within:border-[var(--color-accent)] transition-colors">
        <Search size={16} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder={t('add_business.search_category_placeholder')}
          className="flex-1 bg-transparent outline-none text-sm"
        />
      </div>

      {query.trim() ? (
        <div className="flex flex-col gap-1.5 max-h-72 overflow-y-auto">
          {searchResults?.items.length === 0 && (
            <p className="text-sm text-[var(--color-ink-muted)] text-center py-6">{t('add_business.no_category_found')}</p>
          )}
          {searchResults?.type === 'root' &&
            searchResults.items.map((c) => (
              <button key={c.id} onClick={() => { setExpandedRoot(c as FlatCategory); setQuery('') }} className="flex items-center gap-3 p-3 rounded-lg bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors text-left">
                <span className="text-xl" aria-hidden="true">{c.icon}</span>
                <span className="text-sm font-medium">{c.name}</span>
              </button>
            ))}
          {searchResults?.type === 'leaf' &&
            searchResults.items.map((c) => (
              <button key={c.id} onClick={() => onSelect(c.id, `${c.parentName} · ${c.name}`)} className="flex items-center gap-3 p-3 rounded-lg bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors text-left">
                <span className="text-xl" aria-hidden="true">{c.icon}</span>
                <div>
                  <p className="text-sm font-medium">{c.name}</p>
                  <p className="text-xs text-[var(--color-ink-muted)] flex items-center gap-1 mt-0.5">
                    <span aria-hidden="true">{c.parentIcon}</span> {c.parentName}
                  </p>
                </div>
              </button>
            ))}
        </div>
      ) : (
        <div className="grid grid-cols-3 gap-2.5">
          {data?.roots.map((c) => (
            <button
              key={c.id}
              onClick={() => setExpandedRoot(c as FlatCategory)}
              className="flex flex-col items-center gap-2 py-4 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors"
            >
              <span className="text-2xl" aria-hidden="true">{c.icon}</span>
              <span className="text-xs text-center">{c.name}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  )
}

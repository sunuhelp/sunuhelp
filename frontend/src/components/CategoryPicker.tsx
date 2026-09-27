import { useMemo, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useCategories } from '../hooks/useCategories'
import { useSubCategories } from '../hooks/useSubCategories'
import { Skeleton } from './ui/Skeleton'

interface CategoryPickerProps {
  selectedId: string | null
  onSelect: (id: string, name: string) => void
}

export function CategoryPicker({ selectedId, onSelect }: CategoryPickerProps) {
  const { t } = useTranslation()
  const [expandedRoot, setExpandedRoot] = useState<{ id: string; name: string } | null>(null)
  const [search, setSearch] = useState('')
  const { data: roots, isLoading: rootsLoading } = useCategories()
  const { data: children, isLoading: childrenLoading } = useSubCategories(expandedRoot?.id ?? null)

  const filteredRoots = useMemo(() => {
    if (!search.trim()) return roots
    return roots?.filter((c) => c.name.toLowerCase().includes(search.toLowerCase()))
  }, [roots, search])

  if (expandedRoot) {
    return (
      <div>
        <button
          onClick={() => setExpandedRoot(null)}
          className="flex items-center gap-1.5 text-sm text-[var(--color-ink-muted)] mb-4 hover:text-[var(--color-accent)] transition-colors"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
          </svg>
          {expandedRoot.name}
        </button>

        {childrenLoading ? (
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
            {Array.from({ length: 6 }).map((_, i) => <Skeleton key={i} className="h-20" />)}
          </div>
        ) : (
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3 max-h-72 overflow-y-auto pr-1">
            {children?.map((sub) => (
              <button
                key={sub.id}
                type="button"
                onClick={() => onSelect(sub.id, `${expandedRoot.name} · ${sub.name}`)}
                className={`flex flex-col items-center gap-2 py-5 rounded-xl border transition-all animate-in fade-in duration-150 ${
                  selectedId === sub.id
                    ? 'border-[var(--color-accent)] bg-[var(--color-accent)]/5 ring-2 ring-[var(--color-accent)]/20'
                    : 'border-[var(--color-border)] bg-[var(--color-bg)] hover:border-[var(--color-accent)]/40'
                }`}
              >
                <span className="text-2xl" aria-hidden="true">{sub.icon}</span>
                <span className="text-sm font-medium text-center">{sub.name}</span>
              </button>
            ))}
          </div>
        )}
      </div>
    )
  }

  return (
    <div>
      <p className="text-sm font-medium mb-3">{t('add_business.pick_category')}</p>

      <div className="relative mb-4">
        <svg className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[var(--color-ink-muted)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-4.35-4.35M17 10.5a6.5 6.5 0 11-13 0 6.5 6.5 0 0113 0z" />
        </svg>
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder={t('add_business.search_category_placeholder')}
          className="w-full h-11 pl-10 pr-4 rounded-lg border border-[var(--color-border)] bg-[var(--color-bg)] outline-none focus:border-[var(--color-accent)] text-sm"
        />
      </div>

      {rootsLoading ? (
        <div className="grid grid-cols-3 sm:grid-cols-4 gap-3">
          {Array.from({ length: 8 }).map((_, i) => <Skeleton key={i} className="h-24" />)}
        </div>
      ) : (
        <div className="grid grid-cols-3 sm:grid-cols-4 gap-3 max-h-80 overflow-y-auto pr-1">
          {filteredRoots?.map((cat) => (
            <button
              key={cat.id}
              type="button"
              onClick={() => setExpandedRoot({ id: cat.id, name: cat.name })}
              className="flex flex-col items-center gap-2 py-5 rounded-xl border border-[var(--color-border)] bg-[var(--color-bg)] hover:border-[var(--color-accent)]/40 hover:shadow-sm transition-all"
            >
              <span className="text-2xl" aria-hidden="true">{cat.icon}</span>
              <span className="text-xs font-medium text-center">{cat.name}</span>
            </button>
          ))}
          {filteredRoots?.length === 0 && (
            <p className="col-span-full text-sm text-[var(--color-ink-muted)] text-center py-6">
              {t('add_business.no_category_found')}
            </p>
          )}
        </div>
      )}
    </div>
  )
}

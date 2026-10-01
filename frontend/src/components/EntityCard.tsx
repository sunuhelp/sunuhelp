import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { AlertCircle, CircleCheck, ShieldCheck, ChevronRight } from 'lucide-react'
import { getCategoryIcon } from '../lib/categoryIcons'
import { useRootCategorySlug } from '../hooks/useRootCategorySlug'
import { useCategory } from '../hooks/useCategory'
import { useHasHours } from '../hooks/useHasHours'

interface EntityCardProps {
  id: string
  name: string
  categoryId: string
}

export function EntityCard({ id, name, categoryId }: EntityCardProps) {
  const { t } = useTranslation()
  const { data: category } = useCategory(categoryId)
  const { data: rootSlug } = useRootCategorySlug(categoryId)
  const { data: hoursSet } = useHasHours(id)
  const Icon = getCategoryIcon(rootSlug)

  return (
    <Link
      to={`/mes-etablissements/${id}`}
      className="flex items-center gap-4 sm:gap-5 p-5 rounded-2xl bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors"
    >
      <div className="w-14 h-14 sm:w-16 sm:h-16 rounded-2xl bg-[var(--color-accent)] flex items-center justify-center shrink-0">
        <Icon size={28} className="text-white" strokeWidth={1.75} aria-hidden="true" />
      </div>

      <div className="flex-1 min-w-0">
        <p className="text-base sm:text-lg font-medium truncate">{name}</p>
        {category && <p className="text-xs sm:text-sm text-[var(--color-ink-muted)] mt-0.5 mb-2">{category.name}</p>}

        <div className="flex items-center gap-4 flex-wrap">
          <div className="flex items-center gap-1.5">
            {hoursSet ? (
              <>
                <CircleCheck size={14} className="text-[var(--color-success)]" strokeWidth={1.75} aria-hidden="true" />
                <span className="text-xs font-medium text-[var(--color-success)]">{t('my_businesses.hours_set')}</span>
              </>
            ) : (
              <>
                <AlertCircle size={14} className="text-[var(--color-warning)]" strokeWidth={1.75} aria-hidden="true" />
                <span className="text-xs font-medium text-[var(--color-warning)]">{t('my_businesses.hours_missing')}</span>
              </>
            )}
          </div>
          <div className="flex items-center gap-1.5">
            <ShieldCheck size={14} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} aria-hidden="true" />
            <span className="text-xs text-[var(--color-ink-muted)]">{t('my_businesses.not_verified')}</span>
          </div>
        </div>
      </div>

      <div className="hidden sm:flex items-center gap-1.5 text-sm text-[var(--color-accent)] font-medium shrink-0">
        {t('my_businesses.manage')} <ChevronRight size={16} strokeWidth={1.75} aria-hidden="true" />
      </div>
    </Link>
  )
}

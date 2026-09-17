import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { useCategories } from '../hooks/useCategories'
import { Skeleton } from './ui/Skeleton'

export function CategoryGrid() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { data: categories, isLoading } = useCategories()

  if (isLoading) {
    return (
      <div className="grid grid-cols-4 gap-4 mt-8">
        {Array.from({ length: 8 }).map((_, i) => (
          <Skeleton key={i} className="h-20" />
        ))}
      </div>
    )
  }

  return (
    <div>
      <div className="grid grid-cols-4 gap-4 mt-8">
        {categories?.slice(0, 8).map((cat) => (
          <button
            key={cat.id}
            onClick={() => navigate(`/resultats?categorySlug=${cat.slug}`)}
            className="flex flex-col items-center gap-2 py-3 rounded-lg hover:bg-[var(--color-surface)] transition-colors"
          >
            <span className="text-2xl" aria-hidden="true">{cat.icon}</span>
            <span className="text-xs text-center">{cat.name}</span>
          </button>
        ))}
      </div>
      {categories && categories.length > 8 && (
        <button
          onClick={() => navigate('/categories')}
          className="mt-4 text-sm text-[var(--color-accent)] w-full text-center"
        >
          {t('categories.see_more')}
        </button>
      )}
    </div>
  )
}

import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { useCategories } from '../hooks/useCategories'
import { Skeleton } from './ui/Skeleton'

export function CategoryGrid() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { data: categories, isLoading } = useCategories()

  return (
    <section>
      <h2 className="font-[var(--font-display)] text-xl font-medium mb-6">
        {t('home.categories_title')}
      </h2>

      {isLoading ? (
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          {Array.from({ length: 8 }).map((_, i) => (
            <Skeleton key={i} className="h-28" />
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          {categories?.slice(0, 8).map((cat) => (
            <button
              key={cat.id}
              onClick={() => navigate(`/resultats?categorySlug=${cat.slug}`)}
              className="flex flex-col items-center gap-3 py-6 rounded-xl border border-[var(--color-border)] bg-[var(--color-surface)] hover:shadow-md hover:-translate-y-0.5 transition-all"
            >
              <span className="text-3xl" aria-hidden="true">{cat.icon}</span>
              <span className="text-sm font-medium">{cat.name}</span>
            </button>
          ))}
        </div>
      )}

      {categories && categories.length > 8 && (
        <button
          onClick={() => navigate('/categories')}
          className="mt-6 text-sm text-[var(--color-accent)] font-medium"
        >
          {t('categories.see_more')}
        </button>
      )}
    </section>
  )
}

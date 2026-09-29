import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { MoreHorizontal } from 'lucide-react'
import { motion } from 'framer-motion'
import { useCategories } from '../hooks/useCategories'

export function CategoryGrid() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { data: categories, isLoading } = useCategories()

  return (
    <div className="py-10">
      <p className="text-sm font-medium text-[var(--color-ink-muted)] text-center mb-5">{t('home.categories_title')}</p>

      {isLoading ? (
        <div className="grid grid-cols-3 sm:grid-cols-6 gap-3">
          {Array.from({ length: 6 }).map((_, i) => (
            <div key={i} className="h-28 rounded-xl bg-[var(--color-surface)] animate-pulse" />
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-3 sm:grid-cols-6 gap-3">
          {categories?.slice(0, 5).map((cat) => (
            <motion.button
              key={cat.id}
              whileHover={{ y: -2 }}
              onClick={() => navigate(`/resultats?categorySlug=${cat.slug}`)}
              className="flex flex-col items-center gap-2.5 py-6 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors"
            >
              <span className="text-3xl" aria-hidden="true">{cat.icon}</span>
              <span className="text-xs">{cat.name}</span>
            </motion.button>
          ))}
          <motion.button
            whileHover={{ y: -2 }}
            onClick={() => navigate('/categories')}
            className="flex flex-col items-center gap-2.5 py-6 rounded-xl bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors"
          >
            <MoreHorizontal size={30} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} aria-hidden="true" />
            <span className="text-xs">{t('categories.see_more')}</span>
          </motion.button>
        </div>
      )}
    </div>
  )
}

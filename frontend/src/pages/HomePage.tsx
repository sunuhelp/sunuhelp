import { useTranslation } from 'react-i18next'
import { Link } from 'react-router-dom'
import { SearchBar } from '../components/SearchBar'
import { CategoryGrid } from '../components/CategoryGrid'

export default function HomePage() {
  const { t } = useTranslation()

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)]">
      <header className="flex items-center justify-between px-4 py-4 max-w-3xl mx-auto">
        <span className="font-[var(--font-display)] font-medium text-lg">
          {t('app_name')}
        </span>
        <nav className="flex items-center gap-4 text-sm">
          <Link to="/inscription">{t('auth.register')}</Link>
          <Link to="/connexion" className="text-[var(--color-accent)]">
            {t('auth.login')}
          </Link>
        </nav>
      </header>

      <main className="px-4 pt-8 max-w-3xl mx-auto">
        <SearchBar />
        <CategoryGrid />
      </main>
    </div>
  )
}

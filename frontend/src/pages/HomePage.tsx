import { useTranslation } from 'react-i18next'
import { SearchBar } from '../components/SearchBar'
import { CategoryGrid } from '../components/CategoryGrid'
import { HowItWorks } from '../components/HowItWorks'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'

export default function HomePage() {
  const { t } = useTranslation()

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <div className="relative overflow-hidden bg-[var(--color-surface)] border-b border-[var(--color-border)]">
        <div className="absolute -top-24 -right-24 w-96 h-96 rounded-full opacity-[0.07] pointer-events-none" style={{ background: 'var(--color-accent)' }} />
        <div className="absolute -bottom-32 -left-16 w-80 h-80 rounded-full opacity-[0.06] pointer-events-none" style={{ background: 'var(--color-accent-warm)' }} />

        <main className="relative px-6 pt-14 pb-16 max-w-6xl mx-auto w-full">
          <h1 className="font-[var(--font-display)] text-3xl sm:text-4xl font-medium text-center max-w-2xl mx-auto leading-tight">
            {t('home.headline')}
          </h1>
          <p className="text-center text-[var(--color-ink-muted)] mt-3 max-w-xl mx-auto">
            {t('home.subheadline')}
          </p>
          <div className="mt-8">
            <SearchBar />
          </div>
        </main>
      </div>

      <div className="flex-1 max-w-6xl mx-auto w-full px-6 py-14">
        <CategoryGrid />
        <HowItWorks />
      </div>

      <Footer />
    </div>
  )
}

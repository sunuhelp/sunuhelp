import { useTranslation } from 'react-i18next'
import { motion } from 'framer-motion'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { SearchBar } from '../components/SearchBar'
import { MapIllustration } from '../components/MapIllustration'
import { BenefitsRow } from '../components/BenefitsRow'
import { CategoryGrid } from '../components/CategoryGrid'
import { CtaBand } from '../components/CtaBand'

export default function HomePage() {
  const { t } = useTranslation()

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1">
        <div className="max-w-6xl mx-auto px-7 py-14 grid lg:grid-cols-[1.1fr_0.9fr] gap-10 items-center">
          <motion.div
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, ease: 'easeOut' }}
          >
            <h1 className="font-[var(--font-display)] text-3xl sm:text-4xl font-medium leading-tight mb-4">
              {t('home.headline')}
            </h1>
            <p className="text-sm text-[var(--color-ink-muted)] leading-relaxed max-w-[420px] mb-6">
              {t('home.subheadline')}
            </p>
            <SearchBar />
          </motion.div>

          <MapIllustration />
        </div>

        <div className="max-w-6xl mx-auto px-7">
          <BenefitsRow />
          <CategoryGrid />
        </div>

        <CtaBand />
      </main>

      <Footer />
    </div>
  )
}

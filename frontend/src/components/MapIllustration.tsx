import { useTranslation } from 'react-i18next'
import { Store } from 'lucide-react'
import { motion } from 'framer-motion'

export function MapIllustration() {
  const { t } = useTranslation()

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.97 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5, ease: 'easeOut' }}
      className="relative bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl h-72 overflow-hidden"
    >
      <svg width="100%" height="100%" viewBox="0 0 400 288" className="absolute inset-0" aria-hidden="true">
        <path d="M60 240 Q 160 110 240 150 T 340 90" fill="none" stroke="var(--color-border-strong)" strokeWidth="1.5" strokeDasharray="4 6" />
        <circle cx="60" cy="240" r="7" fill="var(--color-accent)" />
        <circle cx="240" cy="150" r="7" fill="var(--color-accent)" />
        <circle cx="340" cy="90" r="9" fill="var(--color-accent)" />
      </svg>

      <div className="absolute left-[170px] top-6 bg-[var(--color-bg)] border border-[var(--color-border)] rounded-lg px-3.5 py-2.5 min-w-[190px] shadow-sm">
        <div className="flex items-center gap-2 mb-1">
          <Store size={15} className="text-[var(--color-accent)]" strokeWidth={1.75} aria-hidden="true" />
          <span className="text-xs font-medium">{t('home.preview_name')}</span>
        </div>
        <div className="flex items-center gap-2 text-xs text-[var(--color-ink-muted)]">
          <span>{t('home.preview_distance')}</span>
          <span className="text-[var(--color-success)]">{t('home.preview_status')}</span>
        </div>
      </div>
    </motion.div>
  )
}

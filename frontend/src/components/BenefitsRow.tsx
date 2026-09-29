import { useTranslation } from 'react-i18next'
import { RefreshCw, ShieldCheck, LayoutGrid, Gift } from 'lucide-react'

const BENEFITS = [
  { icon: RefreshCw, key: 'fresh' },
  { icon: ShieldCheck, key: 'trust' },
  { icon: LayoutGrid, key: 'coverage' },
  { icon: Gift, key: 'free' },
] as const

export function BenefitsRow() {
  const { t } = useTranslation()

  return (
    <div className="grid grid-cols-2 sm:grid-cols-4 gap-6 py-8 border-y border-[var(--color-border)]">
      {BENEFITS.map(({ icon: Icon, key }) => (
        <div key={key} className="flex flex-col items-center gap-2 text-center">
          <Icon size={22} className="text-[var(--color-accent)]" strokeWidth={1.75} aria-hidden="true" />
          <p className="text-xs font-medium">{t(`home.benefits.${key}_title`)}</p>
          <p className="text-[11px] text-[var(--color-ink-muted)] leading-relaxed">{t(`home.benefits.${key}_body`)}</p>
        </div>
      ))}
    </div>
  )
}

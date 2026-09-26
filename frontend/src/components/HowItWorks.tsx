import { useTranslation } from 'react-i18next'

const STEPS = [
  { icon: '🔍', key: 'search' },
  { icon: '📍', key: 'compare' },
  { icon: '📞', key: 'contact' },
] as const

export function HowItWorks() {
  const { t } = useTranslation()

  return (
    <section className="mt-16">
      <h2 className="font-[var(--font-display)] text-xl font-medium mb-6">
        {t('home.how_it_works_title')}
      </h2>
      <div className="grid sm:grid-cols-3 gap-6">
        {STEPS.map((step, i) => (
          <div key={step.key} className="flex flex-col gap-2">
            <span className="text-2xl" aria-hidden="true">{step.icon}</span>
            <h3 className="font-medium text-sm">
              {i + 1}. {t(`home.steps.${step.key}.title`)}
            </h3>
            <p className="text-sm text-[var(--color-ink-muted)]">
              {t(`home.steps.${step.key}.description`)}
            </p>
          </div>
        ))}
      </div>
    </section>
  )
}

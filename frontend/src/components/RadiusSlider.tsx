import { useTranslation } from 'react-i18next'

interface RadiusSliderProps {
  value: number
  onChange: (value: number) => void
}

const QUICK_VALUES = [5, 10, 20, 50]

export function RadiusSlider({ value, onChange }: RadiusSliderProps) {
  const { t } = useTranslation()

  return (
    <div>
      <div className="flex items-center justify-between mb-2">
        <span className="text-sm text-[var(--color-ink-muted)]">{t('search.radius')}</span>
        <span className="text-sm font-medium text-[var(--color-accent)]">{value} km</span>
      </div>

      <input
        type="range"
        min={1}
        max={50}
        step={1}
        value={value}
        onChange={(e) => onChange(Number(e.target.value))}
        className="sunuhelp-slider w-full"
        aria-label={t('search.radius')}
      />

      <div className="flex items-center gap-2 mt-3">
        {QUICK_VALUES.map((km) => (
          <button
            key={km}
            type="button"
            onClick={() => onChange(km)}
            className={`text-xs px-2.5 py-1 rounded-full border transition-colors ${
              value === km
                ? 'bg-[var(--color-accent)] text-white border-[var(--color-accent)]'
                : 'border-[var(--color-border)] text-[var(--color-ink-muted)] hover:border-[var(--color-accent)]'
            }`}
          >
            {km} km
          </button>
        ))}
      </div>
    </div>
  )
}

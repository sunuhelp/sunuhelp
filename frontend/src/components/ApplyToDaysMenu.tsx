import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { CopyPlus, Check } from 'lucide-react'

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']
const DAY_KEYS: Record<string, string> = {
  MONDAY: 'day_mon', TUESDAY: 'day_tue', WEDNESDAY: 'day_wed', THURSDAY: 'day_thu',
  FRIDAY: 'day_fri', SATURDAY: 'day_sat', SUNDAY: 'day_sun',
}

interface ApplyToDaysMenuProps {
  currentDay: string
  onApply: (days: string[]) => void
}

export function ApplyToDaysMenu({ currentDay, onApply }: ApplyToDaysMenuProps) {
  const { t } = useTranslation()
  const [open, setOpen] = useState(false)
  const [selected, setSelected] = useState<Set<string>>(new Set(DAYS.filter((d) => d !== currentDay)))
  const ref = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const handler = (e: MouseEvent) => { if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false) }
    document.addEventListener('mousedown', handler)
    return () => document.removeEventListener('mousedown', handler)
  }, [])

  const toggle = (day: string) => {
    const next = new Set(selected)
    next.has(day) ? next.delete(day) : next.add(day)
    setSelected(next)
  }

  const apply = () => {
    onApply(Array.from(selected))
    setOpen(false)
  }

  return (
    <div ref={ref} className="relative">
      <button
        type="button"
        onClick={() => setOpen((v) => !v)}
        title={t('manage.apply_this_time')}
        className="text-[var(--color-ink-muted)] hover:text-[var(--color-accent)] transition-colors"
      >
        <CopyPlus size={13} strokeWidth={1.75} aria-hidden="true" />
      </button>

      {open && (
        <div className="absolute z-20 mt-1.5 w-48 bg-[var(--color-bg)] border border-[var(--color-border)] rounded-lg shadow-lg p-2">
          <p className="text-[11px] text-[var(--color-ink-muted)] px-1.5 pb-1.5">{t('manage.apply_to_which_days')}</p>
          {DAYS.filter((d) => d !== currentDay).map((day) => (
            <button
              key={day}
              type="button"
              onClick={() => toggle(day)}
              className="w-full flex items-center gap-2 px-1.5 py-1.5 rounded-md hover:bg-[var(--color-surface)] transition-colors text-left"
            >
              <span className={`w-4 h-4 rounded border flex items-center justify-center shrink-0 ${selected.has(day) ? 'bg-[var(--color-accent)] border-[var(--color-accent)]' : 'border-[var(--color-border-strong)]'}`}>
                {selected.has(day) && <Check size={11} className="text-white" strokeWidth={3} />}
              </span>
              <span className="text-xs">{t(`entity.${DAY_KEYS[day]}`)}</span>
            </button>
          ))}
          <button
            type="button"
            onClick={apply}
            className="w-full h-8 mt-1.5 rounded-md bg-[var(--color-accent)] text-white text-xs font-medium"
          >
            {t('common.apply')}
          </button>
        </div>
      )}
    </div>
  )
}

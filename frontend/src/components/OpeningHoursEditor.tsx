import { useTranslation } from 'react-i18next'
import { TimeSelect } from './TimeSelect'
import { ApplyToDaysMenu } from './ApplyToDaysMenu'

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']
const DAY_KEYS: Record<string, string> = {
  MONDAY: 'day_mon', TUESDAY: 'day_tue', WEDNESDAY: 'day_wed', THURSDAY: 'day_thu',
  FRIDAY: 'day_fri', SATURDAY: 'day_sat', SUNDAY: 'day_sun',
}

export interface DayHours { dayOfWeek: string; closed: boolean; openingTime: string; closingTime: string }

interface OpeningHoursEditorProps { value: DayHours[]; onChange: (value: DayHours[]) => void }

export function OpeningHoursEditor({ value, onChange }: OpeningHoursEditorProps) {
  const { t } = useTranslation()
  const today = DAYS[new Date().getDay() === 0 ? 6 : new Date().getDay() - 1]

  const update = (day: string, patch: Partial<DayHours>) => onChange(value.map((d) => (d.dayOfWeek === day ? { ...d, ...patch } : d)))

  const applyTimeToDays = (field: 'openingTime' | 'closingTime', time: string, targetDays: string[]) => {
    onChange(value.map((d) => (targetDays.includes(d.dayOfWeek) ? { ...d, [field]: time } : d)))
  }

  return (
    <div className="space-y-1.5">
      {DAYS.map((day) => {
        const entry = value.find((d) => d.dayOfWeek === day) ?? { dayOfWeek: day, closed: true, openingTime: '08:00', closingTime: '18:00' }
        const isToday = day === today
        return (
          <div key={day} className={`flex items-center gap-2.5 p-3 rounded-xl transition-colors ${isToday ? 'bg-[var(--color-accent)]/5 border border-[var(--color-accent)]/30' : ''}`}>
            <span className={`text-sm w-[76px] shrink-0 ${isToday ? 'font-medium' : ''} ${entry.closed ? 'text-[var(--color-ink-muted)]' : ''}`}>
              {t(`entity.${DAY_KEYS[day]}`)}
            </span>
            <button
              type="button"
              onClick={() => update(day, { closed: !entry.closed })}
              className={`w-9 h-[21px] rounded-full relative transition-colors shrink-0 ${!entry.closed ? 'bg-[var(--color-accent)]' : 'bg-[var(--color-border)]'}`}
            >
              <span className={`absolute top-[3.5px] w-[14px] h-[14px] rounded-full bg-white transition-transform ${!entry.closed ? 'translate-x-[18px]' : 'translate-x-[3.5px]'}`} />
            </button>
            {entry.closed ? (
              <span className="text-xs text-[var(--color-ink-muted)]">{t('entity.day_closed')}</span>
            ) : (
              <div className="flex items-center gap-1.5">
                <div className="flex items-center gap-1">
                  <TimeSelect value={entry.openingTime} onChange={(v) => update(day, { openingTime: v })} />
                  <ApplyToDaysMenu currentDay={day} onApply={(days) => applyTimeToDays('openingTime', entry.openingTime, days)} />
                </div>
                <span className="text-xs text-[var(--color-ink-muted)]">–</span>
                <div className="flex items-center gap-1">
                  <TimeSelect value={entry.closingTime} onChange={(v) => update(day, { closingTime: v })} />
                  <ApplyToDaysMenu currentDay={day} onApply={(days) => applyTimeToDays('closingTime', entry.closingTime, days)} />
                </div>
              </div>
            )}
          </div>
        )
      })}
    </div>
  )
}

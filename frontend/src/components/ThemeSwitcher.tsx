import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { Sun, Moon, MonitorSmartphone, ChevronDown, Check } from 'lucide-react'
import { useThemeStore, type Theme } from '../stores/themeStore'

const OPTIONS: { value: Theme; icon: typeof Sun }[] = [
  { value: 'light', icon: Sun },
  { value: 'dark', icon: Moon },
  { value: 'system', icon: MonitorSmartphone },
]

export function ThemeSwitcher() {
  const { t } = useTranslation()
  const { theme, setTheme } = useThemeStore()
  const [open, setOpen] = useState(false)
  const ref = useRef<HTMLDivElement>(null)

  const current = OPTIONS.find((o) => o.value === theme) ?? OPTIONS[2]
  const CurrentIcon = current.icon

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setOpen((v) => !v)}
        aria-haspopup="listbox"
        aria-expanded={open}
        aria-label={t('theme.label')}
        className="flex items-center gap-1.5 text-sm px-2.5 py-1.5 rounded-md border border-[var(--color-border)] hover:bg-[var(--color-surface)] transition-colors"
      >
        <CurrentIcon size={15} strokeWidth={1.75} aria-hidden="true" />
        <ChevronDown size={13} strokeWidth={1.75} className={`text-[var(--color-ink-muted)] transition-transform ${open ? 'rotate-180' : ''}`} aria-hidden="true" />
      </button>

      {open && (
        <div
          role="listbox"
          className="absolute right-0 mt-2 w-44 bg-[var(--color-bg)] border border-[var(--color-border)] rounded-lg shadow-lg py-1 z-50"
        >
          {OPTIONS.map((opt) => {
            const Icon = opt.icon
            return (
              <button
                key={opt.value}
                role="option"
                aria-selected={opt.value === theme}
                onClick={() => { setTheme(opt.value); setOpen(false) }}
                className="w-full flex items-center gap-2.5 px-3 py-2 text-sm hover:bg-[var(--color-surface)] transition-colors"
              >
                <Icon size={15} strokeWidth={1.75} aria-hidden="true" />
                <span className="flex-1 text-left">{t(`theme.${opt.value}`)}</span>
                {opt.value === theme && <Check size={14} className="text-[var(--color-accent)]" aria-hidden="true" />}
              </button>
            )
          })}
        </div>
      )}
    </div>
  )
}

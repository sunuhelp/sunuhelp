import { useEffect, useRef, useState } from 'react'

interface TimeSelectProps {
  value: string
  onChange: (value: string) => void
}

export function TimeSelect({ value, onChange }: TimeSelectProps) {
  const [isEditing, setIsEditing] = useState(false)
  const [hours, setHours] = useState('')
  const [minutes, setMinutes] = useState('')
  const hourRef = useRef<HTMLInputElement>(null)
  const minuteRef = useRef<HTMLInputElement>(null)
  const wrapperRef = useRef<HTMLDivElement>(null)

  const startEditing = () => {
    const [h, m] = value.split(':')
    setHours(h ?? '08')
    setMinutes(m ?? '00')
    setIsEditing(true)
  }

  useEffect(() => {
    if (isEditing) hourRef.current?.focus()
  }, [isEditing])

  // Ne se referme que si le clic sort VRAIMENT du bloc entier (heures +
  // minutes ensemble) - passer d'un champ a l'autre a l'interieur ne
  // doit jamais interrompre l'edition.
  useEffect(() => {
    if (!isEditing) return
    const handler = (e: MouseEvent) => {
      if (wrapperRef.current && !wrapperRef.current.contains(e.target as Node)) finish()
    }
    document.addEventListener('mousedown', handler)
    return () => document.removeEventListener('mousedown', handler)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isEditing, hours, minutes])

  const finish = () => {
    const validH = Math.min(23, Math.max(0, parseInt(hours || '0', 10))).toString().padStart(2, '0')
    const validM = Math.min(59, Math.max(0, parseInt(minutes || '0', 10))).toString().padStart(2, '0')
    onChange(`${validH}:${validM}`)
    setIsEditing(false)
  }

  const handleHoursChange = (raw: string) => {
    let digits = raw.replace(/\D/g, '').slice(0, 2)
    if (digits.length === 2 && parseInt(digits, 10) > 23) digits = '23'
    setHours(digits)
    if (digits.length === 2) minuteRef.current?.focus()
  }

  const handleMinutesChange = (raw: string) => {
    let digits = raw.replace(/\D/g, '').slice(0, 2)
    if (digits.length === 2 && parseInt(digits, 10) > 59) digits = '59'
    setMinutes(digits)
  }

  if (!isEditing) {
    return (
      <button
        type="button"
        onClick={startEditing}
        className="text-sm font-medium px-4 py-2.5 rounded-lg bg-[var(--color-bg)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/50 transition-colors min-w-[68px]"
      >
        {value}
      </button>
    )
  }

  return (
    <div ref={wrapperRef} className="flex items-center gap-1.5 px-4 py-2.5 rounded-lg bg-[var(--color-bg)] border-2 border-[var(--color-accent)]">
      <input
        ref={hourRef}
        type="text"
        inputMode="numeric"
        value={hours}
        onChange={(e) => handleHoursChange(e.target.value)}
        onKeyDown={(e) => e.key === 'Enter' && finish()}
        placeholder="HH"
        maxLength={2}
        className="text-sm font-medium w-[24px] text-center bg-transparent outline-none"
      />
      <span className="text-sm text-[var(--color-ink-muted)]">:</span>
      <input
        ref={minuteRef}
        type="text"
        inputMode="numeric"
        value={minutes}
        onChange={(e) => handleMinutesChange(e.target.value)}
        onKeyDown={(e) => e.key === 'Enter' && finish()}
        placeholder="MM"
        maxLength={2}
        className="text-sm font-medium w-[24px] text-center bg-transparent outline-none"
      />
    </div>
  )
}

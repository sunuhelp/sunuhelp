interface PhoneInputProps {
  value: string
  onChange: (digits: string) => void
  error?: boolean
}

/** Drapeau du Senegal - dessine, jamais un emoji, rendu identique partout. */
function SenegalFlag() {
  return (
    <svg width="20" height="14" viewBox="0 0 20 14" aria-hidden="true">
      <rect x="0" width="6.66" height="14" fill="#00853F" />
      <rect x="6.66" width="6.68" height="14" fill="#FDEF42" />
      <rect x="13.34" width="6.66" height="14" fill="#E31B23" />
      <polygon points="10,4.5 10.6,6.3 12.5,6.3 11,7.4 11.5,9.2 10,8.1 8.5,9.2 9,7.4 7.5,6.3 9.4,6.3" fill="#00853F" />
    </svg>
  )
}

/** Espace les 9 chiffres par paires : "771234567" -> "77 123 45 67". */
function formatDigits(digits: string): string {
  return digits.replace(/(\d{2})(?=\d)/g, '$1 ').trim()
}

export function PhoneInput({ value, onChange, error }: PhoneInputProps) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const digitsOnly = e.target.value.replace(/\D/g, '').slice(0, 9)
    onChange(digitsOnly)
  }

  return (
    <div
      className={`flex items-center h-12 rounded-lg border overflow-hidden transition-colors ${
        error ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus-within:border-[var(--color-accent)]'
      }`}
    >
      <div className="flex items-center gap-1.5 px-3 h-full bg-[var(--color-surface)] border-r border-[var(--color-border)] shrink-0">
        <SenegalFlag />
        <span className="text-sm text-[var(--color-ink-muted)]">+221</span>
      </div>
      <input
        type="tel"
        inputMode="numeric"
        value={formatDigits(value)}
        onChange={handleChange}
        placeholder="77 123 45 67"
        className="flex-1 h-full px-3 bg-transparent outline-none text-sm placeholder:text-[var(--color-ink-muted)]"
      />
    </div>
  )
}

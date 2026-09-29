import { useRef } from 'react'

interface OtpInputProps {
  value: string
  onChange: (value: string) => void
  error?: boolean
}

export function OtpInput({ value, onChange, error }: OtpInputProps) {
  const inputRefs = useRef<(HTMLInputElement | null)[]>([])
  const digits = value.padEnd(6, ' ').split('').slice(0, 6)

  const setDigit = (index: number, digit: string) => {
    const newDigits = [...digits]
    newDigits[index] = digit
    onChange(newDigits.join('').trimEnd())
  }

  const handleChange = (index: number, raw: string) => {
    const digit = raw.replace(/[^0-9]/g, '').slice(-1)
    setDigit(index, digit || ' ')
    if (digit && index < 5) inputRefs.current[index + 1]?.focus()
  }

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !digits[index].trim() && index > 0) inputRefs.current[index - 1]?.focus()
  }

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault()
    const pasted = e.clipboardData.getData('text').replace(/[^0-9]/g, '').slice(0, 6)
    onChange(pasted)
    inputRefs.current[Math.min(pasted.length, 5)]?.focus()
  }

  return (
    <div className="flex gap-2 justify-center" onPaste={handlePaste}>
      {digits.map((digit, i) => (
        <input
          key={i}
          ref={(el) => { inputRefs.current[i] = el }}
          type="text"
          inputMode="numeric"
          maxLength={1}
          value={digit.trim()}
          onChange={(e) => handleChange(i, e.target.value)}
          onKeyDown={(e) => handleKeyDown(i, e)}
          className={`w-12 h-14 text-center text-xl font-medium rounded-lg border-2 outline-none transition-colors ${
            error ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'
          }`}
          aria-label={`Chiffre ${i + 1}`}
        />
      ))}
    </div>
  )
}

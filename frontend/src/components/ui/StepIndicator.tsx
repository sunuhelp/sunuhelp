interface StepIndicatorProps {
  steps: string[]
  current: number
}

export function StepIndicator({ steps, current }: StepIndicatorProps) {
  return (
    <div className="flex items-center justify-center gap-2 mb-8">
      {steps.map((label, i) => (
        <div key={label} className="flex items-center gap-2">
          <div className="flex flex-col items-center gap-1.5">
            <div
              className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium transition-colors ${
                i < current
                  ? 'bg-[var(--color-accent)] text-white'
                  : i === current
                  ? 'bg-[var(--color-accent)] text-white ring-4 ring-[var(--color-accent)]/20'
                  : 'bg-[var(--color-surface)] text-[var(--color-ink-muted)] border border-[var(--color-border)]'
              }`}
            >
              {i < current ? (
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" />
                </svg>
              ) : (
                i + 1
              )}
            </div>
            <span className="text-xs text-[var(--color-ink-muted)] hidden sm:block whitespace-nowrap">{label}</span>
          </div>
          {i < steps.length - 1 && (
            <div className={`w-8 sm:w-12 h-0.5 ${i < current ? 'bg-[var(--color-accent)]' : 'bg-[var(--color-border)]'}`} />
          )}
        </div>
      ))}
    </div>
  )
}

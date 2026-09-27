interface PersonTypeCardProps {
  icon: string
  title: string
  description: string
  selected: boolean
  onClick: () => void
}

export function PersonTypeCard({ icon, title, description, selected, onClick }: PersonTypeCardProps) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`flex-1 text-left p-5 rounded-xl border-2 transition-all ${
        selected
          ? 'border-[var(--color-accent)] bg-[var(--color-accent)]/5'
          : 'border-[var(--color-border)] hover:border-[var(--color-accent)]/40'
      }`}
    >
      <span className="text-2xl block mb-2" aria-hidden="true">{icon}</span>
      <p className="font-medium text-sm mb-1">{title}</p>
      <p className="text-xs text-[var(--color-ink-muted)]">{description}</p>
    </button>
  )
}

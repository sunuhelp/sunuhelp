import type { LucideIcon } from 'lucide-react'

interface PersonTypeCardProps {
  icon: LucideIcon
  title: string
  description: string
  selected: boolean
  onClick: () => void
}

export function PersonTypeCard({ icon: Icon, title, description, selected, onClick }: PersonTypeCardProps) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`flex-1 text-left p-4 rounded-xl border-2 transition-all ${
        selected ? 'border-[var(--color-accent)] bg-[var(--color-accent)]/5' : 'border-[var(--color-border)] hover:border-[var(--color-accent)]/40'
      }`}
    >
      <Icon size={22} className="text-[var(--color-accent)] mb-2" strokeWidth={1.75} aria-hidden="true" />
      <p className="font-medium text-sm mb-0.5">{title}</p>
      <p className="text-xs text-[var(--color-ink-muted)]">{description}</p>
    </button>
  )
}

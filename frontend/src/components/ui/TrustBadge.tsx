import { useTranslation } from 'react-i18next'

export function TrustBadge({ trustLevel }: { trustLevel: string }) {
  const { t } = useTranslation()
  if (trustLevel !== 'VERIFIED') return null
  return (
    <span className="inline-flex items-center gap-1 text-xs font-medium px-2.5 py-1 rounded-full bg-[color-mix(in_srgb,var(--color-accent)_12%,transparent)] text-[var(--color-accent)]">
      <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      {t('entity.verified')}
    </span>
  )
}

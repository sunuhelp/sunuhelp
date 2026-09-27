import { useState } from 'react'
import { useParams } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useEntity } from '../hooks/useEntity'
import { useCategory } from '../hooks/useCategory'
import { useServicePoints, useOpeningHours } from '../hooks/useServicePoints'
import { useReviews } from '../hooks/useReviews'
import { useAuthStore } from '../stores/authStore'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { TrustBadge } from '../components/ui/TrustBadge'
import { Skeleton } from '../components/ui/Skeleton'
import { isOpenNow, closingTimeLabel } from '../lib/openingStatus'

const DAY_LABELS: Record<string, string> = {
  MONDAY: 'day_mon', TUESDAY: 'day_tue', WEDNESDAY: 'day_wed', THURSDAY: 'day_thu',
  FRIDAY: 'day_fri', SATURDAY: 'day_sat', SUNDAY: 'day_sun',
}

export default function EntityDetailPage() {
  const { t } = useTranslation()
  const { id } = useParams<{ id: string }>()
  const accountId = useAuthStore((s) => s.accountId)
  const { data: entity, isLoading: entityLoading } = useEntity(id)
  const { data: category } = useCategory(entity?.categoryId)
  const { data: points, isLoading: pointsLoading } = useServicePoints(id)
  const { data: reviews } = useReviews(id)

  const [selectedPointId, setSelectedPointId] = useState<string | null>(null)
  const activePoint = points?.find((p) => p.id === selectedPointId) ?? points?.[0]
  const { data: hours } = useOpeningHours(activePoint?.id)

  const openStatus = hours && activePoint ? isOpenNow(hours, activePoint.isOpen247) : null
  const closesAt = hours ? closingTimeLabel(hours) : null
  const isOwner = !!accountId && entity?.ownerAccountId === accountId

  const averageRating = reviews?.content.length
    ? (reviews.content.reduce((sum, r) => sum + r.rating, 0) / reviews.content.length).toFixed(1)
    : null

  if (entityLoading) {
    return (
      <div className="min-h-screen bg-[var(--color-bg)] flex flex-col">
        <Header />
        <div className="max-w-3xl mx-auto w-full px-6 py-10">
          <Skeleton className="h-40 mb-4" />
          <Skeleton className="h-8 w-1/2 mb-2" />
          <Skeleton className="h-4 w-1/3" />
        </div>
        <Footer />
      </div>
    )
  }

  if (!entity) return null

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-3xl mx-auto w-full px-6 py-8 pb-28">
        {isOwner && (
          <div className="bg-[var(--color-surface)] border border-[var(--color-accent)]/30 rounded-xl p-4 mb-5">
            <p className="text-sm font-medium mb-1">{t('entity.owner_banner_title')}</p>
            <p className="text-xs text-[var(--color-ink-muted)]">{t('entity.owner_banner_body')}</p>
          </div>
        )}

        <div className="h-36 rounded-2xl bg-[var(--color-surface)] flex items-center justify-center mb-5">
          {entity.logoUrl ? (
            <img src={entity.logoUrl} alt={entity.name} className="max-h-full max-w-full object-contain" />
          ) : (
            <svg className="w-10 h-10 text-[var(--color-ink-muted)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 21h18M5 21V7l8-4v18M13 21V11l6 3v7M9 9v.01M9 12v.01M9 15v.01" />
            </svg>
          )}
        </div>

        <div className="mb-1">
          {category && (
            <p className="text-xs text-[var(--color-ink-muted)] flex items-center gap-1 mb-1">
              <span aria-hidden="true">{category.icon}</span> {category.name}
            </p>
          )}
          <h1 className="text-xl font-medium">{entity.name}</h1>
          {entity.description && (
            <p className="text-sm text-[var(--color-ink-muted)] mt-1">{entity.description}</p>
          )}
        </div>

        <div className="flex flex-wrap items-center gap-2 mb-6 mt-3">
          <TrustBadge trustLevel={entity.trustLevel} />
          {activePoint && openStatus !== null && (
            <span className={`text-xs font-medium px-2.5 py-1 rounded-full ${openStatus ? 'bg-[color-mix(in_srgb,var(--color-accent)_12%,transparent)] text-[var(--color-accent)]' : 'bg-[var(--color-surface)] text-[var(--color-ink-muted)]'}`}>
              {openStatus ? (closesAt ? t('entity.open_until', { time: closesAt }) : t('entity.open_now')) : t('entity.closed_now')}
            </span>
          )}
          {activePoint && openStatus === null && (
            <span className="text-xs px-2.5 py-1 rounded-full bg-[var(--color-surface)] text-[var(--color-ink-muted)]">
              {t('entity.hours_unknown')}
            </span>
          )}
          {averageRating && (
            <span className="text-xs text-[var(--color-ink-muted)] flex items-center gap-1">
              <svg className="w-3.5 h-3.5 fill-current" viewBox="0 0 20 20"><path d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z" /></svg>
              {averageRating} · {reviews?.totalElements} {t('entity.reviews_count')}
            </span>
          )}
        </div>

        {points && points.length > 1 && (
          <div className="flex gap-2 mb-5 overflow-x-auto pb-1">
            {points.map((p) => (
              <button
                key={p.id}
                onClick={() => setSelectedPointId(p.id)}
                className={`shrink-0 text-sm px-3 py-1.5 rounded-full border transition-colors ${
                  (activePoint?.id === p.id) ? 'border-[var(--color-accent)] bg-[var(--color-accent)]/5 text-[var(--color-accent)]' : 'border-[var(--color-border)] text-[var(--color-ink-muted)]'
                }`}
              >
                {p.name}
              </button>
            ))}
          </div>
        )}

        {pointsLoading ? (
          <Skeleton className="h-24 mb-6" />
        ) : activePoint && (
          <div className="bg-[var(--color-surface)] rounded-xl p-4 mb-6 space-y-2 text-sm">
            {(activePoint.address || activePoint.coverageZone) && (
              <div className="flex items-start gap-2">
                <svg className="w-4 h-4 text-[var(--color-ink-muted)] mt-0.5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a2 2 0 01-2.828 0l-4.243-4.243a8 8 0 1111.314 0z M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                <span>{activePoint.address || activePoint.coverageZone}</span>
              </div>
            )}
            {activePoint.phoneNumber && (
              <div className="flex items-center gap-2">
                <svg className="w-4 h-4 text-[var(--color-ink-muted)] shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                </svg>
                <span>{activePoint.phoneNumber}</span>
              </div>
            )}
            {hours && hours.length > 0 ? (
              <details className="pt-1">
                <summary className="cursor-pointer text-[var(--color-accent)] text-xs font-medium">{t('entity.see_hours')}</summary>
                <table className="w-full mt-2 text-xs">
                  <tbody>
                    {hours.map((h) => (
                      <tr key={h.dayOfWeek}>
                        <td className="py-0.5 text-[var(--color-ink-muted)]">{t(`entity.${DAY_LABELS[h.dayOfWeek]}`)}</td>
                        <td className="py-0.5 text-right">
                          {h.closed ? t('entity.day_closed') : `${h.openingTime?.slice(0, 5)} – ${h.closingTime?.slice(0, 5)}`}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </details>
            ) : (
              <p className="text-xs text-[var(--color-ink-muted)] pt-1">{t('entity.no_hours_set')}</p>
            )}
          </div>
        )}

        <div>
          <p className="text-sm font-medium mb-3">{t('entity.reviews_title')} {reviews?.totalElements ? `(${reviews.totalElements})` : ''}</p>
          {!reviews?.content.length ? (
            <p className="text-sm text-[var(--color-ink-muted)]">{t('entity.no_reviews')}</p>
          ) : (
            <div className="space-y-4">
              {reviews.content.slice(0, 3).map((r) => (
                <div key={r.id} className="border-b border-[var(--color-border)] pb-4">
                  <div className="flex items-center gap-1 mb-1">
                    {Array.from({ length: 5 }).map((_, i) => (
                      <svg key={i} className={`w-3.5 h-3.5 ${i < r.rating ? 'fill-current text-[var(--color-accent-warm)]' : 'fill-current text-[var(--color-border)]'}`} viewBox="0 0 20 20">
                        <path d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z" />
                      </svg>
                    ))}
                  </div>
                  {r.comment && <p className="text-sm">{r.comment}</p>}
                  {r.responseContent && (
                    <div className="mt-2 ml-3 pl-3 border-l-2 border-[var(--color-border)] text-sm text-[var(--color-ink-muted)]">
                      {r.responseContent}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </main>

      {activePoint?.phoneNumber && (
        <div className="fixed bottom-0 left-0 right-0 bg-[var(--color-bg)] border-t border-[var(--color-border)] p-4">
          <a
          
            href={`tel:${activePoint.phoneNumber}`}
            className="max-w-3xl mx-auto flex items-center justify-center gap-2 h-12 rounded-lg bg-[var(--color-accent)] text-white font-medium"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
            </svg>
            {t('entity.call')}
          </a>
        </div>
      )}

      <Footer />
    </div>
  )
}

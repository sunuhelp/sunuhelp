import { useMemo, useState } from 'react'
import { useSearchParams, useNavigate, Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useSearch } from '../hooks/useSearch'
import { useGeolocation } from '../hooks/useGeolocation'
import { haversineKm } from '../lib/distance'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { Skeleton } from '../components/ui/Skeleton'
import { TrustBadge } from '../components/ui/TrustBadge'

const RADIUS_STEPS = [5, 10, 15, 20, 50]

export default function ResultsPage() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const query = searchParams.get('query') ?? ''
  const categorySlug = searchParams.get('categorySlug') ?? undefined
  const [radiusKm, setRadiusKm] = useState(Number(searchParams.get('radiusKm')) || 5)
  const [openNowOnly, setOpenNowOnly] = useState(false)
  const [showFilters, setShowFilters] = useState(false)

  const geo = useGeolocation()

  const { data: results, isLoading } = useSearch({
    query: query || undefined,
    categorySlug,
    latitude: geo.latitude ?? undefined,
    longitude: geo.longitude ?? undefined,
    radiusKm,
    openNowOnly: openNowOnly || undefined,
  })

  const enriched = useMemo(() => {
    if (!results) return []
    return results
      .map((r) => ({
        ...r,
        distanceKm: geo.latitude != null && geo.longitude != null
          ? haversineKm(geo.latitude, geo.longitude, r.latitude, r.longitude)
          : null,
      }))
      .sort((a, b) => (a.distanceKm ?? 0) - (b.distanceKm ?? 0))
  }, [results, geo.latitude, geo.longitude])

  const expandRadius = () => {
    const next = RADIUS_STEPS.find((r) => r > radiusKm)
    if (next) setRadiusKm(next)
  }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-3xl mx-auto w-full px-6 py-6">
        <div className="flex items-center justify-between mb-5">
          <div>
            <h1 className="text-lg font-medium">
              {query ? t('results.title_query', { query }) : t('results.title_category')}
            </h1>
            {geo.status === 'denied' && (
              <p className="text-xs text-[var(--color-ink-muted)] mt-0.5">{t('results.location_denied_hint')}</p>
            )}
          </div>
          <button
            onClick={() => setShowFilters(true)}
            className="flex items-center gap-1.5 text-sm px-3 py-2 rounded-lg border border-[var(--color-border)] shrink-0"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
            </svg>
            {t('results.filters')} · {radiusKm}km
          </button>
        </div>

        {isLoading ? (
          <div className="space-y-3">
            {Array.from({ length: 4 }).map((_, i) => <Skeleton key={i} className="h-24" />)}
          </div>
        ) : !enriched.length ? (
          <div className="text-center py-16">
            <svg className="w-10 h-10 text-[var(--color-ink-muted)] mx-auto mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p className="text-sm font-medium mb-1">{t('results.empty_title', { radius: radiusKm })}</p>
            <p className="text-sm text-[var(--color-ink-muted)] mb-4">{t('results.empty_body')}</p>
            {RADIUS_STEPS.some((r) => r > radiusKm) && (
              <button onClick={expandRadius} className="text-sm px-4 py-2 rounded-lg bg-[var(--color-accent)] text-white font-medium">
                {t('results.expand_radius', { radius: RADIUS_STEPS.find((r) => r > radiusKm) })}
              </button>
            )}
          </div>
        ) : (
          <div className="space-y-3">
            {enriched.map((r) => (
              <Link
                key={r.servicePointId}
                to={`/entites/${r.entityId}`}
                className="block bg-[var(--color-surface)] border border-[var(--color-border)] rounded-xl p-4 hover:border-[var(--color-accent)]/40 transition-colors"
              >
                <div className="flex items-start justify-between gap-3">
                  <div className="min-w-0">
                    <p className="font-medium text-sm truncate">{r.name}</p>
                    {r.description && (
                      <p className="text-xs text-[var(--color-ink-muted)] mt-0.5 line-clamp-1">{r.description}</p>
                    )}
                  </div>
                  {r.distanceKm != null && (
                    <span className="text-xs text-[var(--color-ink-muted)] shrink-0 whitespace-nowrap">
                      {r.distanceKm < 1 ? `${Math.round(r.distanceKm * 1000)} m` : `${r.distanceKm.toFixed(1)} km`}
                    </span>
                  )}
                </div>
                <div className="flex items-center gap-2 mt-2.5 flex-wrap">
                  <TrustBadge trustLevel={r.trustLevel} />
                  <span className={`text-xs px-2 py-0.5 rounded-full ${r.openNow ? 'bg-[color-mix(in_srgb,var(--color-accent)_12%,transparent)] text-[var(--color-accent)]' : 'bg-[var(--color-bg)] text-[var(--color-ink-muted)]'}`}>
                    {r.openNow ? t('entity.open_now') : t('entity.closed_now')}
                  </span>
                  {r.reviewCount > 0 && (
                    <span className="text-xs text-[var(--color-ink-muted)] flex items-center gap-1">
                      <svg className="w-3 h-3 fill-current" viewBox="0 0 20 20"><path d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z" /></svg>
                      {r.averageRating.toFixed(1)}
                    </span>
                  )}
                </div>
              </Link>
            ))}
          </div>
        )}
      </main>

      {showFilters && (
        <div className="fixed inset-0 z-50 flex items-end justify-center bg-black/40" onClick={() => setShowFilters(false)}>
          <div
            className="w-full max-w-lg bg-[var(--color-bg)] rounded-t-2xl p-6 animate-in slide-in-from-bottom duration-200"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="w-9 h-1 bg-[var(--color-border)] rounded-full mx-auto mb-5" />
            <p className="font-medium mb-4">{t('results.filters')}</p>

            <div className="flex items-center justify-between py-3 border-b border-[var(--color-border)]">
              <span className="text-sm">{t('entity.open_now')}</span>
              <button
                onClick={() => setOpenNowOnly((v) => !v)}
                className={`w-10 h-6 rounded-full relative transition-colors ${openNowOnly ? 'bg-[var(--color-accent)]' : 'bg-[var(--color-border)]'}`}
              >
                <span className={`absolute top-1 w-4 h-4 rounded-full bg-white transition-transform ${openNowOnly ? 'translate-x-5' : 'translate-x-1'}`} />
              </button>
            </div>

            <div className="py-4">
              <span className="text-sm block mb-2">{t('search.radius')}</span>
              <div className="flex gap-2 flex-wrap">
                {RADIUS_STEPS.map((km) => (
                  <button
                    key={km}
                    onClick={() => setRadiusKm(km)}
                    className={`text-sm px-3 py-1.5 rounded-lg border ${radiusKm === km ? 'bg-[var(--color-accent)] text-white border-[var(--color-accent)]' : 'border-[var(--color-border)]'}`}
                  >
                    {km} km
                  </button>
                ))}
              </div>
            </div>

            <button
              onClick={() => setShowFilters(false)}
              className="w-full h-12 mt-4 rounded-lg bg-[var(--color-accent)] text-white font-medium"
            >
              {t('results.show_results', { count: enriched.length })}
            </button>
          </div>
        </div>
      )}

      <Footer />
    </div>
  )
}

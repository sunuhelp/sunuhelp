import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { motion, AnimatePresence } from 'framer-motion'
import { ArrowLeft, ArrowRight, Clock, ShieldCheck, Image, CircleCheck } from 'lucide-react'
import { useEntity } from '../hooks/useEntity'
import { useServicePoints, useOpeningHours } from '../hooks/useServicePoints'
import { useRootCategorySlug } from '../hooks/useRootCategorySlug'
import { getCategoryIcon } from '../lib/categoryIcons'
import { formatHoursSummary } from '../lib/formatHours'
import { fetchMediaByOwner } from '../api/media'
import { useQuery } from '@tanstack/react-query'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { HoursDialog } from '../components/HoursDialog'
import { PhotoThumbnailPreview } from '../components/PhotoThumbnailPreview'
import { useAuthStore } from '../stores/authStore'

const cardVariants = {
  hidden: { opacity: 0, y: 10 },
  visible: (i: number) => ({ opacity: 1, y: 0, transition: { delay: i * 0.06, duration: 0.3, ease: 'easeOut' } }),
}

export default function EntityManagePage() {
  const { t } = useTranslation()
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { data: entity, isLoading: entityLoading } = useEntity(id)
  const { data: rootSlug } = useRootCategorySlug(entity?.categoryId)
  const { data: points } = useServicePoints(id)
  const primaryPoint = points?.[0]
  const { data: existingHours, refetch } = useOpeningHours(primaryPoint?.id)
  const { data: galleryPhotos } = useQuery({
    queryKey: ['media', 'gallery', id],
    queryFn: () => fetchMediaByOwner('ENTITY_PHOTO', id!),
    enabled: !!id,
  })
  const setLastEntity = useAuthStore((s) => s.setLastEntity)

  const [hoursOpen, setHoursOpen] = useState(false)
  const hasSavedOnce = !!(existingHours && existingHours.length > 0)
  const hasPhotos = !!(galleryPhotos && galleryPhotos.length > 0)
  const Icon = getCategoryIcon(rootSlug)
  const hoursSummary = existingHours ? formatHoursSummary(existingHours) : []
  const completedCount = (hasSavedOnce ? 1 : 0) + (hasPhotos ? 1 : 0)

  useEffect(() => {
    if (entity) setLastEntity({ id: entity.id, name: entity.name })
  }, [entity, setLastEntity])

  if (entityLoading) {
    return (
      <div className="min-h-screen bg-[var(--color-bg)] flex flex-col">
        <Header />
        <div className="max-w-5xl mx-auto w-full px-7 py-10"><div className="h-60 rounded-xl bg-[var(--color-surface)] animate-pulse" /></div>
        <Footer />
      </div>
    )
  }

  if (!entity || !id) return null

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-5xl mx-auto w-full px-7 py-8">
        <button onClick={() => navigate('/mes-etablissements')} className="flex items-center gap-1.5 text-sm text-[var(--color-ink-muted)] hover:text-[var(--color-ink)] transition-colors mb-5">
          <ArrowLeft size={15} strokeWidth={1.75} aria-hidden="true" /> {t('account.my_businesses')}
        </button>

        <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.3 }} className="flex items-center gap-4 mb-3">
          <div className="w-[58px] h-[58px] rounded-2xl bg-[var(--color-accent)] flex items-center justify-center shrink-0">
            <Icon size={28} className="text-white" strokeWidth={1.75} aria-hidden="true" />
          </div>
          <div>
            <p className="text-[11px] text-[var(--color-accent)] font-medium mb-0.5">{t('manage.private_label')}</p>
            <h1 className="text-[22px] font-medium">{entity.name}</h1>
          </div>
        </motion.div>

        <p className="text-sm text-[var(--color-ink-muted)] leading-relaxed max-w-xl mb-4">{t('manage.intro')}</p>

        <div className="flex items-center gap-2.5 mb-7 max-w-[280px]">
          <div className="flex-1 h-[5px] bg-[var(--color-border)] rounded-full overflow-hidden">
            <motion.div
              className="h-full bg-[var(--color-accent)]"
              initial={{ width: 0 }}
              animate={{ width: `${(completedCount / 3) * 100}%` }}
              transition={{ duration: 0.6, ease: 'easeOut', delay: 0.2 }}
            />
          </div>
          <span className="text-xs text-[var(--color-ink-muted)] whitespace-nowrap">{t('manage.completion', { count: completedCount })}</span>
        </div>

        <div className="grid sm:grid-cols-3 gap-3.5">
          <motion.button
            custom={0}
            initial="hidden"
            animate="visible"
            variants={cardVariants}
            whileHover={{ y: -3, boxShadow: '0 8px 20px rgba(15,107,98,0.14)' }}
            whileTap={{ scale: 0.98 }}
            onClick={() => setHoursOpen(true)}
            className={`group text-left p-[18px] rounded-2xl bg-[var(--color-surface)] flex flex-col gap-2.5 ${
              hasSavedOnce ? 'border-[1.5px] border-[var(--color-accent)]/40' : 'border-[1.5px] border-dashed border-[var(--color-warning)]'
            }`}
          >
            <div className="flex items-center justify-between">
              <Clock size={22} className={hasSavedOnce ? 'text-[var(--color-success)]' : 'text-[var(--color-warning)]'} strokeWidth={1.75} aria-hidden="true" />
              <AnimatePresence mode="wait">
                {hasSavedOnce ? (
                  <motion.div key="done" initial={{ scale: 0 }} animate={{ scale: 1 }} transition={{ type: 'spring', stiffness: 400, damping: 15 }}>
                    <CircleCheck size={18} className="text-[var(--color-success)]" strokeWidth={1.75} aria-hidden="true" />
                  </motion.div>
                ) : (
                  <span key="todo" className="text-[11px] px-2.5 py-0.5 rounded-full bg-[var(--color-bg)] text-[var(--color-warning)] font-medium">{t('manage.todo_badge')}</span>
                )}
              </AnimatePresence>
            </div>
            <div>
              <p className="text-sm font-medium mb-1">{t('manage.hours_title')}</p>
              {hasSavedOnce ? (
                <p className="text-xs text-[var(--color-ink-muted)] leading-relaxed">
                  {hoursSummary.map((line, i) => <span key={i}>{line}<br /></span>)}
                </p>
              ) : (
                <p className="text-xs text-[var(--color-ink-muted)] leading-relaxed">{t('manage.hours_description')}</p>
              )}
            </div>
            <span className={`text-xs font-medium flex items-center gap-1 ${hasSavedOnce ? 'text-[var(--color-accent)]' : 'text-[var(--color-warning)]'}`}>
              {hasSavedOnce ? t('manage.edit') : t('manage.complete')}
              <ArrowRight size={13} strokeWidth={2} className="transition-transform group-hover:translate-x-0.5" aria-hidden="true" />
            </span>
          </motion.button>

          <motion.button
            custom={1}
            initial="hidden"
            animate="visible"
            variants={cardVariants}
            whileHover={{ y: -3, boxShadow: '0 8px 20px rgba(15,107,98,0.14)' }}
            whileTap={{ scale: 0.98 }}
            onClick={() => navigate(`/mes-etablissements/${id}/photos`)}
            className={`group text-left p-[18px] rounded-2xl bg-[var(--color-surface)] flex flex-col gap-2.5 ${
              hasPhotos ? 'border-[1.5px] border-[var(--color-accent)]/40' : 'border-[1.5px] border-dashed border-[var(--color-warning)]'
            }`}
          >
            <div className="flex items-center justify-between">
              <Image size={22} className={hasPhotos ? 'text-[var(--color-success)]' : 'text-[var(--color-warning)]'} strokeWidth={1.75} aria-hidden="true" />
              {hasPhotos ? (
                <CircleCheck size={18} className="text-[var(--color-success)]" strokeWidth={1.75} aria-hidden="true" />
              ) : (
                <span className="text-[11px] px-2.5 py-0.5 rounded-full bg-[var(--color-bg)] text-[var(--color-warning)] font-medium">{t('manage.todo_badge')}</span>
              )}
            </div>
            <div>
              <p className="text-sm font-medium mb-1">{t('manage.photos_title')}</p>
              {hasPhotos ? (
                <div className="flex gap-1.5">
                  {galleryPhotos!.slice(0, 3).map((p) => <PhotoThumbnailPreview key={p.id} mediaId={p.id} />)}
                  {galleryPhotos!.length > 3 && (
                    <div className="w-[38px] h-[38px] rounded-lg bg-[var(--color-bg)] flex items-center justify-center text-xs font-medium text-[var(--color-ink-muted)]">
                      +{galleryPhotos!.length - 3}
                    </div>
                  )}
                </div>
              ) : (
                <p className="text-xs text-[var(--color-ink-muted)] leading-relaxed">{t('manage.photos_description')}</p>
              )}
            </div>
            <span className={`text-xs font-medium flex items-center gap-1 ${hasPhotos ? 'text-[var(--color-accent)]' : 'text-[var(--color-warning)]'}`}>
              {hasPhotos ? t('manage.edit') : t('manage.complete')}
              <ArrowRight size={13} strokeWidth={2} className="transition-transform group-hover:translate-x-0.5" aria-hidden="true" />
            </span>
          </motion.button>

          <motion.div custom={2} initial="hidden" animate="visible" variants={cardVariants} className="p-[18px] rounded-2xl bg-[var(--color-surface)] opacity-60">
            <ShieldCheck size={22} className="text-[var(--color-ink-muted)] mb-2.5" strokeWidth={1.75} aria-hidden="true" />
            <p className="text-sm font-medium text-[var(--color-ink-muted)] mb-0.5">{t('manage.verification_title')}</p>
            <p className="text-xs text-[var(--color-ink-muted)] mb-2">{t('manage.status_soon')}</p>
            <p className="text-xs text-[var(--color-ink-muted)] leading-relaxed">{t('manage.verification_description')}</p>
          </motion.div>
        </div>
      </main>

      <HoursDialog
        open={hoursOpen}
        onClose={() => setHoursOpen(false)}
        servicePointId={primaryPoint?.id}
        existingHours={existingHours}
        onSaved={refetch}
      />


      <Footer />
    </div>
  )
}

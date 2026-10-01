import { useRef, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { ArrowLeft, Camera, Upload, ImagePlay, ChevronDown } from 'lucide-react'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { PhotoThumbnail } from '../components/PhotoThumbnail'
import { PhotoViewer } from '../components/PhotoViewer'
import { useEntity } from '../hooks/useEntity'
import { useMediaImage } from '../hooks/useMediaImage'
import { fetchMediaByOwner, uploadMedia, deleteMedia } from '../api/media'
import { notify } from '../lib/toast'

const MAX_SIZE = 5 * 1024 * 1024
const MAX_PHOTOS = 40
const INITIAL_VISIBLE = 11
const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/webp']

export default function EntityPhotosPage() {
  const { t } = useTranslation()
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const logoInputRef = useRef<HTMLInputElement>(null)
  const galleryInputRef = useRef<HTMLInputElement>(null)

  const { data: entity } = useEntity(id)
  const { data: logoFiles, refetch: refetchLogo } = useQuery({
    queryKey: ['media', 'logo', id],
    queryFn: () => fetchMediaByOwner('ENTITY_LOGO', id!),
    enabled: !!id,
  })
  const { data: galleryFiles, refetch: refetchGallery } = useQuery({
    queryKey: ['media', 'gallery', id],
    queryFn: () => fetchMediaByOwner('ENTITY_PHOTO', id!),
    enabled: !!id,
  })

  const [viewerIndex, setViewerIndex] = useState<number | null>(null)
  const [showAll, setShowAll] = useState(false)
  const [isUploadingLogo, setIsUploadingLogo] = useState(false)
  const [isUploadingGallery, setIsUploadingGallery] = useState(false)
  const [isDragging, setIsDragging] = useState(false)

  const logo = logoFiles?.[0]
  const logoUrl = useMediaImage(logo?.id)
  const gallery = galleryFiles ?? []
  const visibleGallery = showAll ? gallery : gallery.slice(0, INITIAL_VISIBLE)
  const remainingCount = gallery.length - INITIAL_VISIBLE

  const validateFile = (file: File): string | null => {
    if (!ALLOWED_TYPES.includes(file.type)) return t('manage.photos.invalid_type')
    if (file.size > MAX_SIZE) return t('manage.photos.too_large')
    return null
  }

  const handleLogoSelect = async (file: File | undefined) => {
    if (!file) return
    const error = validateFile(file)
    if (error) { notify.error(error); return }

    setIsUploadingLogo(true)
    try {
      if (logo) await deleteMedia(logo.id)
      await uploadMedia(file, 'ENTITY_LOGO', id!)
      refetchLogo()
      queryClient.invalidateQueries({ queryKey: ['entities', 'mine'] })
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsUploadingLogo(false)
    }
  }

  const handleGalleryFiles = async (files: File[]) => {
    if (!files.length) return
    if (gallery.length + files.length > MAX_PHOTOS) {
      notify.error(t('manage.photos.max_reached', { max: MAX_PHOTOS }))
      return
    }
    setIsUploadingGallery(true)
    try {
      for (const file of files) {
        const error = validateFile(file)
        if (error) { notify.error(`${file.name} : ${error}`); continue }
        await uploadMedia(file, 'ENTITY_PHOTO', id!)
      }
      refetchGallery()
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsUploadingGallery(false)
    }
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragging(false)
    handleGalleryFiles(Array.from(e.dataTransfer.files))
  }

  const handleDeletePhoto = async (mediaId: string) => {
    try {
      await deleteMedia(mediaId)
      notify.success(t('manage.photos.deleted'))
      refetchGallery()
      setViewerIndex(null)
    } catch {
      notify.error(t('errors.generic'))
    }
  }

  if (!entity || !id) return null

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 max-w-5xl mx-auto w-full px-7 py-8">
        <button onClick={() => navigate(`/mes-etablissements/${id}`)} className="flex items-center gap-1.5 text-sm text-[var(--color-ink-muted)] hover:text-[var(--color-ink)] transition-colors mb-6">
          <ArrowLeft size={15} strokeWidth={1.75} aria-hidden="true" /> {entity.name}
        </button>

        <div className="grid sm:grid-cols-[230px_1fr] gap-5">
          <div className="bg-[var(--color-surface)] rounded-2xl p-6 flex flex-col items-center text-center">
            <div className="flex items-center gap-1.5 mb-1">
              <Camera size={16} className="text-[var(--color-accent)]" strokeWidth={1.75} aria-hidden="true" />
              <p className="text-[15px] font-semibold">{t('manage.photos.logo_label')}</p>
            </div>
            <p className="text-[11px] text-[var(--color-ink-muted)] leading-relaxed mb-4">{t('manage.photos.logo_hint')}</p>

            <button
              onClick={() => logoInputRef.current?.click()}
              disabled={isUploadingLogo}
              className="group relative w-[150px] h-[150px] rounded-3xl overflow-hidden mb-3"
            >
              {logo && logoUrl ? (
                <>
                  <img src={logoUrl} alt="" className="w-full h-full object-cover" />
                  <div className="absolute inset-0 bg-black/0 group-hover:bg-black/45 transition-colors" />
                </>
              ) : (
                <div className="w-full h-full border-[1.5px] border-dashed border-[var(--color-border-strong)] rounded-3xl flex items-center justify-center bg-[var(--color-bg)]">
                  {isUploadingLogo ? (
                    <div className="w-6 h-6 border-2 border-[var(--color-border)] border-t-[var(--color-accent)] rounded-full animate-spin" />
                  ) : (
                    <Camera size={28} className="text-[var(--color-ink-muted)]" strokeWidth={1.5} aria-hidden="true" />
                  )}
                </div>
              )}
            </button>
            <span className="text-xs text-[var(--color-accent)] font-medium">{logo ? t('manage.photos.replace') : t('manage.photos.add')}</span>
            <input ref={logoInputRef} type="file" accept="image/*" className="hidden" onChange={(e) => handleLogoSelect(e.target.files?.[0])} />
          </div>

          <div className="bg-[var(--color-surface)] rounded-2xl p-6">
            <div className="flex items-center gap-1.5 mb-1">
              <ImagePlay size={16} className="text-[var(--color-accent)]" strokeWidth={1.75} aria-hidden="true" />
              <p className="text-[15px] font-semibold">{t('manage.photos.gallery_label')}</p>
              {gallery.length > 0 && <span className="text-sm text-[var(--color-ink-muted)]">· {gallery.length}</span>}
            </div>
            <p className="text-[11px] text-[var(--color-ink-muted)] mb-4">{t('manage.photos.gallery_hint')}</p>

            <div
              onDragOver={(e) => { e.preventDefault(); setIsDragging(true) }}
              onDragLeave={() => setIsDragging(false)}
              onDrop={handleDrop}
              className={`grid grid-cols-3 sm:grid-cols-6 gap-2.5 ${isDragging ? 'ring-2 ring-[var(--color-accent)] rounded-xl' : ''}`}
            >
              {visibleGallery.map((photo, i) => (
                <PhotoThumbnail
                  key={photo.id}
                  mediaId={photo.id}
                  isCover={i === 0}
                  coverLabel={t('manage.photos.cover_badge')}
                  onView={() => setViewerIndex(i)}
                  onDelete={() => handleDeletePhoto(photo.id)}
                />
              ))}

              {gallery.length < MAX_PHOTOS && (showAll || gallery.length <= INITIAL_VISIBLE) && (
                <button
                  onClick={() => galleryInputRef.current?.click()}
                  disabled={isUploadingGallery}
                  className="aspect-square rounded-xl border-[1.5px] border-dashed border-[var(--color-border-strong)] hover:border-[var(--color-accent)]/50 transition-colors flex flex-col items-center justify-center gap-1 bg-[var(--color-bg)]"
                >
                  {isUploadingGallery ? (
                    <div className="w-5 h-5 border-2 border-[var(--color-border)] border-t-[var(--color-accent)] rounded-full animate-spin" />
                  ) : (
                    <>
                      <Upload size={18} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} aria-hidden="true" />
                      <span className="text-[9px] text-[var(--color-ink-muted)] text-center hidden sm:block">{t('manage.photos.drop_hint')}</span>
                    </>
                  )}
                </button>
              )}
            </div>
            <input ref={galleryInputRef} type="file" accept="image/*" multiple className="hidden" onChange={(e) => handleGalleryFiles(Array.from(e.target.files ?? []))} />

            {!showAll && remainingCount > 0 && (
              <button
                onClick={() => setShowAll(true)}
                className="w-full mt-4 py-2.5 rounded-lg bg-[var(--color-bg)] border border-[var(--color-border)] text-sm font-medium text-[var(--color-accent)] flex items-center justify-center gap-1.5 hover:border-[var(--color-accent)]/40 transition-colors"
              >
                {t('manage.photos.see_more', { count: remainingCount })}
                <ChevronDown size={15} strokeWidth={1.75} aria-hidden="true" />
              </button>
            )}
          </div>
        </div>
      </main>

      {viewerIndex !== null && (
        <PhotoViewer
          mediaIds={gallery.map((p) => p.id)}
          index={viewerIndex}
          onClose={() => setViewerIndex(null)}
          onNavigate={setViewerIndex}
          onDelete={handleDeletePhoto}
        />
      )}

      <Footer />
    </div>
  )
}

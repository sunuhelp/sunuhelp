import { useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { Camera, Plus } from 'lucide-react'
import { Dialog } from './Dialog'
import { PhotoThumbnail } from './PhotoThumbnail'
import { PhotoViewer } from './PhotoViewer'
import { fetchMediaByOwner, uploadMedia, deleteMedia } from '../api/media'
import { useMediaImage } from '../hooks/useMediaImage'
import { notify } from '../lib/toast'

const MAX_SIZE = 5 * 1024 * 1024
const MAX_PHOTOS = 20
const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/webp']

interface PhotosDialogProps {
  open: boolean
  onClose: () => void
  entityId: string
}

export function PhotosDialog({ open, onClose, entityId }: PhotosDialogProps) {
  const { t } = useTranslation()
  const queryClient = useQueryClient()
  const logoInputRef = useRef<HTMLInputElement>(null)
  const galleryInputRef = useRef<HTMLInputElement>(null)
  const [viewerIndex, setViewerIndex] = useState<number | null>(null)
  const [isUploadingLogo, setIsUploadingLogo] = useState(false)
  const [isUploadingGallery, setIsUploadingGallery] = useState(false)

  const { data: logoFiles, refetch: refetchLogo } = useQuery({
    queryKey: ['media', 'logo', entityId],
    queryFn: () => fetchMediaByOwner('ENTITY_LOGO', entityId),
    enabled: open,
  })
  const { data: galleryFiles, refetch: refetchGallery } = useQuery({
    queryKey: ['media', 'gallery', entityId],
    queryFn: () => fetchMediaByOwner('ENTITY_PHOTO', entityId),
    enabled: open,
  })

  const logo = logoFiles?.[0]
  const logoUrl = useMediaImage(logo?.id)
  const gallery = galleryFiles ?? []

  const validateFile = (file: File): string | null => {
    if (!ALLOWED_TYPES.includes(file.type)) return t('manage.photos.invalid_type')
    if (file.size > MAX_SIZE) return t('manage.photos.too_large')
    return null
  }

  const handleLogoSelect = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    e.target.value = ''
    if (!file) return
    const error = validateFile(file)
    if (error) { notify.error(error); return }

    setIsUploadingLogo(true)
    try {
      if (logo) await deleteMedia(logo.id)
      await uploadMedia(file, 'ENTITY_LOGO', entityId)
      refetchLogo()
      queryClient.invalidateQueries({ queryKey: ['entities', 'mine'] })
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsUploadingLogo(false)
    }
  }

  const handleGallerySelect = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files ?? [])
    e.target.value = ''
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
        await uploadMedia(file, 'ENTITY_PHOTO', entityId)
      }
      refetchGallery()
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsUploadingGallery(false)
    }
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

  return (
    <>
      <Dialog open={open} onClose={onClose} title={t('manage.photos_title')} wide>
        <div className="mb-6">
          <p className="text-sm font-medium mb-1">{t('manage.photos.logo_label')}</p>
          <p className="text-xs text-[var(--color-ink-muted)] mb-3">{t('manage.photos.logo_hint')}</p>
          <button
            onClick={() => logoInputRef.current?.click()}
            disabled={isUploadingLogo}
            className={`w-[72px] h-[72px] rounded-2xl flex items-center justify-center overflow-hidden transition-colors ${
              logo ? 'bg-[var(--color-accent)]' : 'border-[1.5px] border-dashed border-[var(--color-border-strong)] hover:border-[var(--color-accent)]/50'
            }`}
          >
            {isUploadingLogo ? (
              <div className="w-5 h-5 border-2 border-white/40 border-t-white rounded-full animate-spin" />
            ) : logo && logoUrl ? (
              <img src={logoUrl} alt="" className="w-full h-full object-cover" />
            ) : (
              <Camera size={24} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} aria-hidden="true" />
            )}
          </button>
          <input ref={logoInputRef} type="file" accept="image/*" className="hidden" onChange={handleLogoSelect} />
        </div>

        <div>
          <div className="flex items-baseline gap-2 mb-1">
            <p className="text-sm font-medium">{t('manage.photos.gallery_label')}</p>
            {gallery.length > 0 && <span className="text-xs text-[var(--color-ink-muted)]">· {gallery.length}</span>}
          </div>
          <p className="text-xs text-[var(--color-ink-muted)] mb-3">{t('manage.photos.gallery_hint')}</p>

          <div className="grid grid-cols-4 gap-2 max-h-[190px] overflow-y-auto pr-0.5">
            {gallery.map((photo, i) => (
              <PhotoThumbnail
                key={photo.id}
                mediaId={photo.id}
                onView={() => setViewerIndex(i)}
                onDelete={() => handleDeletePhoto(photo.id)}
              />
            ))}

            {gallery.length < MAX_PHOTOS && (
              <button
                onClick={() => galleryInputRef.current?.click()}
                disabled={isUploadingGallery}
                className="aspect-square rounded-lg border-[1.5px] border-dashed border-[var(--color-border-strong)] hover:border-[var(--color-accent)]/50 transition-colors flex items-center justify-center"
              >
                {isUploadingGallery ? (
                  <div className="w-4 h-4 border-2 border-[var(--color-border)] border-t-[var(--color-accent)] rounded-full animate-spin" />
                ) : (
                  <Plus size={18} className="text-[var(--color-ink-muted)]" strokeWidth={1.75} aria-hidden="true" />
                )}
              </button>
            )}
          </div>
          <input ref={galleryInputRef} type="file" accept="image/*" multiple className="hidden" onChange={handleGallerySelect} />
        </div>
      </Dialog>

      {viewerIndex !== null && (
        <PhotoViewer
          mediaIds={gallery.map((p) => p.id)}
          index={viewerIndex}
          onClose={() => setViewerIndex(null)}
          onNavigate={setViewerIndex}
          onDelete={handleDeletePhoto}
        />
      )}
    </>
  )
}

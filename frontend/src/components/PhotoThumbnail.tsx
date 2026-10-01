import { Eye, X } from 'lucide-react'
import { motion } from 'framer-motion'
import { useMediaImage } from '../hooks/useMediaImage'

interface PhotoThumbnailProps {
  mediaId: string
  onView: () => void
  onDelete: () => void
  isCover?: boolean
  coverLabel?: string
}

export function PhotoThumbnail({ mediaId, onView, onDelete, isCover, coverLabel }: PhotoThumbnailProps) {
  const url = useMediaImage(mediaId)

  return (
    <motion.div
      whileHover={{ scale: 1.05 }}
      transition={{ duration: 0.15 }}
      className="relative aspect-square rounded-xl overflow-hidden bg-[var(--color-bg)] cursor-pointer group"
      onClick={onView}
    >
      {url ? (
        <img src={url} alt="" className="w-full h-full object-cover" />
      ) : (
        <div className="w-full h-full animate-pulse bg-[var(--color-border)]" />
      )}

      {isCover && (
        <span className="absolute bottom-1.5 left-1.5 text-[9px] font-medium px-2 py-0.5 rounded-full bg-black/60 text-white">
          {coverLabel}
        </span>
      )}

      <div className="absolute inset-0 bg-black/0 group-hover:bg-black/35 transition-colors flex items-center justify-center">
        <Eye size={20} className="text-white opacity-0 group-hover:opacity-100 transition-opacity" strokeWidth={1.75} aria-hidden="true" />
      </div>

      <button
        onClick={(e) => { e.stopPropagation(); onDelete() }}
        className="absolute top-1.5 right-1.5 w-5 h-5 rounded-full bg-black/60 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity hover:bg-black/80"
        aria-label="Supprimer"
      >
        <X size={11} className="text-white" strokeWidth={2.5} aria-hidden="true" />
      </button>
    </motion.div>
  )
}

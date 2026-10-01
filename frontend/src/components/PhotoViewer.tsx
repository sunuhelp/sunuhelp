import { AnimatePresence, motion } from 'framer-motion'
import { X, ChevronLeft, ChevronRight, Trash2 } from 'lucide-react'
import { useMediaImage } from '../hooks/useMediaImage'

interface PhotoViewerProps {
  mediaIds: string[]
  index: number
  onClose: () => void
  onNavigate: (index: number) => void
  onDelete: (mediaId: string) => void
}

export function PhotoViewer({ mediaIds, index, onClose, onNavigate, onDelete }: PhotoViewerProps) {
  const currentId = mediaIds[index]
  const url = useMediaImage(currentId)

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="fixed inset-0 z-[60] bg-black/90 flex flex-col"
        onClick={onClose}
      >
        <div className="flex items-center justify-between px-6 py-4" onClick={(e) => e.stopPropagation()}>
          <span className="text-sm text-white/60">{index + 1} / {mediaIds.length}</span>
          <div className="flex items-center gap-5">
            <button onClick={() => onDelete(currentId)} aria-label="Supprimer">
              <Trash2 size={19} className="text-white/85 hover:text-white transition-colors" strokeWidth={1.75} />
            </button>
            <button onClick={onClose} aria-label="Fermer">
              <X size={22} className="text-white hover:opacity-80 transition-opacity" strokeWidth={1.75} />
            </button>
          </div>
        </div>

        <div className="flex-1 flex items-center justify-between gap-3 px-4 pb-6" onClick={(e) => e.stopPropagation()}>
          <button
            onClick={() => onNavigate(index - 1)}
            disabled={index === 0}
            className="shrink-0 disabled:opacity-30"
            aria-label="Précédent"
          >
            <ChevronLeft size={28} className="text-white/70 hover:text-white transition-colors" strokeWidth={1.75} />
          </button>

          <div className="flex-1 max-w-2xl aspect-[4/3] bg-white/5 rounded-xl flex items-center justify-center overflow-hidden">
            {url ? (
              <img src={url} alt="" className="w-full h-full object-contain" />
            ) : (
              <div className="w-10 h-10 border-2 border-white/30 border-t-white rounded-full animate-spin" />
            )}
          </div>

          <button
            onClick={() => onNavigate(index + 1)}
            disabled={index === mediaIds.length - 1}
            className="shrink-0 disabled:opacity-30"
            aria-label="Suivant"
          >
            <ChevronRight size={28} className="text-white hover:opacity-80 transition-opacity" strokeWidth={1.75} />
          </button>
        </div>
      </motion.div>
    </AnimatePresence>
  )
}

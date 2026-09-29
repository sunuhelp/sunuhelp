import { X } from 'lucide-react'
import { motion, AnimatePresence } from 'framer-motion'

interface DialogProps {
  open: boolean
  onClose: () => void
  title: string
  children: React.ReactNode
}

export function Dialog({ open, onClose, title, children }: DialogProps) {
  return (
    <AnimatePresence>
      {open && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.15 }}
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/45 px-4"
          onClick={onClose}
        >
          <motion.div
            initial={{ opacity: 0, scale: 0.96, y: 8 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.96, y: 8 }}
            transition={{ duration: 0.18, ease: 'easeOut' }}
            role="dialog"
            aria-modal="true"
            aria-label={title}
            className="w-full max-w-[380px] bg-[var(--color-surface)] rounded-2xl p-7"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="flex items-center justify-between mb-5">
              <p className="text-base font-medium">{title}</p>
              <button onClick={onClose} aria-label="Fermer" className="text-[var(--color-ink-muted)] hover:text-[var(--color-ink)] transition-colors">
                <X size={18} strokeWidth={1.75} />
              </button>
            </div>
            {children}
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  )
}

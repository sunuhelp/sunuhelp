import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { Heart, Store, LogOut } from 'lucide-react'
import { useAuthStore } from '../stores/authStore'
import { notify } from '../lib/toast'

export function AccountMenu() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const logout = useAuthStore((s) => s.logout)
  const phoneNumber = useAuthStore((s) => s.phoneNumber)
  const [open, setOpen] = useState(false)
  const [confirmingLogout, setConfirmingLogout] = useState(false)
  const ref = useRef<HTMLDivElement>(null)

  const lastTwoDigits = phoneNumber?.slice(-2) ?? '··'

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) { setOpen(false); setConfirmingLogout(false) }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const handleLogout = () => {
    logout()
    notify.info(t('auth.logged_out'))
    navigate('/')
    setOpen(false)
    setConfirmingLogout(false)
  }

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setOpen((v) => !v)}
        aria-haspopup="menu"
        aria-expanded={open}
        className="flex items-center justify-center w-9 h-9 rounded-full bg-[var(--color-accent)] text-white text-xs font-medium hover:opacity-90 transition-opacity"
      >
        {lastTwoDigits}
      </button>

      {open && (
        <div role="menu" className="absolute right-0 mt-2 w-56 bg-[var(--color-bg)] border border-[var(--color-border)] rounded-lg shadow-lg py-1 z-50">
          {!confirmingLogout ? (
            <>
              <button role="menuitem" onClick={() => navigate('/favoris')} className="w-full flex items-center gap-2.5 text-left px-3 py-2.5 text-sm hover:bg-[var(--color-surface)] transition-colors">
                <Heart size={15} strokeWidth={1.75} aria-hidden="true" /> {t('account.favorites')}
              </button>
              <button role="menuitem" onClick={() => navigate('/mes-etablissements')} className="w-full flex items-center gap-2.5 text-left px-3 py-2.5 text-sm hover:bg-[var(--color-surface)] transition-colors">
                <Store size={15} strokeWidth={1.75} aria-hidden="true" /> {t('account.my_businesses')}
              </button>
              <div className="border-t border-[var(--color-border)] my-1" />
              <button role="menuitem" onClick={() => setConfirmingLogout(true)} className="w-full flex items-center gap-2.5 text-left px-3 py-2.5 text-sm text-[var(--color-danger)] hover:bg-[var(--color-surface)] transition-colors">
                <LogOut size={15} strokeWidth={1.75} aria-hidden="true" /> {t('auth.logout')}
              </button>
            </>
          ) : (
            <div className="px-3 py-2.5">
              <p className="text-sm mb-3">{t('auth.logout_confirm')}</p>
              <div className="flex gap-2">
                <button onClick={() => setConfirmingLogout(false)} className="flex-1 text-xs px-3 py-2 rounded-md border border-[var(--color-border)]">{t('common.cancel')}</button>
                <button onClick={handleLogout} className="flex-1 text-xs px-3 py-2 rounded-md bg-[var(--color-danger)] text-white">{t('auth.logout')}</button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

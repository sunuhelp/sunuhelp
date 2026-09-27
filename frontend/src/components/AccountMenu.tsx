import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../stores/authStore'
import { notify } from '../lib/toast'
import { useMyEntities } from '../hooks/useMyEntities'

/**
 * Remplace S'inscrire/Se connecter une fois authentifie. Affiche le nom
 * du premier commerce possede en tete du menu (seule identite reellement
 * disponible aujourd'hui - le prenom/nom personnel n'est jamais collecte,
 * UserProfile existe cote backend mais reste vide tant qu'aucun ecran ne
 * le demande). La deconnexion exige une confirmation - action sensible.
 */
export function AccountMenu() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const logout = useAuthStore((s) => s.logout)
  const { data: myEntities } = useMyEntities()
  const hasBusiness = (myEntities?.totalElements ?? 0) > 0
  const primaryBusinessName = myEntities?.content?.[0]?.name
  const [open, setOpen] = useState(false)
  const [confirmingLogout, setConfirmingLogout] = useState(false)
  const ref = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false)
        setConfirmingLogout(false)
      }
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

  const initial = primaryBusinessName?.charAt(0).toUpperCase() ?? null

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setOpen((v) => !v)}
        aria-haspopup="menu"
        aria-expanded={open}
        className="flex items-center justify-center w-9 h-9 rounded-full bg-[var(--color-accent)] text-white text-sm font-medium hover:opacity-90 transition-opacity"
      >
        {initial ?? (
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
        )}
      </button>

      {open && (
        <div
          role="menu"
          className="absolute right-0 mt-2 w-60 bg-[var(--color-bg)] border border-[var(--color-border)] rounded-lg shadow-lg py-1 z-50"
        >
          <div className="px-3 py-2.5 border-b border-[var(--color-border)]">
            <p className="text-sm font-medium truncate">
              {primaryBusinessName ?? t('account.default_label')}
            </p>
            {hasBusiness && (myEntities?.totalElements ?? 0) > 1 && (
              <p className="text-xs text-[var(--color-ink-muted)] mt-0.5">
                {t('account.other_businesses_count', { count: (myEntities?.totalElements ?? 1) - 1 })}
              </p>
            )}
          </div>

          {!confirmingLogout ? (
            <>
              <button
                role="menuitem"
                onClick={() => navigate('/favoris')}
                className="w-full text-left px-3 py-2.5 text-sm hover:bg-[var(--color-surface)] transition-colors"
              >
                {t('account.favorites')}
              </button>
              <button
                role="menuitem"
                onClick={() => navigate(hasBusiness ? '/mes-commerces' : '/mon-commerce')}
                className="w-full text-left px-3 py-2.5 text-sm hover:bg-[var(--color-surface)] transition-colors"
              >
                {hasBusiness ? t('account.my_businesses') : t('account.add_business')}
              </button>
              <div className="border-t border-[var(--color-border)] my-1" />
              <button
                role="menuitem"
                onClick={() => setConfirmingLogout(true)}
                className="w-full text-left px-3 py-2.5 text-sm text-[var(--color-danger)] hover:bg-[var(--color-surface)] transition-colors"
              >
                {t('auth.logout')}
              </button>
            </>
          ) : (
            <div className="px-3 py-2.5">
              <p className="text-sm mb-3">{t('auth.logout_confirm')}</p>
              <div className="flex gap-2">
                <button
                  onClick={() => setConfirmingLogout(false)}
                  className="flex-1 text-xs px-3 py-2 rounded-md border border-[var(--color-border)]"
                >
                  {t('common.cancel')}
                </button>
                <button
                  onClick={handleLogout}
                  className="flex-1 text-xs px-3 py-2 rounded-md bg-[var(--color-danger)] text-white"
                >
                  {t('auth.logout')}
                </button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

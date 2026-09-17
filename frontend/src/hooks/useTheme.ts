import { useEffect } from 'react'
import { useThemeStore } from '../stores/themeStore'

/**
 * Applique la classe "dark" sur <html> selon le choix stocke, ou selon
 * le reglage systeme si "system" est choisi. Ecoute aussi les changements
 * systeme en direct (utilisateur qui bascule son telephone en mode sombre
 * pendant que l'app est ouverte).
 */
export function useTheme() {
  const theme = useThemeStore((s) => s.theme)

  useEffect(() => {
    const root = document.documentElement
    const media = window.matchMedia('(prefers-color-scheme: dark)')

    const apply = () => {
      const isDark = theme === 'dark' || (theme === 'system' && media.matches)
      root.classList.toggle('dark', isDark)
    }

    apply()
    media.addEventListener('change', apply)
    return () => media.removeEventListener('change', apply)
  }, [theme])
}

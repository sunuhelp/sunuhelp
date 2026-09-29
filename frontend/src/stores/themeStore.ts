import { create } from 'zustand'
import { persist } from 'zustand/middleware'

export type Theme = 'light' | 'dark' | 'system'

interface ThemeState {
  theme: Theme
  setTheme: (theme: Theme) => void
}

/**
 * Persiste dans localStorage - le choix de l'utilisateur reste actif
 * a chaque visite, exactement comme la langue. "system" suit le reglage
 * de l'appareil, mais reste un choix explicite parmi les 3, jamais impose.
 */
export const useThemeStore = create<ThemeState>()(
  persist(
    (set) => ({
      theme: 'system',
      setTheme: (theme) => set({ theme }),
    }),
    { name: 'app_theme' }
  )
)

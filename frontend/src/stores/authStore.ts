import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface AuthState {
  accessToken: string | null
  refreshToken: string | null
  accountId: string | null
  role: 'USER' | 'ADMIN' | null
  setTokens: (accessToken: string, refreshToken: string) => void
  logout: () => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      accessToken: null,
      refreshToken: null,
      accountId: null,
      role: null,
      setTokens: (accessToken, refreshToken) => {
        const payload = JSON.parse(atob(accessToken.split('.')[1]))
        set({ accessToken, refreshToken, accountId: payload.sub, role: payload.role })
      },
      logout: () => set({ accessToken: null, refreshToken: null, accountId: null, role: null }),
    }),
    { name: 'sunuhelp_auth' }
  )
)

import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface AuthState {
  accessToken: string | null
  refreshToken: string | null
  accountId: string | null
  phoneNumber: string | null
  role: 'USER' | 'ADMIN' | null
  setTokens: (accessToken: string, refreshToken: string, phoneNumber?: string) => void
  logout: () => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      refreshToken: null,
      accountId: null,
      phoneNumber: null,
      role: null,
      setTokens: (accessToken, refreshToken, phoneNumber) => {
        const payload = JSON.parse(atob(accessToken.split('.')[1]))
        set({
          accessToken,
          refreshToken,
          accountId: payload.sub,
          role: payload.role,
          phoneNumber: phoneNumber ?? get().phoneNumber,
        })
      },
      logout: () => set({ accessToken: null, refreshToken: null, accountId: null, phoneNumber: null, role: null }),
    }),
    { name: 'app_auth' }
  )
)

import { Navigate, useLocation } from 'react-router-dom'
import { useAuthStore } from '../stores/authStore'

export function RequireAuth({ children }: { children: React.ReactNode }) {
  const accessToken = useAuthStore((s) => s.accessToken)
  const setPendingRedirect = useAuthStore((s) => s.setPendingRedirect)
  const location = useLocation()

  if (!accessToken) {
    setPendingRedirect(location.pathname)
    return <Navigate to="/" replace />
  }
  return <>{children}</>
}

import { Navigate } from 'react-router-dom'
import { useAuthStore } from '../stores/authStore'

export function RequireAuth({ children }: { children: React.ReactNode }) {
  const accessToken = useAuthStore((s) => s.accessToken)
  if (!accessToken) return <Navigate to="/connexion" replace />
  return <>{children}</>
}

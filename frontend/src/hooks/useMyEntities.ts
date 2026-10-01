import { useQuery } from '@tanstack/react-query'
import { fetchMyEntities } from '../api/entities'
import { useAuthStore } from '../stores/authStore'

export function useMyEntities() {
  const accessToken = useAuthStore((s) => s.accessToken)
  return useQuery({ queryKey: ['entities', 'mine'], queryFn: fetchMyEntities, enabled: !!accessToken })
}

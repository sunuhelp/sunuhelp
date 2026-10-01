import { useQuery } from '@tanstack/react-query'
import api from '../lib/axios'

async function checkHasHours(entityId: string): Promise<boolean> {
  const { data: points } = await api.get(`/api/v1/entities/${entityId}/service-points`)
  if (!points?.[0]) return false
  const { data: hours } = await api.get(`/api/v1/service-points/${points[0].id}/opening-hours`)
  return Array.isArray(hours) && hours.length > 0
}

export function useHasHours(entityId: string) {
  return useQuery({ queryKey: ['has-hours', entityId], queryFn: () => checkHasHours(entityId) })
}

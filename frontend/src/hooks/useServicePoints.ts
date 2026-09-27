import { useQuery } from '@tanstack/react-query'
import { fetchServicePoints, fetchOpeningHours } from '../api/servicePoints'

export function useServicePoints(entityId: string | undefined) {
  return useQuery({
    queryKey: ['service-points', entityId],
    queryFn: () => fetchServicePoints(entityId!),
    enabled: !!entityId,
  })
}

export function useOpeningHours(servicePointId: string | undefined) {
  return useQuery({
    queryKey: ['opening-hours', servicePointId],
    queryFn: () => fetchOpeningHours(servicePointId!),
    enabled: !!servicePointId,
  })
}

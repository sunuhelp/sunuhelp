import { useQuery } from '@tanstack/react-query'
import { fetchReviews } from '../api/reviews'

export function useReviews(entityId: string | undefined) {
  return useQuery({
    queryKey: ['reviews', entityId],
    queryFn: () => fetchReviews(entityId!),
    enabled: !!entityId,
  })
}

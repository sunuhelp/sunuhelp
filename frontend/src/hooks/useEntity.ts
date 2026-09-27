import { useQuery } from '@tanstack/react-query'
import { fetchEntity } from '../api/entities'

export function useEntity(id: string | undefined) {
  return useQuery({
    queryKey: ['entity', id],
    queryFn: () => fetchEntity(id!),
    enabled: !!id,
  })
}

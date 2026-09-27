import { useQuery } from '@tanstack/react-query'
import { searchEntities, type SearchParams } from '../api/search'

export function useSearch(params: SearchParams) {
  return useQuery({
    queryKey: ['search', params],
    queryFn: () => searchEntities(params),
    enabled: !!(params.query || params.categorySlug),
  })
}

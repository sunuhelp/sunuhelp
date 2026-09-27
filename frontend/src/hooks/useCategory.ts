import { useQuery } from '@tanstack/react-query'
import { fetchCategoryById } from '../api/categories'

export function useCategory(id: string | undefined) {
  return useQuery({
    queryKey: ['category', id],
    queryFn: () => fetchCategoryById(id!),
    enabled: !!id,
  })
}

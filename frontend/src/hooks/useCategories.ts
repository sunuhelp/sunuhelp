import { useQuery } from '@tanstack/react-query'
import { fetchRootCategories } from '../api/categories'

export function useCategories() {
  return useQuery({
    queryKey: ['categories', 'root'],
    queryFn: fetchRootCategories,
  })
}

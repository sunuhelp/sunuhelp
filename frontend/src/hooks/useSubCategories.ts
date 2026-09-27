import { useQuery } from '@tanstack/react-query'
import api from '../lib/axios'
import type { Category } from '../api/categories'

async function fetchChildren(parentId: string): Promise<Category[]> {
  const { data } = await api.get(`/api/v1/categories/${parentId}/children`)
  return data
}

export function useSubCategories(parentId: string | null) {
  return useQuery({
    queryKey: ['categories', 'children', parentId],
    queryFn: () => fetchChildren(parentId!),
    enabled: !!parentId,
  })
}

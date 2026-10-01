import { useQuery } from '@tanstack/react-query'
import { fetchCategoryById } from '../api/categories'

export function useRootCategorySlug(categoryId: string | undefined) {
  return useQuery({
    queryKey: ['category-root-slug', categoryId],
    queryFn: async () => {
      const category = await fetchCategoryById(categoryId!)
      if (!category.parentId) return category.slug
      const parent = await fetchCategoryById(category.parentId)
      return parent.slug
    },
    enabled: !!categoryId,
    staleTime: 5 * 60_000,
  })
}

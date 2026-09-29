import { useQuery } from '@tanstack/react-query'
import { fetchRootCategories, fetchChildren, type Category } from '../api/categories'

export interface FlatCategory extends Category {
  parentId: string | null
  parentName?: string
  parentIcon?: string
}

/**
 * Precharge l'integralite de l'arbre (~120 categories) en une fois, mis
 * en cache pour toute la session - permet une recherche instantanee
 * cote client, sans requete a chaque frappe, sans modification backend.
 */
export function useAllCategories() {
  return useQuery({
    queryKey: ['categories', 'all-flat'],
    queryFn: async () => {
      const roots = await fetchRootCategories()
      const childrenLists = await Promise.all(roots.map((r) => fetchChildren(r.id)))

      const flat: FlatCategory[] = []
      roots.forEach((root, i) => {
        flat.push({ ...root, parentId: null })
        childrenLists[i].forEach((child) => {
          flat.push({ ...child, parentId: root.id, parentName: root.name, parentIcon: root.icon })
        })
      })
      return { roots, flat }
    },
    staleTime: 5 * 60_000,
  })
}

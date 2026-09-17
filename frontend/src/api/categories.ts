import api from '../lib/axios'

export interface Category {
  id: string
  slug: string
  icon: string
  name: string
}

export async function fetchRootCategories(): Promise<Category[]> {
  const { data } = await api.get('/api/v1/categories')
  return data
}

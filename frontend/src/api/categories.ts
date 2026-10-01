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

export async function fetchChildren(parentId: string): Promise<Category[]> {
  const { data } = await api.get(`/api/v1/categories/${parentId}/children`)
  return data
}

export interface CategoryDetail extends Category {
  parentId: string | null
}

export async function fetchCategoryById(id: string): Promise<CategoryDetail> {
  const { data } = await api.get(`/api/v1/categories/${id}`)
  return data
}

import api from '../lib/axios'

export interface SearchResult {
  servicePointId: string
  entityId: string
  name: string
  description: string | null
  categorySlug: string
  trustLevel: string
  latitude: number
  longitude: number
  openNow: boolean
  averageRating: number
  reviewCount: number
}

export interface SearchParams {
  query?: string
  categorySlug?: string
  latitude?: number
  longitude?: number
  radiusKm?: number
  openNowOnly?: boolean
}

export async function searchEntities(params: SearchParams): Promise<SearchResult[]> {
  const { data } = await api.get('/api/v1/search', { params })
  return data
}

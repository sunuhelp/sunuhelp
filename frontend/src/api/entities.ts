import api from '../lib/axios'

export interface CreateEntityPayload {
  personType: 'INDIVIDUAL' | 'LEGAL_ENTITY'
  categoryId: string
  translations: { locale: string; name: string; description?: string }[]
}

export interface EntityResponse {
  id: string
  trustLevel: string
  name: string
}

interface PageResponse<T> {
  content: T[]
  totalElements: number
}

export async function createEntity(payload: CreateEntityPayload): Promise<EntityResponse> {
  const { data } = await api.post('/api/v1/entities', payload)
  return data
}

export async function fetchMyEntities(): Promise<PageResponse<EntityResponse>> {
  const { data } = await api.get('/api/v1/entities/mine')
  return data
}

import api from '../lib/axios'

export interface CreateServicePointPayload {
  name: string
  type: 'PHYSICAL' | 'ONLINE'
  address?: string
  coverageZone?: string
  phoneNumber?: string
}

export async function createServicePoint(entityId: string, payload: CreateServicePointPayload) {
  const { data } = await api.post(`/api/v1/entities/${entityId}/service-points`, payload)
  return data
}

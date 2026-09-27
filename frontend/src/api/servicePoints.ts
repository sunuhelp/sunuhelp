import api from '../lib/axios'

export interface CreateServicePointPayload {
  name: string
  type: 'PHYSICAL' | 'ONLINE'
  address?: string
  coverageZone?: string
  phoneNumber?: string
}

export interface OpeningHourEntry {
  dayOfWeek: string
  closed: boolean
  openingTime: string | null
  closingTime: string | null
}

export interface ServicePointResponse {
  id: string
  entityId: string
  name: string
  type: 'PHYSICAL' | 'ONLINE'
  address: string | null
  coverageZone: string | null
  phoneNumber: string | null
  isOpen247: boolean
  currentlyOpen: boolean
  temporaryStatus: string
}

export async function createServicePoint(entityId: string, payload: CreateServicePointPayload) {
  const { data } = await api.post(`/api/v1/entities/${entityId}/service-points`, payload)
  return data
}

export async function fetchServicePoints(entityId: string): Promise<ServicePointResponse[]> {
  const { data } = await api.get(`/api/v1/entities/${entityId}/service-points`)
  return data
}

export async function fetchOpeningHours(servicePointId: string): Promise<OpeningHourEntry[]> {
  const { data } = await api.get(`/api/v1/service-points/${servicePointId}/opening-hours`)
  return data
}

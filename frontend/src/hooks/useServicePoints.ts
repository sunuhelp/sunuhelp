import { useQuery } from '@tanstack/react-query'
import api from '../lib/axios'

export interface OpeningHourEntry { dayOfWeek: string; closed: boolean; openingTime: string | null; closingTime: string | null }
export interface ServicePointResponse {
  id: string; entityId: string; name: string; type: 'PHYSICAL' | 'ONLINE'
  address: string | null; coverageZone: string | null; phoneNumber: string | null
  open247: boolean; currentlyOpen: boolean
}

async function fetchServicePoints(entityId: string): Promise<ServicePointResponse[]> {
  const { data } = await api.get(`/api/v1/entities/${entityId}/service-points`)
  return data
}
async function fetchOpeningHours(servicePointId: string): Promise<OpeningHourEntry[]> {
  const { data } = await api.get(`/api/v1/service-points/${servicePointId}/opening-hours`)
  return data
}

export function useServicePoints(entityId: string | undefined) {
  return useQuery({ queryKey: ['service-points', entityId], queryFn: () => fetchServicePoints(entityId!), enabled: !!entityId })
}
export function useOpeningHours(servicePointId: string | undefined) {
  return useQuery({ queryKey: ['opening-hours', servicePointId], queryFn: () => fetchOpeningHours(servicePointId!), enabled: !!servicePointId })
}

import api from '../lib/axios'

export interface ReviewResponse {
  id: string
  entityId: string
  reviewerAccountId: string
  rating: number
  comment: string | null
  createdAt: string
  responseContent: string | null
}

interface PageResponse<T> {
  content: T[]
  totalElements: number
}

export async function fetchReviews(entityId: string, page = 0): Promise<PageResponse<ReviewResponse>> {
  const { data } = await api.get(`/api/v1/entities/${entityId}/reviews`, { params: { page } })
  return data
}

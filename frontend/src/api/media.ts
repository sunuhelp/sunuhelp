import api from '../lib/axios'

export type MediaOwnerType = 'ENTITY_LOGO' | 'ENTITY_PHOTO' | 'OFFER_PHOTO' | 'VERIFICATION_DOCUMENT' | 'USER_AVATAR'

export interface MediaFileResponse {
  id: string
  ownerType: MediaOwnerType
  ownerId: string
  fileUrl: string
  mimeType: string
  fileSizeBytes: number
  processingStatus: 'PENDING' | 'SCANNING' | 'READY' | 'REJECTED' | 'FAILED'
  public: boolean
}

export async function fetchMediaByOwner(ownerType: MediaOwnerType, ownerId: string): Promise<MediaFileResponse[]> {
  const { data } = await api.get('/api/v1/media', { params: { ownerType, ownerId } })
  return data
}

export async function uploadMedia(file: File, ownerType: MediaOwnerType, ownerId: string): Promise<MediaFileResponse> {
  const formData = new FormData()
  formData.append('file', file)
  const { data } = await api.post('/api/v1/media', formData, {
    params: { ownerType, ownerId },
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return data
}

export async function deleteMedia(id: string) {
  await api.delete(`/api/v1/media/${id}`)
}

/**
 * Telecharge le contenu binaire via Axios (qui ajoute le jeton
 * automatiquement), puis cree une URL locale affichable - une balise
 * <img> seule ne peut jamais envoyer de jeton d'autorisation.
 */
export async function fetchMediaBlobUrl(id: string): Promise<string> {
  const { data } = await api.get(`/api/v1/media/${id}/download`, { responseType: 'blob' })
  return URL.createObjectURL(data)
}

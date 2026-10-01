import { useEffect, useState } from 'react'
import { fetchMediaBlobUrl } from '../api/media'

/**
 * Charge une image protegee et en gere le cycle de vie - revoke l'URL
 * locale au demontage pour ne jamais accumuler de fuite memoire.
 */
export function useMediaImage(mediaId: string | undefined) {
  const [url, setUrl] = useState<string | null>(null)

  useEffect(() => {
    if (!mediaId) return
    let objectUrl: string | null = null
    let cancelled = false

    fetchMediaBlobUrl(mediaId).then((blobUrl) => {
      if (cancelled) { URL.revokeObjectURL(blobUrl); return }
      objectUrl = blobUrl
      setUrl(blobUrl)
    })

    return () => {
      cancelled = true
      if (objectUrl) URL.revokeObjectURL(objectUrl)
    }
  }, [mediaId])

  return url
}

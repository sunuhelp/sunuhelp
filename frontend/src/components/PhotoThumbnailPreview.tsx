import { useMediaImage } from '../hooks/useMediaImage'

export function PhotoThumbnailPreview({ mediaId }: { mediaId: string }) {
  const url = useMediaImage(mediaId)
  return (
    <div className="w-[38px] h-[38px] rounded-lg bg-[var(--color-bg)] overflow-hidden">
      {url && <img src={url} alt="" className="w-full h-full object-cover" />}
    </div>
  )
}

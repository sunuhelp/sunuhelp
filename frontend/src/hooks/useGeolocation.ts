import { useEffect, useState } from 'react'

interface GeoState {
  latitude: number | null
  longitude: number | null
  status: 'idle' | 'granted' | 'denied'
}

export function useGeolocation() {
  const [state, setState] = useState<GeoState>({ latitude: null, longitude: null, status: 'idle' })

  useEffect(() => {
    if (!navigator.geolocation) {
      setState((s) => ({ ...s, status: 'denied' }))
      return
    }
    navigator.geolocation.getCurrentPosition(
      (pos) => setState({ latitude: pos.coords.latitude, longitude: pos.coords.longitude, status: 'granted' }),
      () => setState((s) => ({ ...s, status: 'denied' })),
      { timeout: 8000 }
    )
  }, [])

  return state
}

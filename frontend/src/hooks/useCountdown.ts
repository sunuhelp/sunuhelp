import { useEffect, useState } from 'react'

export function useCountdown(initialSeconds: number) {
  const [seconds, setSeconds] = useState(initialSeconds)

  useEffect(() => {
    if (seconds <= 0) return
    const timer = setInterval(() => setSeconds((s) => s - 1), 1000)
    return () => clearInterval(timer)
  }, [seconds])

  const reset = () => setSeconds(initialSeconds)
  const format = () => `${Math.floor(seconds / 60)}:${String(seconds % 60).padStart(2, '0')}`

  return { seconds, isActive: seconds > 0, reset, format }
}

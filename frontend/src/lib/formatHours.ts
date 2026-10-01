import type { OpeningHourEntry } from '../hooks/useServicePoints'

const DAY_ORDER = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']
const SHORT_LABELS: Record<string, string> = {
  MONDAY: 'Lun', TUESDAY: 'Mar', WEDNESDAY: 'Mer', THURSDAY: 'Jeu',
  FRIDAY: 'Ven', SATURDAY: 'Sam', SUNDAY: 'Dim',
}

/**
 * Regroupe les jours consecutifs ayant les memes horaires en une seule
 * ligne ("Lun - Sam : 08:00 - 18:00"), plutot que 7 lignes repetitives.
 */
export function formatHoursSummary(hours: OpeningHourEntry[]): string[] {
  if (!hours.length) return []

  const sorted = DAY_ORDER.map((day) => hours.find((h) => h.dayOfWeek === day)).filter(Boolean) as OpeningHourEntry[]
  const lines: string[] = []
  let groupStart = 0

  const sameSchedule = (a: OpeningHourEntry, b: OpeningHourEntry) =>
    a.closed === b.closed && a.openingTime === b.openingTime && a.closingTime === b.closingTime

  for (let i = 1; i <= sorted.length; i++) {
    if (i === sorted.length || !sameSchedule(sorted[i], sorted[groupStart])) {
      const start = sorted[groupStart]
      const dayRange = groupStart === i - 1
        ? SHORT_LABELS[start.dayOfWeek]
        : `${SHORT_LABELS[start.dayOfWeek]} – ${SHORT_LABELS[sorted[i - 1].dayOfWeek]}`
      const schedule = start.closed ? 'Fermé' : `${start.openingTime?.slice(0, 5)} – ${start.closingTime?.slice(0, 5)}`
      lines.push(`${dayRange} · ${schedule}`)
      groupStart = i
    }
  }
  return lines
}

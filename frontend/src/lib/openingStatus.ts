import type { OpeningHourEntry } from '../api/servicePoints'

const DAY_ORDER = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']

/**
 * Calcule "ouvert maintenant" cote client, a partir des donnees brutes -
 * meme principe que search-service : jamais stocke, toujours recalcule
 * au moment de l'affichage pour rester exact a la minute pres.
 * Retourne null si aucune donnee n'existe - distinct de false (ferme,
 * mais on sait) : jamais affirmer "ferme" quand on ne sait simplement pas.
 */
export function isOpenNow(hours: OpeningHourEntry[], isOpen247: boolean): boolean | null {
  if (isOpen247) return true
  if (!hours.length) return null

  const now = new Date()
  const today = DAY_ORDER[now.getDay() === 0 ? 6 : now.getDay() - 1]
  const todayHours = hours.find((h) => h.dayOfWeek === today)
  if (!todayHours) return null
  if (todayHours.closed || !todayHours.openingTime || !todayHours.closingTime) return false

  const current = now.getHours() * 60 + now.getMinutes()
  const [openH, openM] = todayHours.openingTime.split(':').map(Number)
  const [closeH, closeM] = todayHours.closingTime.split(':').map(Number)
  return current >= openH * 60 + openM && current <= closeH * 60 + closeM
}

export function closingTimeLabel(hours: OpeningHourEntry[]): string | null {
  const now = new Date()
  const today = DAY_ORDER[now.getDay() === 0 ? 6 : now.getDay() - 1]
  const todayHours = hours.find((h) => h.dayOfWeek === today)
  return todayHours?.closingTime?.slice(0, 5) ?? null
}

import { useTranslation } from 'react-i18next'

export function useGreeting(): string {
  const { t } = useTranslation()
  const hour = new Date().getHours()
  if (hour < 5) return t('dashboard.greeting_night')
  if (hour < 18) return t('dashboard.greeting_day')
  return t('dashboard.greeting_evening')
}

import { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { Dialog } from './Dialog'
import { OpeningHoursEditor, type DayHours } from './OpeningHoursEditor'
import { setOpeningHours } from '../api/servicePoints'
import { notify } from '../lib/toast'
import type { OpeningHourEntry } from '../hooks/useServicePoints'

const DEFAULT_DAYS: DayHours[] = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']
  .map((dayOfWeek) => ({ dayOfWeek, closed: dayOfWeek === 'SUNDAY', openingTime: '08:00', closingTime: '18:00' }))

interface HoursDialogProps {
  open: boolean
  onClose: () => void
  servicePointId: string | undefined
  existingHours: OpeningHourEntry[] | undefined
  onSaved: () => void
}

export function HoursDialog({ open, onClose, servicePointId, existingHours, onSaved }: HoursDialogProps) {
  const { t } = useTranslation()
  const [hours, setHours] = useState<DayHours[]>(DEFAULT_DAYS)
  const [isSaving, setIsSaving] = useState(false)

  useEffect(() => {
    if (existingHours && existingHours.length > 0) {
      setHours(existingHours.map((h) => ({
        dayOfWeek: h.dayOfWeek, closed: h.closed,
        openingTime: h.openingTime?.slice(0, 5) ?? '08:00',
        closingTime: h.closingTime?.slice(0, 5) ?? '18:00',
      })))
    }
  }, [existingHours])

  const handleSave = async () => {
    if (!servicePointId) return
    setIsSaving(true)
    try {
      await setOpeningHours(servicePointId, { days: hours })
      notify.success(t('manage.hours_saved'))
      onSaved()
      onClose()
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsSaving(false)
    }
  }

  return (
    <Dialog open={open} onClose={onClose} title={t("manage.hours_title")} wide>
      <OpeningHoursEditor value={hours} onChange={setHours} />
      <button
        onClick={handleSave}
        disabled={isSaving}
        className="w-full h-11 mt-5 rounded-lg bg-[var(--color-accent)] text-white font-medium disabled:opacity-60"
      >
        {isSaving ? t('manage.saving') : t('manage.save')}
      </button>
    </Dialog>
  )
}

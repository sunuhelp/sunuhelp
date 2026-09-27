import { useEffect, useRef, useState } from 'react'
import { AnimatePresence, motion } from 'framer-motion'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { useQueryClient } from '@tanstack/react-query'
import { createEntity } from '../api/entities'
import { createServicePoint } from '../api/servicePoints'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'
import { CategoryPicker } from '../components/CategoryPicker'
import { PersonTypeCard } from '../components/PersonTypeCard'
import { notify } from '../lib/toast'

type PersonType = 'INDIVIDUAL' | 'LEGAL_ENTITY'
type LocationType = 'PHYSICAL' | 'ONLINE'

interface FormState {
  personType: PersonType | null
  name: string
  description: string
  categoryId: string | null
  categoryName: string
  locationType: LocationType | null
  address: string
  coverageZone: string
  phoneNumber: string
}

const STEP_KEYS = ['personType', 'name', 'description', 'category', 'locationType', 'place', 'phone', 'review'] as const
type StepKey = typeof STEP_KEYS[number]

const PHONE_REGEX = /^\+?[0-9]{9,15}$/

export default function AddBusinessPage() {
  const { t, i18n } = useTranslation()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const inputRef = useRef<HTMLInputElement | HTMLTextAreaElement>(null)

  const [stepIndex, setStepIndex] = useState(0)
  const [direction, setDirection] = useState(1)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [fieldError, setFieldError] = useState<string | null>(null)

  const [data, setData] = useState<FormState>({
    personType: null, name: '', description: '', categoryId: null, categoryName: '',
    locationType: null, address: '', coverageZone: '', phoneNumber: '',
  })

  const step: StepKey = STEP_KEYS[stepIndex]

  useEffect(() => {
    setFieldError(null)
    const t = setTimeout(() => inputRef.current?.focus(), 250)
    return () => clearTimeout(t)
  }, [step])

  const goTo = (i: number) => {
    setDirection(i > stepIndex ? 1 : -1)
    setStepIndex(i)
  }
  const advance = () => goTo(Math.min(stepIndex + 1, STEP_KEYS.length - 1))
  const retreat = () => goTo(Math.max(stepIndex - 1, 0))

  const update = <K extends keyof FormState>(key: K, value: FormState[K]) => setData((d) => ({ ...d, [key]: value }))

  // Filled chips: only steps strictly before current one, in fixed order.
  const chips = [
    data.personType && { label: data.personType === 'INDIVIDUAL' ? t('add_business.individual') : t('add_business.legal_entity'), icon: data.personType === 'INDIVIDUAL' ? '👤' : '🏢', step: 0 },
    data.name && { label: data.name, icon: '🏪', step: 1 },
    data.categoryName && { label: data.categoryName, icon: '🏷️', step: 3 },
    data.locationType && { label: data.locationType === 'PHYSICAL' ? (data.address || t('add_business.physical')) : (data.coverageZone || t('add_business.online')), icon: '📍', step: 4 },
  ].filter(Boolean) as { label: string; icon: string; step: number }[]

  const handleContinue = () => {
    if (step === 'name') {
      if (data.name.trim().length < 2) return setFieldError(t('validation.name_required'))
    }
    if (step === 'description' && data.description.length > 500) {
      return setFieldError(t('validation.description_too_long'))
    }
    if (step === 'place') {
      if (data.locationType === 'PHYSICAL' && !data.address.trim()) return setFieldError(t('validation.address_required'))
      if (data.locationType === 'ONLINE' && !data.coverageZone.trim()) return setFieldError(t('validation.coverage_zone_required'))
    }
    if (step === 'phone' && data.phoneNumber && !PHONE_REGEX.test(data.phoneNumber)) {
      return setFieldError(t('validation.phone_invalid'))
    }
    advance()
  }

  const handleFinalSubmit = async () => {
    setIsSubmitting(true)
    try {
      const entity = await createEntity({
        personType: data.personType!,
        categoryId: data.categoryId!,
        translations: [{ locale: i18n.language, name: data.name, description: data.description || undefined }],
      })
      await createServicePoint(entity.id, {
        name: data.name,
        type: data.locationType!,
        address: data.address || undefined,
        coverageZone: data.coverageZone || undefined,
        phoneNumber: data.phoneNumber || undefined,
      })
      // Rafraichit immediatement le menu compte - "Ajouter mon commerce" doit
      // devenir "Mes commerces" sans attendre le cache de 30s.
      await queryClient.invalidateQueries({ queryKey: ['entities', 'mine'] })
      notify.success(t('add_business.created_success'))
      navigate('/commerce-cree', { state: { name: data.name } })
    } catch {
      notify.error(t('errors.generic'))
    } finally {
      setIsSubmitting(false)
    }
  }

  const variants = {
    enter: (dir: number) => ({ opacity: 0, x: dir > 0 ? 24 : -24 }),
    center: { opacity: 1, x: 0 },
    exit: (dir: number) => ({ opacity: 0, x: dir > 0 ? -24 : 24 }),
  }

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />

      <main className="flex-1 flex flex-col items-center justify-center px-6 py-10">
        <div className="w-full max-w-lg">
          {chips.length > 0 && (
            <motion.div layout className="flex flex-wrap gap-2 mb-6 justify-center">
              {chips.map((chip) => (
                <motion.button
                  key={chip.step + chip.label}
                  layout
                  initial={{ opacity: 0, scale: 0.85 }}
                  animate={{ opacity: 1, scale: 1 }}
                  onClick={() => goTo(chip.step)}
                  className="flex items-center gap-1.5 text-xs font-medium px-3 py-1.5 rounded-full bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/50 transition-colors"
                >
                  <span aria-hidden="true">{chip.icon}</span>
                  <span className="max-w-[10rem] truncate">{chip.label}</span>
                </motion.button>
              ))}
            </motion.div>
          )}

          <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl p-6 sm:p-8 min-h-[280px] flex flex-col overflow-hidden">
            <AnimatePresence mode="wait" custom={direction}>
              <motion.div
                key={step}
                custom={direction}
                variants={variants}
                initial="enter"
                animate="center"
                exit="exit"
                transition={{ duration: 0.2, ease: 'easeOut' }}
                className="flex-1 flex flex-col"
              >
                {step === 'personType' && (
                  <div>
                    <p className="text-lg font-medium mb-5 text-center">{t('add_business.q_person_type')}</p>
                    <div className="flex gap-3">
                      <PersonTypeCard icon="👤" title={t('add_business.individual')} description={t('add_business.individual_desc')}
                        selected={data.personType === 'INDIVIDUAL'}
                        onClick={() => { update('personType', 'INDIVIDUAL'); setTimeout(advance, 150) }} />
                      <PersonTypeCard icon="🏢" title={t('add_business.legal_entity')} description={t('add_business.legal_entity_desc')}
                        selected={data.personType === 'LEGAL_ENTITY'}
                        onClick={() => { update('personType', 'LEGAL_ENTITY'); setTimeout(advance, 150) }} />
                    </div>
                  </div>
                )}

                {step === 'name' && (
                  <div className="flex-1 flex flex-col">
                    <p className="text-lg font-medium mb-4 text-center">{t('add_business.q_name')}</p>
                    <input
                      ref={inputRef as React.RefObject<HTMLInputElement>}
                      value={data.name}
                      onChange={(e) => { update('name', e.target.value); setFieldError(null) }}
                      onKeyDown={(e) => e.key === 'Enter' && handleContinue()}
                      placeholder={t('add_business.name_placeholder')}
                      className={`w-full h-12 px-4 rounded-lg border bg-[var(--color-bg)] outline-none text-center transition-colors ${fieldError ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'}`}
                    />
                    {fieldError && <p className="text-sm text-[var(--color-danger)] mt-2 text-center">{fieldError}</p>}
                  </div>
                )}

                {step === 'description' && (
                  <div className="flex-1 flex flex-col">
                    <p className="text-lg font-medium mb-1 text-center">{t('add_business.q_description')}</p>
                    <p className="text-xs text-[var(--color-ink-muted)] text-center mb-4">{t('add_business.optional_hint')}</p>
                    <textarea
                      ref={inputRef as React.RefObject<HTMLTextAreaElement>}
                      value={data.description}
                      onChange={(e) => { update('description', e.target.value); setFieldError(null) }}
                      rows={3}
                      maxLength={500}
                      placeholder={t('add_business.description_placeholder')}
                      className="w-full px-4 py-3 rounded-lg border border-[var(--color-border)] bg-[var(--color-bg)] outline-none focus:border-[var(--color-accent)] resize-none"
                    />
                    <span className="text-xs text-[var(--color-ink-muted)] text-right mt-1">{data.description.length}/500</span>
                  </div>
                )}

                {step === 'category' && (
                  <div>
                    <p className="text-lg font-medium mb-4 text-center">{t('add_business.q_category')}</p>
                    <CategoryPicker
                      selectedId={data.categoryId}
                      onSelect={(id, name) => { update('categoryId', id); update('categoryName', name); setTimeout(advance, 200) }}
                    />
                  </div>
                )}

                {step === 'locationType' && (
                  <div>
                    <p className="text-lg font-medium mb-5 text-center">{t('add_business.q_location_type')}</p>
                    <div className="flex gap-3">
                      <PersonTypeCard icon="📍" title={t('add_business.physical')} description={t('add_business.physical_desc')}
                        selected={data.locationType === 'PHYSICAL'}
                        onClick={() => { update('locationType', 'PHYSICAL'); setTimeout(advance, 150) }} />
                      <PersonTypeCard icon="🌐" title={t('add_business.online')} description={t('add_business.online_desc')}
                        selected={data.locationType === 'ONLINE'}
                        onClick={() => { update('locationType', 'ONLINE'); setTimeout(advance, 150) }} />
                    </div>
                  </div>
                )}

                {step === 'place' && (
                  <div className="flex-1 flex flex-col">
                    <p className="text-lg font-medium mb-4 text-center">
                      {data.locationType === 'PHYSICAL' ? t('add_business.q_address') : t('add_business.q_coverage_zone')}
                    </p>
                    <input
                      ref={inputRef as React.RefObject<HTMLInputElement>}
                      value={data.locationType === 'PHYSICAL' ? data.address : data.coverageZone}
                      onChange={(e) => { update(data.locationType === 'PHYSICAL' ? 'address' : 'coverageZone', e.target.value); setFieldError(null) }}
                      onKeyDown={(e) => e.key === 'Enter' && handleContinue()}
                      placeholder={data.locationType === 'PHYSICAL' ? t('add_business.address_placeholder') : t('add_business.coverage_zone_placeholder')}
                      className={`w-full h-12 px-4 rounded-lg border bg-[var(--color-bg)] outline-none text-center transition-colors ${fieldError ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'}`}
                    />
                    {fieldError && <p className="text-sm text-[var(--color-danger)] mt-2 text-center">{fieldError}</p>}
                  </div>
                )}

                {step === 'phone' && (
                  <div className="flex-1 flex flex-col">
                    <p className="text-lg font-medium mb-1 text-center">{t('add_business.q_phone')}</p>
                    <p className="text-xs text-[var(--color-ink-muted)] text-center mb-4">{t('add_business.optional_hint')}</p>
                    <input
                      ref={inputRef as React.RefObject<HTMLInputElement>}
                      value={data.phoneNumber}
                      onChange={(e) => { update('phoneNumber', e.target.value); setFieldError(null) }}
                      onKeyDown={(e) => e.key === 'Enter' && handleContinue()}
                      type="tel"
                      placeholder="+221 77 123 45 67"
                      className={`w-full h-12 px-4 rounded-lg border bg-[var(--color-bg)] outline-none text-center transition-colors ${fieldError ? 'border-[var(--color-danger)]' : 'border-[var(--color-border)] focus:border-[var(--color-accent)]'}`}
                    />
                    {fieldError && <p className="text-sm text-[var(--color-danger)] mt-2 text-center">{fieldError}</p>}
                  </div>
                )}

                {step === 'review' && (
                  <div className="flex-1 flex flex-col">
                    <p className="text-lg font-medium mb-1 text-center">{t('add_business.review_title')}</p>
                    <p className="text-xs text-[var(--color-ink-muted)] text-center mb-5">{t('add_business.review_hint')}</p>
                    {data.description && (
                      <p className="text-sm text-[var(--color-ink-muted)] text-center italic mb-4">"{data.description}"</p>
                    )}
                    {data.phoneNumber && (
                      <p className="text-sm text-center mb-2">📞 {data.phoneNumber}</p>
                    )}
                  </div>
                )}
              </motion.div>
            </AnimatePresence>

            <div className="flex gap-3 mt-6 pt-2">
              {stepIndex > 0 && (
                <button onClick={retreat} disabled={isSubmitting} className="h-11 px-5 rounded-lg border border-[var(--color-border)] text-sm font-medium disabled:opacity-50">
                  {t('common.back')}
                </button>
              )}
              {['name', 'description', 'place', 'phone'].includes(step) && (
                <button onClick={handleContinue} className="flex-1 h-11 rounded-lg bg-[var(--color-accent)] text-white text-sm font-medium">
                  {step === 'description' || step === 'phone' ? (data.description || data.phoneNumber ? t('common.next') : t('common.skip')) : t('common.next')}
                </button>
              )}
              {step === 'review' && (
                <button
                  onClick={handleFinalSubmit}
                  disabled={isSubmitting}
                  className="flex-1 h-11 rounded-lg bg-[var(--color-accent)] text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
                >
                  {isSubmitting && (
                    <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                    </svg>
                  )}
                  {isSubmitting ? t('add_business.creating') : t('add_business.confirm_create')}
                </button>
              )}
            </div>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  )
}

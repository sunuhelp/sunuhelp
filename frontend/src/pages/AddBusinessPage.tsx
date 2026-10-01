import { useEffect, useRef, useState } from 'react'
import { AnimatePresence, motion } from 'framer-motion'
import { useTranslation } from 'react-i18next'
import { useNavigate } from 'react-router-dom'
import { useQueryClient } from '@tanstack/react-query'
import { User, Building2, MapPin, Globe, X, Phone, ChevronRight } from 'lucide-react'
import { createEntity } from '../api/entities'
import { createServicePoint } from '../api/servicePoints'
import { CategoryStep } from '../components/CategoryStep'
import { PersonTypeCard } from '../components/PersonTypeCard'
import { PhoneInput } from '../components/PhoneInput'
import { notify } from '../lib/toast'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer'

type PersonType = 'INDIVIDUAL' | 'LEGAL_ENTITY'
type LocationType = 'PHYSICAL' | 'ONLINE'

interface FormState {
  personType: PersonType | null
  name: string
  description: string
  categoryId: string | null
  categoryLabel: string
  locationType: LocationType | null
  address: string
  coverageZone: string
  phoneDigits: string
}

const STEP_KEYS = ['personType', 'name', 'description', 'category', 'locationType', 'place', 'phone', 'review'] as const
type StepKey = typeof STEP_KEYS[number]

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
    personType: null, name: '', description: '', categoryId: null, categoryLabel: '',
    locationType: null, address: '', coverageZone: '', phoneDigits: '',
  })

  const step: StepKey = STEP_KEYS[stepIndex]
  const progress = ((stepIndex + 1) / STEP_KEYS.length) * 100

  useEffect(() => {
    setFieldError(null)
    const timer = setTimeout(() => inputRef.current?.focus(), 250)
    return () => clearTimeout(timer)
  }, [step])

  const goTo = (i: number) => { setDirection(i > stepIndex ? 1 : -1); setStepIndex(i) }
  const advance = () => goTo(Math.min(stepIndex + 1, STEP_KEYS.length - 1))
  const retreat = () => goTo(Math.max(stepIndex - 1, 0))
  const update = <K extends keyof FormState>(key: K, value: FormState[K]) => setData((d) => ({ ...d, [key]: value }))

  const chips = [
    data.personType && { label: data.personType === 'INDIVIDUAL' ? t('add_business.individual') : t('add_business.legal_entity'), icon: data.personType === 'INDIVIDUAL' ? User : Building2, step: 0 },
    data.name && { label: data.name, icon: Building2, step: 1 },
    data.categoryLabel && { label: data.categoryLabel, icon: null, step: 3 },
    data.locationType && { label: data.locationType === 'PHYSICAL' ? (data.address || t('add_business.physical')) : (data.coverageZone || t('add_business.online')), icon: MapPin, step: 4 },
  ].filter(Boolean) as { label: string; icon: typeof User | null; step: number }[]

  const handleContinue = () => {
    if (step === 'name' && data.name.trim().length < 2) return setFieldError(t('validation.name_required'))
    if (step === 'place') {
      if (data.locationType === 'PHYSICAL' && !data.address.trim()) return setFieldError(t('validation.address_required'))
      if (data.locationType === 'ONLINE' && !data.coverageZone.trim()) return setFieldError(t('validation.coverage_zone_required'))
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
        phoneNumber: data.phoneDigits ? `+221${data.phoneDigits}` : undefined,
      })
      await queryClient.invalidateQueries({ queryKey: ['entities', 'mine'] })
      notify.success(t('add_business.created_success'))
      navigate(`/mes-etablissements/${entity.id}`)
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

  const reviewRows = [
    { icon: data.personType === 'INDIVIDUAL' ? User : Building2, label: data.personType === 'INDIVIDUAL' ? t('add_business.individual') : t('add_business.legal_entity'), step: 0 },
    { icon: Building2, label: data.name, step: 1 },
    { icon: null, label: data.categoryLabel, step: 3, emoji: true },
    { icon: data.locationType === 'PHYSICAL' ? MapPin : Globe, label: data.locationType === 'PHYSICAL' ? data.address : data.coverageZone, step: 4 },
    ...(data.phoneDigits ? [{ icon: Phone, label: `+221 ${data.phoneDigits}`, step: 6 }] : []),
  ]

  return (
    <div className="min-h-screen bg-[var(--color-bg)] text-[var(--color-ink)] flex flex-col">
      <Header />
      <div className="max-w-lg mx-auto px-6 pt-5">
        <div className="flex items-center justify-between mb-4">
          <div className="w-7 h-7 rounded-md bg-[var(--color-accent)]" />
          <button onClick={() => navigate('/dashboard')} className="flex items-center gap-1.5 text-sm text-[var(--color-ink-muted)] hover:text-[var(--color-ink)] transition-colors">
            {t('common.close')} <X size={15} strokeWidth={1.75} aria-hidden="true" />
          </button>
        </div>

        <div className="h-[3px] bg-[var(--color-border)] rounded-full overflow-hidden mb-6">
          <motion.div className="h-full bg-[var(--color-accent)]" animate={{ width: `${progress}%` }} transition={{ duration: 0.3 }} />
        </div>

        {chips.length > 0 && step !== 'review' && (
          <motion.div layout className="flex flex-wrap gap-2 justify-center mb-5">
            {chips.map((chip) => {
              const Icon = chip.icon
              return (
                <motion.button
                  key={chip.step + chip.label}
                  layout
                  initial={{ opacity: 0, scale: 0.85 }}
                  animate={{ opacity: 1, scale: 1 }}
                  onClick={() => goTo(chip.step)}
                  className="flex items-center gap-1.5 text-xs font-medium px-3 py-1.5 rounded-full bg-[var(--color-surface)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/50 transition-colors"
                >
                  {Icon && <Icon size={12} strokeWidth={1.75} aria-hidden="true" />}
                  <span className="max-w-[10rem] truncate">{chip.label}</span>
                </motion.button>
              )
            })}
          </motion.div>
        )}

        <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl p-6 sm:p-7 min-h-[300px] flex flex-col overflow-hidden mb-10">
          <AnimatePresence mode="wait" custom={direction}>
            <motion.div key={step} custom={direction} variants={variants} initial="enter" animate="center" exit="exit" transition={{ duration: 0.2, ease: 'easeOut' }} className="flex-1 flex flex-col">

              {step === 'personType' && (
                <div>
                  <p className="text-base font-medium mb-5 text-center">{t('add_business.q_person_type')}</p>
                  <div className="flex gap-3">
                    <PersonTypeCard icon={User} title={t('add_business.individual')} description={t('add_business.individual_desc')} selected={data.personType === 'INDIVIDUAL'} onClick={() => { update('personType', 'INDIVIDUAL'); setTimeout(advance, 150) }} />
                    <PersonTypeCard icon={Building2} title={t('add_business.legal_entity')} description={t('add_business.legal_entity_desc')} selected={data.personType === 'LEGAL_ENTITY'} onClick={() => { update('personType', 'LEGAL_ENTITY'); setTimeout(advance, 150) }} />
                  </div>
                </div>
              )}

              {step === 'name' && (
                <div className="flex-1 flex flex-col">
                  <p className="text-base font-medium mb-4 text-center">{t('add_business.q_name')}</p>
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
                  <p className="text-base font-medium mb-1 text-center">{t('add_business.q_description')}</p>
                  <p className="text-xs text-[var(--color-ink-muted)] text-center mb-4">{t('add_business.optional_hint')}</p>
                  <textarea
                    ref={inputRef as React.RefObject<HTMLTextAreaElement>}
                    value={data.description}
                    onChange={(e) => update('description', e.target.value)}
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
                  <p className="text-base font-medium mb-4 text-center">{t('add_business.q_category')}</p>
                  <CategoryStep onSelect={(id, label) => { update('categoryId', id); update('categoryLabel', label); setTimeout(advance, 200) }} />
                </div>
              )}

              {step === 'locationType' && (
                <div>
                  <p className="text-base font-medium mb-5 text-center">{t('add_business.q_location_type')}</p>
                  <div className="flex gap-3">
                    <PersonTypeCard icon={MapPin} title={t('add_business.physical')} description={t('add_business.physical_desc')} selected={data.locationType === 'PHYSICAL'} onClick={() => { update('locationType', 'PHYSICAL'); setTimeout(advance, 150) }} />
                    <PersonTypeCard icon={Globe} title={t('add_business.online')} description={t('add_business.online_desc')} selected={data.locationType === 'ONLINE'} onClick={() => { update('locationType', 'ONLINE'); setTimeout(advance, 150) }} />
                  </div>
                </div>
              )}

              {step === 'place' && (
                <div className="flex-1 flex flex-col">
                  <p className="text-base font-medium mb-4 text-center">
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
                  <p className="text-base font-medium mb-1 text-center">{t('add_business.q_phone')}</p>
                  <p className="text-xs text-[var(--color-ink-muted)] text-center mb-4">{t('add_business.optional_hint')}</p>
                  <PhoneInput value={data.phoneDigits} onChange={(v) => update('phoneDigits', v)} />
                </div>
              )}

              {step === 'review' && (
                <div className="flex-1 flex flex-col">
                  <p className="text-base font-medium mb-1 text-center">{t('add_business.review_title')}</p>
                  <p className="text-xs text-[var(--color-ink-muted)] text-center mb-5">{t('add_business.review_hint')}</p>

                  <div className="flex flex-col gap-1.5 mb-4">
                    {reviewRows.map((row, i) => (
                      <button
                        key={i}
                        onClick={() => goTo(row.step)}
                        className="flex items-center gap-3 p-3 rounded-lg bg-[var(--color-bg)] border border-[var(--color-border)] hover:border-[var(--color-accent)]/40 transition-colors text-left"
                      >
                        {row.emoji ? (
                          <span className="text-base w-[18px] text-center shrink-0" aria-hidden="true">{row.label.split(' · ')[0]?.charAt(0)}</span>
                        ) : row.icon ? (
                          <row.icon size={17} className="text-[var(--color-accent)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
                        ) : null}
                        <span className="text-sm flex-1 truncate">{row.label}</span>
                        <ChevronRight size={15} className="text-[var(--color-ink-muted)] shrink-0" strokeWidth={1.75} aria-hidden="true" />
                      </button>
                    ))}
                  </div>

                  {data.description && (
                    <p className="text-sm text-[var(--color-ink-muted)] text-center italic">"{data.description}"</p>
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
                {(step === 'description' || step === 'phone') && !data.description && !data.phoneDigits ? t('common.skip') : t('common.next')}
              </button>
            )}
            {step === 'review' && (
              <button onClick={handleFinalSubmit} disabled={isSubmitting} className="flex-1 h-11 rounded-lg bg-[var(--color-accent)] text-white text-sm font-medium disabled:opacity-60">
                {isSubmitting ? t('add_business.creating') : t('add_business.confirm_create')}
              </button>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  )
}

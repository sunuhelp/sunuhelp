import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import LanguageDetector from 'i18next-browser-languagedetector'
import fr from '../locales/fr/common.json'
import en from '../locales/en/common.json'

// LanguageDetector lit/ecrit automatiquement dans localStorage - la langue
// choisie une fois reste active a chaque visite, jusqu'a changement explicite.
i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources: {
      fr: { translation: fr },
      en: { translation: en },
    },
    fallbackLng: 'fr',
    detection: {
      order: ['localStorage', 'navigator'],
      caches: ['localStorage'],
      lookupLocalStorage: 'sunuhelp_locale',
    },
    interpolation: { escapeValue: false },
  })

export default i18n

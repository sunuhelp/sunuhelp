import { Link } from 'react-router-dom'

interface LogoProps {
  size?: number
}

/**
 * Marque geometrique : un repere de localisation combine a une loupe -
 * evoque "trouver quelque chose pres de soi" sans texte, reutilisable
 * partout dans l'app (en-tete, pied de page), toujours cliquable vers
 * l'accueil.
 */
export function Logo({ size = 36 }: LogoProps) {
  return (
    <Link to="/" aria-label="Accueil" className="inline-flex items-center justify-center shrink-0">
      <svg width={size} height={size} viewBox="0 0 40 40" fill="none">
        <rect width="40" height="40" rx="10" fill="var(--color-accent)" />
        <path
          d="M20 9c-4.4 0-8 3.5-8 7.9 0 5.9 8 13.1 8 13.1s8-7.2 8-13.1c0-4.4-3.6-7.9-8-7.9z"
          fill="white"
          fillOpacity="0.95"
        />
        <circle cx="20" cy="16.5" r="3.2" fill="var(--color-accent)" />
      </svg>
    </Link>
  )
}

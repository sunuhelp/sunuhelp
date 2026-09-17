interface SkeletonProps {
  className?: string
}

// Squelette de contenu - donne l'impression que l'app repond deja,
// plutot qu'un ecran vide pendant le chargement.
export function Skeleton({ className = '' }: SkeletonProps) {
  return (
    <div
      className={`animate-pulse rounded-md bg-[var(--color-border)] ${className}`}
      aria-hidden="true"
    />
  )
}

import {
  Stethoscope, ShoppingBag, Wrench, Landmark, UtensilsCrossed,
  Building2, Croissant, ShieldQuestion,
} from 'lucide-react'
import type { LucideIcon } from 'lucide-react'

// Mapping par mot-cle du slug de categorie racine - reste valable meme
// si de nouvelles sous-categories sont ajoutees par un Admin plus tard.
const ICON_MAP: Record<string, LucideIcon> = {
  sante: Stethoscope,
  commerce: ShoppingBag,
  'artisanat-reparation': Wrench,
  finance: Landmark,
  restauration: UtensilsCrossed,
  hebergement: Building2,
  'religion-culte': Building2,
  boulangerie: Croissant,
}

export function getCategoryIcon(rootSlug: string | undefined): LucideIcon {
  if (!rootSlug) return ShieldQuestion
  return ICON_MAP[rootSlug] ?? Building2
}

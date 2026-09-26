import { toast } from 'sonner'

// Durees et couleurs cohérentes avec nos regles d'ergonomie :
// plus le message est grave, plus il reste longtemps ; une erreur
// ne disparait jamais seule, l'utilisateur doit la fermer ou agir.
export const notify = {
  success: (message: string) => toast.success(message, { duration: 3000 }),
  info: (message: string) => toast.info(message, { duration: 3000 }),
  warning: (message: string) => toast.warning(message, { duration: 4000 }),
  error: (message: string) => toast.error(message, { duration: Infinity, closeButton: true }),
}

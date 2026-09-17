import { QueryClient } from '@tanstack/react-query'

// staleTime : les donnees restent "fraiches" 30s avant qu'une nouvelle
// requete soit refaite en arriere-plan - evite de re-interroger le
// serveur a chaque retour sur un ecran deja visite recemment.
// retry : 1 nouvelle tentative silencieuse avant d'afficher une erreur -
// absorbe les coupures reseau breves, frequentes en mobilite.
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 30_000,
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
})

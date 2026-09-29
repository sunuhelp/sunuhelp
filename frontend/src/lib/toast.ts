import { toast } from 'sonner'

export const notify = {
  success: (message: string) => toast.success(message, { duration: 3000 }),
  info: (message: string) => toast.info(message, { duration: 3000 }),
  warning: (message: string) => toast.warning(message, { duration: 4000 }),
  error: (message: string) => toast.error(message, { duration: Infinity, closeButton: true }),
}

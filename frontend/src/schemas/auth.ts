import { z } from 'zod'

export const registerSchema = z.object({
  phoneDigits: z.string().length(9, 'validation.phone_incomplete'),
  email: z.string().email('validation.email_invalid').optional().or(z.literal('')),
  password: z.string().min(8, 'validation.password_too_short'),
})
export type RegisterFormData = z.infer<typeof registerSchema>

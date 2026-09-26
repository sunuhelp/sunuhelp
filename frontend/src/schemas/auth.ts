import { z } from 'zod'

// Regles identiques a RegisterRequest (auth-service) - jamais deux
// logiques de validation differentes entre frontend et backend.
export const registerSchema = z.object({
  phoneNumber: z
    .string()
    .min(1, 'validation.phone_required')
    .regex(/^\+?[0-9]{9,15}$/, 'validation.phone_invalid'),
  email: z.string().email('validation.email_invalid').optional().or(z.literal('')),
  password: z
    .string()
    .min(8, 'validation.password_too_short'),
})

export type RegisterFormData = z.infer<typeof registerSchema>

export const otpSchema = z.object({
  code: z.string().length(6, 'validation.otp_invalid'),
})

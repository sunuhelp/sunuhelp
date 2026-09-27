import { z } from 'zod'

export const businessInfoSchema = z.object({
  personType: z.enum(['INDIVIDUAL', 'LEGAL_ENTITY']),
  name: z.string().min(2, 'validation.name_required'),
  description: z.string().max(500, 'validation.description_too_long').optional(),
})
export type BusinessInfoData = z.infer<typeof businessInfoSchema>

export const locationSchema = z
  .object({
    type: z.enum(['PHYSICAL', 'ONLINE']),
    address: z.string().optional(),
    coverageZone: z.string().optional(),
    phoneNumber: z.string().regex(/^\+?[0-9]{9,15}$/, 'validation.phone_invalid').optional().or(z.literal('')),
  })
  .refine((data) => data.type !== 'PHYSICAL' || !!data.address?.trim(), {
    message: 'validation.address_required',
    path: ['address'],
  })
  .refine((data) => data.type !== 'ONLINE' || !!data.coverageZone?.trim(), {
    message: 'validation.coverage_zone_required',
    path: ['coverageZone'],
  })
export type LocationData = z.infer<typeof locationSchema>

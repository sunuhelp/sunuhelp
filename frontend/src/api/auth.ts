import api from '../lib/axios'

export interface RegisterPayload {
  phoneNumber: string
  email?: string
  password: string
}

export interface TokenResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  expiresIn: number
}

export async function register(payload: RegisterPayload) {
  const { data } = await api.post('/api/v1/auth/register', payload)
  return data
}

export async function verifyOtp(phoneNumber: string, code: string) {
  await api.post('/api/v1/auth/verify-otp', {
    phoneNumber,
    otpType: 'REGISTRATION',
    code,
  })
}

export async function login(phoneNumber: string, password: string): Promise<TokenResponse> {
  const { data } = await api.post('/api/v1/auth/login', { phoneNumber, password })
  return data
}

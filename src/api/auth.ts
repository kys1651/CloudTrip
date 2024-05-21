import client, { type BaseResponse } from '@/api/client'
import { useAuthenticationStore } from '@/stores/authentication'
import type {
  EmailConfirmForm,
  FindPasswordForm,
  JwtToken,
  ResetPasswordForm,
  SignInForm,
  SignUpForm
} from '@/types/auth.type'

export const signInRequest = async (signIn: SignInForm) => {
  const {
    data: {
      isSuccess,
      message,
      data: { accessToken }
    }
  } = await client.post<BaseResponse<JwtToken>>(`/auth/sign-in`, signIn)
  if (!isSuccess) throw new Error(message)

  const authentication = useAuthenticationStore()
  authentication.setAccessToken(accessToken)
}

export const signUpRequest = async (signUp: SignUpForm) => {
  console.log(signUp)
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/auth/sign-up`, signUp)
  if (!isSuccess) throw new Error(message)
}

export const findPasswordRequest = async (findPassword: FindPasswordForm) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/auth/find-password`, findPassword)
  if (!isSuccess) throw new Error(message)
}

export const resetPasswordRequest = async (resetPassword: ResetPasswordForm) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/auth/reset-password`, resetPassword)
  if (!isSuccess) throw new Error(message)
}

export const emailConfirmRequest = async (confirm: EmailConfirmForm) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/auth/confirm`, confirm)
  if (!isSuccess) throw new Error(message)
}

export const signOutRequest = async () => {
  await client.delete<BaseResponse<void>>(`/auth/sign-out`)
  const { clearAuthentication } = useAuthenticationStore()
  clearAuthentication()
}

export const refreshRequest = async () => {
  const {
    data: {
      isSuccess,
      message,
      data: { accessToken }
    }
  } = await client.post<BaseResponse<JwtToken>>(`/auth/refresh`)
  if (!isSuccess) throw new Error(message)

  const authentication = useAuthenticationStore()
  authentication.setAccessToken(accessToken)
}

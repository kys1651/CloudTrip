export enum FormStatus {
  SIGN_IN,
  SIGN_UP,
  FIND_PASSWORD,
}

export enum FindPasswordStatus {
  EMAIL,
  CONFIRM,
  RESET,
}

export interface JwtToken {
  accessToken: string
  refreshToken: string
}

export interface SignInForm {
  username: string
  password: string
}

export interface SignUpForm {
  username: string
  password: string
  email: string
  nickname: string
}

export interface FindPasswordForm {
  email: string
}

export interface EmailConfirmForm {
  type: string
  code: string
  email: string
}

export interface ResetPasswordForm {
  email: string
  password: string
}
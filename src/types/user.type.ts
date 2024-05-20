
export interface Authentication {
  isLogin: boolean
  profile?: Profile
}

export interface Profile {
  id: number
  username: string
  email: string
  nickname?: string
  profileImage?: string
  roleType: string
  isEmailVerified: boolean
  cityCode: number
  townCode: number
}

export interface IMyPage {
  username: string
  nickname: string
  cityCode: number
  townCode: number
  isDefault: boolean
}

export interface ChangePwForm {
  password: string
  passwordConfirm: string
}

export interface SimpleProfile {
  userId: number
  email: string
  nickname: string
  profileImage: string
  city: string
  town: string
}

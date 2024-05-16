export interface AUthentication {
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
  birth: string | undefined
  isDefault: boolean
}

export interface ChangePwForm {
  password: string
  passwordConfirm: string
}

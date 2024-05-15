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
}

export interface IMyPage {
  username: string
  nickname: string
  city: number
  town: number
  birth: string | undefined
}

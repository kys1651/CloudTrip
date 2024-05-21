import client, { type BaseResponse } from '@/api/client'
import { useAuthenticationStore } from '@/stores/authentication'
import type { Authentication, IMyPage, IUserCard, Profile, SimpleProfile } from '@/types/user.type'

export const userDataRequest = async () => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<Authentication>>(`/user/me`)
  if (!isSuccess) throw new Error(message)
  const store = useAuthenticationStore()
  if (data.isLogin) {
    store.updateProfile(data.profile!!)
  }
  return data
}

export const userDataByUserIdRequest = async (userId: number): Promise<Profile> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<Profile>>(`/user/${userId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const userDataModifyRequest = async (update: IMyPage) => {
  const formData = new FormData()
  formData.append('nickname', update.nickname)
  formData.append('cityCode', update.cityCode.toString())
  formData.append('townCode', update.townCode.toString())
  const {
    data: { isSuccess, message }
  } = await client.patch<BaseResponse<void>>('/user/me', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  if (!isSuccess) throw new Error(message)
  userDataRequest()
  return isSuccess
}

export const userDataModifyImageRequest = async (update: IMyPage, image: File | null) => {
  const formData = new FormData()
  if (image) {
    formData.append('profileImage', image)
  }
  formData.append('defaultImage', update.isDefault.toString())
  formData.append('nickname', update.nickname)
  formData.append('cityCode', update.cityCode.toString())
  formData.append('townCode', update.townCode.toString())
  const {
    data: { isSuccess, message }
  } = await client.patch<BaseResponse<void>>('/user/me', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  if (!isSuccess) throw new Error(message)
  userDataRequest()
  return isSuccess
}

export const changePasswordRequest = async (password: string) => {
  const {
    data: { isSuccess, message }
  } = await client.patch<BaseResponse<void>>('/user/updatePassword', {
    password: password
  })
  if (!isSuccess) throw new Error(message)
  return isSuccess
}

export const deleteRequest = async (userId: number) => {
  const {
    data: { code }
  } = await client.delete<BaseResponse<void>>(`/user/${userId}`)
  return true
}

export const simpleProfileRequest = async (userId: any): Promise<SimpleProfile> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SimpleProfile>>(`/user/${userId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const followCheckRequest = async (userId: number): Promise<boolean> => {
  const {
    data: { isSuccess, data }
  } = await client.get<BaseResponse<boolean>>(`/user/${userId}/check`)
  if (!isSuccess) return false
  return data
}

export const followRequest = async (userId: number): Promise<boolean> => {
  const {
    data: { isSuccess }
  } = await client.post<BaseResponse<void>>(`/user/${userId}/follow`)
  return isSuccess
}

export const unFollowRequest = async (userId: number): Promise<boolean> => {
  try {
    await client.delete<BaseResponse<void>>(`/user/${userId}/unfollow`)
    return true
  } catch (e) {
    return false
  }
}

export const followCountRequest = async (userId: number): Promise<number> => {
  const {
    data: { isSuccess, data, message }
  } = await client.get<BaseResponse<number>>(`/user/${userId}/followerCount`)
  if (!isSuccess) throw Error(message)
  return data
}

export const followingCountRequest = async (userId: number): Promise<number> => {
  const {
    data: { isSuccess, data, message }
  } = await client.get<BaseResponse<number>>(`/user/${userId}/followingCount`)
  if (!isSuccess) throw Error(message)
  return data
}

export const follwerRequest = async (userId: number): Promise<IUserCard[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IUserCard[]>>(`/user/${userId}/follower`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const follwingRequest = async (userId: number): Promise<IUserCard[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IUserCard[]>>(`/user/${userId}/following`)
  if (!isSuccess) throw new Error(message)
  return data
}

// export const getAllUser = async() => {
//   const {
//     data: {isSuccess, data, message}
//   } = await client.get<BaseResponse
// }
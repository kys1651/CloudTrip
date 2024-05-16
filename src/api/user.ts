import type {
  AUthentication,
  ChangePwForm,
  IMyPage,
  Profile,
  SimpleProfile
} from '@/types/user.type'
import client, { type BaseResponse } from '@/api/client'
import { useAuthenticationStore } from '@/stores/authentication'

export const userDataRequest = async () => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<AUthentication>>(`/user/me`)
  if (!isSuccess) throw new Error(message)
  const store = useAuthenticationStore()
  if (data.isLogin) {
    store.updateProfile(data.profile!!)
  }
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
  if (code === 204) {
    return false
  }
  return true
}

export const simpleProfileRequest = async (userId: any): Promise<SimpleProfile> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SimpleProfile>>(`/user/${userId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

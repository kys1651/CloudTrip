import type { AUthentication, Profile } from '@/types/user.type'
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

export const userDataModifyRequest = async (update: any) => {
  const formData = new FormData()
  formData.append('nickname', update.nickname)
  formData.append('cityCode', update.city)
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

export const userDataModifyImageRequest = async (update: any, image: File) => {
  const formData = new FormData()
  formData.append('profileImage', image)
  formData.append('nickname', update.nickname)
  console.log(formData.get('image'))
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

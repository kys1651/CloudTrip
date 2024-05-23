import client, { type BaseResponse } from '@/api/client'
import { toDate } from '@/lib/formatter'
import type {
  IChat,
  InviteForm,
  ISchedule,
  IScheduleSearch,
  ITripAndVehicle,
  PathResponse,
  ScheduleForm,
  ScheduleTripCreate
} from '@/types/schedule.type'

export const scheduleCreateRequest = async (
  form: ScheduleForm,
  image: File | undefined
): Promise<number> => {
  const formData = new FormData()
  formData.append('name', form.name)
  formData.append('multi', form.multi.toString())
  formData.append('private', form.private.toString())
  formData.append('startDate', toDate(form.start))
  formData.append('endDate', toDate(form.end))
  formData.append('cityCode', form.city.toString())
  formData.append('day', form.day.toString())
  if (image) {
    formData.append('image', image)
  }

  const {
    data: { isSuccess, message, data }
  } = await client.post<BaseResponse<number>>(`/schedule`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  if (!isSuccess) throw new Error(message)
  return data
}

export const scheduleListRequest = async (): Promise<ISchedule[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ISchedule[]>>(`/schedule`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const userScheduleListRequest = async (userId: number): Promise<IScheduleSearch[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IScheduleSearch[]>>(`/user/${userId}/schedule`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const scheduleDetailRequest = async (scheduleId: number): Promise<ISchedule> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ISchedule>>(`/schedule/${scheduleId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const scheduleTripCreateRequest = async (
  scheduleId: number,
  schedules: ScheduleTripCreate
) => {
  const {
    data: { isSuccess, message, data }
  } = await client.post<BaseResponse<void>>(`/schedule/${scheduleId}`, {
    trips: schedules.trips,
    vehicles: schedules.vehicles.map((vehicle) => ({
      ...vehicle,
      toTourId: vehicle.toTourId === 0 ? undefined : vehicle.toTourId
    }))
  })
  if (!isSuccess) throw new Error(message)
  return data
}

export const scheduleChatRequst = async (scheduleId: number): Promise<IChat[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IChat[]>>(`/schedule/${scheduleId}/chat`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const scheduleTripAndVehicleRequest = async (
  scheduleId: number
): Promise<ITripAndVehicle> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ITripAndVehicle>>(`/schedule/${scheduleId}/trip`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const schedulePathRequest = async (scheduleId: number): Promise<PathResponse> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<PathResponse>>(`/schedule/${scheduleId}/path`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const schedulePublicRequest = async (scheduleId: number): Promise<void> => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/schedule/${scheduleId}/public`)
  if (!isSuccess) throw new Error(message)
}

export const scheduleRevokeRequest = async (scheduleId: number): Promise<void> => {
  const {
    data: { isSuccess, message }
  } = await client.delete<BaseResponse<void>>(`/schedule/${scheduleId}/revoke`)
  if (!isSuccess) throw new Error(message)
}

export const scheduleInviteRequest = async (
  scheduleId: number,
  invite: InviteForm
): Promise<void> => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/schedule/${scheduleId}/invite`, invite)
  if (!isSuccess) throw new Error(message)
}

interface InviteConfirmRequest {
  scheduleId: number
  email: string
  code: string
}

export const scheduleInviteConfirmRequest = async (
  request: InviteConfirmRequest
): Promise<void> => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/schedule/${request.scheduleId}/invite/confirm`, {
    email: request.email,
    code: request.code
  })
  if (!isSuccess) throw new Error(message)
}

export const scheduleSearchRequest = async (params: any): Promise<IScheduleSearch[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IScheduleSearch[]>>(`/schedule`, {
    params: { ...params }
  })
  if (!isSuccess) throw new Error(message)
  return data
}

export const deleteScheduleListRequest = async (scheduleId: number): Promise<boolean> => {
  try {
    await client.delete<BaseResponse<void>>(`/schedule/${scheduleId}`)
    return true
  } catch (e) {
    return false
  }
}

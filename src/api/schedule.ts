import client, { type BaseResponse } from '@/api/client'
import { toDate } from '@/lib/formatter'
import type { IChat, ISchedule, ITripAndVehicle, ScheduleForm, ScheduleTripCreate } from '@/types/schedule.type'

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

export const scheduleDetailRequest = async (scheduleId: number): Promise<ISchedule> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ISchedule>>(`/schedule/${scheduleId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const scheduleTripCreateRequest = async (scheduleId: number, schedules: ScheduleTripCreate) => {
  const {
    data: { isSuccess, message, data }
  } = await client.post<BaseResponse<void>>(`/schedule/${scheduleId}`, schedules)
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

export const scheduleTripAndVehicleRequest = async (scheduleId: number): Promise<ITripAndVehicle> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ITripAndVehicle>>(`/schedule/${scheduleId}/trip`)
  if (!isSuccess) throw new Error(message)
  return data
}

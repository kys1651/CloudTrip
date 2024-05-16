import type { ICity, ITown, SearchQuery, SearchTrip, TripCategory } from '@/types/trip.type'
import type { BaseResponse } from './client'
import client from './client'

export const categoryRequest = async (): Promise<TripCategory[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<TripCategory[]>>(`/tour/category`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const citiesRequest = async (): Promise<ICity[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ICity[]>>(`/tour/city`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const townsRequest = async (cityCode: number): Promise<ITown[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ITown[]>>(`/tour/town/${cityCode}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const searchTripRequest = async (query: SearchQuery): Promise<SearchTrip[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SearchTrip[]>>(`/tour/search`, {
    params: query
  })
  if (!isSuccess) throw new Error(message)
  return data;
}

export const cityRequest = async (cityId: number): Promise<ICity> => {
  const { data: { isSuccess, message, data } } = await client.get<BaseResponse<ICity>>(`/tour/city/${cityId}`)
  if (!isSuccess) throw new Error(message)
  return data;
}

export const cityStayRequest = async (cityId: number): Promise<SearchTrip[]> => {
  const { data: { isSuccess, message, data } } = await client.get<BaseResponse<SearchTrip[]>>(`/tour/${cityId}/stay`)
  if (!isSuccess) throw new Error(message)
  return data;
}

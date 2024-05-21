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

export interface DirectionRequest {
  startTourId: number
  endTourId: number
  startX: number
  startY: number
  endX: number
  endY: number
}

export interface IStep {
  stepId: number
  vehicleId: number
  mode: string
  startName: string
  endName: string
  sectionTime: number
  distance: number
  routeName: string
}

export interface IDirection {
  vehicleId: number
  directionId: number
  fare: number
  spentTime: number
  walkTime: number
  transferCount: number
  distance: number
  walkDistance: number
  path: string
  steps: IStep[]
}

export interface MobilityResponse {
  walk: IDirection,
  bike: IDirection,
  bus?: IDirection,
  metro?: IDirection,
  car?: IDirection,
} 

export const directionRequest = async (direction: DirectionRequest): Promise<MobilityResponse> => {
  const { data: { isSuccess, message, data } } = await client.post<BaseResponse<MobilityResponse>>(`/direction`, direction)
  if (!isSuccess) throw new Error(message)
  return data;
}
import client, { type BaseResponse } from '@/api/client'
import type { ReviewForm, ReviewDetail, SearchCondition } from '@/types/board.type'

export const reviewWriteRequest = async (review: ReviewForm) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/review`, review)
  if (!isSuccess) throw new Error(message)
}

export const searchReviews = async (params: URLSearchParams): Promise<ReviewDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ReviewDetail[]>>(`/review/search?${params.toString()}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const reviewsRequest = async (): Promise<ReviewDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ReviewDetail[]>>(`/review`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const reviewRequest = async (id: number): Promise<ReviewDetail> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ReviewDetail>>(`/review/${id}`)
  if (!isSuccess) throw new Error(message)
  return data
}

import client, { type BaseResponse, type PageResponse } from '@/api/client'
import type {
  IReview,
  ReviewCommentDetail,
  ReviewDetail,
  ReviewForm,
  SimpleCommentDetail
} from '@/types/board.type'

export const reviewWriteRequest = async (review: ReviewForm) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/review`, review)
  if (!isSuccess) throw new Error(message)
}

export const reviewsRequest = async ({
  pageParam = 0
}: {
  pageParam: number
}): Promise<PageResponse<IReview>> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<PageResponse<IReview>>>(`/review`, {
    params: { page: pageParam }
  })
  if (!isSuccess) throw new Error(message)
  return data
}

export const myReviewsRequest = async (userId: number): Promise<IReview[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IReview[]>>(`/review/${userId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const myCommentRequest = async (userId: number): Promise<SimpleCommentDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SimpleCommentDetail[]>>(`/comment/simple/${userId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const LikeReviewRequest = async (userId: number): Promise<SimpleCommentDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SimpleCommentDetail[]>>(`/review/like/${userId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const reviewCreateRequest = async (review: ReviewForm, images: File[]) => {
  const formData = new FormData()
  images.forEach((image) => {
    formData.append('images', image)
  })
  formData.append('content', review.content)
  formData.append('tourId', review.tourId.toString())

  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/review`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  if (!isSuccess) throw new Error(message)
  return isSuccess
}

export const reviewRequest = async (id: number): Promise<ReviewDetail> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<ReviewDetail>>(`/review/${id}`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const reviewLikeRequest = async (id: number) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/review/${id}/like`)
  if (!isSuccess) throw new Error(message)
}

export const reviewDisLikeRequest = async (id: number) => {
  const {
    data: { isSuccess, message }
  } = await client.delete<BaseResponse<void>>(`/review/${id}/like`)
  if (!isSuccess) throw new Error(message)
}

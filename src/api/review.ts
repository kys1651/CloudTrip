import client, { type BaseResponse, type PageResponse } from '@/api/client'
import type {
  IComment,
  IReview,
  ReviewDetail,
  ReviewForm,
  SimpleCommentDetail,
  SumaryReview
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

export const userReviewsRequest = async (userId: number): Promise<SumaryReview[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SumaryReview[]>>(`/user/${userId}/reviews`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const userLikedReviewRequest = async (userId: number): Promise<SumaryReview[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SumaryReview[]>>(`/user/${userId}/likes`)
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
  formData.append('rating', review.rating.toString())
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

export const reviewDeleteRequest = async (id: number) => {
  const {
    data: { code }
  } = await client.delete<BaseResponse<void>>(`/review/${id}`)
  if (code === 204) {
    return false
  }
  return true
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

export const userCommentsRequest = async (userId: number): Promise<SimpleCommentDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<SimpleCommentDetail[]>>(`/user/${userId}/comments`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const commentCreateRequest = async (reviewId: number, content: string) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/review/${reviewId}/comment`, { content })
  if (!isSuccess) throw new Error(message)
}

export const commentsRequest = async (reviewId: number): Promise<IComment[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<IComment[]>>(`/review/${reviewId}/comment`)
  if (!isSuccess) throw new Error(message)
  return data
}

export const commentDeleteRequest = async (commentId: number) => {
  const {
    data: { code }
  } = await client.delete<BaseResponse<void>>(`/review/${commentId}/comment`)
  if (code === 204) {
    return false
  }
  return true
}

export const commentUpdateRequest = async (commentId: number, content: string) => {
  const {
    data: { isSuccess }
  } = await client.patch<BaseResponse<void>>(`/review/${commentId}/comment`, {
    content: content
  })
  return isSuccess
}

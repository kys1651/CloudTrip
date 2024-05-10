import client, { type BaseResponse } from '@/api/client'
import type { ReviewCommentForm, ReviewCommentDetail } from '@/types/board.type'

export const commentWriteRequest = async (commentForm: ReviewCommentForm) => {
  const {
    data: { isSuccess, message }
  } = await client.post<BaseResponse<void>>(`/comment`, commentForm)
  if (!isSuccess) throw new Error(message)
}

export const commentsRequest = async (reviewId: Number): Promise<ReviewCommentDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<Promise<ReviewCommentDetail[]>>>(`/comment/${reviewId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

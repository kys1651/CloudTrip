import client, { type BaseResponse } from '@/api/client'
import type { ReviewCommentDetail } from '@/types/board.type'

export const commentsRequest = async (reviewId: Number): Promise<ReviewCommentDetail[]> => {
  const {
    data: { isSuccess, message, data }
  } = await client.get<BaseResponse<Promise<ReviewCommentDetail[]>>>(`/comment/${reviewId}`)
  if (!isSuccess) throw new Error(message)
  return data
}

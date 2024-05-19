import type { IChatBotMessage } from '@/types/chat.type'
import client, { type BaseResponse } from './client'

export const aiResponseRequest = async (userRequest: string): Promise<string> => {
  const {
    data: { isSuccess, data }
  } = await client.post<BaseResponse<string>>(`/gpt/chat`, {
    userRequest: userRequest
  })
  if (!isSuccess) return '현재 응답할 수 없는 상태입니다. 다시 요청해주세요'
  return data
}

export const reviewRecommendRequest = async (
  location: string,
  content: string
): Promise<string> => {
  const {
    data: { isSuccess, message, data }
  } = await client.post<BaseResponse<string>>(`/gpt/review`, {
    content: content,
    location: location
  })
  if (!isSuccess) throw new Error(message)
  return data
}

export const userChatBotRequest = async (): Promise<IChatBotMessage[]> => {
  const {
    data: { isSuccess, data }
  } = await client.get<BaseResponse<IChatBotMessage[]>>(`/gpt/chat`)
  if (!isSuccess) throw Error('현재 조회할 수 있는 채팅 기록이 없습니다.')
  return data
}

export const deleteChatReqeust = async (): Promise<void> => {
  const {
    data: { isSuccess }
  } = await client.delete<BaseResponse<void>>(`/gpt/chat`)
}

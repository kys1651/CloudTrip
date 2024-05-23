import client, { type BaseResponse } from '@/api/client'
import { type ISearch } from '@/types/search.type'

export const searchResult = async (searchKeyword: string) => {
  const {
    data: { isSuccess, data, message }
  } = await client.get<BaseResponse<ISearch>>('/search', {
    params: {
      searchKeyword: searchKeyword
    }
  })
  if (!isSuccess) throw new Error(message)
  return data
}

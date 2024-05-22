import axios from 'axios'

export const FRONTEND_URL = 'http://localhost:5173'
const client = axios.create({
  baseURL: 'https://dapi.kakao.com',
  headers: {
    Authorization: `KakaoAK ${import.meta.env.VITE_APP_KAKAO_REST_API_KEY}`,
    contentType: 'application/json',
    'Access-Control-Allow-Origin': '*'
  }
})

export interface KakaoSearchParams {
  query: string
  x: number
  y: number
  radius: number
}

export interface KakaoSearchResponse {
  total_count: number
  pageable_count: number
  is_end: boolean
  documents: KakaoSearch[]
}

export interface KakaoSearch {
  id: string
  place_name: string
  distance: string
  place_url: string
  address_name: string
  road_address_name: string
  x: number
  y: number
}

export const searchAddress = async (params: KakaoSearchParams) => {
  const { data, status } = await client.get<KakaoSearchResponse>('/v2/local/search/address.json', {
    params
  })

  if (status !== 200) {
    console.error(data)
    throw new Error('Failed to fetch data')
  }

  return data
}

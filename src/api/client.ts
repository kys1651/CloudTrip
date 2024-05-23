import { useAuthenticationStore } from '@/stores/authentication'
import type { JwtToken } from '@/types/auth.type'
import axios from 'axios'
import { storeToRefs } from 'pinia'

const BACKEND_URL = import.meta.env.VITE_APP_BACKEND_URL
const BASE_URL = `${BACKEND_URL}/api/v1`

export const NAVER_LOGIN_URL = `${BACKEND_URL}/oauth2/authorization/naver`
export const GOOGLE_LOGIN_URL = `${BACKEND_URL}/oauth2/authorization/google`
export const KAKAO_LOGIN_URL = `${BACKEND_URL}/oauth2/authorization/kakao`
export const GITHUB_LOGIN_URL = `${BACKEND_URL}/oauth2/authorization/github`

const instance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json'
  }
})
const REFRESH_URI = `${BASE_URL}/auth/refresh`

instance.interceptors.request.use(
  (config) => {
    const authentication = useAuthenticationStore()
    const { isLogin, accessToken } = storeToRefs(authentication)
    if (isLogin) {
      config.headers['Authorization'] = `Bearer ${accessToken.value}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

instance.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const {
      config,
      response: { status }
    } = error
    const originalRequest = config
    const authentication = useAuthenticationStore()

    // 인증 에러가 아닌 경우 바로 에러 처리
    if (status !== 401) return Promise.reject(error)

    // Refresh Token 재발급 중 에러가 발생한 경우
    if (config.url === REFRESH_URI) {
      authentication.clearAuthentication()
      throw new Error('인증 기간이 만료되었습니다. 다시 로그인 해주세요.')
    }

    try {
      const {
        data: {
          isSuccess,
          message,
          data: { accessToken: newAccessToken }
        }
      } = await instance.post<BaseResponse<JwtToken>>(REFRESH_URI)
      if (!isSuccess) throw new Error(message)

      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
      authentication.setAccessToken(newAccessToken)
      return axios(originalRequest)
    } catch (error) {
      authentication.clearAuthentication()

      throw new Error('인증 과정에서 에러가 발생하였습니다.')
    }
  }
)

export interface BaseResponse<T> {
  isSuccess: boolean
  code: number
  message: string
  data: T
  errors: object[]
}

export interface PageResponse<T> {
  contents: T[]
  nextCursor: number
  hasNext: boolean
}

export default instance

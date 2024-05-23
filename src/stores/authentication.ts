import type { Profile } from '@/types/user.type'
import { jwtDecode } from 'jwt-decode'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAuthenticationStore = defineStore('authentication', () => {
  const accessToken = ref<string | undefined>(undefined)
  const profile = ref<Profile | undefined>(undefined)
  const expireTime = computed<number | undefined>(() => {
    if (accessToken.value) {
      return (jwtDecode(accessToken.value).exp!! * 1000) - (1000 * 60 * 3)
    }
    return undefined
  })

  const isLogin = computed(
    () => accessToken.value !== undefined && accessToken.value.length > 0 && profile
  )

  const authUserId = computed(() => {
    return profile.value?.id || 0
  })

  const setAccessToken = (newAccessToken: string) => {
    accessToken.value = newAccessToken
  }

  const clearAuthentication = () => {
    accessToken.value = undefined
    profile.value = undefined
  }

  const updateProfile = (newProfile: Profile) => {
    profile.value = newProfile
  }

  return { accessToken, expireTime, profile, authUserId, isLogin, clearAuthentication, setAccessToken, updateProfile }
}, { 
  persist: {
    storage: sessionStorage,
    key: 'authentication',
    paths: ['accessToken', 'profile'],
    
  }
 })

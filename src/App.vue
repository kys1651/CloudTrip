<script setup lang="ts">
import { userDataRequest } from '@/api/user'
import CommonHeader from '@/components/common/CommonHeader.vue'
import Toaster from '@/components/ui/toast/Toaster.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { VueQueryDevtools } from '@tanstack/vue-query-devtools'
import { storeToRefs } from 'pinia'
import { RouterView } from 'vue-router'
import { refreshRequest } from './api/auth'
import Chatbot from './components/chat/Chatbot.vue'

const authenticationStore = useAuthenticationStore()
const { isLogin } = storeToRefs(authenticationStore)

if (!isLogin.value) {
  if (sessionStorage.getItem('accessToken')) {
    authenticationStore.setAccessToken(sessionStorage.getItem('accessToken')!)
    userDataRequest().then((response) => {
      if (!response.isLogin) {
        refreshRequest().then(() => {
          userDataRequest()
        })
      }
    })
  } else {
    refreshRequest().then(() => {
      userDataRequest()
    })
  }
}
</script>

<template>
  <CommonHeader />
  <RouterView />
  <Toaster />
  <VueQueryDevtools position="left" buttonPosition="bottom-left" />
  <Chatbot />
</template>

<template>
  <div>
    <!-- 챗봇 창이 열려 있을 때 표시되는 부분 -->
    <div
      v-if="isOpen"
      class="fixed bottom-10 right-10 w-[600px] h-[800px] bg-white border border-gray-300 shadow-lg flex flex-col"
    >
      <!-- 챗봇 창의 헤더 부분 -->
      <ChatHeader :toggleChat="toggleChat" />
      <!-- 채팅 메시지 섹션 -->
      <!-- <ChatMessage v-if="isLogin && profile" :messages="messages" /> -->
      <ChatMessage v-if="isLogin && profile && messages.length > 0" :messages="messages" />
      <div
        v-else-if="isLogin && profile && messages.length == 0"
        class="flex flex-1 items-center justify-center text-xl text-gray-700"
      >
        현재 기록된 채팅이 존재하지 않습니다. <br />
        챗봇에게 인사를하여 채팅을 시작해보세요!
      </div>
      <div v-else class="flex flex-1 items-center justify-center text-xl text-gray-700">
        로그인 후 이용해주세요
      </div>

      <!-- 입력 창 섹션 -->
      <ChatInput v-if="isLogin && profile" @send-message="sendMessage" />
    </div>
    <div
      v-else
      class="fixed bottom-10 right-10 w-20 h-20 cursor-pointer flex items-center justify-center text-white rounded-full shadow-lg bg-gradient-to-r from-blue-400 to-blue-600"
      @click="toggleChat"
    >
      <div class="flex items-center justify-center w-16 h-16 rounded-full bg-white shadow-inner">
        <div
          class="flex items-center justify-center w-full h-full rounded-full border-2 border-gray-300"
        >
          <Bot class="w-10 h-10 text-blue-500" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import ChatHeader from './ChatHeader.vue'
import ChatInput from './ChatInput.vue'
import ChatMessage from './ChatMessage.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import type { IChatBotMessage } from '@/types/chat.type'
import { aiResponseRequest, deleteChatReqeust, userChatBotRequest } from '@/api/chat'
import { Bot } from 'lucide-vue-next'
import { useQuery, useQueryClient } from '@tanstack/vue-query'
const authenticationStore = useAuthenticationStore()
const { isLogin, profile } = storeToRefs(authenticationStore)

// 챗봇 창이 열려 있는지 여부를 저장하는 상태 변수
const isOpen = ref(false)

// 메시지를 저장하는 상태 변수
const messages = ref<IChatBotMessage[]>([])

// Query Client 인스턴스 생성
const queryClient = useQueryClient()

// useQuery를 통해 데이터를 가져오고, 가져온 데이터가 변경될 때마다 messages를 업데이트
const { data: savedMessages } = useQuery({
  queryKey: ['messages', profile.value?.id],
  queryFn: () => userChatBotRequest()
})

// watchEffect로 savedMessages가 변경될 때마다 messages를 업데이트
watchEffect(() => {
  if (savedMessages.value) {
    messages.value = savedMessages.value
  }
})

// 챗봇 창을 열고 닫는 함수
const toggleChat = () => {
  isOpen.value = !isOpen.value
}

// 사용자 메시지를 보내는 함수
const sendMessage = async (userInput: string) => {
  if (userInput.trim().length === 0) {
    return
  }

  // IChatBotMessage 인터페이스를 따르는 새 메시지 객체 생성
  const newMessage = ref<IChatBotMessage>({
    userRequest: userInput,
    aiResponse: ''
  })

  if (userInput == '/clear') {
    newMessage.value.aiResponse =
      '알겠습니다. 지금까지의 모든 채팅 내역을 삭제하겠습니다. 채팅 내역 청소중🧹💬'
    messages.value = [newMessage.value]
    await deleteChatReqeust()
    return
  }

  // 응답 받기 전에 메시지를 추가
  messages.value = [...messages.value, newMessage.value]

  // // AI 응답을 요청하고 백엔드에 메시지 저장
  const response = await aiResponseRequest(userInput)
  newMessage.value.aiResponse = response

  // useQuery에서 사용하는 캐시를 지워줌
  queryClient.invalidateQueries(['messages', profile.value?.id])
}
</script>

<template>
  <div>
    <div
      v-if="!isChatbotOpen"
      class="fixed bottom-4 right-4 flex items-center justify-center w-16 h-16 bg-blue-500 rounded-full shadow-lg cursor-pointer"
      @click="toggleChatbot"
    >
      <svg
        class="w-8 h-8 text-white"
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M8 9l4-4 4 4m0 6l-4 4-4-4"
        />
      </svg>
    </div>

    <div
      v-if="isChatbotOpen"
      class="fixed inset-0 bg-opacity-50 z-40"
      @click.self="closeChatbot"
    ></div>

    <div
      v-if="isChatbotOpen"
      class="fixed bottom-20 right-4 w-80 bg-white rounded-lg shadow-lg z-50"
    >
      <div class="p-4 border-b">
        <h2 class="text-lg font-semibold">Chatbot</h2>
      </div>
      <div class="p-4 h-64 overflow-y-auto">
        <p class="text-gray-700">Welcome! How can I assist you today?</p>
        <!-- 채팅 메시지 및 입력 필드 등을 여기에 추가 -->
      </div>
      <div class="p-4 border-t">
        <input
          type="text"
          class="w-full p-2 border rounded"
          placeholder="Type a message..."
          v-model="userMessage"
          @keydown.enter="sendMessage"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const isChatbotOpen = ref(false)
const userMessage = ref('')

const toggleChatbot = () => {
  isChatbotOpen.value = !isChatbotOpen.value
}

const closeChatbot = () => {
  isChatbotOpen.value = false
}

const sendMessage = () => {
  // 메시지를 전송하는 로직을 여기에 추가
  console.log('User message:', userMessage.value)
  userMessage.value = ''
}

onMounted(() => {
  document.addEventListener('click', (event) => {
    const chatbotElement = document.querySelector(
      '.fixed.bottom-4.right-4.flex.items-center.justify-center.w-16.h-16.bg-blue-500.rounded-full.shadow-lg.cursor-pointer'
    )
    if (chatbotElement && !chatbotElement.contains(event.target as Node) && isChatbotOpen.value) {
      closeChatbot()
    }
  })
})
</script>

<style scoped>
/* 추가적인 스타일을 여기에 정의할 수 있습니다. */
</style>

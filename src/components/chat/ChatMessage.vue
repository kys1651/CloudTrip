<template>
  <!-- 채팅 메시지 섹션 -->
  <div ref="chatContainer" class="flex-1 overflow-auto p-4">
    <div class="space-y-4">
      <div
        v-for="message in messages"
        :key="message.userRequest + message.aiResponse"
        class="space-y-4"
      >
        <!-- 사용자 요청 메시지 -->
        <UserMessage :message="message.userRequest" />
        <!-- AI 응답 메시지 -->
        <BotMessage :message="message.aiResponse" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, nextTick, onMounted, ref, watch } from 'vue'
import type { IChatBotMessage } from '@/types/chat.type'
import UserMessage from './UserMessage.vue'
import BotMessage from './BotMessage.vue'

const props = defineProps<{ messages: IChatBotMessage[] }>()

const chatContainer = ref(null)

// 스크롤을 맨 아래로 이동시키는 함수
const scrollToBottom = () => {
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

// 컴포넌트가 마운트될 때 스크롤을 맨 아래로 이동
onMounted(() => {
  scrollToBottom()
})

// 메시지가 변경될 때 스크롤을 맨 아래로 이동
watch(
  () => props.messages,
  () => {
    nextTick(() => {
      scrollToBottom()
    })
  },
  { deep: true }
)
</script>

<template>
  <!-- 챗봇 창의 헤더 부분 -->
  <header class="flex items-center justify-between bg-gray-900 p-4 text-white">
    <div class="flex items-center space-x-2">
      <IconChat />
      <h2 class="text-lg font-medium">Chat Bot</h2>
    </div>
    <button @click="toggleChat" class="rounded-full bg-gray-800 p-2 hover:bg-gray-700">
      <IconX />
    </button>
  </header>
</template>

<script setup lang="ts">
import IconX from '../icons/IconX.vue'
import IconChat from '../icons/IconChat.vue'

// 부모 컴포넌트로부터 전달받은 props
defineProps<{ toggleChat: () => void }>()
</script>

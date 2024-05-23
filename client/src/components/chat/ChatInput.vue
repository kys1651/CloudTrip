<template>
  <!-- 입력 창 섹션 -->
  <div class="border-t border-gray-200 bg-white p-4 dark:border-gray-800 dark:bg-gray-900">
    <div class="flex items-center space-x-2">
      <input
        ref="inputRef"
        v-model="userInput"
        @keyup.enter="send"
        class="flex h-10 w-full border border-input text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 flex-1 rounded-lg bg-gray-100 px-4 py-2 text-gray-800 focus:outline-none dark:bg-gray-800 dark:text-gray-200"
        placeholder="채팅 기록을 지우려면 '/clear'를 입력하세요"
        type="text"
      />
      <Button @click="send" size="icon" variant="ghost">
        <Send :size="20" />
      </Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Button from '../ui/button/Button.vue'
import { Send } from 'lucide-vue-next'

const userInput = ref('')
const inputRef = ref<HTMLInputElement | null>(null)
const emit = defineEmits(['send-message'])

const send = () => {
  // 입력값이 없다면 포커스
  if (userInput.value.trim().length === 0) {
    inputRef.value?.focus()
    return
  }
  emit('send-message', userInput.value)
  userInput.value = ''
}
</script>

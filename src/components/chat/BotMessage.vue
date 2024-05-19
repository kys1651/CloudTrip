<template>
  <div class="flex items-start space-x-3">
    <div class="flex-shrink-0">
      <div class="rounded-full border-2 border-gray-500 p-1" style="width: 60px; height: 60px">
        <Bot class="w-10 h-10 text-gray-500" style="width: 100%; height: 100%" />
      </div>
    </div>
    <div
      class="max-w-[70%] rounded-lg bg-gray-100 p-3 text-gray-800 dark:bg-gray-800 dark:text-gray-200"
    >
      <p v-if="message" v-html="message"></p>
      <div v-else class="flex items-center space-x-2">
        <span>AI가 응답을 생성중입니다</span>
        <LoaderCircle class="mr-2 h-5 w-5 animate-spin text-blue-500" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import { LoaderCircle, Bot } from 'lucide-vue-next'

defineProps<{ message: string }>()
</script>

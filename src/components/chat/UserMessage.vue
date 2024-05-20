<template>
  <div class="flex justify-end space-x-3">
    <div class="max-w-[70%] rounded-lg bg-blue-500 p-3 text-white flex items-center me-2">
      <p class="m-0">{{ message }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

defineProps<{ message: string }>()
</script>

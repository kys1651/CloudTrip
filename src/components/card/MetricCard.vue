<template>
  <div class="rounded-lg p-4 flex flex-col items-center justify-center">
    <h3 class="text-2xl font-bold">{{ shownCount.toLocaleString() }}</h3>
    <p class="text-gray-500 dark:text-gray-400">{{ name }}</p>
  </div>
</template>

<script setup lang="ts">
import type { MetricProps } from '@/types/trip.type'
import { onMounted, ref } from 'vue'

const props = defineProps<MetricProps>()

const shownCount = ref(0)

onMounted(() => {
  const counting = setInterval(() => {
    if (shownCount.value >= props.count) {
      shownCount.value = props.count
      clearInterval(counting)
    } else {
      shownCount.value += props.count / 100
    }
  }, 10)
})
</script>

<style scoped></style>

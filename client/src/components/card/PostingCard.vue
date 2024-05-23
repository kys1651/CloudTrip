<script setup lang="ts">
import type { PostingSummaryProps } from '@/types/trip.type'
import IconCalendar from '../icons/IconCalendar.vue'
import IconEyes from '../icons/IconEyes.vue'
import AspectRatio from '@/components/ui/aspect-ratio/AspectRatio.vue'
defineProps<PostingSummaryProps>()
</script>
<template>
  <div
    class="rounded-lg border bg-card text-card-foreground shadow-sm cursor-pointer select-none"
    data-v0-t="card"
  >
    <div class="p-0">
      <AspectRatio :ratio="4 / 3">
        <img :src="backgroundImage" alt="Blog Post" class="object-cover h-full w-full" />
      </AspectRatio>
    </div>
    <div class="flex flex-col justify-center items-start p-4 space-y-2">
      <span class="font-medium">{{ name }}</span>
      <div class="text-sm text-gray-500 dark:text-gray-400 line-clamp-2">
        {{ description }}
      </div>
      <div class="w-full flex items-center mt-2 flex-row justify-between">
        <div class="flex flex-row space-x-1 items-center">
          <IconCalendar />
          <span class="text-sm text-gray-500 dark:text-gray-400">{{
            createdAt.toLocaleDateString()
          }}</span>
        </div>
        <div class="flex flex-row space-x-1 items-center">
          <IconEyes />
          <span class="text-sm text-gray-500 dark:text-gray-400">{{ views.toLocaleString() }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

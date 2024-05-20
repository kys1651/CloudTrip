<script setup lang="ts">
import type { TripSummaryProps } from '@/types/trip.type'
import IconLocation from '../icons/IconLocation.vue'
import IconRating from '../icons/IconRating.vue'
import { computed } from 'vue';

const props = defineProps<TripSummaryProps>()

const shortDescription = computed(() => {
  return props.description.length > 10
    ? props.description.slice(0, 70) + '...'
    : props.description
})

</script>
<template>
  <div
    class="rounded-lg border bg-card text-card-foreground shadow-sm cursor-pointer select-none"
    data-v0-t="card"
  >
    <div class="p-0">
      <img
        :src="backgroundImage"
        width="400"
        height="300"
        alt="Destination"
        class="aspect-[4/3] object-cover rounded-t-lg"
      />
    </div>
    <div class="flex items-center p-4 flex-col">
      <!-- 여행지 이름과 별점 및 여행지 위치 소개 -->
      <div class="flex flex-row w-full justify-between pb-2">
        <span class="font-medium">{{ name }}</span>
        <div class="flex flex-row items-center space-x-4">
          <div class="flex flex-row items-center space-x-1">
            <IconLocation />
            <span class="text-sm text-gray-500 dark:text-gray-400 br-4">{{ location }}</span>
          </div>
          <div class="flex flex-row items-center space-x-1">
            <IconRating />
            <span class="text-sm font-medium">{{ rating }}</span>
          </div>
        </div>
      </div>
      <!-- 여행지 정보 상세 설명  -->
      <div class="text-sm font-light text-gray-400 dark:text-gray-300">
        {{ shortDescription }}
      </div>
      <div class="flex items-center gap-2 mt-2"></div>
    </div>
  </div>
</template>

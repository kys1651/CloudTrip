<script setup lang="ts">
import { type IDirection } from '@/api/trip';
import { convertDistance, convertTime } from '@/lib/formatter';
import { Car } from "lucide-vue-next";

defineProps<{
  car?: IDirection;
}>();
</script>

<template>
  <div class="flex flex-row items-center">
    <Car class="mr-5" :size="16" />
    <div
      v-if="car"
      class="text-gray-500 dark:text-gray-400 py-4 flex flex-col justify-start text-xs"
    >
      <span>소요시간: {{ convertTime(car.spentTime) }}</span>
      <span>거리: {{ convertDistance(car.distance) }}</span>
    </div>
    <div
      v-else
      class="text-gray-500 dark:text-gray-400 py-4 flex flex-col justify-start text-xs"
    >
      <span>자동차 정보가 없습니다.</span>
    </div>
  </div>
</template>

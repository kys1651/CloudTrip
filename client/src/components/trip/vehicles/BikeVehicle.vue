<script setup lang="ts">
import type { IDirection } from "@/api/trip";
import { convertDistance, convertTime } from "@/lib/formatter";
import { Bike } from "lucide-vue-next";

defineProps<{
  bike?: IDirection;
}>();
</script>

<template>
  <div class="flex flex-row items-center">
    <Bike class="mr-5" :size="16" />
    <div
      class="text-gray-500 dark:text-gray-400 py-4 flex flex-col justify-start text-xs"
    >
      <span>소요시간: {{ convertTime(bike?.spentTime || 0) }}</span>
      <span>거리: {{ convertDistance(bike?.spentTime || 0) }}</span>
    </div>
  </div>
</template>

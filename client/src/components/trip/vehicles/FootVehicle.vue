<script setup lang="ts">
import type { IDirection } from "@/api/trip";
import { convertDistance, convertTime } from "@/lib/formatter";
import { Footprints } from "lucide-vue-next";

defineProps<{
  walk?: IDirection;
}>();
</script>

<template>
  <div class="flex flex-row items-center">
    <Footprints class="mr-5" :size="16" />
    <div
      class="text-gray-500 dark:text-gray-400 py-4 flex flex-col justify-start text-xs"
    >
      <span>소요시간: {{ convertTime(walk?.spentTime || 0) }}</span>
      <span>거리: {{ convertDistance(walk?.distance || 0) }}</span>
    </div>
  </div>
</template>

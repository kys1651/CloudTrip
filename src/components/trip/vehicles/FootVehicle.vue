<script setup lang="ts">
import { convertDistance, convertTime } from "@/lib/formatter";
import { Footprints } from "lucide-vue-next";

defineProps<{
  time: number;
  distance: number;
}>();
</script>

<template>
  <div class="flex flex-row items-center">
    <Footprints class="mr-5" :size="16" />
    <div
      class="text-gray-500 dark:text-gray-400 py-4 flex flex-col justify-start text-xs"
    >
      <span>소요시간: {{ convertTime(time) }}</span>
      <span>거리: {{ convertDistance(distance) }}</span>
    </div>
  </div>
</template>

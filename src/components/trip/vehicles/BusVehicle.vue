<script setup lang="ts">
import { type IDirection } from '@/api/trip';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger
} from '@/components/ui/tooltip';
import { convertDistance, convertTime } from '@/lib/formatter';
import { ArrowRight, Bus, Search } from "lucide-vue-next";


defineProps<{
  bus?: IDirection;
}>();
</script>

<template>
  <TooltipProvider>
    <Tooltip>
      <div class="flex flex-row items-center w-full">
        <Bus class="mr-5" :size="16" />
        <div
          v-if="bus"
          class="text-gray-500 dark:text-gray-400 py-4 flex flex-row justify-around w-4/5 text-xs"
        >
          <div class="flex flex-col">
            <span>소요시간: {{ convertTime(bus.spentTime) }}</span>
            <span>거리: {{ convertDistance(bus.distance) }}</span>
            <span>비용: {{ bus.fare }}원</span>
            <span v-if="bus.transferCount > 0">환승 횟수: {{ bus.transferCount }}회</span>
          </div>
          <TooltipTrigger>
            <div class="flex flex-col items-center justify-center space-y-1">
              <Search :size="14" />
              <span>자세히</span>
            </div>
          </TooltipTrigger>
          <TooltipContent variant="outline" class="py-4">
            <div class="flex mt-3 flex-col text-xs space-y-2 w-full">
              <div
                v-for="(step, index) in bus.steps"
                :key="step.stepId"
                class="flex flex-col w-full"
              >
                <div class="w-full flex justify-start">
                  {{ index + 1 }}.
                  {{ step.routeName === "WALK" ? "도보 이동" : step.routeName }}
                </div>
                <div class="flex flex-row space-x-2 items-center">
                  <span>{{ step.startName }}</span>
                  <ArrowRight :size="12" />
                  <span>{{ step.endName }}</span>
                </div>
                <div class="flex flex-row space-x-2 items-center">
                  <span>{{ convertDistance(step.distance) }}</span>
                  <span>{{ convertTime(step.sectionTime) }}</span>
                </div>
              </div>
            </div>
          </TooltipContent>
        </div>
        <div
          v-else
          class="text-gray-500 dark:text-gray-400 py-4 flex flex-col justify-start text-xs"
        >
          <span>버스 정보가 없습니다.</span>
        </div>
      </div>
    </Tooltip>
  </TooltipProvider>
</template>

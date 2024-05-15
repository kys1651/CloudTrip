<script setup lang="ts">
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { useToast } from "@/components/ui/toast";
import { imageOrDefault } from "@/lib/image-load";
import { TEXT_COLORS, useTripPlanStore } from "@/stores/trip-plan";
import { storeToRefs } from "pinia";
import { VueDraggableNext } from "vue-draggable-next";

const { pickedTrips } = storeToRefs(useTripPlanStore());

const days = Array.from({ length: pickedTrips.value.length }, (_, i) => `${i + 1}일차`);

const toast = useToast();

const onSubmit = () => {};
// TODO: 하버사인 알고리즘을 사용하여 마지막 장소 근처의 최단 거리를 순서대로 추천해주는 방식
// TODO: 숙소까지 선택 완료하게 된다면 다음으로 버튼을 활성화하고, 클릭 시 다음 단계로 넘어가게끔 구현
</script>

<template>
  <div
    class="relative flex flex-col space-y-2 my-6 max-h-screen overflow-scroll scrollbar-hide"
  >
    <div
      class="flex flex-col items-start p-1 border-gray-200 rounded-md"
      v-for="(day, index) in days"
      :key="index"
      :class="{
        'border ': pickedTrips[index].length === 0,
      }"
    >
      <div
        class="flex flex-row w-full px-3 justify-between"
        :class="`${TEXT_COLORS[index % TEXT_COLORS.length]}`"
      >
        <span class="text-md mb-1 font-bold">{{ day }}</span>
      </div>
      <div class="flex flex-col rounded-md border-gray-400 w-full min-h-12">
        <VueDraggableNext v-model="pickedTrips[index]" group="trip">
          <div
            v-for="(trip, tripIndex) in pickedTrips[index]"
            :key="tripIndex"
            className="bg-white dark:bg-gray-800 rounded-lg p-2 border border-gray-200 dark:border-gray-700"
          >
            <Card className="flex justify-between items-center cursor-pointer">
              <div class="flex w-12 h-12 aspect-square">
                <img
                  :src="imageOrDefault(trip.backgroundImage)"
                  alt="avatar"
                  class="w-full h-full rounded-md object-cover"
                />
                <Skeleton class="h-full w-full rounded-xl" />
              </div>
              <div
                className="flex-grow ml-4 space-y-1 w-full text-ellipsis whitespace-nowrap overflow-hidden pr-5"
              >
                <div
                  className="font-semibold text-sm text-ellipsis whitespace-nowrap overflow-hidden"
                >
                  {{ trip.name }}
                </div>
                <div
                  className="flex flex-row justify-start space-x-1 items-center text-xs text-gray-600"
                >
                  <span>{{ trip.address }}</span>
                </div>
              </div>
            </Card>
          </div>
          <div
            className="mt-4 bg-white h-16 w-full dark:bg-gray-800 rounded-lg p-2 border border-gray-200 dark:border-gray-700 cursor-pointer"
          >
            <span
              class="text-sm w-full h-full flex justify-center items-center text-gray-400"
              >+ 숙소 선택하기</span
            >
          </div>
        </VueDraggableNext>
      </div>
    </div>
    <div class="min-h-[300px]" />
    <div class="w-[350px] bg-white h-16 fixed bottom-0 flex justify-center items-center">
      <Button variant="default" class="w-full" @click="onSubmit">다음으로</Button>
    </div>
  </div>
</template>

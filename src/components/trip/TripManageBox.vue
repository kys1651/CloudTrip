<script setup lang="ts">
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { useToast } from "@/components/ui/toast";
import { imageOrDefault } from "@/lib/image-load";
import { TEXT_COLORS, useTripPlanStore } from "@/stores/trip-plan";
import { X } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { VueDraggableNext } from "vue-draggable-next";

const emit = defineEmits(["nextStep"]);

const { removeTrip, calculateOptimalRoute } = useTripPlanStore();
const { pickedTrips, dailyTrip } = storeToRefs(useTripPlanStore());

const days = Array.from({ length: pickedTrips.value.length }, (_, i) => `${i + 1}일차`);

const toast = useToast();

const onSubmit = () => {
  const isAllPlan = dailyTrip.value.every((trip) => trip.count > 0);
  if (!isAllPlan) {
    toast.toast({
      title: "여행지를 선택해주세요.",
      description: "모든 일정에 여행지를 선택해주세요.",
      duration: 2000,
      variant: "destructive",
    });
    return;
  }
  emit("nextStep");
};
</script>

<template>
  <div
    class="relative flex flex-col space-y-2 my-6 max-h-screen overflow-scroll scrollbar-hide"
  >
    <Button variant="outline" class="w-full flex" @click="calculateOptimalRoute"
      >경로 최적화</Button
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
        <span v-if="dailyTrip[index]" class="text-xs mb-1"
          >총 거리: {{ dailyTrip[index].distance.toFixed(2) }}km</span
        >
      </div>
      <div class="flex flex-col rounded-md border-gray-400 w-full min-h-12">
        <VueDraggableNext v-model="pickedTrips[index]" group="trip">
          <div
            v-for="(trip, tripIndex) in pickedTrips[index]"
            :key="tripIndex"
            className="bg-white dark:bg-gray-800 rounded-lg p-2 cursor-move transition-transform duration-300 hover:-translate-y-1 border border-gray-200 dark:border-gray-700"
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

              <X :size="18" @click="() => removeTrip(index, trip.tourId)" />
            </Card>
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

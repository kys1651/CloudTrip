<script setup lang="ts">
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { useToast } from "@/components/ui/toast";
import { imageOrDefault } from "@/lib/image-load";
import { useTripPlanStore } from "@/stores/trip-plan";
import { useScheduleSocket } from "@/stores/web-stomp";
import type { SearchTrip } from "@/types/trip.type";
import { useImage } from "@vueuse/core";
import { ClipboardList, Locate, SquareCheckBig, Star } from "lucide-vue-next";

const props = defineProps<{
  trip: SearchTrip;
}>();
const backgroundImage = imageOrDefault(props.trip.backgroundImage);
const { isLoading } = useImage({ src: backgroundImage });

const { addTrip, exists, updateVehicles } = useTripPlanStore();
const { lock } = useScheduleSocket();

const toast = useToast();

const pickTrip = () => {
  if (exists(props.trip.tourId)) return;
  toast.toast({
    title: "여행지를 선택했습니다.",
    description: `여행지를 ${props.trip.name} 선택했습니다.`,
    duration: 2000,
    variant: "success",
  });
  addTrip(0, props.trip);
  updateVehicles();
  lock();
};
</script>

<template>
  <Card className="flex flex-row justify-between py-4 w-full items-center">
    <div class="flex flex-row w-5/6 cursor-pointer">
      <div class="flex w-16 h-16 aspect-square">
        <img
          :src="backgroundImage"
          alt="avatar"
          class="w-full h-full rounded-md object-cover"
        />
        <div v-if="isLoading"><Skeleton class="h-full w-full rounded-xl" /></div>
      </div>
      <div className="flex-grow ml-4 space-y-1 w-1/2">
        <div
          className="font-semibold text-ellipsis  whitespace-nowrap overflow-hidden pr-4"
        >
          {{ props.trip.name }}
        </div>
        <div
          className="flex flex-row justify-start space-x-1 items-center text-sm text-gray-600"
        >
          <Locate :size="16" />
          <span class="text-xs whitespace-nowrap text-ellipsis overflow-hidden pr-4">{{
            props.trip.address
          }}</span>
        </div>
        <div className="flex items-center mt-1 space-x-4">
          <div class="flex flex-row items-center space-x-1">
            <SquareCheckBig :size="16" />
            <span className="text-sm text-gray-600 mr-2">{{ trip.scheduleCount }}</span>
          </div>
          <div class="flex flex-row items-center space-x-1">
            <Star :size="16" />
            <span className="text-sm text-gray-600">{{ trip.rating }}</span>
          </div>
          <div class="flex flex-row items-center space-x-1">
            <ClipboardList :size="16" />
            <span className="text-sm text-gray-600">{{ trip.reviewCount }}</span>
          </div>
        </div>
      </div>
    </div>
    <Button
      variant="outline"
      class="w-8 h-8 cursor-pointer disabled:opacity-100 disabled:bg-green-500 disabled:border-green-500 disabled:text-white disabled:cursor-no-drop"
      :disabled="exists(props.trip.tourId)"
      @click="pickTrip"
      >✓</Button
    >
  </Card>
</template>

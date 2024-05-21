<script setup lang="ts">
import { cityStayRequest } from "@/api/trip";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "@/components/ui/dialog";
import { Skeleton } from "@/components/ui/skeleton";
import { useToast } from "@/components/ui/toast";
import { calculateDistance, type Point } from "@/lib/distance";
import { imageOrDefault } from "@/lib/image-load";
import { TEXT_COLORS, useTripPlanStore } from "@/stores/trip-plan";
import { useScheduleSocket } from "@/stores/web-stomp";
import type { SearchTripWithDistance } from "@/types/trip.type";
import { useQuery } from "@tanstack/vue-query";
import { X } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { computed, ref } from "vue";

const props = defineProps<{
  cityId: number;
}>();

const emit = defineEmits(["nextStep"])

const { lock } = useScheduleSocket();
const { addRoom, removeRoom, updateVehicles } = useTripPlanStore();
const { tripsExceptRooms, rooms } = storeToRefs(useTripPlanStore());

const day = ref<number>(0);

const days = Array.from({ length: tripsExceptRooms.value.length }, (_, i) => `${i + 1}일차`);

const sortedStaies = computed<SearchTripWithDistance[]>(() => {
  const dayTrips = tripsExceptRooms.value[day.value];
  const { latitude, longitude, tourId } = dayTrips[dayTrips.length - 1];
  const point: Point = { latitude, longitude, tourId };
  return [...staies.value!!].map(
    (stay) => ({ ...stay, distance: calculateDistance(stay, point) })
  ).sort((a, b) => {
    return a.distance - b.distance;
  });
});

const toast = useToast();
const { data: staies } = useQuery({
  queryKey: ["trip", "stay", String(props.cityId)],
  queryFn: () => cityStayRequest(props.cityId),
});

const setDay = (index: number) => {
  day.value = index;
};

const setDayRoom = (day: number, stay: SearchTripWithDistance) => {
  if (day === -1) {
    rooms.value.forEach((room, index) => {
      if (!room) addRoom(index, stay)
    })
    updateVehicles();
    lock()
    return;
  }

  addRoom(day, stay);
  updateVehicles();
  lock();
};

const onSubmit = () => {
  if (rooms.value.some((stay) => !stay)) {
    toast.toast({
      title: "숙소를 선택해주세요.",
      description: "숙소를 선택하지 않은 일자가 있습니다.",
      variant: "destructive"
    })
    return;
  }
  emit("nextStep");
};
</script>

<template>
  <Dialog>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>{{ day + 1 }} 일차 숙소 선택</DialogTitle>
        <DialogDescription class="text-xs pb-4">
          여행 일정에 맞는 숙소를 선택해주세요. 숙소는 마지막 장소 근처의 숙소를
          추천해드립니다.
        </DialogDescription>
        <div class="w-full h-96 overflow-scroll space-y-6">
          <div v-for="stay in sortedStaies" :key="stay.tourId">
            <div class="flex flex-row items-center space-x-2 justify-between">
              <img
                :src="imageOrDefault(stay.backgroundImage)"
                alt="stay"
                class="w-12 h-12 object-cover rounded-md"
              />
              <div class="w-40 flex flex-col space-x-1 items-start">
                <span class="text-xs text-gray-500">{{ stay.name }}</span>
                <span class="text-xs text-gray-400"
                  >{{ stay.distance.toFixed(2) }}km</span
                >
              </div>
              <div class="flex flex-row space-x-1">
                <Button class="w-20 h-8" variant="outline" @click="setDayRoom(day, stay)"
                  >{{ day + 1 }}일차 숙소</Button
                >
                <Button class="w-23 h-8" variant="outline" @click="setDayRoom(-1, stay)"
                  >남은 일정 숙소</Button
                >
              </div>
            </div>
          </div>
        </div>
      </DialogHeader>
    </DialogContent>

    <div
      class="relative flex flex-col space-y-2 my-6 max-h-screen overflow-scroll scrollbar-hide"
    >
      <div
        class="flex flex-col items-start p-1"
        v-for="(day, index) in days"
        :key="index"
      >
        <div
          class="flex flex-row w-full px-3 justify-between"
          :class="`${TEXT_COLORS[index % TEXT_COLORS.length]}`"
        >
          <span class="text-md mb-1 font-bold">{{ day }}</span>
        </div>
        <div class="flex flex-col rounded-md border-gray-400 w-full min-h-12">
          <div
            v-for="(trip, tripIndex) in tripsExceptRooms[index]"
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
            className="mt-4 bg-white h-14 w-full dark:bg-gray-800 rounded-lg p-2 border border-gray-200 dark:border-gray-700 cursor-pointer"
            v-if="!rooms[index]"
          >
            <DialogTrigger class="w-full h-full" @click="setDay(index)">
              <span
                class="text-sm w-full h-full flex justify-center items-center text-gray-400"
                >+ 숙소 선택하기</span
              >
            </DialogTrigger>
          </div>
          <div
            className="mt-4 bg-white w-full dark:bg-gray-800 rounded-lg p-2 border-red-200 border-2 dark:border-gray-700 cursor-pointer"
            v-else
          >
            <Card className="relative flex justify-between items-center cursor-pointer">
              <X
                class="absolute top-0 right-0"
                :size="14"
                @click="
                  () => {
                    removeRoom(index);
                    updateVehicles();
                    lock();
                  }
                "
              />
              <div class="flex w-12 h-12 aspect-square">
                <img
                  :src="imageOrDefault(rooms[index].backgroundImage)"
                  alt="avatar"
                  class="w-full h-full rounded-md object-cover"
                />
                <Skeleton class="h-full w-full rounded-xl" />
              </div>
              <div
                className="flex-grow ml-4 space-y-1 w-full text-ellipsis whitespace-nowrap overflow-hidden pr-5"
              >
                <div
                  className="font-semibold w-40 text-sm text-ellipsis whitespace-nowrap overflow-hidden"
                >
                  {{ rooms[index].name }}
                </div>
                <div
                  className="flex flex-row justify-start space-x-1 items-center text-xs text-gray-600"
                >
                  <span>{{ rooms[index].address }}</span>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>
      <div class="min-h-[50px]" />
      <div
        class="w-[350px] bg-white h-16 fixed bottom-0 flex justify-center items-center"
      >
        <Button variant="default" class="w-full" @click="onSubmit">다음으로</Button>
      </div>
    </div>
  </Dialog>
</template>

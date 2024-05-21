<script setup lang="ts">
import { scheduleTripCreateRequest } from "@/api/schedule";
import { directionRequest } from "@/api/trip";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import { useTripPlanStore } from "@/stores/trip-plan";
import { useScheduleSocket } from "@/stores/web-stomp";
import type { ScheduleTripCreate } from "@/types/schedule.type";
import { useMutation, useQueryClient } from "@tanstack/vue-query";
import {
  ArrowRightLeft,
  Bike,
  Bus,
  Car,
  Footprints,
  RotateCcw,
  TramFront,
} from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import Button from "../ui/button/Button.vue";
import { useToast } from "../ui/toast";
import BikeVehicle from "./vehicles/BikeVehicle.vue";
import BusVehicle from "./vehicles/BusVehicle.vue";
import CarVehicle from "./vehicles/CarVehicle.vue";
import FootVehicle from "./vehicles/FootVehicle.vue";
import MetroVehicle from "./vehicles/MetroVehicle.vue";

const {
  addVehicles,
  setDefaultTripVehicles,
  setTripVehicles,
  findVehicle,
} = useTripPlanStore();
const { trips, tripVehicles, tripVehicleList } = storeToRefs(useTripPlanStore());
const vehicleType = ref<string>("walk");
const isLoading = ref<boolean>(true);
const isAllChanged = ref<boolean>(false);
const { lock } = useScheduleSocket();

watch(vehicleType, (value) => {
  setDefaultTripVehicles(value);
  lock();
});

const route = useRoute();
const scheduleId = route.params.scheduleId;

const toast = useToast();
const queryClient = useQueryClient();

const { mutate } = useMutation({
  mutationKey: ["trip", "direction"],
  mutationFn: (schedules: ScheduleTripCreate) =>
    scheduleTripCreateRequest(Number(scheduleId), schedules),
  onSuccess: () => {
    toast.toast({
      title: "여행 계획",
      description: "여행 경로 저장이 완료되었습니다.",
      variant: "success",
    });
    queryClient.invalidateQueries({
      queryKey: ["schedule", scheduleId],
    });
  },
  onError: () => {
    toast.toast({
      title: "여행 계획",
      description: "여행 경로 저장에 실패했습니다.",
      duration: 2000,
      variant: "destructive",
    });
  },
});

const onSubmit = () => {
  if (
    tripVehicles.value
      .filter((vehicle) => vehicle.toTourId !== 0)
      .some((vehicle) => vehicle.type === "none" || vehicle.vehicleId === 0)
  ) {
    toast.toast({
      title: "이동수단을 선택해주세요.",
      description: "이동수단을 선택해주세요.",
      duration: 2000,
      variant: "destructive",
    });
    return;
  }
  mutate({
    trips: trips.value.flatMap((dayTrips, day) =>
      dayTrips.map((trip, index) => ({
        tourId: trip.tourId,
        day: day + 1,
        order: index,
        room: trip.room,
      }))
    ),
    vehicles: tripVehicles.value.filter((vehicle) => vehicle.toTourId !== 0),
  });
};

const updateVehicle = (day: number, idx: number, vehicleType: string) => {
  setTripVehicles(day, idx, vehicleType);
  lock();
};

onMounted(() => {
  isLoading.value = true;
  trips.value.map((trips) => {
    return trips
      .filter((_, index) => index !== 0)
      .map((__, idx) => {
        const prev = trips[idx];
        const trip = trips[idx + 1];
        if (!prev || !trip) return { distance: 0, walk: 0, bike: 0 };
        directionRequest({
          startTourId: prev.tourId,
          endTourId: trip.tourId,
          startX: prev.longitude,
          startY: prev.latitude,
          endX: trip.longitude,
          endY: trip.latitude,
        }).then((response) => addVehicles(response, prev.tourId, trip.tourId));
      });
  });
  isLoading.value = false;
});
</script>
<template>
  <div class="flex flex-col w-full">
    <div class="mb-4">
      <Button
        v-show="!isAllChanged"
        class="flex flex-row justify-center w-full h-10"
        @click="isAllChanged = !isAllChanged"
        variant="outline"
      >
        전체 일정 덮어씌우기
      </Button>
      <ToggleGroup v-show="isAllChanged" type="single" v-model="vehicleType" class="">
        <ToggleGroupItem
          value="none"
          class="w-12 h-12 border border-gray-400 p-0 flex flex-col"
          aria-label="초기화"
        >
          <RotateCcw :size="18" />
          <span class="text-[10px]">초기화</span>
        </ToggleGroupItem>
        <ToggleGroupItem
          value="car"
          class="w-12 h-12 border border-gray-400 p-0 flex flex-col"
          aria-label="자동차"
        >
          <Car :size="18" />
          <span class="text-[10px]">자동차</span>
        </ToggleGroupItem>
        <ToggleGroupItem
          value="bus"
          class="w-12 h-12 border border-gray-400 p-0 flex flex-col"
          aria-label="버스"
        >
          <Bus :size="18" />
          <span class="text-[10px]">버스</span>
        </ToggleGroupItem>
        <ToggleGroupItem
          value="metro"
          class="w-12 h-12 border border-gray-400 p-0 flex flex-col"
          aria-label="지하철"
        >
          <TramFront :size="18" />
          <span class="text-[10px]">지하철</span>
        </ToggleGroupItem>
        <ToggleGroupItem
          value="walk"
          class="w-12 h-12 border border-gray-400 p-0 flex flex-col"
          aria-label="도보"
        >
          <Footprints :size="18" />
          <span class="text-[10px]">도보</span>
        </ToggleGroupItem>
        <ToggleGroupItem
          value="bike"
          class="w-12 h-12 border border-gray-400 p-0 flex flex-col"
          aria-label="자전거"
        >
          <Bike :size="18" />
          <span class="text-[10px]">자전거</span>
        </ToggleGroupItem>
      </ToggleGroup>
    </div>
    <div class="flex flex-col space-y-5 overflow-scroll h-screen scrollbar-hide">
      <div
        v-for="(dayVehicles, day) in tripVehicleList"
        :key="day"
        class="px-5 py-3 sm:px-8 sm:py-4 border border-gray-400 rounded-md mx-2"
      >
        <span class="flex w-full justify-center items-center pb-1 text-lg font-bold"
          >{{ day + 1 }} 일차</span
        >
        <div
          class="after:absolute after:inset-y-0 after:w-px after:bg-gray-500/20 relative pl-6 after:left-0 grid gap-10 dark:after:bg-gray-400/20"
        >
          <div>
            <div
              v-for="(vehicle, index) in dayVehicles"
              :key="day + index"
              class="grid text-sm relative"
            >
              <div
                class="aspect-square w-3 bg-gray-900 rounded-full absolute left-0 translate-x-[-29.5px] z-10 top-1 dark:bg-gray-50"
              />
              <div class="font-medium">{{ vehicle.name }}</div>
              <div
                v-if="vehicle.fromTourId && vehicle.toTourId"
                class="w-full flex flex-row items-center"
              >
                <FootVehicle
                  v-if="vehicle.type === 'walk'"
                  :walk="findVehicle(vehicle.fromTourId, vehicle.toTourId, 'walk')!!"
                />
                <BikeVehicle
                  v-else-if="vehicle.type === 'bike'"
                  :bike="findVehicle(vehicle.fromTourId, vehicle.toTourId, 'bike')!!"
                />
                <BusVehicle
                  v-else-if="vehicle.type === 'bus'"
                  :bus="findVehicle(vehicle.fromTourId, vehicle.toTourId, 'bus')!!"
                />
                <MetroVehicle
                  v-else-if="vehicle.type === 'metro'"
                  :metro="findVehicle(vehicle.fromTourId, vehicle.toTourId, 'metro')!!"
                />
                <CarVehicle
                  v-else-if="vehicle.type === 'car'"
                  :car="findVehicle(vehicle.fromTourId, vehicle.toTourId, 'car')!!"
                />
                <div v-else class="flex flex-row py-4 space-x-2">
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="updateVehicle(day, index, 'car')"
                  >
                    <Car :size="14" />
                    <span class="text-[10px]">자동차</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="updateVehicle(day, index, 'bus')"
                  >
                    <Bus :size="14" />
                    <span class="text-[10px]">버스</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="updateVehicle(day, index, 'metro')"
                  >
                    <TramFront :size="14" />
                    <span class="text-[10px]">지하철</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="updateVehicle(day, index, 'walk')"
                  >
                    <Footprints :size="14" />
                    <span class="text-[10px]">도보</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="updateVehicle(day, index, 'bike')"
                  >
                    <Bike :size="14" />
                    <span class="text-[10px]">자전거</span>
                  </div>
                </div>

                <ArrowRightLeft
                  v-if="vehicle.type !== 'none'"
                  class="ml-auto cursor-pointer"
                  :size="16"
                  @click="updateVehicle(day, index, 'none')"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="min-h-[300px]" />
      <div
        class="w-[350px] bg-white h-16 fixed bottom-0 flex justify-center items-center"
      >
        <Button variant="default" class="w-full" @click="onSubmit">저장하기</Button>
      </div>
    </div>
  </div>
</template>

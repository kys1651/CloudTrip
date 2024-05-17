<script setup lang="ts">
import { directionRequest } from "@/api/trip";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import { useTripPlanStore } from "@/stores/trip-plan";
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
import Button from "../ui/button/Button.vue";
import BikeVehicle from "./vehicles/BikeVehicle.vue";
import BusVehicle from "./vehicles/BusVehicle.vue";
import CarVehicle from "./vehicles/CarVehicle.vue";
import FootVehicle from "./vehicles/FootVehicle.vue";
import MetroVehicle from "./vehicles/MetroVehicle.vue";

const { addVehicles } = useTripPlanStore();
const { tourSum, tourSumDistance } = storeToRefs(useTripPlanStore());
const tourSumType = ref<string[][]>([]);
const vehicleType = ref<string>("walk");
const isLoading = ref<boolean>(true);
const isAllChanged = ref<boolean>(false);

watch(vehicleType, (value) => {
  tourSumType.value = tourSumDistance.value.map((tours) => tours.map(() => value));
  isAllChanged.value = false;
});

onMounted(() => {
  isLoading.value = true;
  tourSum.value.map((trips) => {
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
  tourSumType.value = tourSumDistance.value.map((tours) => tours.map(() => "none"));
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
    <div class="flex flex-col space-y-5 overflow-scroll h-[550px]">
      <div
        v-for="(tours, index) in tourSum"
        :key="index"
        class="px-5 py-3 sm:px-8 sm:py-4 border border-gray-400 rounded-md mx-2"
      >
        <span class="flex w-full justify-center items-center pb-1 text-lg font-bold"
          >{{ index + 1 }} 일차</span
        >
        <div
          class="after:absolute after:inset-y-0 after:w-px after:bg-gray-500/20 relative pl-6 after:left-0 grid gap-10 dark:after:bg-gray-400/20"
        >
          <div>
            <div
              v-for="(tour, idx) in tours"
              :key="tour?.tourId"
              class="grid text-sm relative"
            >
              <div
                class="aspect-square w-3 bg-gray-900 rounded-full absolute left-0 translate-x-[-29.5px] z-10 top-1 dark:bg-gray-50"
              />
              <div class="font-medium">{{ tour?.name }}</div>
              <div
                v-if="idx !== tours.length - 1 && tourSumType.length > 0"
                class="w-full flex flex-row items-center"
              >
                <FootVehicle
                  v-if="tourSumType[index][idx] === 'walk'"
                  :distance="tourSumDistance[index][idx].distance"
                  :time="tourSumDistance[index][idx].walk"
                />
                <BikeVehicle
                  v-else-if="tourSumType[index][idx] === 'bike'"
                  :distance="tourSumDistance[index][idx].distance"
                  :time="tourSumDistance[index][idx].bike"
                />
                <BusVehicle
                  v-else-if="tourSumType[index][idx] === 'bus'"
                  :bus="tourSumDistance[index][idx].bus"
                />
                <MetroVehicle
                  v-else-if="tourSumType[index][idx] === 'metro'"
                  :metro="tourSumDistance[index][idx].metro"
                />
                <CarVehicle
                  v-else-if="tourSumType[index][idx] === 'car'"
                  :car="tourSumDistance[index][idx].car"
                />
                <div v-else class="flex flex-row py-4 space-x-2">
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="tourSumType[index][idx] = 'car'"
                  >
                    <Car :size="14" />
                    <span class="text-[10px]">자동차</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="tourSumType[index][idx] = 'bus'"
                  >
                    <Bus :size="14" />
                    <span class="text-[10px]">버스</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="tourSumType[index][idx] = 'metro'"
                  >
                    <TramFront :size="14" />
                    <span class="text-[10px]">지하철</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="tourSumType[index][idx] = 'walk'"
                  >
                    <Footprints :size="14" />
                    <span class="text-[10px]">도보</span>
                  </div>
                  <div
                    class="w-10 h-10 border rounded-md border-gray-400 p-0 flex flex-col items-center justify-center cursor-pointer"
                    @click="tourSumType[index][idx] = 'bike'"
                  >
                    <Bike :size="14" />
                    <span class="text-[10px]">자전거</span>
                  </div>
                </div>

                <ArrowRightLeft
                  v-if="tourSumType[index][idx] && tourSumType[index][idx] !== 'none'"
                  class="ml-auto cursor-pointer"
                  :size="16"
                  @click="tourSumType[index][idx] = 'none'"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

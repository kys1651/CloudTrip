<script setup lang="ts">
import { cityRequest } from "@/api/trip";
import ChatMessage from "@/components/trip/ChatMessage.vue";
import TripMainTab from "@/components/trip/TripMainTab.vue";
import TripStayTab from "@/components/trip/TripStayTab.vue";
import TripVehicleTab from "@/components/trip/TripVehicleTab.vue";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from "@/components/ui/dialog";

import { RangeCalendar } from '@/components/ui/range-calendar';

import { useToast } from "@/components/ui/toast";

import { useTripPlanStore } from "@/stores/trip-plan";
import { TripStep, type ICity } from "@/types/trip.type";

import { storeToRefs } from "pinia";
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { KakaoMap, KakaoMapCustomOverlay } from "vue3-kakao-maps";

const city = ref<ICity | null>(null);
const step = ref<TripStep>(TripStep.PLAN);
const stepMessage = computed(() => {
  switch(step.value) {
    case TripStep.PLAN:
      return "여행지를 선택해주세요.";
    case TripStep.STAY:
      return "숙소를 선택해주세요.";
    case TripStep.VEHICLE:
      return "이동수단을 선택해주세요.";
    default:
      return "";
  }
})

const toast = useToast();
const router = useRouter();

const isOpen = ref<boolean>(true);
const map = ref<kakao.maps.Map>();

const onLoadKakaoMap = (mapRef: kakao.maps.Map) => {
  map.value = mapRef;
};

const route = useRoute();
const cityId = route.params.cityId;

const tripStore = useTripPlanStore();
const { changeRange, disabledDate, setRange, resetRange } = tripStore;

const { coordinates, centercoordinate, range, isRangeSetted, staies } = storeToRefs(useTripPlanStore());

watch(centercoordinate, (center) => {
  if (map.value && center) {
    map.value.setBounds(center);
  }
});

const handleCalendar = () => {
  isOpen.value = false;
}

onMounted(() => {
  cityRequest(Number(cityId)).then((data) => {
    city.value = data;
  }).catch(() => {
    toast.toast({
      title: "도시 정보를 불러오는 중 오류가 발생했습니다.",
      description: "존재하지 않는 도시 정보입니다. 다시 한번 확인해주세요.",
      duration: 2000,
      variant: "destructive",
    })
    router.push({ name: "home" });
  });
})
</script>

<template>
  <Dialog :open="isOpen || !isRangeSetted">
    <DialogContent class="max-w-2xl">
      <DialogHeader>
        <DialogTitle>{{ city?.name }} 여행 일정 선택하기</DialogTitle>
        <DialogDescription>
          계획하고자 하는 여행의 일정을 선택해주세요. <br />
          여행 일정은 최대 5일까지 가능합니다.
        </DialogDescription>
      </DialogHeader>
      <div class="flex flex-row space-x-10 py-5 m-auto">
        <RangeCalendar
          @update:start-value="changeRange"
          @update:model-value="setRange"
          :isDateDisabled="disabledDate"
          :locale="'ko-kr'"
          :number-of-months="2"
          :value="range"
          class="rounded-md border"
        />
      </div>
      <DialogFooter class="">
        <Button class="w-24 flex flex-end mx-5" @click="resetRange">일정 취소</Button>
        <Button class="w-24 flex flex-end mx-5" @click="handleCalendar">선택 완료</Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
  <div class="flex flex-row max-h-screen h-screen overflow-hidden">
    <div class="w-[500px] h-full flex flex-col px-3 py-5">
      <div className="flex flex-col items-center justify-center gap-4">
        <div className="flex flex-row items-center justify-center gap-4">
          <div
            v-for="i in [1, 2, 3]"
            class="w-4 h-4 rounded-full"
            :key="i"
            :class="{
              'bg-blue-500': i <= step,
              'bg-gray-300': i > step,
            }"
          />
        </div>
        <div className="text-sm text-gray-500">{{ stepMessage }}</div>
      </div>
      <h2 class="text-black text-2xl font-bold">{{ city?.name }}</h2>
      <span class="text-gray-400 text-md py-5"
        >{{ range?.start }} ~ {{ range?.end }}</span
      >
      <TripMainTab
        v-if="step === TripStep.PLAN"
        :cityId="Number(cityId)"
        @next-step="step = TripStep.STAY"
      />
      <TripStayTab
        v-else-if="step === TripStep.STAY"
        @next-step="step = TripStep.VEHICLE"
      />
      <TripVehicleTab v-else />
    </div>
    <KakaoMap
      :width="'100vw'"
      :height="'100vh'"
      :lat="37.5665"
      :lng="126.978"
      :draggable="false"
      @on-load-kakao-map="onLoadKakaoMap"
    >
      <KakaoMapCustomOverlay
        v-for="(coordinate, index) in coordinates"
        :key="index"
        :lat="coordinate.latitude"
        :lng="coordinate.longitude"
        :content="`<div class=' w-5 h-5 text-sm text-white flex justify-center items-center rounded-full ${coordinate.color}'>${coordinate.day}</div>`"
      />
      <KakaoMapCustomOverlay
        v-for="(stay, index) in staies"
        :key="`stay-${index}`"
        :lat="stay?.latitude || 0"
        :lng="stay?.longitude || 0"
        :content="`<div class='w-5 h-5 text-sm text-white flex justify-center items-center rounded-full ${
          coordinates && coordinates[index] && coordinates[index].color
        }'>S</div>`"
      />
    </KakaoMap>
    <ChatMessage class="fixed z-50 bottom-0 w-96 right-0 bg-white" />
  </div>
</template>

<style scoped></style>

<script setup lang="ts">
import { scheduleDetailRequest, scheduleTripAndVehicleRequest } from "@/api/schedule";
import CircularLoading from "@/components/common/CircularLoading.vue";
import ChatMessage from "@/components/trip/ChatMessage.vue";
import TripMainTab from "@/components/trip/TripMainTab.vue";
import TripRoomTab from "@/components/trip/TripRoomTab.vue";
import TripVehicleTab from "@/components/trip/TripVehicleTab.vue";
import { useToast } from "@/components/ui/toast";
import { useAuthenticationStore } from "@/stores/authentication";

import { useTripPlanStore } from "@/stores/trip-plan";
import { useScheduleSocket } from "@/stores/web-stomp";
import { TripStep } from "@/types/trip.type";
import { useQuery } from "@tanstack/vue-query";
import { ChevronLeft, Share } from "lucide-vue-next";

import { storeToRefs } from "pinia";
import { computed, ref, watch, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { KakaoMap, KakaoMapCustomOverlay } from "vue3-kakao-maps";

const step = ref<TripStep>(TripStep.PLAN);
const stepMessage = computed(() => {
  switch (step.value) {
    case TripStep.PLAN:
      return "여행지를 선택해주세요.";
    case TripStep.STAY:
      return "숙소를 선택해주세요.";
    case TripStep.VEHICLE:
      return "이동수단을 선택해주세요.";
    default:
      return "";
  }
});

const route = useRoute();
const scheduleId = route.params.scheduleId;
const toast = useToast();

const map = ref<kakao.maps.Map>();

const onLoadKakaoMap = (mapRef: kakao.maps.Map) => {
  map.value = mapRef;
};
const { updateLock, updatedUser, profileDictionary } = storeToRefs(useScheduleSocket());
const { connect, unLock, sendUpdate } = useScheduleSocket();
const { setTripDay, initTripAndVehicle } = useTripPlanStore();

const { coordinates, centercoordinate, rooms } = storeToRefs(useTripPlanStore());
const { profile } = storeToRefs(useAuthenticationStore());

watch(centercoordinate, (center) => {
  if (map.value && center) {
    map.value.setBounds(center);
  }
});

const router = useRouter();

const { data: initTrips } = useQuery({
  queryKey: ["schedule-trip", scheduleId],
  queryFn: () => scheduleTripAndVehicleRequest(Number(scheduleId)),
});

watchEffect(() => {
  if (initTrips.value) {
    initTripAndVehicle(initTrips.value.trips, initTrips.value.vehicles);
  }
});

const { data: schedule, isLoading } = useQuery({
  queryKey: ["schedule", scheduleId],
  queryFn: async () => {
    try {
      const response = await scheduleDetailRequest(Number(scheduleId));
      return response;
    } catch (error) {
      router.push({ name: "home" }).then(() => {
        toast.toast({
          title: "존재하지 않은 일정입니다.",
          description: "해당 일정은 존재하지 않습니다. 다시 확인해주세요.",
          duration: 2000,
          variant: "destructive",
        });
      });
    }
  },
});

watch(updateLock.value, (lock) => {
  if (lock.isLock && schedule.value) {
    sendUpdate(schedule.value.scheduleId, schedule.value.day);
    unLock();
  }
});

watchEffect(() => {
  if (schedule.value) {
    if (
      schedule.value.private &&
      !schedule.value.usernames.includes(profile.value!!.username)
    ) {
      router.push({ name: "home" }).then(() => {
        toast.toast({
          title: "비공개 일정입니다.",
          description: "해당 일정은 비공개로 설정되어 있습니다.",
          duration: 2000,
          variant: "destructive",
        });
      });
      return;
    }

    if (schedule.value.multi) {
      connect(schedule.value.scheduleId, schedule.value.day);
    } else {
      setTripDay(schedule.value.day);
    }
  }
});
</script>

<template>
  <div v-if="isLoading || !schedule">
    <CircularLoading />
  </div>
  <div v-else class="flex flex-row max-h-screen h-screen overflow-hidden">
    <div class="w-[450px] max-w-[450px] h-full flex flex-col px-3 py-5">
      <div class="flex relative flex-row justify-center space-x-2 items-end pb-3">
        <ChevronLeft
          class="absolute left-0 top-0 cursor-pointer"
          v-if="step !== TripStep.PLAN"
          @click="step = step - 1"
          :size="16"
        />
        <Share class="absolute right-0 top-0 cursor-pointer" :size="16" />
        <div class="flex flex-col space-y-1">
          <div class="flex flex-row items-center justify-center gap-4">
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
          <div class="text-sm text-gray-500">{{ stepMessage }}</div>
        </div>
      </div>
      <div class="flex flex-row justify-between">
        <h2 class="text-black text-xl font-bold">{{ schedule.name }}</h2>
        <span class="text-xs text-gray-400 flex items-end"
          >마지막 수정: {{ profileDictionary[updatedUser]?.nickname }}</span
        >
      </div>

      <span class="text-gray-400 text-sm py-2 pb-4">
        {{ schedule.cityName }} ({{ schedule.startDate }} ~ {{ schedule.endDate }})
      </span>
      <div class="w-[350px]">
        <TripMainTab
          v-if="step === TripStep.PLAN"
          :cityId="schedule.cityCode"
          :multi="schedule.multi"
          @next-step="step = TripStep.STAY"
        />
        <TripRoomTab
          :cityId="schedule.cityCode"
          v-else-if="step === TripStep.STAY"
          @next-step="step = TripStep.VEHICLE"
        />
        <TripVehicleTab v-else />
      </div>
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
        v-for="(room, index) in rooms"
        :key="`room-${index}`"
        :lat="room?.latitude || 0"
        :lng="room?.longitude || 0"
        :content="`<div class='w-5 h-5 text-sm text-white flex justify-center items-center rounded-full ${
          coordinates && coordinates[index] && coordinates[index].color
        }'>S</div>`"
      />
    </KakaoMap>
    <ChatMessage
      class="fixed z-50 bottom-0 w-96 right-0 bg-white"
      v-if="schedule.multi"
      :name="schedule.name"
      :schedule-id="schedule.scheduleId"
    />
  </div>
</template>

<style scoped></style>

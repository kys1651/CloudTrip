<script setup lang="ts">
import {
  scheduleDetailRequest,
  schedulePathRequest,
  scheduleTripAndVehicleRequest,
} from "@/api/schedule";
import CircularLoading from "@/components/common/CircularLoading.vue";
import ChatMessage from "@/components/trip/ChatMessage.vue";
import ScheduleInvite from "@/components/trip/ScheduleInvite.vue";
import TripMainTab from "@/components/trip/TripMainTab.vue";
import TripRoomTab from "@/components/trip/TripRoomTab.vue";
import TripVehicleTab from "@/components/trip/TripVehicleTab.vue";
import Dialog from "@/components/ui/dialog/Dialog.vue";
import DialogContent from "@/components/ui/dialog/DialogContent.vue";
import DialogTrigger from "@/components/ui/dialog/DialogTrigger.vue";
import { useToast } from "@/components/ui/toast";
import { convertDistance, convertTime, toDate } from "@/lib/formatter";
import { imageOrDefault } from "@/lib/image-load";
import { cn } from "@/lib/utils";
import { useAuthenticationStore } from "@/stores/authentication";
import { STORKE_COLORS, useRoadStore, type LatLng } from "@/stores/road";

import { COLORS, HOVER_COLORS, TEXT_COLORS, useTripPlanStore } from "@/stores/trip-plan";
import { useScheduleSocket } from "@/stores/web-stomp";
import type {
  ScheduleTripResponse,
  ScheduleVehicleResponse,
} from "@/types/schedule.type";
import { TripStep } from "@/types/trip.type";
import { useQuery } from "@tanstack/vue-query";
import {
  ArrowRight,
  Bike,
  Bus,
  Car,
  ChevronDown,
  ChevronLeft,
  Footprints,
  Share2,
  TramFront
} from "lucide-vue-next";

import { storeToRefs } from "pinia";
import { computed, ref, watch, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { KakaoMap, KakaoMapCustomOverlay, KakaoMapPolyline } from "vue3-kakao-maps";

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

const { roads, isLoading: isRoadLoading } = storeToRefs(useRoadStore());
const { initRoads } = useRoadStore();

watch(centercoordinate, (center) => {
  if (map.value && center) {
    map.value.setBounds(center);
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

const router = useRouter();

const { data: initTrips } = useQuery({
  queryKey: ["schedule", scheduleId, "trip"],
  queryFn: () => scheduleTripAndVehicleRequest(Number(scheduleId)),
  enabled() {
    return schedule.value?.multi;
  },
});

watchEffect(() => {
  if (initTrips.value) {
    initTripAndVehicle(initTrips.value.trips, initTrips.value.vehicles);
  }
});

interface DayTrip {
  type: string;
  trip?: ScheduleTripResponse;
  vehicle?: ScheduleVehicleResponse;
}

const { data: schedulePath } = useQuery({
  queryKey: ["schedule", scheduleId, "path"],
  queryFn: () => schedulePathRequest(Number(scheduleId)),
  enabled() {
    return !!schedule.value?.finished;
  },
  select: (data) => {
    const days: DayTrip[][] = Array.from({ length: schedule.value!!.day }, (_) => []);
    [1, 2, 3, 4, 5].forEach((day) => {
      const dayTrips = data.trips.filter((trip) => trip.day === day);
      const dayVehicles = data.vehicles.filter((vehicle) => vehicle.day === day - 1);
      dayTrips.forEach((trip, index) => {
        days[trip.day - 1].push({ type: "trip", trip: dayTrips[index] });
        if (index !== dayVehicles.length) {
          days[trip.day - 1].push({ type: "vehicle", vehicle: dayVehicles[index] });
        }
      });
    });
    return days;
  },
});

const scheduleTrips = computed(() => {
  if (schedulePath.value) {
    return schedulePath.value.map((trips) =>
      trips.filter((trip) => trip.type === "trip" && trip.trip).map((trip) => trip.trip!!)
    );
  }
  return [];
});

const days = ref<number>(0);
const tripIndex = ref<number>(-1);
const roadIndex = ref<number>(-1);

watch(days, () => {
  if (scheduleTrips.value && map.value) {
    const paths = scheduleTrips.value[days.value - 1];
    const bounds = new kakao.maps.LatLngBounds();
    paths.forEach((path) => {
      bounds.extend(new kakao.maps.LatLng(path.tour.latitude, path.tour.longitude));
    });
    map.value.setBounds(bounds, 200, 200, 200, 200);
  }
});

watch(updateLock.value, (lock) => {
  if (lock.isLock && schedule.value) {
    sendUpdate(schedule.value.scheduleId, schedule.value.day);
    unLock();
  }
});

watchEffect(() => {
  if (schedulePath.value && schedule.value) {
    if (roads.value.length === 0) {
      const positions = scheduleTrips.value.map((trips) =>
        trips.map((trip) => ({ latitude: trip.tour.latitude, longitude: trip.tour.longitude } as LatLng))
      );
      initRoads(schedule.value.scheduleId, positions);
    }
  }
});

watchEffect(() => {
  if (schedule.value) {
    if (
      schedule.value.private &&
      !schedule.value.invitedUsers.map((user) => user.username).includes(profile.value!!.username)
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

const onRoadClick = (index: number) => {
  if (tripIndex.value !== -1) {
    tripIndex.value = -1;
  }
  if (index === roadIndex.value) {
    roadIndex.value = -1;
    if (scheduleTrips.value && map.value) {
      const paths = scheduleTrips.value[days.value - 1];
      const bounds = new kakao.maps.LatLngBounds();
      paths.forEach((path) => {
        bounds.extend(new kakao.maps.LatLng(path.tour.latitude, path.tour.longitude));
      });
      map.value.setBounds(bounds, 200, 200, 200, 200);
    }
    return;
  }
  roadIndex.value = index;
  if (scheduleTrips.value && map.value) {
    const paths = scheduleTrips.value[days.value - 1].filter((_, index) => index === roadIndex.value || index === roadIndex.value + 1);
    const bounds = new kakao.maps.LatLngBounds();
    paths.forEach((path) => {
      bounds.extend(new kakao.maps.LatLng(path.tour.latitude, path.tour.longitude));
    });
    map.value.setBounds(bounds, 200, 200, 200, 200);
  }
}

const onTripClick = (index: number) => {
  if (roadIndex.value !== -1) {
    roadIndex.value = -1;
  }
  if (index === tripIndex.value) {
    tripIndex.value = -1;
    if (scheduleTrips.value && map.value) {
      const paths = scheduleTrips.value[days.value - 1];
      const bounds = new kakao.maps.LatLngBounds();
      paths.forEach((path) => {
        bounds.extend(new kakao.maps.LatLng(path.tour.latitude, path.tour.longitude));
      });
      map.value.setBounds(bounds, 200, 200, 200, 200);
    }
    return;
  }
  tripIndex.value = index;
  if (map.value) {
    map.value.panTo(
      new kakao.maps.LatLng(
        scheduleTrips.value[days.value - 1][index].tour.latitude,
        scheduleTrips.value[days.value - 1][index].tour.longitude
      )
    );
  }
};
</script>

<template>
  <div v-if="isLoading || !schedule">
    <CircularLoading />
  </div>
  <div v-else class="flex flex-row max-h-screen h-screen overflow-hidden">
    <div
      v-if="!schedule.finished"
      class="w-[450px] max-w-[450px] h-full flex flex-col px-3 py-5"
    >
      <div class="flex relative flex-row justify-center space-x-2 items-end pb-3">
        <ChevronLeft
          class="absolute left-0 top-0 cursor-pointer"
          v-if="step !== TripStep.PLAN"
          @click="step = step - 1"
          :size="16"
        />
        <Dialog>
          <DialogTrigger>
            <Share2 class="absolute right-0 top-0 cursor-pointer" :size="16" />
          </DialogTrigger>
          <DialogContent>
            <ScheduleInvite
              :schedule-id="schedule.scheduleId"
              :public-key="schedule.publicKey"
              :name="schedule.name"
              :owner="schedule.user.nickname"
              :invited-users="schedule.invitedUsers"
            />
          </DialogContent>
        </Dialog>
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
      @on-load-kakao-map="onLoadKakaoMap"
    >
      <div v-if="!schedule.finished">
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
      </div>
      <div v-else-if="schedule.finished && scheduleTrips">
        <KakaoMapCustomOverlay
          v-for="(path, index) in scheduleTrips[days - 1]"
          :key="index"
          :lat="path.tour.latitude"
          :lng="path.tour.longitude"
          :content="`<div class='w-5 h-5 text-sm text-white flex justify-center items-center rounded-full ${
            HOVER_COLORS[days - 1]
          } ${tripIndex === index && 'animate-pulse'}'>${
            index + 1 === scheduleTrips[days - 1].length ? 'R' : path.order + 1
          }</div>`"
        />
        <div v-if="roads && days !== 0">
          <div v-for="(road, index) in roads[days - 1]" :key="index">
            <KakaoMapPolyline
              v-if="index === roadIndex"
              :latLngList="road"
              :stroke-color="STORKE_COLORS[days - 1].highlight3"
              :stroke-weight="5"
              stroke-style="solid"
            />
            <KakaoMapPolyline
              v-else
              :latLngList="road"
              :stroke-color="STORKE_COLORS[days - 1].highlight2"
              :stroke-weight="5"
              stroke-style="shortdot"
            />
          </div>
        </div>
      </div>
    </KakaoMap>
    <div v-if="schedule.finished" class="z-50">
      <div class="fixed top-3 left-1/2 -translate-x-1/2 flex flex-row space-x-5">
        <div v-for="(day, index) in schedule.day" :key="day">
          <div
            class="flex flex-row items-center space-x-2 px-4 py-2 rounded-xl cursor-pointer"
            :class="cn(HOVER_COLORS[index % COLORS.length], days === day && 'border-2')"
          >
            <span
              class="text-sm font-semibold text-gray-700"
              @click="
                () => {
                  days = day;
                  tripIndex = -1;
                }
              "
              >{{ day }}일차</span
            >
          </div>
        </div>
      </div>
      <div
        v-if="days !== 0"
        class="fixed right-0 top-1/2 -translate-y-1/2 bg-white flex flex-col w-96 p-3 rounded-xl"
      >
        <div v-if="schedulePath && schedulePath[days - 1]">
          <div class="flex flex-row items-center space-x-2 mb-5">
            <span class="text-lg font-semibold">{{ days }}일차</span>
            <span class="text-gray-400 text-sm"
              >({{ toDate(new Date(schedule.startDate)) }})</span
            >
          </div>
          <div
            class="flex z-50 flex-col w-full max-h-[65vh] overflow-scroll scrollbar-hide"
          >
            <div
              v-for="(trip, index) in schedulePath[days - 1]"
              :key="index"
              class="flex flex-col space-y-4"
            >
              <div
                v-if="trip.type === 'trip' && trip.trip"
                class="flex flex-row items-start justify-between hover:border hover:border-gray-200 p-2 hover:rounded-md cursor-pointer"
                @click="onTripClick(trip.trip.order)"
                :class="{
                  'border border-gray-200 rounded-md': tripIndex === trip.trip.order,
                }"
              >
                <div class="flex flex-col space-y-1">
                  <span
                    class="text-sm font-semibold"
                    :class="TEXT_COLORS[(days - 1) % TEXT_COLORS.length]"
                    >{{
                      trip.trip.order === scheduleTrips[days - 1].length - 1
                        ? days + "일차 숙소"
                        : trip.trip.order + 1 + "번 여행지"
                    }}</span
                  >
                  <span class="text-xs">{{ trip.trip.tour.name }}</span>
                  <span
                    class="text-gray-400"
                    style="font-size: 10px"
                    >{{ trip.trip!!.tour.address }}</span
                  >
                </div>
                <div
                  class="w-24 h-24 bg-cover bg-center rounded-md"
                  :style="{
                    backgroundImage: `url(${imageOrDefault(
                      trip.trip.tour.backgroundImage
                    )})`,
                  }"
                />
              </div>
              <div
                v-else-if="trip.type === 'vehicle' && trip.vehicle"
                class="flex flex-col"
              >
                <div class="flex w-full text-gray-400 justify-center items-center h-10">
                  <ChevronDown />
                </div>
                <div
                  class="flex flex-row items-center space-x-2 px-2 cursor-pointer hover:border-gray-200 p-2 hover:rounded-md"
                  @click="onRoadClick(trip.vehicle.order)"
                  :class="{
                    'border border-gray-200 rounded-md': roadIndex === trip.vehicle.order,
                  }"
                >
                  <div class="flex flex-col space-y-1 items-center px-5">
                    <div
                      class="text-black w-full font-light flex space-x-2 justify-center"
                    >
                      <div
                        v-if="trip.vehicle.type === 'car'"
                        class="flex flex-row items-end space-x-1"
                      >
                        <Car :size="24" />
                        <span class="font-medium text-xs text-gray-400"
                          >자동차로 이동</span
                        >
                      </div>
                      <div
                        v-else-if="trip.vehicle.type === 'bus'"
                        class="flex flex-row items-end space-x-1"
                      >
                        <Bus :size="24" />
                        <span class="font-medium text-xs text-gray-400">버스로 이동</span>
                      </div>
                      <div
                        v-else-if="trip.vehicle.type === 'metro'"
                        class="flex flex-row items-end space-x-1"
                      >
                        <TramFront :size="24" />
                        <span class="font-medium text-xs text-gray-400"
                          >지하철로 이동</span
                        >
                      </div>
                      <div
                        v-else-if="trip.vehicle.type === 'walk'"
                        class="flex flex-row items-end space-x-1"
                      >
                        <Footprints :size="24" />
                        <span class="font-medium text-xs text-gray-400">도보로 이동</span>
                      </div>

                      <div
                        v-else-if="trip.vehicle.type === 'bike'"
                        class="flex flex-row items-end space-x-1"
                      >
                        <Bike :size="24" />
                        <span class="font-medium text-xs text-gray-400"
                          >자전거로 이동</span
                        >
                      </div>
                    </div>
                    <div class="flex flex-col text-gray-500">
                      <span class="text-xs"
                        >소요시간: {{ convertTime(trip.vehicle.vehicle.spentTime) }}</span
                      >
                      <span class="text-xs"
                        >이동거리:
                        {{ convertDistance(trip.vehicle.vehicle.distance) }}</span
                      >
                      <span v-show="trip.vehicle.vehicle.fare > 0" class="text-xs"
                        >비용: {{ trip.vehicle.vehicle.fare }}원</span
                      >
                      <span class="text-xs" v-show="trip.vehicle.vehicle.walkTime > 0"
                        >도보 시간: {{ convertTime(trip.vehicle.vehicle.walkTime) }}</span
                      >
                      <span
                        class="text-xs"
                        v-show="trip.vehicle.vehicle.transferCount > 0"
                        >환승 횟수: {{ trip.vehicle.vehicle.transferCount }}회</span
                      >
                      <span class="text-xs" v-show="trip.vehicle.vehicle.walkDistance > 0"
                        >도보 거리:
                        {{ convertDistance(trip.vehicle.vehicle.walkDistance) }}</span
                      >
                    </div>
                  </div>
                  <div
                    class="flex mt-3 flex-col text-xs space-y-2 flex-1 items-center px-5"
                  >
                    <div
                      v-for="(step, index) in trip.vehicle.vehicle.steps"
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
                </div>
                <div class="flex w-full text-gray-400 justify-center items-center h-10">
                  <ChevronDown />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <ChatMessage
      class="fixed z-50 bottom-0 w-96 right-0 bg-white"
      v-if="schedule.multi && !schedule.finished"
      :name="schedule.name"
      :schedule-id="schedule.scheduleId"
    />
  </div>
</template>

<style scoped></style>

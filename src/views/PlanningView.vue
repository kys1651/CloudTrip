<script setup lang="ts">
import { categoryRequest, searchTripRequest } from "@/api/trip";
import TripPlanCard from "@/components/card/TripPlanCard.vue";
import TripManageBox from "@/components/trip/TripManageBox.vue";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import { useTripPlanStore } from "@/stores/trip-plan";
import type { SearchQuery, SearchTrip } from "@/types/trip.type";
import { useMutation, useQuery } from "@tanstack/vue-query";
import { Search } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { ref, watch } from "vue";
import { useRoute } from "vue-router";
import { KakaoMap, KakaoMapMarker } from "vue3-kakao-maps";

const map = ref<kakao.maps.Map>();

const onLoadKakaoMap = (mapRef: kakao.maps.Map) => {
  map.value = mapRef;
};

const route = useRoute();
console.log(route.params);
const cityId = route.params.cityId;

const trips = ref<SearchTrip[]>([]);
const categoryGroup = ref([]);
const searchKeyword = ref("");
const { coordinates, centercoordinate } = storeToRefs(useTripPlanStore());

watch(centercoordinate, (center) => {
  if (map.value && center) {
    map.value.setBounds(center);
  }
});

const { data: categories, isLoading: isCategoryLoading } = useQuery({
  queryKey: ["categories"],
  queryFn: categoryRequest,
});

const { mutate, isPending: isTripLoading } = useMutation({
  mutationKey: ["trip-search", searchKeyword.value],
  mutationFn: (params: SearchQuery) => searchTripRequest(params),
  onSuccess: (data) => {
    trips.value = data;
  },
  onError: (error) => {
    console.log(error);
  },
});

const onSubmit = () => {
  trips.value = [];
  mutate({ query: searchKeyword.value, city: Number(cityId) });
};
</script>

<template>
  <div class="flex flex-row max-h-screen h-screen overflow-hidden">
    <div class="w-[400px] h-full flex flex-col px-3 py-5">
      <h2 class="text-black text-2xl font-bold">제주</h2>
      <span class="text-gray-400 text-md py-5">2024.05.10 ~ 2024.05.16</span>
      <Tabs default-value="trip" class="w-[350px] flex-grow">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger value="trip"> 여행지 선택 </TabsTrigger>
          <TabsTrigger value="manage"> 여행지 관리 </TabsTrigger>
        </TabsList>
        <TabsContent class="flex flex-col" value="trip">
          <div class="flex flex-col h-full mx-1">
            <div class="relative flex justify-center w-full items-center my-2">
              <Input
                autocomplete="off"
                id="search"
                type="text"
                placeholder="검색어를 입력해주세요."
                class="pl-10"
                v-model="searchKeyword"
                @keyup.enter="onSubmit"
              />
              <span
                class="absolute start-0 inset-y-0 flex items-center justify-center px-2"
              >
                <Search :size="20" />
              </span>
            </div>
            <!-- 카테고리 선택창 -->
            <ToggleGroup
              type="multiple"
              class="justify-start overflow-scroll my-2 py-2 scrollbar-hide"
              v-model="categoryGroup"
              v-if="!isCategoryLoading"
            >
              <ToggleGroupItem
                variant="outline"
                v-for="category in categories"
                :key="category.contentId"
                :value="category.name"
                ><span class="text-nowrap w-14 h-5">{{ category.korName }}</span>
              </ToggleGroupItem>
            </ToggleGroup>
            <div v-else class="flex flex-row space-x-4 my-2">
              <Skeleton
                v-for="i in [1, 2, 3, 4, 5]"
                :key="i"
                class="w-56 h-9 rounded-md"
              />
            </div>

            <div
              v-if="trips.length !== 0"
              class="flex flex-grow max-h-screen flex-col space-y-2 overflow-y-scroll scrollbar-hide"
            >
              <TripPlanCard v-for="trip in trips" :key="trip.tourId" :trip="trip" />
              <div class="min-h-[300px] block"></div>
            </div>
            <div v-else class="flex flex-grow h-[500px] items-center justify-center">
              <div v-if="isTripLoading" class="flex flex-col space-y-10">
                <div
                  v-for="i in [1, 2, 3, 4, 5]"
                  :key="i"
                  class="flex items-center space-x-4 bg-white"
                >
                  <Skeleton class="w-16 h-16 rounded-md" />
                  <div class="flex-1 space-y-2">
                    <Skeleton class="h-4 w-3/4" />
                    <Skeleton class="h-4 w-5/6" />
                    <div class="flex items-center space-x-4">
                      <Skeleton class="h-4 w-12" />
                      <Skeleton class="h-4 w-12" />
                      <Skeleton class="h-4 w-12" />
                    </div>
                  </div>
                  <Skeleton class="h-6 w-6 rounded-full" />
                </div>
              </div>
              <span
                v-else
                class="text-sm h-full flex-grow text-gray-400 flex items-center justify-center"
                >조회된 게시글이 없습니다. 다시 검색해주세요.</span
              >
            </div>
          </div>
        </TabsContent>
        <TabsContent value="manage"> <TripManageBox /> </TabsContent>
      </Tabs>
    </div>
    <KakaoMap
      ref="map"
      :width="'100vw'"
      :height="'100vh'"
      :lat="33.450701"
      :lng="126.570667"
      @on-load-kakao-map="onLoadKakaoMap"
    >
      <KakaoMapMarker
        v-for="(coordinate, index) in coordinates"
        :key="index"
        :lat="coordinate.latitude"
        :lng="coordinate.longitude"
      />
    </KakaoMap>
  </div>
</template>

<style scoped></style>

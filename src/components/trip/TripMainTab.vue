<script setup lang="ts">
import { categoryRequest, searchTripRequest } from '@/api/trip'
import TripPlanCard from '@/components/card/TripPlanCard.vue'
import TripManageBox from '@/components/trip/TripManageBox.vue'
import { Input } from '@/components/ui/input'
import { Skeleton } from '@/components/ui/skeleton'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'
import { type SearchQuery, type SearchTrip } from '@/types/trip.type'
import { useMutation, useQuery } from '@tanstack/vue-query'
import { Search } from 'lucide-vue-next'
import { computed, ref } from 'vue'

const props = defineProps<{
  cityId: number
}>()

defineEmits(['nextStep'])

const trips = ref<SearchTrip[]>([])
const categoryGroup = ref<string[]>([])
const searchKeyword = ref('')

const categoryTrips = computed(() => {
  if (categoryGroup.value.length === 0) return trips.value
  return trips.value.filter((trip) => categoryGroup.value.includes(trip.contentType))
})

const { mutate, isPending: isTripLoading } = useMutation({
  mutationKey: ['trip-search', searchKeyword.value],
  mutationFn: (params: SearchQuery) => searchTripRequest(params),
  onSuccess: (data) => {
    trips.value = data
  },
  onError: (error) => {
    console.log(error)
  }
})

const { data: categories, isLoading: isCategoryLoading } = useQuery({
  queryKey: ['categories'],
  queryFn: categoryRequest
})

const onSubmit = () => {
  trips.value = []
  categoryGroup.value = []
  mutate({ query: searchKeyword.value, city: Number(props.cityId) })
}

const changeCategory = (category: string) => {
  if (categoryGroup.value.includes(category)) {
    categoryGroup.value = categoryGroup.value.filter((item) => item !== category)
  } else {
    categoryGroup.value.push(category)
  }
}
</script>

<template>
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
          <span class="absolute start-0 inset-y-0 flex items-center justify-center px-2">
            <Search :size="20" />
          </span>
        </div>
        <!-- 카테고리 선택창 -->
        <ToggleGroup
          type="multiple"
          class="justify-start overflow-scroll my-2 py-2 scrollbar-hide"
          v-model="categoryGroup"
          @update:model-value="changeCategory"
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
          <Skeleton v-for="i in [1, 2, 3, 4, 5]" :key="i" class="w-56 h-9 rounded-md" />
        </div>

        <div
          v-if="trips.length !== 0"
          class="flex flex-grow max-h-screen flex-col space-y-2 overflow-y-scroll scrollbar-hide"
        >
          <TripPlanCard v-for="trip in categoryTrips" :key="trip.tourId" :trip="trip" />
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
    <TabsContent value="manage">
      <TripManageBox @next-step="$emit('nextStep')" />
    </TabsContent>
  </Tabs>
</template>

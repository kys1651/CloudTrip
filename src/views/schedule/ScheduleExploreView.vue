<script setup lang="ts">
import { scheduleSearchRequest } from '@/api/schedule'
import { citiesRequest } from '@/api/trip'
import ScheduleCard from '@/components/schedule/ScheduleCard.vue'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import Button from '@/components/ui/button/Button.vue'
import { Calendar } from '@/components/ui/calendar'
import { Card, CardContent } from '@/components/ui/card'
import Checkbox from '@/components/ui/checkbox/Checkbox.vue'
import { CommandEmpty, CommandGroup, CommandItem, CommandList } from '@/components/ui/command'
import Input from '@/components/ui/input/Input.vue'
import Label from '@/components/ui/label/Label.vue'
import {
  Pagination,
  PaginationEllipsis,
  PaginationFirst,
  PaginationLast,
  PaginationList,
  PaginationListItem,
  PaginationNext,
  PaginationPrev
} from '@/components/ui/pagination'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import {
  TagsInput,
  TagsInputInput,
  TagsInputItem,
  TagsInputItemDelete,
  TagsInputItemText
} from '@/components/ui/tags-input'
import { useToast } from '@/components/ui/toast'
import { toMonthDay } from '@/lib/formatter'
import { imageOrCityImage, imageOrDefault } from '@/lib/image-load'
import { cn } from '@/lib/utils'
import { useAuthenticationStore } from '@/stores/authentication'
import { DateFormatter, getLocalTimeZone, type DateValue } from '@internationalized/date'
import { useQuery } from '@tanstack/vue-query'
import {
  Ban,
  CalendarIcon,
  CircleCheck,
  Globe,
  Lock,
  LockOpen,
  Luggage,
  UserRound,
  UsersRound
} from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { ComboboxAnchor, ComboboxInput, ComboboxPortal, ComboboxRoot } from 'radix-vue'
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const currentPage = ref<number>(1)
const maximumPage = computed(() => Math.ceil(schedules.value.length / 12))
const { profile } = storeToRefs(useAuthenticationStore())

const timestamp = ref<number>(0)
const modelValue = ref<string[]>([])
const isOpen = ref(false)
const searchTerm = ref('')
const { data: cities } = useQuery({
  queryKey: ['schedules', 'cities'],
  queryFn: () => citiesRequest()
})

const searchCities = computed(() => {
  if (cities.value) {
    return cities.value.map((city) => ({ value: city.cityCode, label: city.name }))
  }
  return []
})

const route = useRoute()

const filteredCities = computed(() =>
  searchCities.value.filter((city) => !modelValue.value.includes(city.label))
)

const { data: schedules, refetch } = useQuery({
  queryKey: ['schedules'],
  queryFn: () => scheduleSearchRequest(route.query),
  initialData: []
})

const pagingSchedules = computed(() => {
  const start = (currentPage.value - 1) * 12
  const end = start + 12
  return schedules.value.slice(start, end)
})

const df = new DateFormatter('ko-kr', {
  dateStyle: 'long'
})

const startDate = ref<DateValue>()
const endDate = ref<DateValue>()

const isSingle = ref<boolean>(false)
const isMulti = ref<boolean>(false)

const minValue = ref<number>(0)
const maxValue = ref<number>(100)

const reset = () => {
  startDate.value = undefined
  endDate.value = undefined
  modelValue.value = []
  isSingle.value = false
  isMulti.value = false
  minValue.value = 0
  maxValue.value = 100
  router.push({ name: 'schedule-explore' }).then(() => {
    currentPage.value = 1
    refetch()
  })
}

const router = useRouter()
const toast = useToast()

const onSubmit = () => {
  if (startDate.value && endDate.value && startDate.value > endDate.value) {
    toast.toast({
      title: '검색 실패',
      description: '시작일이 종료일보다 늦습니다.',
      variant: 'destructive'
    })
    return
  }
  if (minValue.value > maxValue.value) {
    toast.toast({
      title: '검색 실패',
      description: '최소 여행지 수가 최대 여행지 수보다 큽니다.',
      variant: 'destructive'
    })
    return
  }

  let mode = undefined
  if (isSingle.value && isMulti.value) {
    mode = 'both'
  } else if (isSingle.value) {
    mode = 'single'
  } else if (isMulti.value) {
    mode = 'multi'
  }

  const params = {
    startDate: startDate.value?.toString(),
    endDate: endDate.value?.toString(),
    names: modelValue.value.length > 0 ? modelValue.value.toString() : undefined,
    mode: mode,
    minCount: minValue.value.toString(),
    maxCount: maxValue.value.toString()
  }
  router.push({ name: 'schedule-explore', query: params }).then(() => {
    currentPage.value = 1
    refetch()
  })
}
</script>

<template>
  <div class="container relative flex flex-row py-8 md:py-12">
    <div class="max-w-48 min-w-48 mx-8 space-y-2 bg-white">
      <h3 class="text-2xl font-bold">검색 조건</h3>
      <div class="flex flex-col">
        <Label htmlFor="start-date" class="mb-1">시작 기간</Label>
        <Popover>
          <PopoverTrigger as-child>
            <Button
              variant="outline"
              :class="
                cn('justify-start text-left font-normal', !startDate && 'text-muted-foreground')
              "
            >
              <CalendarIcon class="mr-2 h-4 w-4" />
              {{
                startDate
                  ? df.format(startDate.toDate(getLocalTimeZone()))
                  : '시작일을 선택해주세요.'
              }}
            </Button>
          </PopoverTrigger>
          <PopoverContent class="w-auto p-0">
            <Calendar v-model="startDate" initial-focus />
          </PopoverContent>
        </Popover>
      </div>
      <div class="flex flex-col">
        <Label htmlFor="end-date" class="mb-1">종료 기간</Label>
        <Popover>
          <PopoverTrigger as-child>
            <Button
              variant="outline"
              :class="
                cn('justify-start text-left font-normal', !endDate && 'text-muted-foreground')
              "
            >
              <CalendarIcon class="mr-2 h-4 w-4" />
              {{
                endDate ? df.format(endDate.toDate(getLocalTimeZone())) : '종료일을 선택해주세요.'
              }}
            </Button>
          </PopoverTrigger>
          <PopoverContent class="w-auto p-0">
            <Calendar v-model="endDate" initial-focus />
          </PopoverContent>
        </Popover>
      </div>
      <div>
        <Label htmlFor="location" class="mt-1">지역</Label>
        <TagsInput class="px-0 gap-0" :model-value="modelValue">
          <div class="flex gap-2 flex-wrap items-center px-3">
            <TagsInputItem v-for="item in modelValue" :key="item" :value="item">
              <TagsInputItemText />
              <TagsInputItemDelete />
            </TagsInputItem>
          </div>

          <ComboboxRoot
            v-model="modelValue"
            v-model:open="isOpen"
            v-model:searchTerm="searchTerm"
            class="w-full"
          >
            <ComboboxAnchor as-child>
              <ComboboxInput placeholder="지역 입력하기" as-child>
                <TagsInputInput
                  class="w-full px-3"
                  :class="modelValue.length > 0 ? 'mt-2' : ''"
                  @keydown.enter.prevent
                />
              </ComboboxInput>
            </ComboboxAnchor>

            <ComboboxPortal>
              <CommandList
                position="popper"
                class="w-[--radix-popper-anchor-width] rounded-md mt-2 border bg-popover text-popover-foreground shadow-md outline-none data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2"
              >
                <CommandEmpty />
                <CommandGroup>
                  <CommandItem
                    v-for="city in filteredCities"
                    :key="city.value"
                    :value="city.label"
                    @select.prevent="
                      (ev) => {
                        if (
                          typeof ev.detail.value === 'string' &&
                          ev.timeStamp > timestamp + 1000
                        ) {
                          modelValue.push(ev.detail.value)
                          timestamp = ev.timeStamp
                        }
                        searchTerm = ''
                        isOpen = false
                      }
                    "
                  >
                    {{ city.label }}
                  </CommandItem>
                </CommandGroup>
              </CommandList>
            </ComboboxPortal>
          </ComboboxRoot>
        </TagsInput>
      </div>
      <div class="py-3 space-y-3 flex flex-col">
        <div>
          <Label class="flex items-center space-x-2">
            <Checkbox id="is-private" v-model:checked="isSingle" />
            <span>싱글 모드</span>
          </Label>
        </div>
        <div>
          <Label class="flex items-center space-x-2">
            <Checkbox id="is-multimode" v-model:checked="isMulti" />
            <span>멀티 모드</span>
          </Label>
        </div>
      </div>
      <div>
        <Label htmlFor="location">최소 여행지 수</Label>
        <Input
          class="mt-1"
          type="number"
          v-model="minValue"
          :min="0"
          @update:modelValue="
            () => {
              if (minValue < 0) {
                minValue = 0
              }
            }
          "
        />
      </div>
      <div>
        <Label htmlFor="location">최대 여행지 수</Label>
        <Input
          class="mt-1"
          type="number"
          v-model="maxValue"
          :min="0"
          @update:modelValue="
            () => {
              if (maxValue < 0) {
                maxValue = 100
              }
            }
          "
        />
      </div>
      <div class="pt-2 flex justify-between">
        <Button class="w-20" size="sm" variant="outline" @click="reset"> 초기화 </Button>
        <Button class="w-20" size="sm" variant="outline" @click="onSubmit"> 검색 </Button>
      </div>
    </div>
    <div
      v-if="!schedules || schedules.length === 0"
      class="w-full justify-center min-w-[60vw] max-w-[60vw] min-h-[50vh] items-center text-gray-400"
    >
      <span class="w-full h-full flex justify-center items-center">조회된 게시글이 없습니다.</span>
    </div>
    <div v-else class="min-w-[60vw] min-h-[50vh] flex flex-col items-center">
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        <ScheduleCard
          v-for="schedule in pagingSchedules"
          :key="schedule.scheduleId"
          :schedule="schedule"
        />
      </div>
      <Pagination
        class="flex justify-center py-10"
        v-slot="{ page }"
        :items-per-page="12"
        :page="currentPage"
        :total="schedules.length"
        show-edges
        :default-page="1"
      >
        <PaginationList v-slot="{ items }" class="flex items-center gap-1">
          <PaginationFirst @click="currentPage = 1" />
          <PaginationPrev
            @click="
              () => {
                if (currentPage > 1) {
                  currentPage -= 1
                }
              }
            "
          />

          <template v-for="(item, index) in items">
            <PaginationListItem
              v-if="item.type === 'page'"
              :key="index"
              :value="item.value"
              as-child
            >
              <Button
                class="w-10 h-10 p-0"
                :variant="item.value === page ? 'default' : 'outline'"
                @click="currentPage = item.value"
              >
                {{ item.value }}
              </Button>
            </PaginationListItem>
            <PaginationEllipsis v-else :key="item.type" :index="index" />
          </template>

          <PaginationNext
            @click="
              () => {
                if (currentPage < maximumPage) {
                  currentPage += 1
                }
              }
            "
          />
          <PaginationLast @click="currentPage = maximumPage" />
        </PaginationList>
      </Pagination>
    </div>
  </div>
</template>

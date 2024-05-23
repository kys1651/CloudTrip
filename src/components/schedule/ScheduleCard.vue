<script setup lang="ts">
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
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Card, CardContent } from '@/components/ui/card'
import { useAuthenticationStore } from '@/stores/authentication'
import { imageOrCityImage, imageOrDefault } from '@/lib/image-load'
import { toMonthDay } from '@/lib/formatter'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import type { IScheduleSearch } from '@/types/schedule.type'
import ScheduleDropdownMenu from '@/components/schedule/ScheduleDropdownMenu.vue'

const router = useRouter()
defineProps<{
  schedule: IScheduleSearch
}>()

const { profile } = storeToRefs(useAuthenticationStore())
</script>

<template>
  <Card
    :key="schedule.scheduleId"
    class="hover:scale-105 cursor-pointer transition-transform duration-500 ease-in-out shadow-md"
    :class="{
      'cursor-pointer': schedule.usernames.includes(profile?.email || '') || !schedule.private,
      'cursor-not-allowed': !schedule.usernames.includes(profile?.email || '') && schedule.private
    }"
  >
    <img
      :src="imageOrCityImage(schedule.thumbnailImage, schedule.cityCode)"
      :alt="schedule.name"
      class="w-full h-48 object-cover rounded-t-lg"
      @click="
        () => {
          if (schedule.usernames.includes(profile?.email || '') || !schedule.private) {
            router.push({
              name: 'schedule-detail',
              params: { scheduleId: schedule.scheduleId }
            })
          }
        }
      "
    />
    <CardContent class="p-3 space-y-1">
      <div
        class="flex flex-col"
        @click="
          () => {
            if (schedule.usernames.includes(profile?.email || '') || !schedule.private) {
              router.push({
                name: 'schedule-detail',
                params: { scheduleId: schedule.scheduleId }
              })
            }
          }
        "
      >
        <div class="flex flex-row justify-between">
          <h3 class="text-md font-bold text-ellipsis overflow-hidden whitespace-nowrap">
            {{ schedule.name }}
          </h3>
        </div>
        <div class="flex flex-row w-full justify-end items-center space-x-1 text-xs">
          <CalendarIcon class="w-3 h-3 text-gray-500" />
          <span class="text-gray-500"
            >{{ toMonthDay(new Date(schedule.startDate)) }} ~
            {{ toMonthDay(new Date(schedule.endDate)) }}</span
          >
        </div>

        <div class="flex flex-row justify-between">
          <div
            v-if="schedule.multi"
            class="flex flex-row text-sm items-center justify-center space-x-1 text-gray-500"
          >
            <UsersRound :size="14" />
            <span class="flex justify-end">멀티</span>
          </div>
          <div
            v-else
            class="flex flex-row text-sm items-center justify-center space-x-1 text-gray-500"
          >
            <UserRound :size="14" />
            <span class="flex justify-end">싱글</span>
          </div>
          <div
            class="flex flex-row text-sm items-center justify-center space-x-1 text-gray-500"
            v-if="schedule.private"
          >
            <Lock :size="14" />
            <span class="flex justify-end">비공개</span>
          </div>
          <div
            class="flex flex-row text-sm items-center justify-center space-x-1 text-gray-500"
            v-else
          >
            <LockOpen :size="14" />
            <span class="flex justify-en">공개</span>
          </div>
          <div
            class="flex flex-row text-sm items-center justify-center space-x-1 text-gray-500"
            v-if="schedule.finished"
          >
            <CircleCheck :size="14" />
            <span class="flex justify-en">완료</span>
          </div>
          <div
            class="flex flex-row text-sm items-center justify-center space-x-1 text-gray-500"
            v-else
          >
            <Ban :size="14" />
            <span class="flex justify-end">미완료</span>
          </div>
        </div>
        <div class="flex flex-row justify-between"></div>
        <div class="flex flex-row space-x-1 items-center text-gray-500 justify-between">
          <div class="flex flex-row items-center space-x-1">
            <Globe :size="14" />
            <span class="text-sm">{{ schedule.cityName }}</span>
          </div>
          <div class="flex flex-row items-center space-x-1">
            <Luggage :size="14" />
            <span class="text-sm">{{ schedule.count }}곳</span>
          </div>
        </div>
      </div>
      <div class="flex items-center justify-between">
        <div class="flex items-center space-x-1 text-sm mt-2">
          <Avatar class="w-6 h-6">
            <AvatarImage alt="User Avatar" :src="imageOrDefault(schedule.user.profileImage)" />
            <AvatarFallback>{{ schedule.user.userId }}</AvatarFallback>
          </Avatar>
          <span class="text-gray-500">{{ schedule.user.nickname }}</span>
        </div>
        <div class="flex items-center space-x-2">
          <ScheduleDropdownMenu :schedule-id="schedule.scheduleId" />
        </div>
      </div>
    </CardContent>
  </Card>
</template>

<style scoped></style>

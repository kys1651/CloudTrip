<script setup lang="ts">
import Separator from '@/components/ui/separator/Separator.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { useQuery } from '@tanstack/vue-query'
import { storeToRefs } from 'pinia'
import Button from '../ui/button/Button.vue'
import { userScheduleListRequest } from '@/api/schedule'
import { useRoute } from 'vue-router'
import type { IScheduleSearch } from '@/types/schedule.type'
import ScheduleCard from '@/components/schedule/ScheduleCard.vue'
const { profile } = storeToRefs(useAuthenticationStore())
const route = useRoute()
const { data: scheduleList } = useQuery<IScheduleSearch[]>({
  queryKey: ['scheduleList', profile.value?.id],
  queryFn: () => userScheduleListRequest(profile.value!.id)
})
</script>

<template>
  <div ref="scrollRef" class="w-full mx-auto p-5">
    <div>
      <h3 class="text-3xl font-bold mb-5">나의 여행 계획 모아보기</h3>
    </div>
    <Separator />
    <div
      v-if="scheduleList && scheduleList.length > 0"
      class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-5"
    >
      <ScheduleCard
        v-for="schedule in scheduleList"
        :key="schedule.scheduleId"
        :schedule="schedule"
      />
    </div>
    <div v-else class="flex flex-col items-center justify-center min-h-screen">
      <h4 class="text-xl font-semibold mb-3 text-center">
        아직 참여한 여행 계획이 존재하지않습니다.
      </h4>
      <Button class="mt-4"
        ><RouterLink :to="{ name: 'schedule-explore' }">여행 계획 둘러보기</RouterLink></Button
      >
    </div>
  </div>
</template>

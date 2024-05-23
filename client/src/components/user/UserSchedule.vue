<template>
  <div class="container">
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
        아직 작성한 여행 계획이 존재하지않습니다.
      </h4>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useQuery } from '@tanstack/vue-query'
import { useRoute } from 'vue-router'
import { userScheduleListRequest } from '@/api/schedule'
import ScheduleCard from '@/components/schedule/ScheduleCard.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'

const { profile } = storeToRefs(useAuthenticationStore())
const route = useRoute()
const { data: scheduleList } = useQuery({
  queryKey: ['scheduleList', route.params.userId],
  queryFn: () => userScheduleListRequest(Number(route.params.userId))
})
</script>

<style scoped></style>

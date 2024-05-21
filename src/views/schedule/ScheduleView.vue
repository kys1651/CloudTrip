<script setup lang="ts">
import ScheduleCreate from "@/components/schedule/ScheduleCreate.vue";
import Button from "@/components/ui/button/Button.vue";
import { Dialog } from "@/components/ui/dialog";
import DialogContent from "@/components/ui/dialog/DialogContent.vue";
import DialogHeader from "@/components/ui/dialog/DialogHeader.vue";
import DialogTrigger from "@/components/ui/dialog/DialogTrigger.vue";
</script>
<template>
  <Dialog>
    <div
      class="flex flex-row h-screen w-screen bg-center bg-cover relative isolate after:absolute after:z-10"
      style="background-image: url('/images/trip.jpg')"
    >
      <HomeNavigator />
      <div
        class="absolute inset-0 bg-black bg-opacity-20 z-20 flex flex-row items-center justify-around"
      >
        <div class="flex flex-col items-center justify-center">
          <h1 class="text-white text-2xl font-bold pb-2">계획 세우기</h1>
          <p class="text-white text-md">
            여러명이서 동시에 여행 일정을 함께 관리하고 공유하세요
          </p>
          <DialogTrigger>
            <Button class="mt-4" variant="outline">시작하기</Button>
          </DialogTrigger>
        </div>
        <div class="flex flex-col items-center justify-center">
          <h1 class="text-white text-2xl font-bold pb-2">계획 구경하기</h1>
          <p class="text-white text-md">다른 사람들의 여행 계획을 구경하고 참고하세요</p>
          <Button class="mt-4" variant="outline">구경하기</Button>
        </div>
      </div>
    </div>
    <DialogContent>
      <DialogHeader>여행 계획 세우기 </DialogHeader>
      <ScheduleCreate />
    </DialogContent>
  </Dialog>
</template>

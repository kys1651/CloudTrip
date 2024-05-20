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
    <div class="flex flex-row container py-5">
      <div class="flex flex-col w-1/2 space-y-5">
        <h1 class="w-full flex justify-center items-center text-3xl font-bold">
          여행 계획 세우기
        </h1>
        <div
          class="h-96 border rounded-xl bg-center bg-cover relative isolate aftter:content-'' after:absolute after:inset-0 after:z-10"
          style="background-image: url('/images/couple.jpeg')"
        >
          <div
            class="absolute inset-0 rounded-xl bg-black bg-opacity-20 z-20 flex flex-col items-center justify-center"
          >
            <h1 class="text-white text-2xl font-bold pb-2">계획 세우기</h1>
            <p class="text-white text-md">
              여러명이서 동시에 여행 일정을 함께 관리하고 공유하세요
            </p>
            <DialogTrigger>
              <Button class="mt-4" variant="outline">시작하기</Button>
            </DialogTrigger>
          </div>
        </div>
      </div>
    </div>
    <DialogContent>
      <DialogHeader>여행 계획 세우기 </DialogHeader>
      <ScheduleCreate />
    </DialogContent>
  </Dialog>
</template>

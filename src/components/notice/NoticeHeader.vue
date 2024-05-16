<script setup lang="ts">
import SocialBox from '@/components/social/SocialBox.vue'
import { ref } from 'vue'

const meetings = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
const verticalScroll = ref<HTMLElement | null>(null)
const verticalScrollLeft = () => {
  if (verticalScroll.value) {
    verticalScroll.value.scrollLeft -= 100
  }
}
const verticalScrollRight = () => {
  if (verticalScroll.value) {
    verticalScroll.value.scrollLeft += 100
  }
}
</script>

<template>
  <div class="flex flex-col my-6 items-start w-full">
    <h2 class="text-4xl font-bold mb-3">공지사항</h2>
    <span class="text-gray-400 dark:text-gray-500"
      >내가 관심있는 모임에 참여하여 구성원과 대화를 나눠보세요.</span
    >
    <div class="flex flex-row items-center w-full h-full">
      <i
        class="pi pi-angle-left text-2xl pr-4 h-32 cursor-pointer flex items-center"
        @click="verticalScrollLeft"
      />
      <div
        class="w-full flex flex-row gap-3 overflow-scroll scrollbar-hide scroll-smooth"
        ref="verticalScroll"
      >
        <SocialBox v-for="meeting in meetings" />
      </div>
      <i
        class="pi pi-angle-right text-2xl pl-4 h-32 cursor-pointer flex items-center"
        @click="verticalScrollRight"
      />
    </div>
  </div>
</template>

<style scoped></style>

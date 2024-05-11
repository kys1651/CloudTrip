<script setup lang="ts">
import BoardList from '@/components/social/BoardList.vue'
import SocialBox from '@/components/social/SocialBox.vue'
import SocialHeader from '@/components/social/SocialHeader.vue'
import SocialBody from '@/components/social/SocialBody.vue'
import type { BoardListProps } from '@/types/board.type'

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
  <div class="container justify-center flex flex-col my-6 items-start">
    <SocialHeader />
    <SocialBody />
    <!-- <div class="w-full grid grid-cols-2 gap-12 mt-6">
      <BoardList v-for="(category, index) in categories" :key="index" :title="category.title" />
    </div> -->
  </div>
</template>

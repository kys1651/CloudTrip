<script setup lang="ts">
import BoardList from '@/components/social/BoardList.vue'
import SocialBox from '@/components/social/SocialBox.vue'
import SocialHeader from '@/components/social/SocialHeader.vue'
import SocialBody from '@/components/social/SocialBody.vue'
import type { BoardListProps } from '@/types/board.type'

import { onMounted, onUpdated, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
watch(route, () => {
  console.log(route.query.keyword)
})

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
onUpdated(() => {})
</script>

<template>
  <div class="container justify-center flex flex-col my-6 items-start">
    <SocialHeader />
    <SocialBody />
  </div>
</template>

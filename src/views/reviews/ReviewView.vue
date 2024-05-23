<script setup lang="ts">
import { reviewsRequest } from '@/api/review'
import ReviewCard from '@/components/review/ReviewCard.vue'
import Button from '@/components/ui/button/Button.vue'
import { useInfiniteQuery } from '@tanstack/vue-query'
import { useInfiniteScroll } from '@vueuse/core'
import { Pencil } from 'lucide-vue-next'
import { ref } from 'vue'
import { LoaderCircle } from 'lucide-vue-next'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
const scrollRef = ref<HTMLElement | null>(null)

const authenticationStore = useAuthenticationStore()
const { isLogin, profile } = storeToRefs(authenticationStore)
const {
  data: pages,
  fetchNextPage,
  hasNextPage
} = useInfiniteQuery({
  queryKey: ['reviews'],
  queryFn: reviewsRequest,
  getNextPageParam: (lastPage) => (lastPage.hasNext ? lastPage.nextCursor : undefined),
  initialPageParam: 0
})

useInfiniteScroll(
  scrollRef,
  async () => {
    if (hasNextPage.value) {
      await fetchNextPage()
    }
  },
  {
    distance: 10
  }
)
</script>

<template>
  <div class="container flex flex-col items-center w-full py-7 relative">
    <h2 class="text-4xl font-bold mb-3">여행지 후기</h2>
    <div class="flex justify-center mb-4">
      <Button
        v-if="isLogin && profile"
        variant="outline"
        class="rounded-xl"
        @click="$router.push({ name: 'review-create' })"
      >
        <Pencil class="mr-2 h-4 w-4" />
        신규 글 작성
      </Button>
    </div>
    <div class="flex flex-col items-center justify-center h-[80vh] w-full">
      <div
        class="justify-center grid grid-cols-1 gap-10 m-auto overflow-scroll scrollbar-hide px-10"
        ref="scrollRef"
      >
        <template v-if="pages">
          <template v-for="(page, index) in pages.pages" :key="index">
            <ReviewCard v-for="review in page.contents" :key="review.reviewId" :review="review" />
          </template>
        </template>
        <template v-else>
          <LoaderCircle class="mr-2 h-16 w-16 animate-spin" />
        </template>
      </div>
    </div>
  </div>
</template>

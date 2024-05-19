<script setup lang="ts">
import { reviewsRequest } from '@/api/review'
import ReviewCard from '@/components/review/ReviewCard.vue'
import Button from '@/components/ui/button/Button.vue'
import { useInfiniteQuery } from '@tanstack/vue-query'
import { useInfiniteScroll } from '@vueuse/core'
import { Pencil } from 'lucide-vue-next'
import { ref } from 'vue'

const scrollRef = ref<HTMLElement | null>(null)

const { data: pages, fetchNextPage } = useInfiniteQuery({
  queryKey: ['reviews'],
  queryFn: reviewsRequest,
  getNextPageParam: (lastPage) => lastPage.nextCursor + 1,
  initialPageParam: 0
})

useInfiniteScroll(
  scrollRef,
  async () => {
    if (!pages.value?.pages[pages.value.pages.length - 1].hasNext) return
    await fetchNextPage()
  },
  {
    distance: 10
  }
)
</script>

<template>
  <div class="container flex flex-col items-start w-full py-10 relative">
    <div class="fixed right-5 top-20">
      <Button variant="outline" class="rounded-xl" @click="$router.push({ name: 'review-create' })">
        <Pencil class="mr-2 h-4 w-4" />
        신규 글 작성
      </Button>
    </div>
    <div class="flex flex-col items-center justify-center h-[80vh] w-full">
      <div
        class="justify-center grid grid-cols-1 gap-10 m-auto overflow-scroll scrollbar-hide px-10"
        ref="scrollRef"
      >
        <template v-for="(page, index) in pages?.pages" :key="index">
          <ReviewCard v-for="review in page.contents" :key="review.reviewId" :review="review" />
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { LikeReviewRequest } from '@/api/review'
import { useQuery } from '@tanstack/vue-query'
import Separator from '@/components/ui/separator/Separator.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { Button } from '@/components/ui/button'
import SimpleReviewCard from '../card/SimpleReviewCard.vue'

const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const { data: reviews } = useQuery({
  queryKey: ['reviews', profile.value?.id],
  queryFn: () => LikeReviewRequest(profile.value!.id)
})
</script>

<template>
  <div ref="scrollRef" class="container w-full max-w-5xl mx-auto p-5">
    <div>
      <h3 class="text-3xl font-bold mb-5">좋아요한 글 모아보기</h3>
    </div>
    <Separator />
    <div
      v-if="reviews?.length > 0"
      class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-5"
    >
      <SimpleReviewCard v-for="review in reviews" :key="review.reviewId" :review="review" />
    </div>
    <div v-else class="flex flex-col items-center justify-center min-h-screen">
      <h4 class="text-xl font-semibold mb-3 text-center">좋아요한 리뷰가 존재하지않습니다.</h4>
      <Button class="mt-4"><RouterLink :to="{ name: 'review' }">리뷰 둘러보기</RouterLink></Button>
    </div>
  </div>
</template>

<style scoped></style>

<template>
  <div class="container">
    <div
      v-if="userLikedReviews?.length > 0"
      class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-5"
    >
      <SimpleReviewCard
        v-for="review in userLikedReviews"
        :key="review.reviewId"
        :review="review"
      />
    </div>
    <div v-else class="flex flex-col items-center justify-center min-h-screen">
      <h4 class="text-xl font-semibold mb-3 text-center">아직 작성한 리뷰가 존재하지않습니다.</h4>
    </div>
  </div>
</template>

<script setup lang="ts">
import { userLikedReviewRequest } from '@/api/review'
import { useQuery } from '@tanstack/vue-query'
import { useRoute } from 'vue-router'
import SimpleReviewCard from '../card/SimpleReviewCard.vue'
const route = useRoute()
const { data: userLikedReviews } = useQuery({
  queryKey: ['userLikedReviews', route.params.userId],
  queryFn: () => userLikedReviewRequest(Number(route.params.userId))
})
</script>

<style scoped></style>

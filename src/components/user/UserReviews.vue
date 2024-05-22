<template>
  <div class="container">
    <div
      v-if="userReviews && userReviews?.length > 0"
      class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-5"
    >
      <SimpleReviewCard
        v-for="review in userReviews"
        :key="review.reviewId"
        :review="review"
      />
    </div>
    <div v-else class="flex flex-col items-center justify-center min-h-screen">
      <h4 class="text-xl font-semibold mb-3 text-center">
        아직 작성한 리뷰가 존재하지않습니다.
      </h4>
    </div>
  </div>
</template>

<script setup lang="ts">
import { userReviewsRequest } from "@/api/review";
import SimpleReviewCard from "@/components/card/SimpleReviewCard.vue";
import { useQuery } from "@tanstack/vue-query";
import { useRoute } from "vue-router";
const route = useRoute();
const { data: userReviews } = useQuery({
  queryKey: ["userReviews", route.params.userId],
  queryFn: () => userReviewsRequest(Number(route.params.userId)),
});
</script>

<style scoped></style>

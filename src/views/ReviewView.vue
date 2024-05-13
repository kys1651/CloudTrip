<script setup lang="ts">
import { reviewsRequest } from "@/api/review";
import ReviewCard from "@/components/review/ReviewCardV2.vue";
import { useInfiniteQuery } from "@tanstack/vue-query";
import { useInfiniteScroll } from "@vueuse/core";
import { ref } from "vue";

const scrollRef = ref<HTMLElement | null>(null);

const { data: pages, fetchNextPage } = useInfiniteQuery({
  queryKey: ["reviews"],
  queryFn: reviewsRequest,
  getNextPageParam: (lastPage) => lastPage.nextCursor + 1,
  initialPageParam: 0,
});

useInfiniteScroll(
  scrollRef,
  async () => {
    if (!pages.value?.pages[pages.value.pages.length - 1].hasNext) return;
    await fetchNextPage();
  },
  {
    distance: 10,
  }
);
</script>

<template>
  <div class="container flex flex-col items-start w-full">
    <div class="flex flex-col my-6 items-center w-full">
      <h2 class="text-4xl font-bold mb-3">여행지 리뷰</h2>
    </div>
    <div class="flex flex-col items-center justify-center h-[90vh] w-full">
      <div
        class="justify-center grid grid-cols-3 gap-10 m-auto overflow-scroll scrollbar-hide px-10"
        ref="scrollRef"
      >
        <template v-for="(page, index) in pages?.pages" :key="index">
          <ReviewCard
            v-for="review in page.contents"
            :key="review.reviewId"
            :review="review"
          />
        </template>
      </div>
    </div>
  </div>

  <!-- <ReviewSide /> -->
</template>

<script setup lang="ts">
import ReviewCard from "@/components/review/ReviewCard.vue";
import ReviewSide from "@/components/review/ReviewSide.vue";
import { Button } from "@/components/ui/button";
import { useMutation, useQuery } from "@tanstack/vue-query";
import { reviewsRequest } from "@/api/review";
import { onMounted, ref } from "vue";
import type { ReviewDetail } from "@/types/board.type";
import { useAuthenticationStore } from "@/stores/authentication";
import { storeToRefs } from "pinia";

const authentication = useAuthenticationStore();
const { isLogin } = storeToRefs(authentication);

const { data: reviews, isLoading } = useQuery({
  queryKey: ["reviews"],
  queryFn: reviewsRequest,
});
</script>

<style scoped>
.content {
  width: calc(100% - 240px);
}
</style>

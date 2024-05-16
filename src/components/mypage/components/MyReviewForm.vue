<script setup lang="ts">
import { myReviewsRequest } from '@/api/review'
import { useQuery } from '@tanstack/vue-query'
import { ref } from 'vue'
import Separator from '@/components/ui/separator/Separator.vue'
import { useRouter } from 'vue-router'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'

const router = useRouter()
const scrollRef = ref<HTMLElement | null>(null)

const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const { data: myReviews } = useQuery({
  queryKey: ['myReviews', 2],
  queryFn: () => myReviewsRequest(2)
})
console.log(myReviews)
</script>

<template>
  <div ref="scrollRef" class="container w-full max-w-5xl mx-auto p-5">
    <div>
      <h3 class="text-3xl font-bold mb-5">작성한 글 모아보기</h3>
    </div>
    <Separator />
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-5">
      <div
        v-for="review in myReviews"
        :key="review.reviewId"
        class="bg-white shadow rounded-lg p-4 flex flex-col items-center"
      >
        <div v-if="review.images && review.images.length > 0" class="mb-4">
          <img
            :src="review.images[0]"
            alt="Review Image"
            class="w-full h-40 object-cover rounded-lg"
          />
        </div>
        <div
          v-else
          class="content-preview h-40 w-full bg-gray-200 rounded-lg flex items-center justify-center p-4 text-center"
        >
          {{ review ? review.content.substring(0, 20) : '내용 없음' }}...
        </div>
        <div class="review-content mt-2 text-center">
          <p class="font-semibold">{{ review.address }}</p>
          <p class="text-gray-500">{{ new Date(review.createdAt).toLocaleDateString() }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>

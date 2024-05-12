<template>
  <div class="container">
    <div class="content flex flex-col my-6 items-start w-full">
      <div class="flex flex-col my-6 items-center w-full">
        <h2 class="text-4xl font-bold mb-3">여행지 리뷰</h2>
      </div>

      <div class="flex flex-col my-6 items-center w-full space-y-4">
        <div class="flex items-center space-x-2 bg-white p-4 shadow-md rounded-lg">
          <!-- 셀렉트 박스 -->
          <select
            v-model="searchForm.key"
            class="border border-gray-300 text-gray-700 py-2 px-4 rounded leading-tight focus:outline-none focus:border-blue-500"
          >
            <option value="">--선택하세요--</option>
            <option value="nickname">작성자</option>
            <option value="tour_id">위치</option>
            <option value="created_at">날짜</option>
          </select>

          <input
            v-model="searchForm.keyword"
            type="text"
            placeholder="검색어 입력"
            class="flex-1 border border-gray-300 py-2 px-4 rounded focus:outline-none focus:border-blue-500"
          />

          <Button type="submit" @click.prevent="search"> 검색 </Button>
          <Button v-if="isLogin" variant="default">
            <router-link :to="{ name: 'reviewWrite' }">리뷰 작성 바로가기</router-link>
          </Button>
        </div>
        <div v-if="!reviews || reviews.length == 0">
          <h2 class="text-4xl font-bold mt-10">현재 리뷰가 존재하지 않습니다.</h2>
        </div>
        <ReviewCard v-for="review in reviews" :key="review.reviewId" :review="review" />
      </div>
    </div>

    <ReviewSide />
  </div>
</template>

<script setup lang="ts">
import ReviewCard from '@/components/review/ReviewCard.vue'
import ReviewSide from '@/components/review/ReviewSide.vue'
import { Button } from '@/components/ui/button'
import { useQuery, useQueryClient } from '@tanstack/vue-query'
import { reviewsRequest, searchReviews } from '@/api/review'
import { onMounted, ref } from 'vue'
import type { SearchCondition } from '@/types/board.type'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'

const authentication = useAuthenticationStore()
const { isLogin } = storeToRefs(authentication)

const searchForm = ref<SearchCondition>({
  key: '',
  keyword: ''
})

const queryClient = useQueryClient()
const search = async () => {
  const params = new URLSearchParams()
  params.append('key', searchForm.value.key)
  params.append('keyword', searchForm.value.keyword)
  const searchedReviews = await searchReviews(params)
  queryClient.setQueryData(['reviews'], searchedReviews)
}

const { data: reviews, isLoading } = useQuery({
  queryKey: ['reviews'],
  queryFn: reviewsRequest
})
</script>

<style scoped>
.container {
  display: flex;
  width: 100%;
  min-height: 100vh;
}
.content {
  width: calc(100% - 240px);
}
</style>

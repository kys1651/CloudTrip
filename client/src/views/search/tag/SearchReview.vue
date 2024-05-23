<script setup lang="ts">
import ReviewCard from '@/components/review/ReviewCard.vue'
import SearchHeader from '@/components/search/SearchHeader.vue'
import { Badge } from '@/components/ui/badge'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchResult } from '@/api/search'
import { type ISearch } from '@/types/search.type'

const route = useRoute()
const router = useRouter()

const datas = ref<ISearch>()
const reviewLen = ref()
const noticeLen = ref()
const userLen = ref()
const scheduleLen = ref()
onMounted(() => {
  const keyword = route.query.keyword
  if (!keyword) return

  searchResult(keyword?.toString())
    .then((data) => {
      datas.value = data
      reviewLen.value = datas.value?.reviews.length
      noticeLen.value = datas.value?.notices.length
      userLen.value = datas.value?.users.length
      scheduleLen.value = datas.value?.schedules.length
    })
    .catch((error) => {})
})

// tag
const goSearch = () => {
  router.push({ name: 'search', query: { keyword: route.query.keyword } })
}

const goSearchReview = () => {
  if (!(reviewLen.value > 0)) return
  router.push({ name: 'search-review', query: { keyword: route.query.keyword } })
}

const goSearchNotice = () => {
  if (!(noticeLen.value > 0)) return
  router.push({ name: 'search-notice', query: { keyword: route.query.keyword } })
}

const goSearchProfile = () => {
  if (!(userLen.value > 0)) return
  router.push({ name: 'search-profile', query: { keyword: route.query.keyword } })
}

const goSearchSchedule = () => {
  if (!(scheduleLen.value > 0)) return
  router.push({ name: 'search-schedule', query: { keyword: route.query.keyword } })
}
</script>

<template>
  <div v-if="datas" class="container justify-center flex flex-col my-6 items-start">
    <SearchHeader />
    <div className="w-full px-10">
      <div className="flex justify-between mb-10">
        <div className="flex space-x-2 text-sm">
          <Badge class="badge" @click="goSearch" variant="default"
            >전체 {{ reviewLen + noticeLen + userLen }}</Badge
          >
          <Badge class="badge bg-white text-black border-black" @click="goSearchReview"
            >여행 리뷰 {{ reviewLen }}</Badge
          >
          <Badge class="badge cursor-pointer" @click="goSearchSchedule"
            >여행 계획 {{ scheduleLen }}</Badge
          >
          <Badge class="badge" @click="goSearchNotice">공지사항 {{ noticeLen }}</Badge>
          <Badge class="badge" @click="goSearchProfile">프로필 {{ userLen }}</Badge>
        </div>
      </div>
      <hr />
      <div className="mt-4">
        <h2 className="text-lg font-semibold flex items-center justify-between">여행 리뷰</h2>
        <div className="grid gap-6 mt-4">
          <div className="grid grid-cols-3 gap-4">
            <ReviewCard v-for="review in datas.reviews" :key="review.reviewId" :review="review" />
          </div>
        </div>
        <div class="flex justify-end mt-5"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.badge {
  width: 100px;
  height: 40px;
  justify-content: center;
}
</style>

<script setup lang="ts">
import ReviewCard from '@/components/review/ReviewCard.vue'
import SearchHeader from '@/components/search/SearchHeader.vue'
import { Badge } from '@/components/ui/badge'
import { useAuthenticationStore } from '@/stores/authentication'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchResult } from '@/api/search'
import { type ISearch } from '@/types/search.type'

const route = useRoute()
const router = useRouter()

const authentication = useAuthenticationStore()

const datas = ref<ISearch>()
const reviewLen = ref()
const noticeLen = ref()
const userLen = ref()
onMounted(() => {
  const keyword = route.query.keyword
  if (!keyword) return
  console.log(keyword)

  searchResult(keyword?.toString())
    .then((data) => {
      console.log(data)
      datas.value = data
      reviewLen.value = datas.value.reviews.length
      noticeLen.value = datas.value.notices.length
      userLen.value = datas.value.users.length
    })
    .catch((error) => {
      console.log('전체 조회 실패', error)
    })
})

// tag
const goSearch = () => {
  router.push({ name: 'search', query: { keyword: route.query.keyword } })
}

const goSearchReview = () => {
  router.push({ name: 'search-review', query: { keyword: route.query.keyword } })
}

const goSearchNotice = () => {
  router.push({ name: 'search-notice', query: { keyword: route.query.keyword } })
}

const goSearchProfile = () => {
  router.push({ name: 'search-profile', query: { keyword: route.query.keyword } })
}
</script>

<template>
  <div v-if="datas" class="container justify-center flex flex-col my-6 items-start">
    <SearchHeader />
    <div className="w-full px-10">
      <div className="flex justify-between mb-10">
        <div className="flex space-x-2 text-sm">
          <Badge class="badge" @click="goSearch" variant="default">전체</Badge>
          <Badge class="badge bg-white text-black border-black" @click="goSearchReview"
            >여행 리뷰 {{ reviewLen }}</Badge
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

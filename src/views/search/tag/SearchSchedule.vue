<script setup lang="ts">
import { searchResult } from '@/api/search'
import ReviewCard from '@/components/review/ReviewCard.vue'
import SearchCard from '@/components/search/SearchCard.vue'
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion'
import { Badge } from '@/components/ui/badge'
import { type ISearch } from '@/types/search.type'
import { OctagonAlert } from 'lucide-vue-next'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const datas = ref<ISearch>()
const reviewLen = ref()
const noticeLen = ref()
const userLen = ref()
const scheduleLen = ref()
onMounted(() => {
  const keyword = route.query.keyword
  console.log(keyword)
  if (!keyword) return
  console.log(keyword)

  searchResult(keyword?.toString())
    .then((data) => {
      console.log(data)
      datas.value = data
      reviewLen.value = datas.value?.reviews.length
      noticeLen.value = datas.value?.notices.length
      userLen.value = datas.value?.users.length
      scheduleLen.value = datas.value?.schedules.length
    })
    .catch((error) => {
      console.log('전체 조회 실패', error)
    })
})

const isATagExists = (content: string) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content
  return htmlElement.querySelector('a') !== null
}

const goUserProfile = (userId: number) => {
  router.push({ name: 'userDetail', params: { userId: userId } })
}

// tag
const goSearch = () => {
  router.push({ name: 'search', query: { keyword: route.query.keyword } })
}

const goSearchReview = () => {
  if (!(reviewLen.value > 0)) return
  router.push({ name: 'search-review', query: { keyword: route.query.keyword } })
}

const goSearchSchedule = () => {
  if (!(scheduleLen.value > 0)) return
  router.push({ name: 'search-schedule', query: { keyword: route.query.keyword } })
}

const goSearchNotice = () => {
  if (!(noticeLen.value > 0)) return
  router.push({ name: 'search-notice', query: { keyword: route.query.keyword } })
}

const goSearchProfile = () => {
  if (!(userLen.value > 0)) return
  router.push({ name: 'search-profile', query: { keyword: route.query.keyword } })
}
</script>

<template>
  <div v-if="datas" className="w-full px-10">
    <div className="flex justify-between mb-10">
      <div className="flex space-x-2 text-sm">
        <Badge
          class="badge cursor-pointer bg-white text-black border-black"
          @click="goSearch"
          variant="default"
          >전체 {{ reviewLen + noticeLen + userLen + scheduleLen }}</Badge
        >
        <Badge class="badge cursor-pointer" @click="goSearchReview"
          >여행 리뷰 {{ reviewLen }}</Badge
        >
        <Badge class="badge cursor-pointer" @click="goSearchSchedule"
          >여행 계획 {{ scheduleLen }}</Badge
        >
        <Badge class="badge cursor-pointer" @click="goSearchNotice">공지사항 {{ noticeLen }}</Badge>
        <Badge class="badge cursor-pointer" @click="goSearchProfile">프로필 {{ userLen }}</Badge>
      </div>
    </div>
    <hr />
    <div className="mt-4">
      <h2 className="text-lg font-semibold flex items-center justify-between">여행 리뷰</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <ReviewCard
            v-for="review in datas.reviews.slice(0, 6)"
            :key="review.reviewId"
            :review="review"
          />
        </div>
      </div>
      <div class="flex justify-end mt-5"></div>
      <!-- "더보기" button -->
      <div class="flex justify-center">
        <button v-if="datas.reviews.length > 0" class="mt-10 more-button" @click="goSearchReview">
          여행 계획 전체보기 >
        </button>
        <div v-else class="flex flex-col items-center justify-center">
          <OctagonAlert :size="100" class="mb-10" />
          <p>검색 결과에 대한 여행 리뷰가 존재하지 않습니다.</p>
        </div>
      </div>
      <h2 className="text-lg font-semibold mt-10 mb-10">공지사항</h2>
      <div className="grid gap-6 mt-4">
        <Accordion type="multiple" class="w-full" collapsible>
          <AccordionItem
            v-for="notice in datas.notices.slice(0, 6)"
            :key="notice.noticeId"
            :value="notice.content"
          >
            <AccordionTrigger>📢 {{ notice.title }}</AccordionTrigger>
            <AccordionContent>
              <div class="flex justify-between items-center">
                <p
                  class="flex-grow"
                  :class="{ aTag: isATagExists(notice.content) }"
                  v-html="notice.content"
                ></p>
              </div>
            </AccordionContent>
          </AccordionItem>
        </Accordion>
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

.more-button {
  background-color: white;
  border: 1px solid black;
  border-radius: 8px; /* Rounded borders */
  padding: 10px 20px; /* Padding for the button */
  cursor: pointer;
  transition: background-color 0.3s;
  font-weight: 600;
  margin-bottom: 50px;
}

.more-button:hover {
  background-color: #f0f0f0; /* Slightly darker background on hover */
}
</style>

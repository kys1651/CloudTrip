<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted, watch } from 'vue'
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion'
import SearchHeader from '@/components/search/SearchHeader.vue'
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
          <Badge class="badge" @click="goSearchReview">여행 리뷰 {{ reviewLen }}</Badge>
          <Badge class="badge cursor-pointer" @click="goSearchSchedule"
            >여행 계획 {{ scheduleLen }}</Badge
          >
          <Badge class="badge bg-white text-black border-black" @click="goSearchNotice"
            >공지사항 {{ noticeLen }}</Badge
          >
          <Badge class="badge" @click="goSearchProfile">프로필 {{ userLen }}</Badge>
        </div>
      </div>
      <hr />
      <div className="mt-4">
        <div class="flex justify-end mt-5"></div>
        <h2 className="text-lg font-semibold mt-10">공지사항</h2>
        <div className="grid gap-6 mt-4">
          <Accordion type="multiple" class="w-full" collapsible>
            <AccordionItem
              v-for="notice in datas.notices"
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
  </div>
</template>

<style scoped>
.badge {
  width: 100px;
  height: 40px;
  justify-content: center;
}
</style>

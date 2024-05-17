<script setup lang="ts">
import Pagination from '@/components/common/Pagination.vue'
import { onMounted, ref, computed, onUpdated } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import type { INotice } from '@/types/board.type'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion'
import NoticeHeader from '@/components/notice/NoticeHeader.vue'

const authentication = useAuthenticationStore()
const { isLogin } = storeToRefs(authentication)

const route = useRoute()
const router = useRouter()

const addr = 'http://localhost:8080/api/v1/notice/list'
const notices = ref<INotice[]>([])
onMounted(() => {
  axios
    .get(addr)
    .then((response) => {
      notices.value = response.data
      console.log(route.query.page)
      if (!route.query.page) {
        router.push({ name: 'notice', query: { page: 1 } })
      }
    })
    .catch((error) => {
      console.log('공지사항 조회 오류', error)
    })
})

onUpdated(() => {
  pageNumber.value = Number(route.query.page)
})

const pageNumber = ref<number>(1)

const goCreateNotice = () => {
  router.push({ name: 'notice-create' })
}

const goUpdate = (curNoticeId: number) => {
  console.log(curNoticeId)
  router.push({ name: 'notice-modify', params: { noticeId: curNoticeId } })
}

const postsPerPage = ref(6)

const updateCurrentPage = (pageIdx: number) => {
  router.push({ name: 'notice', query: { page: pageIdx } })
}

const displayedPosts = computed(() => {
  const startIndex = (pageNumber.value - 1) * postsPerPage.value
  const endIndex = startIndex + postsPerPage.value
  return notices.value.slice(startIndex, endIndex)
})

const totalPages = computed(() => {
  console.log(notices.value.length + ' ' + postsPerPage.value)
  return Math.ceil(notices.value.length / postsPerPage.value)
})

const isATagExists = (content: string) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content
  return htmlElement.querySelector('a') !== null
}
</script>

<template>
  <div class="container justify-center flex flex-col my-6 items-start">
    <Accordion type="multiple" class="w-full" collapsible>
      <AccordionItem
        v-for="notice in displayedPosts"
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
            <button
              @click="goUpdate(notice.noticeId)"
              class="text-blue-500 font-semibold mr-6 border rounded-full border-blue-600 px-3 py-1 text-xs ml-4"
            >
              수정하기
            </button>
          </div>
        </AccordionContent>
      </AccordionItem>
    </Accordion>
    <div class="w-full flex justify-center items-center mt-5">
      <!-- v-if="isLogin" 추가 해야함 -->
      <!-- @click="goCreateNotice" 관리자 페이지에 추가 -->
      <button
        type="button"
        @click="goCreateNotice"
        class="py-2.5 px-5 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-full border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700"
      >
        공지사항 작성하기
      </button>
    </div>
    <div class="w-full mt-6 flex justify-center">
      <Pagination
        @page-number="updateCurrentPage"
        :total-page="totalPages"
        :total-post="notices.length"
        :items-per-page="postsPerPage"
      />
    </div>
  </div>
</template>

<style>
.aTag a {
  color: rgb(98, 98, 248);
  text-decoration-line: underline;
}
</style>

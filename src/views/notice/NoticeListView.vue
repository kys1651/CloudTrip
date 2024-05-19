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

// Define the formatDate function within the script setup block
const formatDate = (dateString: string) => {
  const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
  return new Date(dateString).toLocaleDateString(undefined)
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
        <AccordionTrigger
          ><div class="flex justify-between items-center w-full">
            <span>📢 {{ notice.title }}</span>
            <span class="mx-5 text-gray-500 text-sm">{{ formatDate(notice.createdAt) }}</span>
          </div></AccordionTrigger
        >
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
    <div class="w-full flex justify-center items-center mt-5">
      <!-- v-if="isLogin" 추가 해야함 -->
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

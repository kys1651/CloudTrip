<script setup lang="ts">
import BoardList from '@/components/social/BoardList.vue'
import SocialBox from '@/components/social/SocialBox.vue'
import Pagination from '@/components/common/Pagination.vue'
import type { BoardListProps } from '@/types/board.type'
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table'
import { onMounted, ref } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import type { INotice } from '@/types/board.type'

const meetings = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
const verticalScroll = ref<HTMLElement | null>(null)
const verticalScrollLeft = () => {
  if (verticalScroll.value) {
    verticalScroll.value.scrollLeft -= 100
  }
}
const verticalScrollRight = () => {
  if (verticalScroll.value) {
    verticalScroll.value.scrollLeft += 100
  }
}

const addr = 'http://localhost:8080/api/v1/notice/list'
const notices = ref<INotice[]>([])
onMounted(() => {
  axios
    .get(addr)
    .then((response) => {
      console.log(response)
      notices.value = response.data
    })
    .catch((error) => {
      console.log('공지사항 조회 오류', error)
    })
})

const route = useRoute()
const router = useRouter()
const viewNotice = (id: number) => {
  router.push({
    name: 'notice-view',
    params: { noticeId: id }
  })
}

const goCreateNotice = () => {
  router.push({ name: 'notice-create' })
}
</script>

<template>
  <div class="container justify-center flex flex-col my-6 items-start">
    <div class="flex flex-col my-6 items-start w-full">
      <h2 class="text-4xl font-bold mb-3">공지사항</h2>
      <span class="text-gray-400 dark:text-gray-500"
        >내가 관심있는 모임에 참여하여 구성원과 대화를 나눠보세요.</span
      >
      <div class="flex flex-row items-center w-full h-full">
        <i
          class="pi pi-angle-left text-2xl pr-4 h-32 cursor-pointer flex items-center"
          @click="verticalScrollLeft"
        />
        <div
          class="w-full flex flex-row gap-3 overflow-scroll scrollbar-hide scroll-smooth"
          ref="verticalScroll"
        >
          <SocialBox v-for="meeting in meetings" />
        </div>
        <i
          class="pi pi-angle-right text-2xl pl-4 h-32 cursor-pointer flex items-center"
          @click="verticalScrollRight"
        />
      </div>
    </div>
    <div class="w-5/6 mt-6 mx-12">
      <Table>
        <TableCaption>A list of your recent invoices.</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead className="bold text-left">글 번호</TableHead>
            <TableHead className="bold text-left">작성자</TableHead>
            <TableHead className="bold text-left">글 제목</TableHead>
            <TableHead className="text-right">글 작성시기</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="notice in notices" :key="notice.noticeId">
            <TableCell className="font-medium">{{ notice.noticeId }}</TableCell>
            <TableCell>Admin</TableCell>
            <TableCell @click="viewNotice(notice.noticeId)">{{ notice.title }}</TableCell>
            <TableCell className="text-right">{{ notice.createdAt }}</TableCell>
          </TableRow>
        </TableBody>
      </Table>
      <div class="mt-5 flex justify-center">
        <button
          type="button"
          @click="goCreateNotice"
          class="py-2.5 px-5 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-full border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700"
        >
          공지사항 작성하기
        </button>
      </div>
      <div class="mt-10 flex justify-center">
        <Pagination />
      </div>
    </div>
  </div>
</template>

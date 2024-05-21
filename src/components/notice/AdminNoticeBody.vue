<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import {
  TableHead,
  TableRow,
  TableHeader,
  TableCell,
  TableBody,
  Table
} from '@/components/ui/table'
import { Checkbox } from '@/components/ui/checkbox'
import { Badge } from '@/components/ui/badge'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted, computed, onUpdated, watch } from 'vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { toast } from '@/components/ui/toast'
import Pagination from '../common/Pagination.vue'
import { useQuery } from '@tanstack/vue-query'
import { noticeListRequest, noticeUpdate, searchNoticeByKeyword } from '@/api/notice'
import type { INotice } from '@/types/notice.type'

const route = useRoute()
const router = useRouter()
const authentication = useAuthenticationStore()

const {data: initNotices} = useQuery({
  queryKey: ["notices"],
  queryFn: () => noticeListRequest()
})

const isSearch = ref<boolean>(false)
const searchNotice = ref<INotice[]>([])

const notices = computed(() => {
  if ((route.query.page && !searchNotice) || isSearch.value) {
    return searchNotice.value
  }
  return initNotices.value
})

const modifyNotice = (notice: Object) => {
  router.push({ name: 'notice-modify', params: { noticeId: notice.noticeId } })
}

const searchKeyword = ref(route.query.keyword || '')
const searchNotices = () => {
  searchNoticeByKeyword(searchKeyword.value.toString())
    .then((data) => {
      searchNotice.value = data
      isSearch.value = true
    })
    .catch((error) => {
      console.log('특정 공지사항 조회 실패', error)
    })
}

const makeNotice = () => {
  router.push({ name: 'notice-create' })
}

// Paging
watch(
  () => route.query.page,
  (newPage) => {
    pageNumber.value = Number(newPage) || 1
  }
)

const pageNumber = ref<number>(1)
const postsPerPage = ref(10)

const updateCurrentPage = (pageIdx: number) => {
  router.push({ name: 'adminNotice', query: { page: pageIdx } })
}

const displayedPosts = computed(() => {
  if (!notices.value) return []
  const startIndex = (pageNumber.value - 1) * postsPerPage.value
  const endIndex = startIndex + postsPerPage.value
  return notices.value.slice(startIndex, endIndex)
})

const totalPages = computed(() => {
  if (!notices.value) return 0
  console.log(notices.value.length + ' ' + postsPerPage.value)
  return Math.ceil(notices.value.length / postsPerPage.value)
})

// Define the formatDate function within the script setup block
const formatDate = (dateString: string) => {
  const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
  return new Date(dateString).toLocaleDateString(undefined)
}
</script>

<template>
  <div className="flex items-center justify-between mb-6">
    <div>
      <h1 className="text-2xl font-semibold">공지사항 관리</h1>
      <p className="text-gray-600">공지사항을 관리할 수 있습니다.</p>
    </div>
    <div className="flex items-center space-x-4">
      <Input
        class="border border-black w-64"
        className="w-64 border rounded-md p-2"
        placeholder="공지사항 검색"
        v-model="searchKeyword"
      />
      <Button class="border border-black" variant="outline" @click="searchNotices">검색</Button>
      <Button class="border border-black" variant="outline" @click="makeNotice"
        >공지사항 등록하기</Button
      >
    </div>
  </div>
  <Table>
    <TableHeader>
      <TableRow>
        <TableHead className="w-[50px]" />
        <TableHead className="text-left">공지사항 ID </TableHead>
        <TableHead>공지사항 제목</TableHead>
        <TableHead>게시 날짜</TableHead>
        <TableHead className="w-[50px]" />
      </TableRow>
    </TableHeader>
    <TableBody>
      <TableRow v-for="notice in displayedPosts" :key="notice.noticeId">
        <TableCell>
          <Checkbox id="task-8782" />
        </TableCell>
        <TableCell className="font-medium">{{ notice.noticeId }}</TableCell>
        <TableCell className="font-medium">{{ notice.title }}</TableCell>
        <TableCell className="font-medium">{{ formatDate(notice.createdAt) }}</TableCell>
        <TableCell>
          <Button @click="modifyNotice(notice)" class="modify-button">공지사항 수정</Button>
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
  <div class="w-full mt-6 flex justify-center">
    <Pagination
      @page-number="updateCurrentPage"
      :total-page="totalPages.toString()"
      :total-post="notices?.length.toString()"
      :items-per-page="postsPerPage.toString()"
    />
  </div>
</template>

<style scoped>
.deactivate-button {
  background-color: rgb(248, 81, 81);
  color: white;
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: none; /* Remove default border */
  height: 25px;
}

.deactivate-button:hover {
  background-color: darkred;
}

.activate-button {
  background-color: green;
  color: white;
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: none; /* Remove default border */
  height: 25px;
}

.activate-button:hover {
  background-color: darkgreen;
}

.modify-button {
  background-color: white;
  color: rgb(81, 112, 248);
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: solid 1px; /* Remove default border */
  height: 25px;
}

.modify-button:hover {
  background-color: rgb(67, 78, 126);
}
</style>

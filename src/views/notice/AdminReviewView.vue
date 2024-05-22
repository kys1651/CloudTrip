<script setup lang="ts">
import { computed, ref } from 'vue'
import {
  TableHead,
  TableRow,
  TableHeader,
  TableCell,
  TableBody,
  Table
} from '@/components/ui/table'
import { toDateTime } from '@/lib/formatter'
import Input from '@/components/ui/input/Input.vue'
import Button from '@/components/ui/button/Button.vue'
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { reviewAllDeleteRequest, reviewAllRequest, reviewSearchRequest } from '@/api/review'
import Checkbox from '@/components/ui/checkbox/Checkbox.vue'
import { useRouter } from 'vue-router'
import Pagination from '@/components/common/Pagination.vue'
import { toast } from '@/components/ui/toast'
import type { IReview } from '@/types/board.type'

// 초기 리뷰 데이터를 가져오는 useQuery 훅
const { data: reviews, refetch: refetchReviews } = useQuery({
  queryKey: ['adminReviews', 'allReviews'],
  queryFn: () => reviewAllRequest(),
  initialData: () => [] // 초기 데이터를 빈 배열로 설정
})

const router = useRouter()
const detail = (reviewId: number) => {
  router.push({ name: 'review-detail', params: { id: reviewId } })
}

const queryClient = useQueryClient()
const currentPage = ref(1)
const itemsPerPage = ref(10)
const searchKeyword = ref<string>('')
const searchResults = ref<IReview[] | null>(null) // 검색 결과를 저장할 상태, 초기값은 null

const paginatedReviews = computed(() => {
  const reviewList = searchResults.value !== null ? searchResults.value : reviews.value ?? []
  if (!Array.isArray(reviewList)) return []
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return reviewList.slice(start, end)
})

const totalPages = computed(() => {
  const reviewList = searchResults.value !== null ? searchResults.value : reviews.value ?? []
  if (!Array.isArray(reviewList)) return 0
  return Math.ceil(reviewList.length / itemsPerPage.value)
})

const checked = ref<boolean[]>(Array(itemsPerPage.value).fill(false))
const updateCurrentPage = (pageNumber: number) => {
  checked.value = Array(itemsPerPage.value).fill(false)
  currentPage.value = pageNumber
}

const { mutate: deleteCheckedAll } = useMutation({
  mutationKey: ['adminReviews', 'delete'],
  mutationFn: (reviewIds: number[]) => reviewAllDeleteRequest(reviewIds),
  onSuccess: () => {
    checked.value = Array(itemsPerPage.value).fill(false)
    if (searchKeyword.value.trim().length == 0) {
      queryClient.invalidateQueries({
        queryKey: ['adminReviews', 'allReviews']
      })
    } else {
      keywordSearch(searchKeyword.value)
    }

    refetchReviews() // 리뷰 목록 새로고침
    toast({
      title: '여행지 후기 목록 삭제',
      description: '여행지 후기 목록을 삭제했습니다.',
      variant: 'success'
    })
  }
})

const checkedDelete = () => {
  const selectedReviewIds = paginatedReviews.value
    .filter((item, idx) => checked.value[idx])
    .map((item) => item.reviewId)
  deleteCheckedAll(selectedReviewIds)
}

const { mutate: keywordSearch } = useMutation({
  mutationKey: ['adminReviews', 'search'],
  mutationFn: (keyword: string) => reviewSearchRequest(keyword),
  onSuccess: (data) => {
    searchResults.value = data // 검색 결과를 상태에 저장
    currentPage.value = 1 // 검색 결과 표시 시 페이지를 초기화
  }
})

const search = () => {
  if (searchKeyword.value.trim() === '') {
    searchResults.value = null // 검색어가 없으면 검색 결과를 초기화
    refetchReviews() // 전체 리뷰를 다시 불러옴
  } else {
    keywordSearch(searchKeyword.value)
  }
  currentPage.value = 1
}
</script>

<template>
  <div class="container mx-auto my-8 p-6 bg-white shadow rounded-lg">
    <!-- 헤더 -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-semibold">리뷰 관리</h1>
        <p class="text-gray-600">리뷰를 관리할 수 있습니다.</p>
      </div>
      <!-- 검색창 -->
      <div class="flex items-center space-x-4">
        <Input
          @keyup.enter="search"
          class="w-64 border rounded-md p-2"
          placeholder="내용 키워드를 입력해주세요"
          v-model="searchKeyword"
        />
        <Button variant="outline" @click="search">검색</Button>
      </div>
    </div>
    <!-- 헤더 종료 -->

    <!-- 삭제 버튼 -->
    <div class="flex justify-end mb-4 mt-4">
      <Button @click="checkedDelete"> 선택된 리뷰 삭제 </Button>
    </div>

    <!-- 리뷰 목록 시작 -->
    <Table class="w-full">
      <!-- 리뷰 헤더 -->
      <TableHeader>
        <TableRow>
          <TableHead class="w-[50px] text-center whitespace-nowrap">선택</TableHead>
          <TableHead class="text-center whitespace-nowrap">ID</TableHead>
          <TableHead class="text-center whitespace-nowrap">내용</TableHead>
          <TableHead class="text-center whitespace-nowrap">등록 날짜</TableHead>
        </TableRow>
      </TableHeader>
      <!-- 리뷰 헤더 종료 -->
      <!-- 리뷰 목록 출력 -->
      <TableBody>
        <TableRow v-for="(review, idx) in paginatedReviews" :key="review.reviewId">
          <TableCell class="text-center whitespace-nowrap">
            <Checkbox v-model:checked="checked[idx]" />
          </TableCell>
          <TableCell class="font-medium text-center whitespace-nowrap">
            {{ review.reviewId }}
          </TableCell>
          <TableCell class="font-medium">
            <p @click="detail(review.reviewId)">{{ review.content }}</p>
          </TableCell>
          <TableCell class="font-medium text-center whitespace-nowrap">
            {{ toDateTime(new Date(review.createdAt)) }}
          </TableCell>
        </TableRow>
        <TableRow v-if="!paginatedReviews.length">
          <TableCell colspan="4" class="text-center">리뷰가 없습니다.</TableCell>
        </TableRow>
      </TableBody>
      <!-- 리뷰 목록 출력 종료 -->
    </Table>
    <!-- 리뷰 목록 종료 -->

    <!-- 리뷰 페이징 처리 -->
    <div class="w-full mt-6 flex justify-center">
      <Pagination
        @page-number="updateCurrentPage"
        :total-page="totalPages"
        :total-post="searchResults !== null ? searchResults.length : reviews?.length || 0"
        :items-per-page="itemsPerPage"
      />
    </div>
    <!-- 리뷰 페이징 종료 -->
  </div>
</template>

<style scoped>
p {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  word-break: keep-all;
}
</style>

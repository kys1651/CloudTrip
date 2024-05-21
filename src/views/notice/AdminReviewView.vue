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
import { reviewAllDeleteRequest, reviewAllRequest } from '@/api/review'
import Checkbox from '@/components/ui/checkbox/Checkbox.vue'
import { useRouter } from 'vue-router'
import Pagination from '@/components/common/Pagination.vue'
import { toast } from '@/components/ui/toast'

const searchKeyword = ref<string>('')

const { data: reviews } = useQuery({
  queryKey: ['reviews'],
  queryFn: () => reviewAllRequest()
})

const router = useRouter()
const detail = (reviewId: number) => {
  router.push({ name: 'review-detail', params: { id: reviewId } })
}

const queryClient = useQueryClient()
const currentPage = ref(1)
const itemsPerPage = ref(10)
const selectedReviews = ref<Set<number>>(new Set())

const paginatedReviews = computed(() => {
  if (!reviews.value) return []
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return reviews.value.slice(start, end)
})

const totalPages = computed(() => {
  if (!reviews.value) return 0
  return Math.ceil(reviews.value.length / itemsPerPage.value)
})

const updateCurrentPage = (pageNumber: number) => {
  currentPage.value = pageNumber
}

const toggleSelectReview = (reviewId: number, isChecked: boolean) => {
  if (isChecked) {
    selectedReviews.value.add(reviewId)
  } else {
    selectedReviews.value.delete(reviewId)
  }
}

const selectedReviewList = computed(() => {
  return reviews.value?.filter((review) => selectedReviews.value.has(review.reviewId)) || []
})

const { mutate: deleteCheckedAll } = useMutation({
  mutationKey: ['reviews'],
  mutationFn: () => reviewAllDeleteRequest(Array.from(selectedReviews.value)),
  onSuccess: () => {
    selectedReviews.value.clear()
    queryClient.invalidateQueries({
      queryKey: ['reviews']
    })
    toast({
      title: '여행지 후기 목록 삭제',
      description: '여행지 후기 목록을 삭제했습니다.',
      variant: 'success'
    })
    queryClient.invalidateQueries({
      queryKey: ['reviews']
    })
  }
})

const checkedDelete = () => {
  deleteCheckedAll()
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
        <Input class="w-64 border rounded-md p-2" placeholder="리뷰 검색" v-model="searchKeyword" />
        <Button variant="outline" @click="console.log('검색')">검색</Button>
      </div>
    </div>
    <!-- 헤더 종료 -->

    <!-- 삭제 버튼 -->
    <div class="flex justify-end mb-4 mt-4">
      <Button @click="checkedDelete" :disabled="selectedReviewList.length === 0"
        >선택된 리뷰 삭제</Button
      >
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
        <TableRow v-for="review in paginatedReviews" :key="review.reviewId">
          <TableCell class="text-center whitespace-nowrap">
            <input
              type="checkbox"
              :checked="selectedReviews.has(review.reviewId)"
              @change="
                (event: Event) =>
                  toggleSelectReview(review.reviewId, (event.target as HTMLInputElement).checked)
              "
            />
          </TableCell>
          <TableCell class="font-medium text-center whitespace-nowrap">{{
            review.reviewId
          }}</TableCell>
          <TableCell class="font-medium">
            <p @click="detail(review.reviewId)">{{ review.content }}</p>
          </TableCell>
          <TableCell class="font-medium text-center whitespace-nowrap">{{
            toDateTime(new Date(review.createdAt))
          }}</TableCell>
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
        :total-post="reviews?.length || 0"
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

<script setup lang="ts">
import {
  commentCreateRequest,
  commentsRequest,
  reviewDisLikeRequest,
  reviewLikeRequest,
  reviewRequest
} from '@/api/review'
import IconReviewRating from '@/components/icons/IconReviewRating.vue'
import Avatar from '@/components/ui/avatar/Avatar.vue'
import AvatarFallback from '@/components/ui/avatar/AvatarFallback.vue'
import AvatarImage from '@/components/ui/avatar/AvatarImage.vue'
import Button from '@/components/ui/button/Button.vue'
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious
} from '@/components/ui/carousel'
import Carousel from '@/components/ui/carousel/Carousel.vue'
import Input from '@/components/ui/input/Input.vue'
import Separator from '@/components/ui/separator/Separator.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { useReview } from '@/stores/review'
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { Heart, MapPin, Share } from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const reviewStore = useReview()
const { review: storedReview } = storeToRefs(reviewStore)
const reviewId = Number(route.params.id)
const authenticationStore = useAuthenticationStore()
const { profile, isLogin } = storeToRefs(authenticationStore)
const queryClient = useQueryClient()

const comment = ref<string>('')

const { data: review, isLoading } = useQuery({
  queryKey: ['reviews', reviewId],
  queryFn: () => reviewRequest(reviewId),
  initialData: {
    reviewId: storedReview.value.reviewId,
    content: storedReview.value.content,
    tourName: storedReview.value.tourName,
    tourId: storedReview.value.tourId,
    address: storedReview.value.address,
    images: storedReview.value.images,
    user: {
      userId: storedReview.value.user.userId,
      nickname: storedReview.value.user.nickname,
      profileImage: storedReview.value.user.profileImage
    },
    isLiked: storedReview.value.isLiked,
    likeCount: 0,
    rating: 0,
    createdAt: storedReview.value.createdAt,
    updatedAt: 0
  }
})

const { data: comments } = useQuery({
  queryKey: ['reviews', reviewId, 'comments'],
  queryFn: () => commentsRequest(reviewId),
  initialData: []
})

const isLiked = ref<boolean>(storedReview.value.isLiked)

watch(review, (newReview) => {
  isLiked.value = newReview.isLiked
})

const { mutate: mutateLike } = useMutation({
  mutationKey: ['reviews', 'like', reviewId],
  mutationFn: () => (isLiked.value ? reviewDisLikeRequest(reviewId) : reviewLikeRequest(reviewId)),
  onSuccess: () => {
    isLiked.value = !isLiked.value
    review.value.likeCount += isLiked.value ? 1 : -1
  },
  onSettled: () => {
    queryClient.invalidateQueries(['reviews', reviewId])
  }
})

const { mutate: mutateComment } = useMutation({
  mutationKey: ['reviews', 'comments', reviewId],
  mutationFn: (content: string) => commentCreateRequest(reviewId, content),
  onSuccess: () => {
    queryClient.invalidateQueries(['reviews', reviewId, 'comments'])
    comment.value = ''
  }
})

const onSubmit = () => {
  if (comment.value.length === 0) return
  mutateComment(comment.value)
}
</script>

<template>
  <!-- 전체 컨테이너 -->
  <div class="flex flex-col md:flex-row gap-6 max-w-6xl mx-auto py-8 px-4">
    <div class="flex-1">
      <div class="flex justify-between mb-4 text-sm text-gray-500 dark:text-gray-400">
        <!-- 주소 및 위치 출력 -->
        <div>
          <MapPin class="w-4 h-4 mr-1 inline" />
          <span>{{ review.tourName }} - {{ review.address }}</span>
        </div>
        <div class="flex items-center">
          <IconReviewRating :filled="true" />
          ({{ review.rating }})
        </div>
      </div>
      <!-- 디테일 뷰 좌측 부분 -->
      <div class="grid gap-6">
        <!-- 사진 출력 -->
        <Carousel class="w-full h-[70vh] m-auto flex justify-center flex-shrink">
          <CarouselContent class="h-full">
            <CarouselItem
              v-for="(image, index) in review.images"
              :key="index"
              class="flex justify-center items-center w-full h-full"
            >
              <img
                class="rounded-md w-full h-full object-contain"
                :src="image"
                alt="Review Image"
              />
            </CarouselItem>
          </CarouselContent>
          <CarouselPrevious class="-left-4" />
          <CarouselNext class="-right-4" />
        </Carousel>
        <!-- 사진 출력 종료 -->
        <Separator />
        <!-- 하단 컨텐츠 시작 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-4">
            <div class="flex items-center">
              <Button size="icon" variant="ghost" @click="mutateLike">
                <Heart v-if="isLiked" fill="red" stroke="red" :size="24" />
                <Heart v-else :size="24" />
              </Button>
              <div v-if="review.likeCount != 0" class="ml-1 text-sm font-semibold">
                ( {{ review.likeCount }} )
              </div>
            </div>
            <Button size="icon" variant="ghost">
              <Share :size="24" />
              <span class="sr-only">Share</span>
            </Button>
          </div>
        </div>
        <!-- 하단 컨텐츠 종료 -->

        <!-- 리뷰 내용 및 날짜 출력 -->
        <div class="relative ml-3">
          <p class="text-lg mb-5">{{ review.content }}</p>
          <div class="absolute bottom-0 right-0 text-sm text-gray-500 dark:text-gray-400 mt-4">
            {{ new Date(review.createdAt).toLocaleDateString() }}
          </div>
        </div>
      </div>
    </div>
    <div
      class="w-full md:w-[300px] lg:w-[350px] bg-gray-100 dark:bg-gray-800 rounded-lg overflow-hidden relative"
    >
      <div class="p-4 border-b border-gray-200 dark:border-gray-700">
        <div @click="router.push(`/user/${review.user.userId}`)" class="flex items-center gap-4">
          <Avatar class="w-10 h-10 border">
            <AvatarImage alt="@shadcn" :src="review.user.profileImage" />
            <AvatarFallback>{{ review.user.userId }}</AvatarFallback>
          </Avatar>
          <div>
            <h4 class="font-medium">{{ review.user.nickname }}</h4>
          </div>
        </div>
      </div>
      <div
        class="px-4 py-2 max-h-[520px] flex-grow overflow-scroll scrollbar-hide relative space-y-2"
      >
        <div v-for="comment in comments" :key="comment.commentId">
          <div class="flex items-center gap-1 pb-1">
            <Avatar class="w-7 h-7 border" @click="router.push(`/user/${comment.user.userId}`)">
              <AvatarImage alt="@shadcn" :src="comment.user.profileImage" />
              <AvatarFallback>{{ comment.user.userId }}</AvatarFallback>
            </Avatar>
            <div class="space-y-1">
              <div class="flex items-center gap-2">
                <h5 class="font-medium text-sm">{{ comment.user.nickname }}</h5>
                <span class="text-xs text-gray-500 dark:text-gray-400">{{
                  new Date(comment.createdAt).toLocaleDateString()
                }}</span>
              </div>
            </div>
          </div>
          <p class="px-2 text-xs text-gray-400">{{ comment.content }}</p>
        </div>
      </div>
      <div v-if="isLogin" class="absolute bottom-4 w-full">
        <Separator class="w-full" />
        <div class="flex pt-3 flex-row w-full space-x-2 h-10 items-center px-2">
          <Avatar class="w-10 h-10 border">
            <AvatarImage alt="프로필 이미지" :src="profile?.profileImage!!" />
            <AvatarFallback>{{ profile?.id }}</AvatarFallback>
          </Avatar>
          <Input
            type="text"
            class="flex-1 w-full bg-transparent border-none focus:border-none focus:ring-0 focus-visible:ring-0 focus-visible:ring-offset-0"
            placeholder="댓글을 입력해주세요."
            v-model="comment"
            @keyup.enter="onSubmit"
          />
          <span
            class="text-sm pr-2"
            :class="comment.length > 0 ? 'text-blue-500 font-bold cursor-pointer' : 'text-gray-400'"
            >작성</span
          >
        </div>
      </div>
    </div>
  </div>
</template>

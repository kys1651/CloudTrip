<script setup lang="ts">
import { reviewDeleteRequest, reviewDisLikeRequest, reviewLikeRequest } from '@/api/review'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious
} from '@/components/ui/carousel'
import CardDropdownMenu from './CardDropdownMenu.vue'
import { useReview } from '@/stores/review'
import type { IReview } from '@/types/board.type'
import { useMutation, useQueryClient } from '@tanstack/vue-query'
import { Bookmark, Heart, MapPin, MessageCircle, Send } from 'lucide-vue-next'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import CommonAvatar from '../common/CommonAvatar.vue'
import Carousel from '../ui/carousel/Carousel.vue'
import IconReviewRating from '../icons/IconReviewRating.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { toast } from '../ui/toast'
const props = defineProps<{
  review: IReview
}>()
const queryClient = useQueryClient()
const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const reviewStore = useReview()

const router = useRouter()

const isLiked = ref<boolean>(props.review.isLiked)

const { mutate } = useMutation({
  mutationKey: ['reviews', 'like', props.review.reviewId],
  mutationFn: () =>
    isLiked.value
      ? reviewDisLikeRequest(props.review.reviewId)
      : reviewLikeRequest(props.review.reviewId),
  onSuccess: () => {
    isLiked.value = !isLiked.value
  }
})

const pushRouter = () => {
  reviewStore.updateReview(props.review)
  router.push({ name: 'review-detail', params: { id: props.review.reviewId } })
}

const deleteReview = async () => {
  if (!confirm('정말 여행지 후기를 삭제하시겠습니까? ')) {
    return
  }
  const isSuccess = await reviewDeleteRequest(props.review.reviewId)
  if (isSuccess) {
    toast({
      title: '여행지 후기 삭제',
      description: '여행지 후기 삭제가 완료되었습니다.',
      variant: 'success'
    })
    queryClient.invalidateQueries(['reviews'])
  } else {
    toast({
      title: '여행지 후기 삭제',
      description: '여행지 후기 삭제가 실패하였습니다. 잠시 후 다시 시도해주세요',
      variant: 'destructive'
    })
  }
}
</script>

<template>
  <Card class="rounded-md shadow border w-full max-w-[500px] border-gray-200">
    <CardHeader class="p-4 flex flex-row items-center justify-between">
      <a
        class="flex items-center gap-2 text-sm font-semibold"
        @click="router.push(`/user/${review.user.userId}`)"
      >
        <CommonAvatar :src="review.user.profileImage" :username="review.user.nickname" />
        <span>{{ review.user.nickname }}</span>
      </a>
      <CardDropdownMenu
        v-if="profile?.id !== review.user.userId"
        @edit="console.log('hi')"
        @delete="deleteReview"
      />
    </CardHeader>

    <CardContent class="p-0">
      <Carousel class="w-full h-96 m-auto flex justify-center flex-shrink">
        <CarouselContent class="h-full">
          <CarouselItem
            v-for="(image, index) in review.images"
            :key="index"
            class="flex justify-center items-center w-full h-full"
            @click="pushRouter"
          >
            <img class="rounded-md w-full h-full object-contain" :src="image" alt="Review Image" />
          </CarouselItem>
        </CarouselContent>
        <CarouselPrevious class="-left-4" />
        <CarouselNext class="-right-4" />
      </Carousel>
    </CardContent>
    <CardFooter class="grid py-2 px-4">
      <div class="flex items-center w-full">
        <Button size="icon" variant="ghost" @click="mutate">
          <Heart v-if="isLiked" :size="20" fill="red" stroke="red" />
          <Heart v-else :size="20" />
        </Button>
        <Button size="icon" variant="ghost" @click="pushRouter">
          <MessageCircle :size="20" />
        </Button>
        <Button size="icon" variant="ghost">
          <Send :size="20" />
        </Button>
        <div class="flex items-center text-xs text-gray-500">
          <IconReviewRating
            :filled="true"
            class="ml-2"
            :stroke="'black'"
            :stroke-width="0.8"
            :size="20"
          />
          ({{ review.rating }})
        </div>
        <Button class="ml-auto" size="icon" variant="ghost">
          <Bookmark :size="20" />
        </Button>
      </div>
      <div class="flex flex-col cursor-pointer" @click="pushRouter">
        <p class="text-sm text-gray-600">
          {{ review.content }}
        </p>
        <div class="flex my-2 flex-row justify-between items-center text-gray-400">
          <div class="flex flex-row">
            <MapPin :size="14" />
            <span class="text-xs text-gray-500"> {{ review.tourName }} - {{ review.address }}</span>
          </div>
          <span class="text-xs">{{ new Date(review.createdAt).toLocaleDateString() }}</span>
        </div>
      </div>
    </CardFooter>
  </Card>
</template>

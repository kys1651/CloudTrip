<template>
  <div class="container">
    <!-- 리뷰 부분 -->
    <div class="grid grid-cols-1 gap-4 p-4">
      <div class="flex flex-col my-6 items-start w-full">
        <h2 class="text-4xl font-bold mb-3">여행지 후기</h2>
      </div>
      <div v-if="review" class="rounded-lg overflow-hidden shadow-lg">
        <!-- 사진 -->
        <img
          v-if="review.imgUrls.length > 0"
          :src="review.imgUrls[0]"
          :alt="review.nickname"
          width="400"
          height="500"
          class="w-full h-80 object-cover"
          style="aspect-ratio: 400 / 500; object-fit: cover"
        />
        <div class="p-4">
          <!-- 사용자 정보 -->
          <div class="flex items-center mb-2">
            <Avatar>
              <AvatarImage
                :src="`https://api.multiavatar.com/user-${review.createdAt}.svg`"
                alt="@radix-vue"
              />
            </Avatar>
            <a class="font-semibold text-sm ms-3" href="#">
              {{ review.nickname ?? 'AK47맞고사랑한성민이' }}
            </a>
            <span class="text-gray-400 dark:text-gray-500 ms-4">{{ review.tourId }}</span>
          </div>
          <!-- Content 출력 -->
          <!-- 사용자 후기 -->
          <div class="user-review p-4">
            <p>{{ review.content }}</p>
          </div>

          <!-- 버튼 출력 -->
          <div class="mb-2">
            <div class="flex items-center gap-2">
              <button
                class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 w-10"
              >
                <Heart />
                <span class="sr-only">Like</span>
              </button>
              <button
                class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 w-10"
              >
                <MessageCircle />
                <span class="sr-only">Comment</span>
              </button>
            </div>
            <!-- 댓글 출력창 시작 -->
            <div
              data-radix-scroll-area-viewport=""
              class="h-full w-full rounded-[inherit]"
              style="overflow: hidden scroll"
            >
              <!-- 댓글 출력 시작 -->
              <div style="min-width: 100%; display: table">
                <div class="p-4 flex flex-col gap-4">
                  <!-- 댓글 출력 -->
                  <ReviewComment
                    v-for="comment in comments"
                    :key="comment.commentId"
                    :comment="comment"
                  />
                  <!-- 댓글 출력 종료 -->
                </div>
              </div>
              <!-- 댓글 출력 창 종료 -->
            </div>
            <!-- 댓글 입력창 -->
            <form v-if="isLogin" class="flex items-start gap-2">
              <!-- 현재 로그인한 유저의 프로필 이미지 -->
              <Avatar>
                <AvatarImage src="https://github.com/radix-vue.png" alt="@radix-vue" />
              </Avatar>
              <!-- 현재 로그인한 유저의 프로필 이미지 종료 -->

              <!-- 댓글 입력창 - 로그인 할 때만 보이게 -->
              <input
                v-model="commentForm.content"
                ref="commentInput"
                id="comment"
                class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 flex-1"
                placeholder="Write a comment..."
              />
              <button
                @click.prevent="create"
                class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2"
              >
                <Send />
                <span class="sr-only">Send</span>
              </button>
              <!-- 댓글 입력창 종료-->
            </form>
            <!-- 댓글 출력창 종료 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Avatar, AvatarImage } from '@/components/ui/avatar'
import { Heart, MessageCircle, Send } from 'lucide-vue-next'
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import ReviewComment from '../review/ReviewComment.vue'
import { useMutation, useQuery } from '@tanstack/vue-query'
import { reviewRequest } from '@/api/review'
import type { ReviewDetail } from '@/types/board.type'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import type { ReviewCommentForm, ReviewCommentDetail } from '@/types/board.type'
import { commentsRequest, commentWriteRequest } from '@/api/comment'
import { useToast } from '@/components/ui/toast'
import { useQueryClient } from '@tanstack/vue-query'

const authentication = useAuthenticationStore()
const { isLogin, profile } = storeToRefs(authentication)

const toast = useToast()
const route = useRoute()
const id = route.params.id
const queryClient = useQueryClient()

const commentForm = ref<ReviewCommentForm>({
  userId: Number(profile.value?.id),
  reviewId: Number(id),
  content: ''
})
const create = () => {
  if (!commentForm.value.content) return
  mutate(commentForm.value)
}
const { mutate } = useMutation({
  mutationKey: ['reviews', id, 'comments', 'create'],
  mutationFn: commentWriteRequest,
  onSuccess: () => {
    toast.toast({
      title: '댓글 작성 성공',
      description: '댓글 작성이 완료되었습니다.',
      duration: 2000,
      variant: 'success'
    })
    queryClient.invalidateQueries(['reviews', id, 'comments'])
    commentForm.value.content = ''
  },
  onError: (error: any) => {
    const {
      response: {
        data: { message }
      }
    } = error
    toast.toast({
      title: '댓글 작성이 실패하였습니다.',
      description: message,
      duration: 2000,
      variant: 'destructive'
    })
  }
})

const { data: review } = useQuery<ReviewDetail>({
  queryKey: ['reviews', id],
  queryFn: () => reviewRequest(Number(id))
})
const { data: comments } = useQuery<ReviewCommentDetail[]>({
  queryKey: ['reviews', id, 'comments'],
  queryFn: () => commentsRequest(Number(id))
})
</script>

<style scoped>
.name-info {
  display: flex;
  align-items: center; /* 세로 중앙 정렬을 확보 */
}
</style>

<script setup lang="ts">
import {
  commentCreateRequest,
  commentsRequest,
  reviewDisLikeRequest,
  reviewLikeRequest,
  reviewRequest,
} from "@/api/review";
import { followRequest, unFollowRequest } from "@/api/user";
import IconReviewRating from "@/components/icons/IconReviewRating.vue";
import ReviewComment from "@/components/review/ReviewComment.vue";
import ReviewDropdownMenu from "@/components/review/ReviewDropdownMenu.vue";
import ReviewShare from "@/components/review/ReviewShare.vue";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import Button from "@/components/ui/button/Button.vue";
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import Carousel from "@/components/ui/carousel/Carousel.vue";
import Input from "@/components/ui/input/Input.vue";
import Separator from "@/components/ui/separator/Separator.vue";
import { toast } from "@/components/ui/toast";
import { useAuthenticationStore } from "@/stores/authentication";
import { useReview } from "@/stores/review";
import { useMutation, useQuery, useQueryClient } from "@tanstack/vue-query";
import { Heart, MapPin } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { nextTick, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const reviewStore = useReview();
const { review: storedReview } = storeToRefs(reviewStore);
const reviewId = Number(route.params.id);
const authenticationStore = useAuthenticationStore();
const { profile, isLogin } = storeToRefs(authenticationStore);
const queryClient = useQueryClient();

const comment = ref<string>("");

const { data: review } = useQuery({
  queryKey: ["reviews", reviewId],
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
      profileImage: storedReview.value.user.profileImage,
    },
    isLiked: storedReview.value.isLiked,
    isFollowing: storedReview.value.isFollowing,
    likeCount: 0,
    rating: 0,
    createdAt: storedReview.value.createdAt,
    updatedAt: 0,
  },
});

const { data: comments } = useQuery({
  queryKey: ["reviews", reviewId, "comments"],
  queryFn: () => commentsRequest(reviewId),
  initialData: [],
});

const isLiked = ref<boolean>(storedReview.value.isLiked);
watch(review, (newReview) => {
  isLiked.value = newReview.isLiked;
});

const { mutate: mutateLike } = useMutation({
  mutationKey: ["reviews", "like", reviewId],
  mutationFn: () =>
    isLiked.value ? reviewDisLikeRequest(reviewId) : reviewLikeRequest(reviewId),
  onSuccess: () => {
    isLiked.value = !isLiked.value;
    queryClient.invalidateQueries({
      queryKey: ["reviews", reviewId],
    });
  },
});

const isFollowing = ref<boolean>(storedReview.value.isFollowing);
watch(review, (newReview) => {
  isFollowing.value = newReview.isFollowing;
});

const { mutate: followMutate } = useMutation({
  mutationKey: ["reviews", "follow", review.value.user.userId],
  mutationFn: () =>
    isFollowing.value
      ? unFollowRequest(review.value.user.userId)
      : followRequest(review.value.user.userId),
  onSuccess: () => {
    isFollowing.value = !isFollowing.value;
    queryClient.invalidateQueries({
      queryKey: ["reviews", "follow", review.value.user.userId],
    });
    const action = isFollowing.value ? "팔로우" : "언팔로우";
    toast({
      title: `${action} 성공`,
      description: `${review.value.user.nickname}을/를 ${action} 하였습니다.`,
      variant: "success",
    });
  },
});

const { mutate: mutateComment } = useMutation({
  mutationKey: ["reviews", "comments", reviewId],
  mutationFn: (content: string) => commentCreateRequest(reviewId, content),
  onSuccess: () => {
    queryClient.invalidateQueries({
      queryKey: ["reviews", reviewId, "comments"],
    });
    comment.value = "";
  },
});

const onSubmit = () => {
  if (comment.value.trim().length === 0) return;
  mutateComment(comment.value);
};

// 댓글 데이터를 감시하여 변경될 때마다 스크롤을 맨 아래로 이동
watch(comments, (newComments, oldComments) => {
  if (newComments.length !== oldComments.length) {
    nextTick(() => {
      scrollToBottom();
    });
  }
});

const commentsContainer = ref<HTMLElement | null>(null);
// 스크롤을 맨 아래로 이동시키는 함수
const scrollToBottom = () => {
  if (commentsContainer.value) {
    commentsContainer.value.scrollTop = commentsContainer.value.scrollHeight;
  }
};
</script>

<template>
  <!-- 전체 컨테이너 -->
  <div class="flex flex-col md:flex-row gap-6 max-w-6xl mx-auto py-8 px-4">
    <div class="flex-1">
      <!-- 디테일 뷰 좌측 부분 -->
      <div class="flex justify-between mb-4 text-sm text-gray-500 dark:text-gray-400">
        <!-- 디테일 뷰 좌측 상단 -->
        <div class="flex flex-col">
          <span class="text-sm text-gray-800">{{ review.tourName }}</span>
          <span class="flex items-center text-xs text-gray-600">
            <MapPin class="w-3 h-3 mr-1 text-gray-500" />{{ review.address }}
          </span>
        </div>
        <ReviewDropdownMenu
          v-if="
            profile && (profile.id === review.user.userId || profile.roleType == 'ADMIN')
          "
          :reviewId="review.reviewId"
        />
      </div>
      <!-- 디테일 뷰 좌측 컨텐츠 부분 -->
      <div class="grid gap-2">
        <!-- 사진 출력 -->
        <Carousel class="w-full h-[50vh] m-auto flex justify-center flex-shrink">
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
                <Heart v-else :size="20" />
              </Button>
              <div v-if="review.likeCount != 0" class="ml-1 text-sm font-semibold">
                ( {{ review.likeCount }} )
              </div>
            </div>
            <Button size="icon" variant="ghost">
              <ReviewShare :review="review" />
            </Button>
          </div>
          <div class="flex items-center text-xs text-gray-500">
            <IconReviewRating
              :filled="true"
              class="ml-2"
              :stroke="'gray'"
              :stroke-width="0.3"
              :size="20"
            />
            ({{ review.rating }})
          </div>
        </div>
        <!-- 하단 컨텐츠 종료 -->

        <!-- 리뷰 내용 및 날짜 출력 -->
        <div class="relative ml-3">
          <p class="text-sm mb-5">{{ review.content }}</p>
          <div
            class="absolute bottom-0 right-0 text-sm text-gray-500 dark:text-gray-400 mt-4"
          >
            {{ new Date(review.createdAt).toLocaleDateString() }}
          </div>
        </div>
      </div>
    </div>
    <!-- 우측 컨텐츠 -->
    <div
      class="w-full md:w-[300px] lg:w-[350px] bg-gray-100 dark:bg-gray-800 rounded-lg overflow-hidden relative"
    >
      <div class="p-3 border-b border-gray-200 dark:border-gray-700">
        <!-- 작성자 정보 출력 -->
        <div class="flex items-center justify-between">
          <div
            @click="router.push(`/user/${review.user.userId}`)"
            class="flex items-center gap-4 cursor-pointer"
          >
            <Avatar class="w-10 h-10 border">
              <AvatarImage :alt="review.user.nickname" :src="review.user.profileImage" />
              <AvatarFallback>{{ review.user.userId }}</AvatarFallback>
            </Avatar>
            <div>
              <h4 class="font-medium">{{ review.user.nickname }}</h4>
            </div>
          </div>
          <Button
            @click="followMutate"
            v-if="profile && review.user.userId !== profile.id"
            class="mr-auto ml-2"
            :variant="isFollowing ? 'outline' : 'default'"
            size="xs"
            >{{ isFollowing ? "팔로잉" : "팔로우" }}
          </Button>
        </div>
      </div>

      <!-- 작성자 정보 출력 종료 -->
      <div class="text-sm m-2 text-gray-500 dark:text-gray-400">
        댓글({{ comments.length }}개)
      </div>
      <!-- 댓글 창 시작 -->
      <div
        class="comments-container px-4 py-2 max-h-[720px] flex-grow overflow-scroll scrollbar-hide relative space-y-2"
        ref="commentsContainer"
      >
        <!-- 댓글 출력 -->
        <ReviewComment
          v-for="comment in comments"
          :key="comment.commentId"
          :comment="comment"
          :reviewId="review.reviewId"
        />
        <div class="h-12"></div>
      </div>
      <!-- 댓글 출력 종료 -->
      <div v-if="isLogin" class="absolute bottom-0 w-full">
        <Separator class="w-full" />
        <div class="flex flex-row w-full space-x-2 h-14 items-center px-2 bg-gray-100">
          <Avatar class="w-10 h-10 border">
            <AvatarImage alt="프로필 이미지" :src="profile?.profileImage!!" />
            <AvatarFallback>{{ profile?.id }}</AvatarFallback>
          </Avatar>
          <Input
            type="text"
            class="flex-1 w-full bg-transparent border-none focus:border-none focus:ring-0 focus-visible:ring-0 focus-visible:ring-offset-0"
            placeholder="댓글을 입력해주세요."
            v-model="comment"
            @keyup.enter.prevent.stop="onSubmit"
          />
          <span
            @click.enter.prevent.stop="onSubmit"
            class="text-sm pr-2"
            :class="
              comment.length > 0
                ? 'text-blue-500 font-bold cursor-pointer'
                : 'text-gray-400'
            "
            >작성
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

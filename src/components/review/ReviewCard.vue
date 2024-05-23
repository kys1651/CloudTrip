<script setup lang="ts">
import { reviewDisLikeRequest, reviewLikeRequest } from "@/api/review";
import { followRequest, unFollowRequest } from "@/api/user";
import ReviewShare from "@/components/review/ReviewShare.vue";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { toast } from "@/components/ui/toast";
import { useAuthenticationStore } from "@/stores/authentication";
import { useReview } from "@/stores/review";
import type { IReview } from "@/types/board.type";
import { useMutation, useQueryClient } from "@tanstack/vue-query";
import { Heart, LoaderCircle, MapPin, MessageCircle } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
import CommonAvatar from "../common/CommonAvatar.vue";
import IconReviewRating from "../icons/IconReviewRating.vue";
import Carousel from "../ui/carousel/Carousel.vue";
import ReviewDropdownMenu from "./ReviewDropdownMenu.vue";

const props = defineProps<{
  review: IReview;
}>();
const queryClient = useQueryClient();
const authenticationStore = useAuthenticationStore();
const { profile } = storeToRefs(authenticationStore);

const reviewStore = useReview();
const router = useRouter();

const isLiked = ref<boolean>(props.review.isLiked);
const { mutate, isPending: isLoading } = useMutation({
  mutationKey: ["reviews", "like", props.review.reviewId],
  mutationFn: () =>
    isLiked.value
      ? reviewDisLikeRequest(props.review.reviewId)
      : reviewLikeRequest(props.review.reviewId),
  onSuccess: () => {
    isLiked.value = !isLiked.value;
    queryClient.invalidateQueries({
      queryKey: ["reviews", "like", props.review.reviewId],
    });
  },
});

const isFollowing = ref<boolean>(props.review.isFollowing);
const { mutate: followMutate, isPending: isFollowLoading } = useMutation({
  mutationKey: ["reviews", "follow", props.review.user.userId],
  mutationFn: () =>
    isFollowing.value
      ? unFollowRequest(props.review.user.userId)
      : followRequest(props.review.user.userId),
  onSuccess: () => {
    isFollowing.value = !isFollowing.value;
    queryClient.invalidateQueries({
      queryKey: ["reviews", "follow", props.review.user.userId],
    });
    const action = isFollowing.value ? "팔로우" : "언팔로우";
    toast({
      title: `${action} 성공`,
      description: `${props.review.user.nickname}을/를 ${action} 하였습니다.`,
      variant: "success",
    });
  },
});
const pushRouter = () => {
  reviewStore.updateReview(props.review);
  router.push({ name: "review-detail", params: { id: props.review.reviewId } });
};
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
      <Button
        :disable="isFollowLoading"
        @click="followMutate"
        v-if="profile && review.user.userId !== profile.id"
        class="mr-auto ml-2"
        :variant="isFollowing ? 'outline' : 'default'"
        size="xs"
      >
        <LoaderCircle v-if="isFollowLoading" class="mr-2 h-4 w-4 animate-spin" />
        <span v-else>{{ isFollowing ? "팔로잉" : "팔로우" }}</span>
      </Button>

      <ReviewDropdownMenu
        v-if="
          profile && (profile.id === review.user.userId || profile.roleType == 'ADMIN')
        "
        :reviewId="review.reviewId"
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
    </CardContent>
    <CardFooter class="grid py-2 px-4">
      <!-- 아이콘 출력 -->
      <div class="flex items-center w-full">
        <Button size="icon" variant="ghost" @click="mutate" :disable="isLoading">
          <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
          <div v-else>
            <Heart v-if="isLiked" :size="20" fill="red" stroke="red" />
            <Heart v-else :size="20" />
          </div>
        </Button>
        <Button size="icon" variant="ghost" @click="pushRouter">
          <MessageCircle :size="20" />
        </Button>
        <Button size="icon" variant="ghost">
          <ReviewShare :review="review" />
        </Button>
        <div class="flex items-center text-xs text-gray-500 ml-auto">
          <IconReviewRating
            :filled="true"
            class="ml-2"
            :stroke="'black'"
            :stroke-width="0.8"
            :size="20"
          />
          ({{ review.rating }})
        </div>
      </div>
      <!-- 아이콘 출력 종료 -->
      <div class="flex flex-col cursor-pointer" @click="pushRouter">
        <p class="text-sm text-black-600">{{ review.content }}</p>
        <div class="flex my-2 flex-row justify-between items-center text-gray-400">
          <div class="flex flex-col">
            <span class="text-xs text-gray-800">{{ review.tourName }}</span>
            <span id="fontSize-small" class="flex items-center text-xs text-gray-600">
              <MapPin class="w-3 h-3 mr-1 text-gray-500" />{{ review.address }}
            </span>
          </div>
          <span class="text-xs">{{
            new Date(review.createdAt).toLocaleDateString()
          }}</span>
        </div>
      </div>
    </CardFooter>
  </Card>
</template>

<style scoped>
p {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  word-break: keep-all;
}

#fontSize-small {
  font-size: 10px;
  line-height: 1rem;
}
</style>

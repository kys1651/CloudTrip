<script setup lang="ts">
import { userReviewsRequest } from "@/api/review";
import {
  followCountRequest,
  followRequest,
  followingCountRequest,
  simpleProfileRequest,
  unFollowRequest,
} from "@/api/user";
import Button from "@/components/ui/button/Button.vue";
import Label from "@/components/ui/label/Label.vue";
import FollowerList from "@/components/user/FollowerList.vue";
import FollowingList from "@/components/user/FollowingList.vue";
import { useAuthenticationStore } from "@/stores/authentication";
import { useMutation, useQuery, useQueryClient } from "@tanstack/vue-query";
import { storeToRefs } from "pinia";
import { useRoute } from "vue-router";

import { toast } from "@/components/ui/toast";
import { ref, watch } from "vue";

const route = useRoute();
const userId = Number(route.params.userId);
const authenticationStore = useAuthenticationStore();
const { profile } = storeToRefs(authenticationStore);
const queryClient = useQueryClient();

const { data: userProfile } = useQuery({
  queryKey: ["user", userId, "profile"],
  queryFn: () => simpleProfileRequest(userId),
});

const { data: userReviews } = useQuery({
  queryKey: ["user", userId, "review"],
  queryFn: () => userReviewsRequest(userId),
});

const { data: followerCount } = useQuery({
  queryKey: ["user", userId, "followCount"],
  queryFn: () => followCountRequest(userId),
});

const { data: followingCount } = useQuery({
  queryKey: ["user", userId, "followingCount"],
  queryFn: () => followingCountRequest(userId),
});

const isFollowing = ref<boolean>(false);
watch(userProfile, (newProfile) => {
  if (newProfile) {
    isFollowing.value = newProfile.isFollowing || false;
  }
});
const { mutate: followMutate, isPending } = useMutation({
  mutationKey: ["user", userProfile.value?.userId, "follow"],
  mutationFn: () => {
    if (userProfile.value) {
      return isFollowing.value
        ? unFollowRequest(userProfile.value.userId)
        : followRequest(userProfile.value.userId);
    }
    return Promise.reject("User profile is not loaded yet");
  },
  onSuccess: () => {
    isFollowing.value = !isFollowing.value;
    queryClient.invalidateQueries({
      queryKey: ["user", userProfile.value?.userId],
    });
    const action = isFollowing.value ? "팔로우" : "언팔로우";
    toast({
      title: `${action} 성공`,
      description: `${userProfile.value?.nickname}을/를 ${action} 하였습니다.`,
      variant: "success",
    });
  },
});

const isOpenFollowing = ref(false);
const isOpenFollower = ref(false);
</script>

<template>
  <div
    v-if="userProfile"
    class="container mx-auto mt-10 p-8 bg-white rounded-lg shadow-md text-center max-w-5xl"
  >
    <h1 @click="$router.push(`/user/${userId}`)" class="text-3xl font-bold mb-8">
      프로필
    </h1>
    <div class="flex items-center justify-center">
      <img
        :src="userProfile.profileImage"
        alt="프로필 이미지"
        class="w-80 h-80 rounded-full border-4 border-gray-300 mr-10"
      />
      <div class="text-left p-4">
        <div class="grid grid-cols-3 gap-4 text-center mb-4">
          <div>
            <span class="block text-3xl font-bold">{{
              userReviews ? userReviews.length : 0
            }}</span>
            <span class="text-sm text-gray-600">전체 리뷰 수</span>
          </div>
          <div @click="isOpenFollower = true">
            <span class="block text-3xl font-bold">{{ followerCount }}</span>
            <span class="text-sm text-gray-600">팔로워</span>
          </div>
          <div @click="isOpenFollowing = true">
            <span class="block text-3xl font-bold">{{ followingCount }}</span>
            <span class="text-sm text-gray-600">팔로잉</span>
          </div>
        </div>
        <div class="mb-4 flex justify-between items-center">
          <Label class="text-2xl">닉네임: {{ userProfile.nickname }}</Label>
          <Button
            :disabled="isPending"
            @click="followMutate"
            v-if="profile && userProfile.userId !== profile.id"
            class="ml-auto"
            :variant="isFollowing ? 'outline' : 'default'"
            size="xs"
          >
            <LoaderCircle v-if="isPending" class="mr-2 h-4 w-4 animate-spin" />
            <span v-else>{{ isFollowing ? "팔로잉" : "팔로우" }}</span>
          </Button>
        </div>
        <div class="mb-4">
          <Label class="text-2xl">이메일: {{ userProfile.email }}</Label>
        </div>
        <div class="mb-4">
          <Label class="text-2xl">지역: {{ userProfile.city }}</Label>
        </div>
        <div class="mb-4">
          <Label class="text-2xl">상세 지역: {{ userProfile.town }}</Label>
        </div>
      </div>
    </div>
    <nav class="bg-gray-800 text-white py-4 mt-10 rounded-xl">
      <ul class="flex justify-center">
        <li class="flex-1 text-center">
          <RouterLink class="text-xl hover:text-gray-300" :to="{ name: 'userReviews' }"
            >작성한 리뷰</RouterLink
          >
        </li>
        <li class="flex-1 text-center">
          <RouterLink class="text-xl hover:text-gray-300" :to="{ name: 'userComments' }"
            >작성한 댓글</RouterLink
          >
        </li>
        <li class="flex-1 text-center">
          <RouterLink
            class="text-xl hover:text-gray-300"
            :to="{ name: 'userLikedReviews' }"
            >좋아요한 리뷰</RouterLink
          >
        </li>
      </ul>
    </nav>
    <RouterView />
    <FollowerList
      :open="isOpenFollower"
      :nickname="userProfile.nickname"
      :userId="userProfile.userId"
      @update:open="isOpenFollower = $event"
    />
    <FollowingList
      :open="isOpenFollowing"
      :nickname="userProfile.nickname"
      :userId="userProfile.userId"
      @update:open="isOpenFollowing = $event"
    ></FollowingList>
  </div>
</template>

<style scoped></style>

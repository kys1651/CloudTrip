<script setup lang="ts">
import { simpleProfileRequest } from '@/api/user'
import { useQuery } from '@tanstack/vue-query'
import { useRoute } from 'vue-router'
import Label from '@/components/ui/label/Label.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'

const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const route = useRoute()
const userId = route.params.userId
const { data: userProfile } = useQuery({
  queryKey: ['simpleProfile', userId],
  queryFn: () => simpleProfileRequest(userId)
})
</script>

<template>
  <div class="container mx-auto mt-10 p-8 bg-white rounded-lg shadow-md text-center max-w-3xl">
    <h1 @click="$router.push(`/user/${userId}`)" class="text-3xl font-bold mb-8">프로필</h1>
    <div class="flex items-center justify-center">
      <img
        :src="userProfile?.profileImage"
        alt="프로필 이미지"
        class="w-60 h-60 rounded-full border-4 border-gray-300 mr-10"
      />
      <div class="text-left">
        <div class="mb-4">
          <Label class="text-2xl">닉네임: {{ userProfile?.nickname }}</Label>
        </div>
        <div class="mb-4">
          <Label class="text-2xl">이메일: {{ userProfile?.email }}</Label>
        </div>
        <div class="mb-4">
          <Label class="text-2xl">지역: {{ userProfile?.city }}</Label>
        </div>
        <div class="mb-4">
          <Label class="text-2xl">상세 지역: {{ userProfile?.town }}</Label>
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
          <RouterLink class="text-xl hover:text-gray-300" :to="{ name: 'userLikedReviews' }"
            >좋아요한 리뷰</RouterLink
          >
        </li>
      </ul>
    </nav>
    <RouterView />
  </div>
</template>

<style scoped></style>

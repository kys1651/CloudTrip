<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
interface MenuItem {
  pathname: string
  name: string
  title: string
}
const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)
const route = useRoute()
const curPath = ref(route.path)
const ROUTES = ref<{ label: string; items: MenuItem[] }[]>([
  {
    label: '사용자 관리',
    items: [
      { pathname: '/mypage', name: 'myProfile', title: '프로필 관리' },
      { pathname: '/mypage/account', name: 'myAccount', title: '계정 관리' }
    ]
  },
  {
    label: '사용자 활동',
    items: [
      { pathname: '/mypage/reviews', name: 'myReviews', title: '내가 작성한 글' },
      { pathname: '/mypage/comments', name: 'myComments', title: '나의 댓글' },
      { pathname: '/custom/likedReview', name: 'myLikedReview', title: '좋아요한 글' }
    ]
  }
])
watch(
  () => route.path,
  (newPath) => {
    curPath.value = newPath
  }
)
</script>

<template>
  <nav class="flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-3">
    <div v-for="group in ROUTES" :key="group.label">
      <h3 class="text-2xl font-bold mt-4 mb-3">{{ group.label }}</h3>
      <div v-for="route in group.items" :key="route.name">
        <Button
          variant="ghost"
          class="w-full text-left justify-start p-4 text-lg text-gray-400 font-semibold"
          :class="{ 'text-black': route.pathname === curPath }"
        >
          <RouterLink @click="$emit('changeTitle', route.title)" :to="{ name: route.name }">{{
            route.title
          }}</RouterLink>
        </Button>
      </div>
    </div>
    <div v-if="profile?.roleType">
      <h3 class="text-2xl font-bold mt-4 mb-3">관리자 관리</h3>
      <Button
        variant="ghost"
        class="w-full text-left justify-start p-4 text-lg text-gray-400 font-semibold"
        :class="{ 'text-black': '/mypage/admin/user' === curPath }"
      >
        <RouterLink :to="{ name: 'adminUser' }">회원 관리</RouterLink>
      </Button>
    </div>
  </nav>
</template>

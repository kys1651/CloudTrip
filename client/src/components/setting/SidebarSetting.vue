<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

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
      { pathname: '/setting', name: 'myProfile', title: '프로필 관리' },
      { pathname: '/setting/account', name: 'myAccount', title: '계정 관리' }
    ]
  },
  {
    label: '사용자 활동 관리',
    items: [
      { pathname: '/setting/reviews', name: 'myReviews', title: '나의 여행후기' },
      { pathname: '/setting/comments', name: 'myComments', title: '나의 댓글' },
      { pathname: '/setting/likedReview', name: 'myLikedReview', title: '나의 좋아요' },
      { pathname: '/setting/mySchedule', name: 'mySchedule', title: '나의 여행 계획' }
    ]
  }
])
const ADMIN_ROUTER = ref<{ label: string; items: MenuItem[] }>({
  label: '관리자 관리',
  items: [
    { pathname: '/setting/admin/user', name: 'adminUser', title: '회원 관리' },
    { pathname: '/setting/admin/notice', name: 'adminNotice', title: '공지사항 관리' },
    { pathname: '/setting/admin/review', name: 'adminReview', title: '리뷰 관리' }
  ]
})
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
    <div v-if="profile?.roleType === 'ADMIN'">
      <h3 class="text-2xl font-bold mt-4 mb-3">{{ ADMIN_ROUTER.label }}</h3>
      <div v-for="route in ADMIN_ROUTER.items" :key="route.name">
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
  </nav>
</template>

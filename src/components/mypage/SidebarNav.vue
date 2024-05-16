<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
import { ref } from 'vue'
import { useRoute } from 'vue-router'
interface MenuItem {
  pathname: string
  name: string
  title: string
}
const route = useRoute()
const curPath = ref(route.path)
const ROUTES = ref<{ label: string; items: MenuItem[] }[]>([
  {
    label: '사용자 관리',
    items: [
      { pathname: '/mypage', name: 'mypage', title: '프로필 관리' },
      { pathname: '/account', name: 'account', title: '계정 관리' }
    ]
  },
  {
    label: '사용자 활동',
    items: [
      { pathname: '/myReview', name: 'myReview', title: '내가 작성한 글' },
      { pathname: '/myComment', name: 'myComment', title: '나의 댓글' },
      { pathname: '/likeReview', name: 'likeReview', title: '좋아요한 글' }
    ]
  }
])
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
          <RouterLink :to="{ name: route.name }">{{ route.title }}</RouterLink>
        </Button>
      </div>
    </div>
  </nav>
</template>

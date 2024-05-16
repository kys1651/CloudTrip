<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
import { ref } from 'vue'
interface MenuItem {
  pathname: string
  name: string
  title: string
}

const ROUTES = ref<MenuItem[]>([
  {
    pathname: '/mypage',
    name: 'mypage',
    title: '프로필 관리'
  },
  {
    pathname: '/account',
    name: 'account',
    title: '계정 관리'
  },
  {
    pathname: '/myReview',
    name: 'myReview',
    title: '내가 작성한 글'
  },
  {
    pathname: '/myComment',
    name: 'myComment',
    title: '나의 댓글'
  },
  {
    pathname: '/likeReview',
    name: 'likeReview',
    title: '좋아요한 글'
  }
])
</script>

<template>
  <nav class="flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-3">
    <Button
      v-for="route in ROUTES"
      :key="route.name"
      variant="ghost"
      class="w-full text-left justify-start p-4 text-lg font-bold"
    >
      <RouterLink :to="{ name: route.name }"> {{ route.title }} </RouterLink>
    </Button>
  </nav>
</template>

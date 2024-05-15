<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'

interface Item {
  title: string
  href: string
}

const ROUTES: { pathname: string; name: string; title: string }[] = [
  {
    pathname: '/mypage',
    name: 'mypage',
    title: '내 정보 관리'
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
    pathname: '/mypage',
    name: 'mypage',
    title: '관리자 페이지(어드민 계정일 때만 보이게)'
  }
]
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

<!-- cn(
          'w-full text-left justify-start',
          $route.path === `${item.href}.html` && 'bg-muted hover:bg-muted'
        ) -->

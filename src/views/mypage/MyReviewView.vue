<script setup lang="ts">
import FormsLayout from '@/components/mypage/FormsLayout.vue'
import MyReview from '@/components/mypage/MyReview.vue'
</script>

<template>
  <FormsLayout title="내가 작성한 글" description="작성한 리뷰를 모아볼 수 있는 페이지입니다.">
    <MyReview />
  </FormsLayout>
</template>

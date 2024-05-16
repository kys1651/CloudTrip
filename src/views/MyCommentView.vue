<script setup lang="ts">
import FormsLayout from '@/components/mypage/FormsLayout.vue'
import MyComment from '@/components/mypage/MyComment.vue'
</script>

<template>
  <FormsLayout title="나의 댓글" description="작성한 댓글을 모아볼 수 있는 페이지입니다.">
    <MyComment />
  </FormsLayout>
</template>

<template>
  <div class="container">
    <div v-if="userComments?.length > 0">
      <SimpleCommentCard
        v-for="comment in userComments"
        :key="comment.commentId"
        :comment="comment"
      />
    </div>
    <div v-else class="flex flex-col items-center justify-center min-h-screen">
      <h4 class="text-xl font-semibold mb-3 text-center">아직 작성한 댓글이 존재하지않습니다.</h4>
    </div>
  </div>
</template>

<script setup lang="ts">
import { userCommentsRequest } from '@/api/review'
import { useQuery } from '@tanstack/vue-query'
import { useRoute } from 'vue-router'
import SimpleCommentCard from '../card/SimpleCommentCard.vue'
const route = useRoute()
const { data: userComments } = useQuery({
  queryKey: ['userComments', route.params.userId],
  queryFn: () => userCommentsRequest(Number(route.params.userId))
})
</script>

<style scoped></style>

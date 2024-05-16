<script setup lang="ts">
import { myCommentRequest } from '@/api/review'
import { useQuery } from '@tanstack/vue-query'
import Separator from '@/components/ui/separator/Separator.vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import SimpleCommentCard from './SimpleCommentCard.vue'

const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const { data: comments } = useQuery({
  queryKey: ['comments', profile.value!.id],
  queryFn: () => myCommentRequest(profile.value!.id)
})
</script>

<template>
  <div class="container w-full max-w-6xl mx-auto p-5">
    <div>
      <h3 class="text-3xl font-bold mb-5">댓글 모아보기</h3>
    </div>
    <Separator />
    <SimpleCommentCard v-for="comment in comments" :key="comment.commentId" :comment="comment" />
  </div>
</template>

<style scoped></style>

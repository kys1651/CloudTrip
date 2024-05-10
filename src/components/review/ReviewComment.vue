<template>
  <hr />
  <div class="flex items-start gap-2">
    <div class="flex items-center mb-2">
      <Avatar>
        <AvatarImage :src="`https://api.multiavatar.com/user-${comment.commentId}.svg`" />
      </Avatar>
    </div>
    <div class="grid gap-1">
      <a class="font-semibold text-sm ms-1" href="#">
        {{ comment.nickname || '이름없음' + comment.commentId }}
        <span class="text-gray-400 dark:text-gray-500 ms-4">{{ comment.createdAt }}</span>
      </a>
      <div class="text-sm">{{ comment.content }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Avatar, AvatarImage } from '@/components/ui/avatar'
import type { ReviewCommentDetail } from '@/types/board.type'
const props = defineProps<{ comment: ReviewCommentDetail }>()
const comment = props.comment
</script>

<style scoped></style>

<script setup lang="ts">
import type { SimpleCommentDetail } from "@/types/board.type";
import { useRouter } from "vue-router";
const router = useRouter();
defineProps<{
  comment: SimpleCommentDetail;
}>();
</script>

<template>
  <div
    @click="router.push(`/review/${comment.reviewId}`)"
    class="bg-white shadow-md rounded-lg p-5 mb-5"
  >
    <div class="mb-2">
      <p class="text-gray-900 font-bold">
        {{ comment.content }}
      </p>
    </div>
    <div class="text-sm text-gray-500 mb-3">
      {{ new Date(comment.createdAt).toLocaleDateString() }}
    </div>
    <div class="text-gray-700">
      <p>
        {{
          comment.reviewContent.length > 50
            ? comment.reviewContent.substring(0, 50) + "..."
            : comment.reviewContent
        }}
      </p>
    </div>
  </div>
</template>

<style scoped>
p {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  word-break: keep-all;
}
</style>

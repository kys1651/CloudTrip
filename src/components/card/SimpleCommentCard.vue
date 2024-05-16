<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
defineProps(['comment'])
</script>

<template>
  <div
    @click="router.push(`/review/${comment.reviewId}`)"
    class="bg-white shadow-md rounded-lg p-5 mb-5"
  >
    <div class="mb-2">
      <p class="text-gray-900 font-bold">
        {{
          comment.content.length > 60 ? comment.content.substring(0, 60) + '...' : comment.content
        }}
      </p>
    </div>
    <div class="text-sm text-gray-500 mb-3">
      {{ new Date(comment.createdAt).toLocaleDateString() }}
    </div>
    <div class="text-gray-700">
      <p>
        {{
          comment.reviewContent.length > 50
            ? comment.reviewContent.substring(0, 50) + '...'
            : comment.reviewContent
        }}
      </p>
    </div>
  </div>
</template>

<style scoped></style>

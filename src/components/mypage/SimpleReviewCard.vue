<script setup>
import { Card, CardContent, CardDescription, CardHeader } from '@/components/ui/card'
import { useRouter } from 'vue-router'
defineProps(['review'])
const router = useRouter()
</script>

<template>
  <Card @click="router.push(`review/${review.reviewId}`)">
    <div>
      <CardHeader>
        <div v-if="review.images && review.images.length > 0">
          <img :src="review.images[0]" alt="Review Image" class="object-cover rounded-lg" />
        </div>
      </CardHeader>
      <CardContent> {{ review.content }} </CardContent>
    </div>
    <div class="flex flex-col items-center mb-3">
      <CardDescription class="text-center">
        {{ review.createdAt.toString().substring(0, 10) }}
      </CardDescription>
    </div>
  </Card>
</template>

<style scoped></style>

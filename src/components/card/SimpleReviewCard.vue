<script setup>
import { Card, CardContent, CardDescription, CardHeader } from '@/components/ui/card'
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious
} from '@/components/ui/carousel'
import Carousel from '@/components/ui/carousel/Carousel.vue'
import { useRouter } from 'vue-router'
defineProps(['review'])
const router = useRouter()
</script>

<template>
  <Card class="flex flex-col h-full">
    <div>
      <CardHeader>
        <Carousel>
          <CarouselContent class="h-48">
            <CarouselItem
              v-for="(image, index) in review.images"
              :key="index"
              class="flex justify-center items-center w-full h-full"
            >
              <img
                class="rounded-md w-full h-full object-contain"
                :src="image"
                alt="Review Image"
              />
            </CarouselItem>
          </CarouselContent>
          <CarouselPrevious class="-left-4" />
          <CarouselNext class="-right-4" />
        </Carousel>
      </CardHeader>
      <CardContent @click="router.push(`/review/${review.reviewId}`)">
        <p>{{ review.content }}</p>
      </CardContent>
    </div>
    <div class="flex flex-col items-center mb-3">
      <CardDescription id="content" class="text-center">
        {{ review.createdAt }}
      </CardDescription>
    </div>
  </Card>
</template>

<style scoped>
p {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
  word-break: keep-all;
}
</style>

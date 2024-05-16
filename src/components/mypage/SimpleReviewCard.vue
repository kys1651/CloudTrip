<script setup>
import { Card, CardContent, CardDescription, CardHeader } from '@/components/ui/card'
import { useRouter } from 'vue-router'
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious
} from '@/components/ui/carousel'
import Carousel from '@/components/ui/carousel/Carousel.vue'
defineProps(['review'])
const router = useRouter()
</script>

<template>
  <Card>
    <div>
      <CardHeader>
        <Carousel>
          <CarouselContent class="h-full">
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
      <CardContent @click="router.push(`review/${review.reviewId}`)">
        {{ review.content }}
      </CardContent>
    </div>
    <div class="flex flex-col items-center mb-3">
      <CardDescription class="text-center">
        {{ review.createdAt }}
      </CardDescription>
    </div>
  </Card>
</template>

<style scoped></style>

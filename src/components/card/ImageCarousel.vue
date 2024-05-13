<script setup lang="ts">
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";

defineProps<{
  images: string[];
}>();
</script>
<template>
  <Carousel class="w-full max-w-md m-auto flex justify-center">
    <CarouselContent>
      <CarouselItem
        v-for="(image, index) in images"
        :key="index"
        class="flex justify-center items-center"
      >
        <img class="rounded-md w-full" :src="image" alt="Review Image" />
      </CarouselItem>
    </CarouselContent>
    <CarouselPrevious class="-left-4" />
    <CarouselNext class="-right-4" />
  </Carousel>
</template>

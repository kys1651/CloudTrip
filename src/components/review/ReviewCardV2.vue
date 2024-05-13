<script setup lang="ts">
import { reviewDisLikeRequest, reviewLikeRequest } from "@/api/review";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import {
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import type { IReview } from "@/types/board.type";
import { useMutation } from "@tanstack/vue-query";
import {
  Bookmark,
  Ellipsis,
  FileWarning,
  Heart,
  MapPin,
  MessageCircle,
  Send,
  Star,
} from "lucide-vue-next";
import { ref } from "vue";
import Carousel from "../ui/carousel/Carousel.vue";

const props = defineProps<{
  review: IReview;
}>();

const isLiked = ref<boolean>(props.review.isLiked);

const { mutate } = useMutation({
  mutationKey: ["reviews", "like", props.review.reviewId],
  mutationFn: () =>
    isLiked.value
      ? reviewDisLikeRequest(props.review.reviewId)
      : reviewLikeRequest(props.review.reviewId),
  onSuccess: () => {
    isLiked.value = !isLiked.value;
  },
});
</script>

<template>
  <Card class="rounded-md shadow border w-full max-w-96 py-2 border-gray-200">
    <CardHeader class="p-4 flex flex-row items-center justify-between">
      <a class="flex items-center gap-2 text-sm font-semibold" href="#">
        <Avatar class="w-8 h-8">
          <AvatarImage alt="프로필 이미지" :src="review.user.profileImage" />
          <AvatarFallback>{{ review.user.userId }}</AvatarFallback>
        </Avatar>
        <span>{{ review.user.nickname }}</span>
      </a>
      <DropdownMenu>
        <DropdownMenuTrigger>
          <Button class="ml-auto w-8 h-8 rounded-full" size="icon" variant="ghost">
            <Ellipsis class="w-4 h-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuItem>
            <Bookmark class="w-4 h-4 mr-2" />
            Save
          </DropdownMenuItem>
          <DropdownMenuItem>
            <Star class="w-4 h-4 mr-2" />
            Add to favorites
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem>
            <FileWarning class="w-4 h-4 mr-2" />
            Report
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </CardHeader>

    <CardContent class="p-0">
      <Carousel class="w-full h-72 m-auto flex justify-center flex-shrink">
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
    </CardContent>
    <CardFooter class="grid py-2 px-4">
      <div class="flex items-center w-full">
        <Button size="icon" variant="ghost" @click="mutate">
          <Heart v-if="isLiked" :size="20" fill="red" stroke="red" />
          <Heart v-else :size="20" />
        </Button>
        <Button size="icon" variant="ghost">
          <MessageCircle :size="20" />
        </Button>
        <Button size="icon" variant="ghost">
          <Send :size="20" />
        </Button>
        <Button class="ml-auto" size="icon" variant="ghost">
          <Bookmark :size="20" />
        </Button>
      </div>
      <div class="flex flex-col cursor-pointer">
        <p class="text-sm text-gray-600">
          {{ review.content }}
        </p>
        <div class="flex my-2 flex-row justify-between items-center text-gray-400">
          <div class="flex flex-row space-x-1">
            <MapPin :size="14" />
            <span class="text-xs text-gray-500">{{ review.address }}</span>
          </div>
          <span class="text-xs">{{
            new Date(review.createdAt).toLocaleDateString()
          }}</span>
        </div>
      </div>
    </CardFooter>
  </Card>
</template>

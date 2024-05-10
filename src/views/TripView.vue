<script setup lang="ts">
import { Card, CardContent, CardTitle } from "@/components/ui/card";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import Autoplay from "embla-carousel-autoplay";
import { PlaneTakeoff } from "lucide-vue-next";
import type { RouterLink } from "vue-router";

const cities: { id: number; name: string; image: string }[] = [
  { id: 1, name: "서울", image: "seoul.jpg" },
  { id: 2, name: "인천", image: "incheon.jpg" },
  { id: 3, name: "대전", image: "daejeon.jpg" },
  { id: 4, name: "대구", image: "daegu.jpg" },
  { id: 5, name: "광주", image: "gwangju.jpg" },
  { id: 6, name: "부산", image: "busan.jpg" },
  { id: 7, name: "울산", image: "ulsan.jpg" },
  { id: 8, name: "세종특별자치시", image: "sejong.jpg" },
  { id: 31, name: "경기도", image: "gyeonggido.jpg" },
  { id: 32, name: "강원도", image: "gangwondo.jpg" },
  { id: 33, name: "충청북도", image: "chungcheongbukdo.jpg" },
  { id: 34, name: "충청남도", image: "chungcheongnamdo.jpg" },
  { id: 35, name: "경상북도", image: "gyeongsangbukdo.jpg" },
  { id: 36, name: "경상남도", image: "gyeongsangnamdo.jpg" },
  { id: 37, name: "전라북도", image: "jeollabukdo.jpg" },
  { id: 38, name: "전라남도", image: "jeollanamdo.jpg" },
  { id: 39, name: "제주도", image: "jejuisland.jpg" },
];

const plugin = Autoplay({
  delay: 2000,
  stopOnMouseEnter: true,
  stopOnInteraction: false,
});
</script>

<template>
  <div
    key="1"
    className="flex flex-col lg:flex-row lg:space-x-8 max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8"
  >
    <div className="flex-1 w-full">
      <h1 className="text-4xl font-bold">기존에 경험하지 못한 새로운 여행 플래너</h1>
      <p className="mt-4 text-lg">
        고민마 한번 여행 계획을 바이크를 통해 부담 없이 스케줄을 해보세요.
      </p>

      <Carousel
        class="w-full max-w-md m-auto flex justify-center"
        :plugins="[plugin]"
        @mouseenter="plugin.stop"
        @mouseleave="[plugin.reset(), plugin.play()]"
      >
        <CarouselContent>
          <CarouselItem v-for="city in cities" :key="city.id">
            <div class="p-1">
              <Card class="border-0 shadow-none">
                <CardContent
                  class="flex aspect-square items-center justify-center p-6 bg-contain bg-center bg-no-repeat rounded-lg"
                  :style="{
                    backgroundImage: `url('/cities/${city.image}')`,
                  }"
                >
                </CardContent>
                <CardTitle class="text-2xl font-bold text-center">
                  {{ city.name }}
                </CardTitle>
                <RouterLink
                  :to="{ name: 'planning', params: { cityId: city.id } }"
                  class="w-full flex justify-center items-center py-2 text-gray-400 hover:text-gray-600 transition-colors duration-400 ease-in-out"
                >
                  <span class="text-lg">지금 바로 여행가기</span>
                  <PlaneTakeoff class="text-gray-400" />
                </RouterLink>
              </Card>
              <div></div>
            </div>
          </CarouselItem>
        </CarouselContent>
        <CarouselPrevious />
        <CarouselNext />
      </Carousel>
    </div>
    <div className="flex-1 mt-8 lg:mt-0">
      <div className="h-full w-full">
        <img
          alt="Map"
          className="rounded-lg shadow-lg object-cover object-center h-full w-full"
          height="600"
          src="https://generated.vusercontent.net/placeholder.svg"
          width="1000"
        />
      </div>
    </div>
  </div>
</template>

<style scoped></style>

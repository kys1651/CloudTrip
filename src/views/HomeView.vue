<script setup lang="ts">
import { homeList } from "@/api/home";
import MetricCard from "@/components/card/MetricCard.vue";
import TripCard from "@/components/card/TripCard.vue";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { imageOrDefault } from "@/lib/image-load";
import type { IHome } from "@/types/home.type";
import Autoplay from "embla-carousel-autoplay";
import emblaCarouselVue from "embla-carousel-vue";
import { CalendarCheck, PlaneTakeoff, Star, UserRound } from "lucide-vue-next";
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const noImg = "/src/assets/img/noImg2.png";

const [emblaRef, emblaApi] = emblaCarouselVue({ loop: true }, [Autoplay()]);

const route = useRoute();
const router = useRouter();

const homeData = ref<IHome>();

homeList()
  .then((data) => {
    homeData.value = data;
  })
  .catch((error) => {
    console.log("데이터 불러오기 실패", error);
  });

const goTourDetail = (tour: Object) => {};

const goReviewDetail = (reviewId: number) => {
  router.push({ name: "review-detail", params: { id: reviewId } });
};
</script>

<template>
  <main v-if="homeData" class="w-full flex flex-col items-center">
    <!-- 메인 화면 - 사이트 소개 -->
    <section class="w-full py-12 md:py-24 lg:py-32 flex justify-center">
      <div class="container px-4 md:px-6">
        <div
          class="grid items-center gap-6 lg:grid-cols-[1fr_500px] lg:gap-12 xl:grid-cols-[1fr_550px]"
        >
          <div class="flex flex-col justify-center space-y-4">
            <div class="space-y-2">
              <!-- <div class="inline-block rounded-lg bg-gray-100 px-3 py-1 text-sm dark:bg-gray-800">
                모두의 여행지 추천 서비스
              </div> -->
              <h2 class="mb-5 text-3xl font-bold tracking-tighter sm:text-5xl">
                실시간 공동 여행계획 수립 서비스 : Cloud Trip
              </h2>
              <p
                class="max-w-[600px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400"
              >
                친구와 함께 실시간으로 AI를 활용해 여행계획을 세워보는 것은 어떨까요?
              </p>
            </div>
            <div class="flex flex-col gap-2 min-[400px]:flex-row">
              <a
                class="inline-flex h-10 items-center justify-center rounded-md bg-gray-900 px-8 text-sm font-medium text-gray-50 shadow transition-colors hover:bg-gray-900/90 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-gray-950 disabled:pointer-events-none disabled:opacity-50 dark:bg-gray-50 dark:text-gray-900 dark:hover:bg-gray-50/90 dark:focus-visible:ring-gray-300"
                href="#"
              >
                <RouterLink :to="{ name: 'schedule' }">
                  여행지 추천 바로 가기
                </RouterLink>
              </a>
              <a
                class="inline-flex h-10 items-center justify-center rounded-md border border-gray-200 bg-white px-8 text-sm font-medium shadow-sm transition-colors hover:bg-gray-100 hover:text-gray-900 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-gray-950 disabled:pointer-events-none disabled:opacity-50 dark:border-gray-800 dark:border-gray-800 dark:bg-gray-950 dark:hover:bg-gray-800 dark:hover:text-gray-50 dark:focus-visible:ring-gray-300"
                href="#"
              >
                <RouterLink :to="{ name: 'review' }"> 여행지 후기 찾아보기 </RouterLink>
              </a>
            </div>
          </div>
          <!-- src="http://tong.visitkorea.or.kr/cms2/website/74/3108474.jpg" -->
          <img
            src="/src/assets/img/notice_img1.jpg"
            width="550"
            height="550"
            alt="Travel Recommendations"
            class="mx-auto aspect-video overflow-hidden rounded-xl object-cover object-center sm:w-full lg:order-last lg:aspect-square"
          />
        </div>
      </div>
    </section>
    <!-- 메인 화면 - 자료 분석 -->
    <section
      class="w-full py-12 md:py-24 lg:py-32 flex justify-center bg-gray-100 dark:bg-gray-800"
    >
      <div class="container px-4 md:px-6">
        <div class="grid gap-6">
          <div class="space-y-2 text-center">
            <h2 class="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">
              Cloud Trip
            </h2>
            <p
              class="mx-auto max-w-[700px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400 pb-4"
            >
              Cloud Trip 한눈에 보기
            </p>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="flex flex-col justify-center items-center">
              <MetricCard
                :name="'등록된 리뷰'"
                :count="homeData.numberData.reviewCount"
              ></MetricCard>
              <Star :size="64" class="mt-5" />
            </div>

            <div class="flex flex-col justify-center items-center">
              <MetricCard
                :name="'등록된 사용자'"
                :count="homeData.numberData.usersCount"
              ></MetricCard>
              <UserRound :size="64" class="mt-5" />
            </div>

            <div class="flex flex-col justify-center items-center">
              <MetricCard
                :name="'등록된 여행지'"
                :count="homeData.numberData.tourCount"
              ></MetricCard>
              <PlaneTakeoff :size="64" class="mt-5" />
            </div>

            <div class="flex flex-col justify-center items-center">
              <MetricCard
                :name="'등록된 여행 계획'"
                :count="homeData.numberData.scheduleCount"
              ></MetricCard>
              <CalendarCheck :size="64" class="mt-5" />
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- 메인 화면 - 여행지 추천  -->
    <section class="w-full py-6 md:py-12 lg:py-24 flex justify-center">
      <div class="container px-4 md:px-6">
        <div class="grid gap-6">
          <div class="space-y-2 text-center py-4">
            <h2 class="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">
              가장 인기있는 여행지
            </h2>
            <p
              class="mx-auto max-w-[700px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400"
            >
              직접 여행을 다녀온 여행객들이 여행지에 대한 평가를 남겨주었어요!
            </p>
          </div>
          <Carousel
            class="w-full max-w-full m-auto flex justify-center"
            :opts="{
              loop: true,
            }"
            :plugins="[
              Autoplay({
                delay: 2000,
                stopOnFocusIn: true,
              }),
            ]"
          >
            <CarouselContent>
              <CarouselItem
                v-for="topTour in homeData.topTours"
                :key="topTour.tourId"
                class="basis-1/4"
              >
                <TripCard
                  :name="topTour.name"
                  :backgroundImage="imageOrDefault(topTour.backgroundImage!)"
                  :description="topTour.description"
                  :rating="topTour.rating"
                  :location="topTour.cityName + ' ' + topTour.townName"
                  @click="goTourDetail(topTour)"
                  class="hover:scale-105 p-3"
                >
                </TripCard>
              </CarouselItem>
            </CarouselContent>
            <CarouselPrevious class="-left-6" />
            <CarouselNext class="-right-6" />
          </Carousel>
        </div>
      </div>
    </section>
    <section
      class="w-full py-12 md:py-24 lg:py-32 flex justify-center bg-gray-100 dark:bg-gray-800"
    >
      <div class="container px-4 md:px-6">
        <div class="grid gap-6">
          <div class="space-y-2 text-center">
            <h2 class="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">
              가장 좋아요가 많은 리뷰
            </h2>
            <p
              class="mx-auto max-w-[700px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400"
            >
              가장 많은 좋아요를 받은 리뷰를 모아봤어요!
            </p>
          </div>
          <!-- <Carousel
            class="w-full max-w-full m-auto flex justify-center"
            :opts="{
              loop: true
            }"
            :plugins="[
              Autoplay({
                delay: 2000
              })
            ]"
          >
            <CarouselContent>
              <CarouselItem
                v-for="topReview in homeData.topReviews"
                :key="topReview.reviewId"
                class="basis-1/4"
              >
                <PostingCard
                  :name="topReview.name"
                  :backgroundImage="imageOrDefault(topReview.image)"
                  :description="topReview.content"
                  :createdAt="new Date(topReview.createdAt)"
                  :views="topReview.likeCount"
                  @click="goReviewDetail(topReview)"
                  class="hover:scale-110"
                ></PostingCard>
              </CarouselItem>
            </CarouselContent>
            <CarouselPrevious class="-left-6" />
            <CarouselNext class="-right-6" />
          </Carousel> -->
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped></style>

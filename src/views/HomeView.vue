<script setup lang="ts">
import { userDataRequest } from '@/api/user'
import MetricCard from '@/components/card/MetricCard.vue'
import PostingCard from '@/components/card/PostingCard.vue'
import TripCard from '@/components/card/TripCard.vue'
import { useToast } from '@/components/ui/toast'
import type { MetricProps } from '@/types/trip.type'
import { useQuery } from '@tanstack/vue-query'
import { ref } from 'vue'

const metrics = ref<MetricProps[]>(
  [1, 2, 3, 4, 5, 6, 7, 8].map(() => ({
    name: 'Accommodations',
    count: Math.floor(Math.random() * 100000)
  }))
)
</script>

<template>
  <main class="w-full flex flex-col items-center">
    <!-- 메인 화면 - 사이트 소개 -->
    <section class="w-full py-12 md:py-24 lg:py-32 flex justify-center">
      <div class="container px-4 md:px-6">
        <div
          class="grid items-center gap-6 lg:grid-cols-[1fr_500px] lg:gap-12 xl:grid-cols-[1fr_550px]"
        >
          <div class="flex flex-col justify-center space-y-4">
            <div class="space-y-2">
              <div class="inline-block rounded-lg bg-gray-100 px-3 py-1 text-sm dark:bg-gray-800">
                모두의 여행지 추천 서비스
              </div>
              <h2 class="text-3xl font-bold tracking-tighter sm:text-5xl">
                당신의 민보수를 추천해드립니다
              </h2>
              <p
                class="max-w-[600px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400"
              >
                저희 민보수에서는 여러분의 행복한 2박 3일 여행지 혹은 계획을 추천해드립니다.
              </p>
            </div>
            <div class="flex flex-col gap-2 min-[400px]:flex-row">
              <a
                class="inline-flex h-10 items-center justify-center rounded-md bg-gray-900 px-8 text-sm font-medium text-gray-50 shadow transition-colors hover:bg-gray-900/90 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-gray-950 disabled:pointer-events-none disabled:opacity-50 dark:bg-gray-50 dark:text-gray-900 dark:hover:bg-gray-50/90 dark:focus-visible:ring-gray-300"
                href="#"
              >
                <RouterLink :to="{ name: 'trip' }"> 여행지 추천 바로 가기 </RouterLink>
              </a>
              <a
                class="inline-flex h-10 items-center justify-center rounded-md border border-gray-200 bg-white px-8 text-sm font-medium shadow-sm transition-colors hover:bg-gray-100 hover:text-gray-900 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-gray-950 disabled:pointer-events-none disabled:opacity-50 dark:border-gray-800 dark:border-gray-800 dark:bg-gray-950 dark:hover:bg-gray-800 dark:hover:text-gray-50 dark:focus-visible:ring-gray-300"
                href="#"
              >
                <RouterLink :to="{ name: 'review' }"> 여행지 후기 찾아보기 </RouterLink>
              </a>
            </div>
          </div>
          <img
            src="http://tong.visitkorea.or.kr/cms2/website/74/3108474.jpg"
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
            <h2 class="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">Key Metrics</h2>
            <p
              class="mx-auto max-w-[700px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400 pb-4"
            >
              Check out our latest travel statistics.
            </p>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <MetricCard
              v-for="(metric, index) in metrics"
              :key="index"
              v-bind:name="metric.name"
              v-bind:count="metric.count"
            ></MetricCard>
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
              Explore the most sought-after travel destinations around the world.
            </p>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <TripCard
              :name="'Tokyo, Japan'"
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :description="'The Neon Metropolis, a vibrant city that blends ancient traditions with cutting-edge technology.'"
              :rating="4"
              :location="'Asia'"
            ></TripCard>
            <TripCard
              :name="'Tokyo, Japan'"
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :description="'The Neon Metropolis, a vibrant city that blends asb ancient traditions with cutting-edge technology.'"
              :rating="4"
              :location="'Asia'"
            ></TripCard>
            <TripCard
              :name="'Tokyo, Japan'"
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :description="'The Neon Metropolis, a vibrant city that blends ancient traditions with cutting-edge technology.'"
              :rating="4"
              :location="'Asia'"
            ></TripCard>
            <TripCard
              :name="'Tokyo, Japan'"
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :description="'The Neon Metropolis, a vibrant city that blends ancient traditions with cutting-edge technology.'"
              :rating="4"
              :location="'Asia'"
            ></TripCard>
          </div>
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
              Featured Blog Posts
            </h2>
            <p
              class="mx-auto max-w-[700px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400"
            >
              Read our latest travel insights and tips.
            </p>
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <PostingCard
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :name="'The Ultimate Guide to Sustainable Travel'"
              :description="'Learn how to minimize your environmental impact while exploring the world, from choosing eco-friendly accommodations to reducing your carbon footprint.'"
              :createdAt="new Date()"
              :views="234"
            ></PostingCard>
            <PostingCard
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :name="'The Ultimate Guide to Sustainable Travel'"
              :description="'Learn how to minimize your environmental impact while exploring the world, from choosing eco-friendly accommodations to reducing your carbon footprint.'"
              :createdAt="new Date()"
              :views="234"
            ></PostingCard>
            <PostingCard
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :name="'The Ultimate Guide to Sustainable Travel'"
              :description="'Learn how to minimize your environmental impact while exploring the world, from choosing eco-friendly accommodations to reducing your carbon footprint.'"
              :createdAt="new Date()"
              :views="234"
            ></PostingCard>
            <PostingCard
              :backgroundImage="'https://generated.vusercontent.net/placeholder.svg'"
              :name="'The Ultimate Guide to Sustainable Travel'"
              :description="'Learn how to minimize your environmental impact while exploring the world, from choosing eco-friendly accommodations to reducing your carbon footprint.'"
              :createdAt="new Date()"
              :views="234"
            ></PostingCard>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>

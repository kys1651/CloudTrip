<script setup lang="ts">
import { Facebook, Instagram, Twitter, Youtube } from 'lucide-vue-next'
import { Link, BracketsIcon, ArrowUpIcon } from 'lucide-vue-next'
import Button from '@/components/ui/button/Button.vue'
const hiddenRoute = [
  'sign-in',
  'sign-up',
  'oauth2-redirect',
  'confirm-email',
  'schedule-detail',
  'schedule'
]
</script>

<template>
  <footer v-show="!hiddenRoute.includes($route.name?.toString() || 'home')">
    <hr />
    <div className="bg-white">
      <div
        className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:py-16 lg:px-8 flex justify-between items-center"
      >
        <div className="flex items-center">
          <div className="ml-4">
            <p className="text-base text-[#00B4E6] font-semibold uppercase tracking-wider">
              Cloud Trip
            </p>
            <p className="text-sm text-gray-500">주식회사 클라우드트립 | 대표 민보수</p>
            <p className="text-sm text-gray-500">서울시 동작구 노량진로 64길 88-10102</p>
            <p className="text-sm text-gray-500">서울특별시 강남구 역삼로 217, 4F</p>
            <p className="text-sm text-gray-500">contact@minbosu.co.kr</p>
            <div className="mt-2 flex space-x-4">
              <Button className="text-sm text-gray-500 hover:text-gray-900" href="#">
                이용약관
              </Button>
              <Button className="text-sm text-gray-500 hover:text-gray-900" href="#">
                개인정보처리방침
              </Button>
              <Button className="text-sm text-gray-500 hover:text-gray-900" href="#">
                고객지원
              </Button>
              <Button className="text-sm text-gray-500 hover:text-gray-900" href="#">
                파트너 문의
              </Button>
            </div>
          </div>
        </div>
        <div>
          <div className="flex items-center space-x-6">
            <Twitter className="h-6 w-6 text-gray-400 hover:text-gray-500" />
            <Instagram className="h-6 w-6 text-gray-400 hover:text-gray-500" />
            <Facebook className="h-6 w-6 text-gray-400 hover:text-gray-500" />
            <Youtube className="h-6 w-6 text-gray-400 hover:text-gray-500" />
          </div>
          <div className="flex items-center space-x-4">
            <img
              alt="Download on the App Store"
              height="24"
              src="/src/assets/img/appstore.png"
              class="app-store-img"
            />
            <img
              alt="Get it on Google Play"
              height="24"
              src="/src/assets/img/googleplay.png"
              class="google-store-img"
            />
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-store-img {
  width: 100px; /* 이미지 너비 조정 */
}

.google-store-img {
  width: 115px;
}
</style>

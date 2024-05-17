<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import SocialCard from '@/components/social/SocialCard.vue'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'

const route = useRoute()
const router = useRouter()

const authentication = useAuthenticationStore()
const { isLogin } = storeToRefs(authentication)

const goSocialCreate = () => {
  router.push({ name: 'social-create' })
}

const datas = ref([])
const reviewLen = ref()
const noticeLen = ref()
const userLen = ref()
onMounted(() => {
  const keyword = route.query.keyword
  console.log(keyword)
  const addr = `http://localhost:8080/api/v1/search?searchKeyword=${keyword}`

  axios
    .get(addr)
    .then((response) => {
      console.log(response)
      datas.value = response.data
      reviewLen.value = datas.value.reviews.length
      noticeLen.value = datas.value.notices.length
      userLen.value = datas.value.users.length
    })
    .catch((error) => {
      console.log('전체 조회 실패', error)
    })
})

watch(datas, () => {})
</script>

<template>
  <div className="w-full px-10">
    <div className="flex justify-between ">
      <div className="flex space-x-2 text-sm">
        <Badge variant="default">전체</Badge>
        <Badge>여행 리뷰 {{ reviewLen }}</Badge>
        <Badge>공지사항 {{ noticeLen }}</Badge>
        <Badge>프로필 {{ userLen }}</Badge>
      </div>
      <div className="flex items-center">
        <Input
          className="shadow-md rounded-full pl-4 pr-10 h-8 w-64"
          placeholder="검색어를 입력해주세요."
          type="search"
        />
      </div>
    </div>
    <div className="mt-4">
      <h2 className="text-lg font-semibold">여행 리뷰</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <SocialCard
            :social-info="review"
            v-for="review in datas.reviews"
            :key="review.reviewId"
          />
        </div>
      </div>
      <!-- v-if="isLogin" 추가 해야 함 -->
      <div class="flex justify-end mt-5">
        <button
          @click="goSocialCreate"
          class="mt-5 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-full shadow-md"
        >
          글 작성하기
        </button>
      </div>
      <h2 className="text-lg font-semibold mt-10">공지사항</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <SocialCard
            :social-info="notice"
            v-for="notice in datas.notices"
            :key="notice.noticeId"
          />
        </div>
      </div>
      <h2 className="text-lg font-semibold mt-10">프로필</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <SocialCard :social-info="user" v-for="user in datas.users" :key="user.userId" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>

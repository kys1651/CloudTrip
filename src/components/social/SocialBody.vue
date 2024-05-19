<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import SocialCard from '@/components/social/SocialCard.vue'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion'
import ReviewCard from '@/components/review/ReviewCard.vue'

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

const isATagExists = (content: string) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content
  return htmlElement.querySelector('a') !== null
}

const goUserProfile = (user: Object) => {
  router.push({ name: 'userDetail', params: { userId: user.userId } })
}
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
    </div>
    <div className="mt-4">
      <h2 className="text-lg font-semibold">여행 리뷰</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <ReviewCard v-for="review in datas.reviews" :key="review.reviewId" :review="review" />
        </div>
      </div>
      <div class="flex justify-end mt-5"></div>
      <h2 className="text-lg font-semibold mt-10">공지사항</h2>
      <div className="grid gap-6 mt-4">
        <Accordion type="multiple" class="w-full" collapsible>
          <AccordionItem
            v-for="notice in datas.notices"
            :key="notice.noticeId"
            :value="notice.content"
          >
            <AccordionTrigger>📢 {{ notice.title }}</AccordionTrigger>
            <AccordionContent>
              <div class="flex justify-between items-center">
                <p
                  class="flex-grow"
                  :class="{ aTag: isATagExists(notice.content) }"
                  v-html="notice.content"
                ></p>
              </div>
            </AccordionContent>
          </AccordionItem>
        </Accordion>
      </div>
      <h2 className="text-lg font-semibold mt-10">프로필</h2>
      <div className="grid justify-center mt-4">
        <div className="grid grid-cols-3 gap-4 justify-center">
          <SocialCard
            @click="goUserProfile(user)"
            :user-info="user"
            v-for="user in datas.users"
            :key="user.userId"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
Badge {
  height: 280px;
}
</style>

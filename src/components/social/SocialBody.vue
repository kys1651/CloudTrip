<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { AvatarFallback, Avatar, AvatarImage } from '@/components/ui/avatar'
import { CardContent, Card } from '@/components/ui/card'
import SocialCard from '@/components/social/SocialCard.vue'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const goSocialCreate = () => {
  router.push({ name: 'social-create' })
}

const addr = `http://localhost:8080/api/v1/social/list`
const socials = ref([])
onMounted(() => {
  axios
    .get(addr)
    .then((response) => {
      console.log(response)
      socials.value = response.data
    })
    .catch((error) => {
      console.log('전체 조회 실패', error)
    })
})
</script>

<template>
  <div className="max-w-4xl mx-auto p-6">
    <div className="flex justify-between">
      <div className="flex space-x-2 text-sm">
        <Badge variant="default">전체</Badge>
        <Badge>포스팅(99+)</Badge>
        <Badge>회사(5)</Badge>
        <Badge>커리어(76)</Badge>
        <Badge>소식(99+)</Badge>
        <Badge>프로필(0)</Badge>
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
      <h2 className="text-lg font-semibold">소식 103</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <SocialCard :social-info="social" v-for="social in socials" :key="social.socialId" />
        </div>
      </div>
      <button
        @click="goSocialCreate"
        class="mt-5 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-full shadow-md"
      >
        글 작성하기
      </button>
    </div>
  </div>
</template>

<style scoped></style>

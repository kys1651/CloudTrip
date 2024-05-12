<script setup lang="ts">
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'

import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import NoticeTextarea from '@/components/notice/NoticeTextarea.vue'

const route = useRoute()
const router = useRouter()

const noticeId = route.params.noticeId
const addr = `http://localhost:8080/api/v1/notice/view/${noticeId}`

const notice = ref([])
onMounted(() => {
  axios
    .get(addr)
    .then((response) => {
      console.log(response)
      notice.value = response.data
    })
    .catch((error) => {
      console.log('공지사항 조회 오류 발생', error)
    })
})

const goHome = () => {
  router.push({ name: 'notice' })
}

const goUpdate = () => {
  router.push({ name: 'notice-modify' })
}
</script>

<template>
  <div class="flex items-center justify-center h-screen">
    <Card class="w-[600px] h-[500px]">
      <CardHeader>
        <CardTitle>공지사항 상세보기</CardTitle>
        <CardDescription>글 작성자 : Admin</CardDescription>
      </CardHeader>
      <CardContent>
        <form>
          <div class="grid items-center w-full gap-4">
            <div class="flex flex-col space-y-1.5">
              <Label for="name">제목</Label>
              <Card class="h-[70px] p-2">{{ notice.title }}</Card>
            </div>
            <Label for="name">내용</Label>
            <Card class="h-[150px] p-2">{{ notice.content }}</Card>
            <div class="flex flex-col space-y-1.5">
              <Label for="framework">#공지사항</Label>
            </div>
          </div>
        </form>
      </CardContent>
      <CardFooter class="flex justify-between px-6 pb-6">
        <Button variant="outline" @click="goHome"> 목록으로 </Button>
        <Button @click="goUpdate">글 수정</Button>
      </CardFooter>
    </Card>
  </div>
</template>

<style scoped></style>

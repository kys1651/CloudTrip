<script setup lang="ts">
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'

import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const noticeId = route.params.noticeId
const addr = `http://localhost:8080/api/v1/notice/view/${noticeId}`

const notice = ref({
  title: '',
  content: ''
})
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
  router.push({ name: 'adminNotice' })
}

const goUpdate = () => {
  router.push({ name: 'notice-modify' })
}

const isATagExists = (content: String) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content
  return htmlElement.querySelector('a') !== null
}
</script>

<template>
  <div class="flex items-center justify-center h-screen">
    <Card class="w-[1200px] h-[800px]">
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
            <Card
              class="h-[400px] p-2 overflow-y-auto"
              :class="{ aTag: isATagExists(notice.content) }"
              v-html="notice.content"
            ></Card>
          </div>
        </form>
      </CardContent>
      <div class="flex flex-col space-y-1.5 mt-10 px-10">
        <Label for="framework">#공지사항</Label>
      </div>
      <CardFooter class="flex justify-between px-6 pb-6 mt-10">
        <Button variant="outline" @click="goHome"> 목록으로 </Button>
        <Button @click="goUpdate">글 수정</Button>
      </CardFooter>
    </Card>
  </div>
</template>

<style scoped></style>

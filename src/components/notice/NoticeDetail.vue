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
import { getNoticeDetail } from '@/api/notice'

const route = useRoute()
const router = useRouter()

const noticeId = route.params.noticeId

const notice = ref({
  title: '',
  content: ''
})
onMounted(() => {
  getNoticeDetail(noticeId.toString())
    .then((data) => {
      notice.value = data
    })
    .catch((error) => {})
})

const goHome = () => {
  router.push({ name: 'adminNotice' })
}

const goUpdate = () => {
  router.push({ name: 'notice-modify' })
}

const isATagExists = (content: String) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content.toString()
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
              ><p v-html="notice.content"></p>
            </Card>
          </div>
        </form>
      </CardContent>
      <CardFooter class="flex justify-between px-6 pb-6 mt-10">
        <Button variant="outline" @click="goHome"> 목록으로 </Button>
        <Button @click="goUpdate">글 수정</Button>
      </CardFooter>
    </Card>
  </div>
</template>

<style scoped></style>

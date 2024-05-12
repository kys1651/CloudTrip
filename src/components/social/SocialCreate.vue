<script setup lang="ts">
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import { CardContent, Card } from '@/components/ui/card'

import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ref, onMounted } from 'vue'

const route = useRoute()
const router = useRouter()

const goSocialList = () => {
  router.push({ name: 'social' })
}

const addr = `http://localhost:8080/api/v1/social/create`
const article = ref({
  userId: 1, // 나중에 바꾸기
  title: '',
  content: ''
})

const postArticle = () => {
  console.log(article.value.userId)
  console.log(article.value.title)
  console.log(article.value.content)
  axios
    .post(addr, {
      userId: article.value.userId,
      title: article.value.title,
      content: article.value.content
    })
    .then((response) => {
      console.log(response)
      router.push({ name: 'social' })
    })
    .catch((error) => {
      console.log('게시글 등록 실패', error)
    })
}
</script>

<template>
  <div className="px-4 py-6 md:px-6 md:py-12 lg:py-16">
    <article className="prose prose-gray mx-auto max-w-6xl dark:prose-invert">
      <div className="space-y-4 not-prose">
        <h1 className="text-2xl font-extrabold tracking-tight lg:text-3xl">게시글 작성하기</h1>
        <div className="flex items-center gap-4 dark:text-gray-400">
          <div class="mb-3"><input type="text" placeholder="제목" v-model="article.title" /></div>
          <div class="mb-3">
            <span className="font-medium">@글 작성자 : {{ article.userId }}</span>
          </div>
        </div>
      </div>
      <hr />
      <div>
        <textarea
          class="mt-5 border w-full h-60"
          placeholder="본문내용1"
          v-model="article.content"
        ></textarea>
      </div>

      <div className="flex justify-end gap-4 mt-20">
        <Button
          @click="goSocialList"
          className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
        >
          작성 취소
        </Button>
        <Button
          @click="postArticle"
          className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
        >
          글 작성
        </Button>
      </div>
    </article>
  </div>
</template>

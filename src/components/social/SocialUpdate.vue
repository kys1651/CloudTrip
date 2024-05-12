<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
import Comment from '@/components/social/Comment.vue'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const socialId = route.params.socialId
const goSocialView = () => {
  router.push({ name: 'social-detail', params: { socialId: socialId } })
}

const addr = `http://localhost:8080/api/v1/social/${socialId}`
const articleInfo = ref([])
onMounted(() => {
  axios
    .get(addr)
    .then((response) => {
      console.log('글 상세보기 성공')
      articleInfo.value = response.data
    })
    .catch((error) => {
      console.log('글 상세보기 실패', error)
    })
})

const modifyAddr = `http://localhost:8080/api/v1/social/modify/${socialId}`
const modifyArticle = () => {
  axios
    .put(modifyAddr, {
      socialId: socialId,
      title: articleInfo.value.title,
      content: articleInfo.value.content
    })
    .then((response) => {
      console.log('글 수정 성공')
      router.push({ name: 'social-detail', params: { socialId: socialId } })
    })
    .catch((error) => {
      console.log('글 수정 실패', error)
    })
}

const deleteAddr = `http://localhost:8080/api/v1/social/delete/${socialId}`
const deleteArticle = () => {
  if (confirm('정말 삭제하시겠습니까?')) {
    axios
      .delete(deleteAddr)
      .then((response) => {
        console.log('글 삭제 성공')
        router.push({ name: 'social' })
      })
      .catch((error) => {
        console.log('글 삭제 실패', error)
      })
  }
}
</script>

<template>
  <div className="px-4 py-6 md:px-6 md:py-12 lg:py-16">
    <article className="prose prose-gray mx-auto max-w-6xl dark:prose-invert">
      <div className="space-y-4 not-prose">
        <input type="text" className="text-4xl font-extrabold tracking-tight lg:text-5xl" v-model="articleInfo.title">
        </input>
        <div className="flex items-center gap-4 text-gray-500 dark:text-gray-400">
          <div class="mb-3">
            <span className="font-medium">@ {{ articleInfo.userId }}</span>
            <span className="mx-2">•</span>
            <span>{{ articleInfo.createdAt }}</span>
            <span className="mx-2">•</span>
            <span>{{ articleInfo.viewCount }}</span>
          </div>
        </div>
      </div>
      <hr />
      <textarea class="w-full h-60 mt-5" v-model="articleInfo.content"></textarea>
      <div className="flex justify-end gap-4 mt-20">
        <Button
          @click="goSocialView"
          className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
        >
          수정취소
        </Button>
        <Button
          @click="modifyArticle"
          className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
        >
          수정완료
        </Button>
        <Button
          @click="deleteArticle"
          className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
        >
          글 삭제
        </Button>
      </div>
    </article>
  </div>
  <div className="px-4 py-6 md:px-6 md:py-12 lg:py-16">
    <div className="prose prose-gray mx-auto max-w-6xl dark:prose-invert">
      <h2 className="text-2xl font-bold mb-3">댓글</h2>
      <hr />
      <div className="space-y-6">
        <Comment />
        <Comment />
        <Comment />
      </div>
    </div>
  </div>
</template>

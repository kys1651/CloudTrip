<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
import Comment from '@/components/social/Comment.vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const goSocialList = () => {
  router.push({ name: 'social' })
}

const goSocialUpdate = () => {
  router.push({ name: 'social-modify', params: { socialId: 1 } })
}
</script>

<template>
  <div className="px-4 py-6 md:px-6 md:py-12 lg:py-16">
    <article className="prose prose-gray mx-auto max-w-6xl dark:prose-invert">
      <div className="space-y-4 not-prose">
        <h1 className="text-4xl font-extrabold tracking-tight lg:text-5xl">게시글 제목</h1>
        <div className="flex items-center gap-4 text-gray-500 dark:text-gray-400">
          <div class="mb-3">
            <span className="font-medium">@글 작성자</span>
            <span className="mx-2">•</span>
            <span>글 작성시기</span>
            <span className="mx-2">•</span>
            <span>조회수</span>
          </div>
        </div>
      </div>
      <hr />
      <p class="mt-5">본문내용1</p>
      <p class="mt-5">본문내용2</p>
      <div className="flex justify-end gap-4 mt-20">
        <Button
          @click="goSocialList"
          className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
        >
          목록으로
        </Button>
        <Button
          @click="goSocialUpdate"
          className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
        >
          글 수정하기
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

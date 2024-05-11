<script setup lang="ts">
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import { CardContent, Card } from '@/components/ui/card'

import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const goSocialList = () => {
  router.push({ name: 'social' })
}
</script>

<template>
  <div className="px-4 py-6 md:px-6 md:py-12 lg:py-16">
    <article className="prose prose-gray mx-auto max-w-6xl dark:prose-invert">
      <div className="space-y-4 not-prose">
        <h1 className="text-4xl font-extrabold tracking-tight lg:text-3xl">게시글 제목</h1>
        <div className="flex items-center gap-4 text-gray-500 dark:text-gray-400">
          <div class="mb-3">
            <span className="font-medium">@글 작성자</span>
          </div>
        </div>
      </div>
      <hr />
      <div>
        <textarea class="mt-5 border w-full" placeholder="본문내용1"></textarea>
      </div>

      <div className="flex justify-end gap-4 mt-20">
        <Button
          @click="goSocialList"
          className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
        >
          작성 취소
        </Button>
        <Button
          className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
        >
          글 작성
        </Button>
      </div>
    </article>
  </div>
</template>

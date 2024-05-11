<script setup>
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { AvatarFallback, Avatar, AvatarImage } from '@/components/ui/avatar'
import { CardContent, Card } from '@/components/ui/card'

import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const goSocialDetail = () => {
  router.push({ name: 'social-detail', params: { socialId: 1 } })
}
</script>

<template>
  <Card className="col-span-1 shadow-md">
    <CardContent>
      <div className="flex items-center space-x-3">
        <Avatar>
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
        <div className="flex flex-col">
          <p className="font-medium">딘지</p>
          <span className="text-sm text-gray-500">게시물 · 2022.08.26</span>
        </div>
      </div>
      <div className="mt-2 text-sm" @click="goSocialDetail">
        Si 언제적에 프로필에디트를 작업하더라며
        <br />
        뭘 궁금하게 쓸까요
      </div>
    </CardContent>
  </Card>
</template>

<style scoped></style>

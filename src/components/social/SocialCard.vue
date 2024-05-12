<script setup>
import { AvatarFallback, Avatar } from '@/components/ui/avatar'
import { CardContent, Card } from '@/components/ui/card'

import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const goSocialDetail = () => {
  router.push({ name: 'social-detail', params: { socialId: props.socialInfo.socialId } })
}

const props = defineProps({
  socialInfo: Object
})
</script>

<template>
  <Card className="col-span-1 shadow-md">
    <CardContent>
      <div className="flex items-center space-x-3">
        <Avatar>
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
        <div className="flex flex-col">
          <p className="font-medium">{{ socialInfo.socialId }}</p>
          <span className="text-sm text-gray-500">[글 작성일]</span>
          <span className="text-sm text-gray-500">{{ socialInfo.createdAt }}</span>
        </div>
      </div>
      <div className="mt-2 text-sm" @click="goSocialDetail">
        {{ socialInfo.content }}
      </div>
    </CardContent>
  </Card>
</template>

<style scoped></style>

<script setup>
import { AvatarFallback, Avatar, AvatarImage } from '@/components/ui/avatar'
import { CardContent, Card } from '@/components/ui/card'

import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const goSocialDetail = () => {
  router.push({ name: 'social-detail', params: { socialId: props.userInfo.userId } })
}

const props = defineProps({
  userInfo: Object
})
</script>

<template>
  <Card className="col-span-1 shadow-md">
    <CardContent>
      <div className="flex items-center space-x-3">
        <Avatar>
          <AvatarImage :src="userInfo.profileImage" alt="No Img" />
          <AvatarFallback></AvatarFallback>
        </Avatar>
        <div className="flex flex-col">
          <p className="font-medium">{{ userInfo.nickname }}</p>
          <span className="text-sm text-gray-500">[이메일]</span>
          <span className="text-sm text-gray-500">{{ userInfo.email }}</span>
        </div>
      </div>
      <!-- <div className="mt-2 text-sm" @click="goSocialDetail">
        {{ userInfo.content }}
      </div> -->
    </CardContent>
  </Card>
</template>

<style scoped></style>

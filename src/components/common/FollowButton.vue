<script setup lang="ts">
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import Button from '../ui/button/Button.vue'
import { ref, watch } from 'vue'
import { useQuery, useQueryClient } from '@tanstack/vue-query'
import { followCheckRequest, followRequest, unFollowRequest } from '@/api/user'
import { toast } from '../ui/toast'

const queryClient = useQueryClient()
const props = defineProps<{ followeeId: number; nickname: string }>()
const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const isFollow = ref<boolean>(false)
const { data: followData } = useQuery<boolean>({
  queryKey: ['follow', profile?.value?.id, props.followeeId],
  queryFn: () => followCheckRequest(props.followeeId),
  enabled: profile.value && profile.value.id !== props.followeeId
})

watch(followData, (newData) => {
  if (newData !== undefined) {
    isFollow.value = newData
  }
})

const invalidateRelatedQueries = () => {
  const queryKeys = [
    ['follow', profile.value?.id, props.followeeId],
    ['follower', props.followeeId],
    ['following', profile.value?.id],
    ['followerCount', props.followeeId],
    ['followingCount', profile.value?.id]
  ]
  queryKeys.forEach((queryKey) => queryClient.invalidateQueries({ queryKey }))
}

const followUnFollow = async () => {
  const request = isFollow.value ? unFollowRequest : followRequest
  const action = isFollow.value ? '언팔로우' : '팔로우'

  const isSuccess = await request(props.followeeId)
  if (isSuccess) {
    toast({
      title: `${action} 성공`,
      description: `${props.nickname}을/를 ${action} 하였습니다.`,
      variant: 'success'
    })
    isFollow.value = !isFollow.value
    invalidateRelatedQueries()
  } else {
    toast({
      title: `${action} 실패`,
      description: `${props.nickname} ${action}를 실패했습니다. 다시 시도해주세요.`,
      variant: 'destructive'
    })
  }
}
</script>

<template>
  <Button
    v-if="profile && followeeId !== profile.id"
    class="mr-auto ml-2"
    :variant="isFollow ? 'outline' : 'default'"
    size="xs"
    @click="followUnFollow"
    >{{ isFollow ? '팔로잉' : '팔로우' }}</Button
  >
</template>

<style scoped></style>
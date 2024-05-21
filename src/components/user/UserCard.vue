<script setup lang="ts">
import type { IUserCard } from '@/types/user.type'
import Button from '@/components/ui/button/Button.vue'
import { useMutation, useQueryClient } from '@tanstack/vue-query'
import { followRequest, unFollowRequest } from '@/api/user'
import { ref } from 'vue'
import { toast } from '@/components/ui/toast'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

const router = useRouter()
const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const queryClient = useQueryClient()
const props = defineProps<{
  user: IUserCard
  invalidId: number
}>()

const isFollowing = ref<boolean>(props.user.isFollowing)
const { mutate: followMutate } = useMutation({
  mutationKey: ['user', props.invalidId, 'follow'],
  mutationFn: () =>
    isFollowing.value ? unFollowRequest(props.user.userId) : followRequest(props.user.userId),
  onSuccess: () => {
    isFollowing.value = !isFollowing.value
    queryClient.invalidateQueries({
      queryKey: ['user', props.invalidId]
    })
    const action = isFollowing.value ? '팔로우' : '언팔로우'
    toast({
      title: `${action} 성공`,
      description: `${props.user.nickname}을/를 ${action} 하였습니다.`,
      variant: 'success'
    })
  }
})

const moveDetail = () => {
  window.location.href = `/user/${props.user.userId}`
}
</script>

<template>
  <div @click="moveDetail" class="user-card mb-4 p-4 border rounded-lg flex items-center">
    <img :src="user.profileImage" alt="프로필 이미지" class="w-10 h-10 rounded-full mr-4" />
    <p class="flex-1">{{ user.nickname }}</p>

    <div class="text-blue-600 dark:text-blue-400" v-if="profile && profile.id === user.userId">
      나
    </div>
    <Button
      v-else
      @click.stop="followMutate"
      class="ml-auto"
      :variant="isFollowing ? 'outline' : 'default'"
      size="xs"
    >
      {{ isFollowing ? '팔로잉' : '팔로우' }}
    </Button>
  </div>
</template>

<style scoped>
.user-card {
  background-color: #f9f9f9;
}
</style>

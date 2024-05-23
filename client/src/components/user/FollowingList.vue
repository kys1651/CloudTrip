<script setup lang="ts">
import { follwerRequest, follwingRequest } from '@/api/user'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from '@/components/ui/dialog'
import UserCard from '@/components/user/UserCard.vue'
import type { IUserCard } from '@/types/user.type'
import { useQuery } from '@tanstack/vue-query'
import { defineProps, ref, watch } from 'vue'
const emit = defineEmits(['update:open'])
const props = defineProps<{
  open: boolean
  nickname: string
  userId: number
}>()

const { data: following } = useQuery<IUserCard[]>({
  queryKey: ['user', props.userId, 'following'],
  queryFn: () => follwingRequest(props.userId)
})

const isOpen = ref(props.open)
watch(
  () => props.open,
  (newValue) => {
    isOpen.value = newValue
  }
)
watch(isOpen, (newValue) => {
  emit('update:open', newValue)
})
</script>

<template>
  <Dialog v-model:open="isOpen">
    <DialogContent class="sm:max-w-[425px] grid-rows-[auto_minmax(0,1fr)_auto] p-0 max-h-[90dvh]">
      <DialogHeader class="p-6 pb-0">
        <DialogTitle>{{ nickname }}님이 팔로우하는 유저</DialogTitle>
        <DialogDescription>팔로워 수: {{ following ? following.length : 0 }}</DialogDescription>
      </DialogHeader>
      <div class="grid gap-4 py-4 overflow-y-auto px-6">
        <div class="flex flex-col">
          <template v-if="following && following.length > 0">
            <UserCard
              v-for="user in following"
              :key="user.userId"
              :user="user"
              :invalidId="userId"
            />
          </template>
          <template v-else>
            <div class="text-center font-semibold">팔로우하는 유저가 존재하지 않습니다.</div>
          </template>
        </div>
      </div>
      <DialogFooter class="p-6 pt-0"> </DialogFooter> </DialogContent
  ></Dialog>
</template>
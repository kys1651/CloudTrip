<!-- src/components/CardDropdownMenu.vue -->
<template>
  <DropdownMenu>
    <DropdownMenuTrigger>
      <Button class="ml-auto w-8 h-8 rounded-full" size="icon" variant="ghost">
        <Ellipsis class="w-4 h-4" />
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end">
      <DropdownMenuItem @click="onDelete">
        <Eraser class="w-4 h-4 mr-2" />
        삭제하기
      </DropdownMenuItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>

<script setup lang="ts">
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem
} from '@/components/ui/dropdown-menu'
import Button from '@/components/ui/button/Button.vue'
import { Ellipsis, Eraser } from 'lucide-vue-next'
import { toast } from '../ui/toast'
import { useQueryClient } from '@tanstack/vue-query'
import { useRouter } from 'vue-router'
import { deleteScheduleListRequest } from '@/api/schedule'

const props = defineProps<{
  scheduleId: number
}>()
const queryClient = useQueryClient()
const router = useRouter()

const onDelete = async () => {
  if (!confirm('정말 여행 계획을 삭제하시겠습니까? ')) {
    return
  }
  const isSuccess = await deleteScheduleListRequest(props.scheduleId)
  if (isSuccess) {
    toast({
      title: '여행 계획 삭제',
      description: '여행 계획 삭제가 완료되었습니다.',
      variant: 'success'
    })
    queryClient.invalidateQueries({
      queryKey: ['scheduleList']
    })
    router.go(0)
  } else {
    toast({
      title: '여행 계획 삭제 실패',
      description: '여행 계획 삭제가 실패하였습니다. 잠시 후 다시 시도해주세요',
      variant: 'destructive'
    })
  }
}
</script>

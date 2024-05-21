<!-- src/components/CardDropdownMenu.vue -->
<template>
  <DropdownMenu>
    <DropdownMenuTrigger>
      <Button class="ml-auto w-8 h-8 rounded-full" size="icon" variant="ghost">
        <Ellipsis class="w-4 h-4" />
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end">
      <RouterLink :to="{ name: 'review-update', params: { reviewId: reviewId } }">
        <DropdownMenuItem>
          <PenLine class="w-4 h-4 mr-2" />
          수정하기
        </DropdownMenuItem>
      </RouterLink>
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
import { Ellipsis, PenLine, Eraser } from 'lucide-vue-next'
import { reviewDeleteRequest } from '@/api/review'
import { toast } from '../ui/toast'
import { useQueryClient } from '@tanstack/vue-query'
import { useRouter } from 'vue-router'

const props = defineProps(['reviewId'])
const queryClient = useQueryClient()
const router = useRouter()

const onDelete = async () => {
  if (!confirm('정말 여행지 후기를 삭제하시겠습니까? ')) {
    return
  }
  const isSuccess = await reviewDeleteRequest(props.reviewId)
  if (isSuccess) {
    toast({
      title: '여행지 후기 삭제',
      description: '여행지 후기 삭제가 완료되었습니다.',
      variant: 'success'
    })
    queryClient.invalidateQueries({
      queryKey: ['reviews']
    })
    router.push({ name: 'review' })
  } else {
    toast({
      title: '여행지 후기 삭제',
      description: '여행지 후기 삭제가 실패하였습니다. 잠시 후 다시 시도해주세요',
      variant: 'destructive'
    })
  }
}
</script>

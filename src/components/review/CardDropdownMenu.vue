<!-- src/components/CardDropdownMenu.vue -->
<template>
  <DropdownMenu>
    <DropdownMenuTrigger>
      <Button class="ml-auto w-8 h-8 rounded-full" size="icon" variant="ghost">
        <Ellipsis class="w-4 h-4" />
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end">
      <DropdownMenuItem @click="onEdit">
        <PenLine class="w-4 h-4 mr-2" />
        수정하기
      </DropdownMenuItem>
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
import { defineEmits } from 'vue'

const emits = defineEmits(['edit', 'delete'])

// 함수 정의
const onEdit = () => {
  emits('edit')
}

const onDelete = () => {
  emits('delete')
}
</script>

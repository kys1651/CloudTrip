<script setup lang="ts">
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
</script>

<template>
  <div className="flex items-start gap-4">
    <Avatar className="w-10 h-10 border">
      <AvatarImage alt="@shadcn" src="/placeholder-user.jpg" />
      <AvatarFallback>Image</AvatarFallback>
    </Avatar>
    <div className="grid gap-1.5">
      <div className="flex items-center gap-2">
        <div className="font-semibold">@ 댓글 작성자 1</div>
        <div className="text-gray-500 text-xs dark:text-gray-400">May 11, 2024</div>
      </div>
      <div>댓글 내용 1</div>
    </div>
  </div>
</template>

<style></style>

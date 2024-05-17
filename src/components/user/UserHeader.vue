<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
</script>

<template>
  <div className="flex items-center justify-between mb-6">
    <div>
      <h1 className="text-2xl font-semibold">회원관리</h1>
      <p className="text-gray-600">회원들을 관리할 수 있습니다.</p>
    </div>
    <div className="flex items-center space-x-4">
      <Input className="w-64 border rounded-md p-2" placeholder="회원 검색" />
      <Button variant="outline">검색</Button>
    </div>
  </div>
</template>

<style scoped></style>

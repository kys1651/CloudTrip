<script setup lang="ts">
import AdminNoticeBody from '@/components/notice/AdminNoticeBody.vue'
import { ref } from 'vue'

const emit = defineEmits({
  pagingNum: Number
})

const pagingNum = ref()
</script>

<template>
  <div className="container w-full mx-auto my-8 p-6 bg-white shadow rounded-lg">
    <AdminNoticeBody />
  </div>
</template>

<style scoped></style>

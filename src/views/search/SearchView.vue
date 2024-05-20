<script setup lang="ts">
import SearchListView from '@/views/search/SearchListView.vue'
</script>

<template>
  <div class="containerx justify-center flex flex-col my-6">
    <!-- :key="$route.fullPath" : 같은 페이지 이동 -->
    <SearchListView :key="$route.fullPath" />
  </div>
</template>

<script setup lang="ts">
import SearchListView from '@/views/search/SearchListView.vue'
</script>

<template>
  <div class="containerx justify-center flex flex-col my-6">
    <SearchListView :key="$route.fullPath" />
  </div>
</template>

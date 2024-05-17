<script setup lang="ts">
import SocialListView from '../social/SocialListView.vue'
</script>

<template>
  <div class="containerx justify-center flex flex-col my-6">
    <!-- :key="$route.fullPath" : 같은 페이지 이동 -->
    <SocialListView :key="$route.fullPath" />
  </div>
</template>

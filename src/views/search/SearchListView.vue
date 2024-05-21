<script setup lang="ts">
import SearchHeader from '@/components/search/SearchHeader.vue'
import SearchBody from '@/components/search/SearchBody.vue'
import CommonFooter from '@/components/common/CommonFooter.vue'

import { watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
watch(route, () => {
  console.log(route.query.keyword)
})
</script>

<template>
  <div class="container justify-center flex flex-col my-6 items-start">
    <SearchHeader />
    <SearchBody />
  </div>
  <CommonFooter />
</template>

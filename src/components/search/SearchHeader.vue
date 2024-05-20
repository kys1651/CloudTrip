<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { Search } from 'lucide-vue-next'

const route = useRoute()

const keyword = route.query.keyword
</script>

<template>
  <div class="flex flex-col my-6 items-center w-full mb-20 h-7">
    <div class="flex items-center bg-slate-100 p-3 rounded-lg">
      <input
        type="text"
        :value="keyword"
        class="w-[400px] h-[50px] text-xl p-3 rounded-lg mr-5 outline-none"
        readonly
      />
      <Search class="ml-4 cursor-pointer no-hover-effect" />
    </div>
  </div>
</template>

<style scoped>
.no-hover-effect:hover {
  transform: none;
  cursor: default;
}
</style>

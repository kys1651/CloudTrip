<script setup lang="ts">
import SocialBox from '@/components/social/SocialBox.vue'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const keyword = route.query.keyword

const searchByKeyword = () => {
  router.push({ name: 'search', query: { keyword: searchKeyword.value } })
}
</script>

<template>
  <div class="flex flex-col my-6 items-center w-full mb-20 h-7">
    <div class="flex">
      <input
        type="text"
        placeholder="검색어를 입력하세요."
        :value="keyword"
        class="w-[500px] text-xl p-2 border-2 border-black rounded-lg mr-5"
        readonly
      />
      <button @click="searchByKeyword" class="text-xl p-2 ml-2 text-white rounded-lg">🔍</button>
    </div>
  </div>
</template>

<style scoped></style>

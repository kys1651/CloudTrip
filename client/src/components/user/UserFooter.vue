<script setup lang="ts">
import CustomPagination from '@/components/common/CustomPagination.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const totalPages = ref(5)
const users = ref([])

const postsPerPage = ref(10)

const router = useRouter()
const updateCurrentPage = (pageIdx: number) => {
  router.push({ name: 'notice', query: { page: pageIdx } })
}
</script>

<template>
  <div class="w-full mt-6 flex justify-center">
    <CustomPagination
      @page-number="updateCurrentPage"
      :total-page="totalPages.toString()"
      :total-post="users.length.toString()"
      :items-per-page="postsPerPage.toString()"
    />
  </div>
</template>

<style scoped></style>

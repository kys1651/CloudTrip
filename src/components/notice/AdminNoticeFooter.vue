<script setup lang="ts">
import Pagination from "@/components/common/Pagination.vue";
import { ref } from "vue";
import { useRouter } from "vue-router";

const totalPages = ref(5);
const notices = ref([]);

const router = useRouter();

const postsPerPage = ref(10);

const updateCurrentPage = (pageIdx: number) => {
  router.push({ name: "notice", query: { page: pageIdx } });
};
</script>

<template>
  <div class="w-full mt-6 flex justify-center">
    <Pagination
      @page-number="updateCurrentPage"
      :total-page="totalPages.toString()"
      :total-post="notices.length.toString()"
      :items-per-page="postsPerPage.toString()"
    />
  </div>
</template>

<style scoped></style>

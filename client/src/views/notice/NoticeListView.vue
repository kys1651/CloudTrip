<script setup lang="ts">
import { noticeListRequest } from '@/api/notice'
import CustomPagination from '@/components/common/CustomPagination.vue'
import NoticeHeader from '@/components/notice/NoticeHeader.vue'
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion'
import { useQuery } from '@tanstack/vue-query'
import { computed, onUpdated, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const { data: notices } = useQuery({
  queryKey: ['notices'],
  queryFn: () => noticeListRequest()
})

onUpdated(() => {
  pageNumber.value = Number(route.query.page)
})

const pageNumber = ref<number>(1)
const postsPerPage = ref(7)

const updateCurrentPage = (pageIdx: number) => {
  router.push({ name: 'notice', query: { page: pageIdx } })
}

const displayedPosts = computed(() => {
  const startIndex = (pageNumber.value - 1) * postsPerPage.value || 0
  const endIndex = startIndex + postsPerPage.value || postsPerPage.value
  return notices.value?.slice(startIndex, endIndex)
})

const totalPages = computed(() => {
  return Math.ceil((notices.value?.length || 0) / postsPerPage.value)
})

const isATagExists = (content: string) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content
  return htmlElement.querySelector('a') !== null
}

// Define the formatDate function within the script setup block
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString(undefined)
}
</script>

<template>
  <NoticeHeader />
  <div class="container justify-center flex flex-col my-6 items-start">
    <Accordion type="multiple" class="w-full" collapsible>
      <AccordionItem
        v-for="notice in displayedPosts"
        :key="notice.noticeId"
        :value="notice.content"
      >
        <AccordionTrigger
          ><div class="flex justify-between items-center w-full">
            <span>📢 {{ notice.title }}</span>
            <span class="mx-5 text-gray-500 text-sm">{{
              formatDate(notice.createdAt.toString())
            }}</span>
          </div></AccordionTrigger
        >
        <AccordionContent>
          <div class="flex justify-between items-center">
            <p
              class="flex-grow"
              :class="{ aTag: isATagExists(notice.content) }"
              v-html="notice.content"
            ></p>
          </div>
        </AccordionContent>
      </AccordionItem>
    </Accordion>
    <div class="w-full flex justify-center items-center mt-5"></div>
    <div class="w-full mt-6 flex justify-center">
      <CustomPagination
        @page-number="updateCurrentPage"
        :total-page="totalPages.toString()"
        :total-post="notices?.length || 0"
        :items-per-page="postsPerPage.toString()"
      />
    </div>
  </div>
</template>

<style>
.aTag a {
  color: rgb(98, 98, 248);
  text-decoration-line: underline;
}
</style>

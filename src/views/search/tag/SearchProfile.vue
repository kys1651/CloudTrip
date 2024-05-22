<script setup lang="ts">
import { searchResult } from '@/api/search'
import SearchCard from '@/components/search/SearchCard.vue'
import SearchHeader from '@/components/search/SearchHeader.vue'
import { Badge } from '@/components/ui/badge'
import { useAuthenticationStore } from '@/stores/authentication'
import { type ISearch } from '@/types/search.type'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const authentication = useAuthenticationStore()

const datas = ref<ISearch>()
const reviewLen = ref<number>(0)
const noticeLen = ref<number>(0)
const userLen = ref<number>(0)
onMounted(() => {
  const keyword = route.query.keyword
  if (!keyword) return
  console.log(keyword)

  searchResult(keyword?.toString())
    .then((data) => {
      console.log(data)
      datas.value = data
      reviewLen.value = datas.value.reviews.length
      noticeLen.value = datas.value.notices.length
      userLen.value = datas.value.users.length
    })
    .catch((error) => {
      console.log('전체 조회 실패', error)
    })
})

const isATagExists = (content: string) => {
  const htmlElement = document.createElement('div')
  htmlElement.innerHTML = content
  return htmlElement.querySelector('a') !== null
}

const goUserProfile = (userId: number) => {
  router.push({ name: 'userDetail', params: { userId: userId } })
}

// tag
const goSearch = () => {
  router.push({ name: 'search', query: { keyword: route.query.keyword } })
}

const goSearchReview = () => {
  router.push({ name: 'search-review', query: { keyword: route.query.keyword } })
}

const goSearchNotice = () => {
  router.push({ name: 'search-notice', query: { keyword: route.query.keyword } })
}

const goSearchProfile = () => {
  router.push({ name: 'search-profile', query: { keyword: route.query.keyword } })
}
</script>

<template>
  <div v-if="datas" class="container justify-center flex flex-col my-6 items-start">
    <SearchHeader />
    <div className="w-full px-10">
      <div className="flex justify-between mb-10">
        <div className="flex space-x-2 text-sm">
          <Badge class="badge" @click="goSearch" variant="default">전체</Badge>
          <Badge class="badge" @click="goSearchReview">여행 리뷰 {{ reviewLen }}</Badge>
          <Badge class="badge" @click="goSearchNotice">공지사항 {{ noticeLen }}</Badge>
          <Badge class="badge bg-white text-black border-black" @click="goSearchProfile"
            >프로필 {{ userLen }}</Badge
          >
        </div>
      </div>
      <hr />
      <div className="mt-4"></div>
      <h2 className="text-lg font-semibold mt-10">프로필</h2>
      <div className="grid justify-center mt-4">
        <div className="grid grid-cols-3 gap-4 justify-center">
          <SearchCard
            v-for="user in datas.users"
            @click="goUserProfile(user.userId)"
            :user-info="user"
            :key="user.userId"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.badge {
  width: 100px;
  height: 40px;
  justify-content: center;
}
</style>

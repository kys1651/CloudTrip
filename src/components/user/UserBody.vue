<script setup lang="ts">
import { dropUserByAdmin, getAllUser, getUserByKeyword, updateIsLocked } from '@/api/user'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Checkbox } from '@/components/ui/checkbox'
import { Input } from '@/components/ui/input'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table'
import { toast } from '@/components/ui/toast'
import { useAuthenticationStore } from '@/stores/authentication'
import { type IUser } from '@/types/user.type'
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CustomPagination from '../common/CustomPagination.vue'

const route = useRoute()
const router = useRouter()
const authentication = useAuthenticationStore()

const users = ref<IUser[]>()

getAllUser()
  .then((data) => {
    console.log(data)
    // ADMIN은 표시하지 X
    users.value = data.filter((user) => user.roleType === 'USER')

    if (!route.query.page) {
      router.push({ name: 'adminUser', query: { page: 1 } })
    }
  })
  .catch((error) => {
    console.log('회원 조회 오류', error)
  })

const changeStatus = (user: IUser) => {
  // view에 활성 상태 변경
  user.isLocked = !user.isLocked
  // 서버로 상태 변경 업데이트
  updateIsLocked(user.userId.toString())
    .then((data) => {
      console.log(data)
    })
    .catch((error) => {
      console.log('활성 상태 변경 실패', error)
    })
}

const dropUser = (user: IUser) => {
  if (confirm('정말 탈퇴하시겠습니까?') && confirm('확인을 누르시면 탈퇴가 완료됩니다.')) {
    dropUserByAdmin(user.userId.toString())
      .then((data) => {
        console.log(data)
        toast({
          title: '회원 탈퇴',
          description: '회원이 탈퇴되었습니다.',
          variant: 'success'
        })
      })
      .catch((error) => {
        console.log('회원 탈퇴 실패', error)
      })
  }

  router.go(0)
}

const searchKeyword = ref('')
const searchUser = () => {
  getUserByKeyword(searchKeyword.value)
    .then((data) => {
      console.log(data)
      users.value = data
    })
    .catch((error) => {
      console.log('특정 회원 조회 실패', error)
    })
}

// Paging
watch(
  () => route.query.page,
  (newPage) => {
    pageNumber.value = Number(newPage) || 1
  }
)

const pageNumber = ref<number>(1)
const postsPerPage = ref(10)

const updateCurrentPage = (pageIdx: number) => {
  router.push({ name: 'adminUser', query: { page: pageIdx } })
}

const displayedPosts = computed(() => {
  const startIndex = (pageNumber.value - 1) * postsPerPage.value
  const endIndex = startIndex + postsPerPage.value
  return users.value ? users.value.slice(startIndex, endIndex) : []
})

const totalPages = computed(() => {
  if (!users.value) {
    return
  }
  console.log(users.value.length + ' ' + postsPerPage.value)
  return Math.ceil(users.value.length / postsPerPage.value)
})
</script>

<template>
  <div className="flex items-center justify-between mb-6">
    <div>
      <h1 className="text-2xl font-semibold">회원관리</h1>
      <p className="text-gray-600">회원들을 관리할 수 있습니다.</p>
    </div>
    <div className="flex items-center space-x-4">
      <Input
        className="w-64 border rounded-md p-2"
        placeholder="회원 검색"
        v-model="searchKeyword"
      />
      <Button variant="outline" @click="searchUser">검색</Button>
    </div>
  </div>
  <Table>
    <TableHeader>
      <TableRow>
        <TableHead className="w-[50px]" />
        <TableHead className="text-left">회원 ID </TableHead>
        <TableHead>회원 이름</TableHead>
        <TableHead>상태</TableHead>
        <TableHead>역할</TableHead>
        <TableHead className="w-[50px]" />
      </TableRow>
    </TableHeader>
    <TableBody>
      <TableRow v-for="user in displayedPosts" :key="user.userId">
        <TableCell>
          <Checkbox id="task-8782" />
        </TableCell>
        <TableCell className="font-medium">{{ user.username }}</TableCell>
        <TableCell>{{ user.nickname }}</TableCell>
        <TableCell>
          <Badge :variant="user.isLocked ? 'secondary' : 'default'">{{
            user.isLocked ? "비활성화" : "활성화"
          }}</Badge>
        </TableCell>
        <TableCell>{{ user.roleType }}</TableCell>
        <TableCell>
          <DotIcon className="w-5 h-5" />
        </TableCell>
        <TableCell>
          <Button
            @click="changeStatus(user)"
            class="mx-2"
            :class="user.isLocked ? 'deactivate-button' : 'activate-button'"
          >
            {{ user.isLocked ? "비활성화" : "활성화" }}
          </Button>
          <Button @click="dropUser(user)" class="delete-button">회원탈퇴</Button>
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
  <div class="w-full mt-6 flex justify-center">
    <CustomPagination
      @page-number="updateCurrentPage"
      :total-page="totalPages || 0"
      :total-post="users?.length || 0"
      :items-per-page="postsPerPage.toString()"
    />
  </div>
</template>

<style scoped>
.deactivate-button {
  background-color: rgb(241, 111, 111);
  color: white;
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: none; /* Remove default border */
  height: 25px;
}

.deactivate-button:hover {
  background-color: darkred;
}

.activate-button {
  background-color: green;
  color: white;
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: none; /* Remove default border */
  height: 25px;
}

.activate-button:hover {
  background-color: darkgreen;
}

.delete-button {
  background-color: rgb(248, 81, 81);
  color: white;
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: none; /* Remove default border */
  height: 25px;
}

.delete-button:hover {
  background-color: darkred;
}
</style>

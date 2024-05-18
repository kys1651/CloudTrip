<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import {
  TableHead,
  TableRow,
  TableHeader,
  TableCell,
  TableBody,
  Table
} from '@/components/ui/table'
import { Checkbox } from '@/components/ui/checkbox'
import { Badge } from '@/components/ui/badge'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useAuthenticationStore } from '@/stores/authentication'
import { toast } from '@/components/ui/toast'

const route = useRoute()
const router = useRouter()
const authentication = useAuthenticationStore()

const addr = `http://localhost:8080/api/v1/user/list`
const users = ref([])
onMounted(() => {
  axios.get(addr).then((response) => {
    console.log(response)
    users.value = response.data
  })
})

const updateAddr = `http://localhost:8080/api/v1/user/update/`
const changeStatus = (user: Object) => {
  // view에 활성 상태 변경
  user.locked = !user.locked
  // 서버로 상태 변경 업데이트
  axios
    .patch(updateAddr + user.userId)
    .then((response) => {
      console.log(response)
    })
    .catch((error) => {
      console.log('활성 상태 변경 실패', error)
    })
}

const deleteAddr = `http://localhost:8080/api/v1/user/delete/`
const dropUser = (user: Object) => {
  if (confirm('정말 탈퇴하시겠습니까?') && confirm('확인을 누르시면 탈퇴가 완료됩니다.')) {
    axios
      .delete(deleteAddr + user.userId)
      .then((response) => {
        console.log(response)
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
const findByKeywordAddr = `http://localhost:8080/api/v1/user?keyword=`
const searchUser = () => {
  axios
    .get(findByKeywordAddr + searchKeyword.value)
    .then((response) => {
      console.log(response)
      users.value = response.data
    })
    .catch((error) => {
      console.log('특정 회원 조회 실패', error)
    })
}
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
      <TableRow v-for="user in users" :key="user.userId">
        <TableCell>
          <Checkbox id="task-8782" />
        </TableCell>
        <TableCell className="font-medium">{{ user.username }}</TableCell>
        <TableCell>{{ user.nickname }}</TableCell>
        <TableCell>
          <Badge :variant="user.locked ? 'secondary' : ''">{{
            user.locked ? '비활성화' : '활성화'
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
            :class="user.locked ? 'activate-button' : 'deactivate-button'"
          >
            {{ user.locked ? '활성화' : '비활성화' }}
          </Button>
          <Button @click="dropUser(user)" class="delete-button">회원탈퇴</Button>
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<style scoped>
.deactivate-button {
  background-color: rgb(248, 81, 81);
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

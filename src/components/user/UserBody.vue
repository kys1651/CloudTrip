<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { AvatarImage, AvatarFallback, Avatar } from '@/components/ui/avatar'
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
import {
  SelectValue,
  SelectTrigger,
  SelectItem,
  SelectContent,
  Select
} from '@/components/ui/select'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'

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
  axios.patch(updateAddr + user.userId)
  .then((response) => {
    console.log(response)
  })
  .catch((error) => {
    console.log("활성 상태 변경 실패", error)
  })
}
</script>

<template>
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
        <TableCell className="font-medium">{{ user.email }}</TableCell>
        <TableCell>{{ user.username }}</TableCell>
        <TableCell>
          <Badge :variant="user.locked ? 'secondary' : ''"  >{{ user.locked ? '비활성화' : '활성화'}}</Badge>
        </TableCell>
        <TableCell>{{ user.roleType }}</TableCell>
        <TableCell>
          <DotIcon className="w-5 h-5" />
        </TableCell>
        <TableCell>
          <Button @click="changeStatus(user)" class="delete-button">회원탈퇴</Button>
        </TableCell>
      </TableRow>

    </TableBody>
  </Table>
</template>

<style scoped>
.delete-button {
  background-color: rgb(248, 81, 81);
  color: white;
  border-radius: 9999px; /* Fully rounded */
  padding: 0.5rem 1rem;
  border: none; /* Remove default border */
  /* cursor: pointer; Pointer cursor on hover */
}

.delete-button:hover{
  background-color: darkred;
}
</style>

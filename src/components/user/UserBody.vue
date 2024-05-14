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
        <TableCell className="font-medium">{{ user.userId }}</TableCell>
        <TableCell>{{ user.username }}</TableCell>
        <TableCell>
          <Badge variant="secondary">{{ user.username }} {{ user.isLocked }}</Badge>
        </TableCell>
        <TableCell>{{ user.roleType }}</TableCell>
        <TableCell>
          <DotIcon className="w-5 h-5" />
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell>
          <Checkbox id="task-7878" />
        </TableCell>
        <TableCell className="font-medium">TASK-7878</TableCell>
        <TableCell>Documentation</TableCell>
        <TableCell>
          <Badge>Backlog</Badge>
        </TableCell>
        <TableCell>Medium</TableCell>
        <TableCell>
          <DotIcon className="w-5 h-5" />
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<style scoped></style>

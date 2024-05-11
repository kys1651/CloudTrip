<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { AvatarFallback, Avatar, AvatarImage } from '@/components/ui/avatar'
import { CardContent, Card } from '@/components/ui/card'
import SocialCard from '@/components/social/SocialCard.vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const goSocialDetail = () => {
  router.push({ name: 'social-detail' })
}
</script>

<template>
  <div className="max-w-4xl mx-auto p-6">
    <div className="flex justify-between">
      <div className="flex space-x-2 text-sm">
        <Badge variant="default">전체</Badge>
        <Badge>포스팅(99+)</Badge>
        <Badge>회사(5)</Badge>
        <Badge>커리어(76)</Badge>
        <Badge>소식(99+)</Badge>
        <Badge>프로필(0)</Badge>
      </div>
      <div className="flex items-center">
        <Input
          className="shadow-md rounded-full pl-4 pr-10 h-8 w-64"
          placeholder="검색어를 입력해주세요."
          type="search"
        />
      </div>
    </div>
    <div className="mt-4">
      <h2 className="text-lg font-semibold">소식 103</h2>
      <div className="grid gap-6 mt-4">
        <div className="grid grid-cols-3 gap-4">
          <div @click="goSocialDetail">
            <SocialCard />
          </div>
          <Card className="col-span-1 shadow-md">
            <CardContent>
              <div className="flex items-center space-x-3">
                <Avatar>
                  <!-- <AvatarImage alt="이서울" src="src/icons/GithubLogo.vue" /> -->
                </Avatar>
                <div className="flex flex-col">
                  <p className="font-medium">이서울</p>
                  <span className="text-sm text-gray-500">게시물 · 2021.10.13</span>
                </div>
              </div>
              <div className="mt-2 text-sm">
                굿즈나...
                <br />
                스타트업에서 디자이너 프로필에디트 개발을 했기하였습니다. 과연 알아요 ...
              </div>
            </CardContent>
          </Card>
          <Card className="col-span-1 shadow-md">
            <CardContent>
              <div className="flex items-center space-x-3">
                <Avatar>
                  <AvatarFallback>CN</AvatarFallback>
                </Avatar>
                <div className="flex flex-col">
                  <p className="font-medium">후르르에디</p>
                  <span className="text-sm text-gray-500">게시물 · 2022.12.10</span>
                </div>
              </div>
              <div className="mt-2 text-sm">
                프로필에디트 게시물 모임간의존 어디서 구하나요?
                <br />
                조작 하기시 어지간 하여야 매우 어렵음을 느끼네요 하며, ...
              </div>
            </CardContent>
          </Card>
        </div>
        <div className="grid grid-cols-3 gap-4">
          <Card className="col-span-1 shadow-md">
            <CardContent>
              <div className="flex items-center space-x-3">
                <Avatar>
                  <!-- <img alt="mitchell" className="rounded-full" src="src/icons/GithubLogo.vue" /> -->
                </Avatar>
                <div className="flex flex-col">
                  <p className="font-medium">글쓰기챌린지</p>
                  <span className="text-sm text-gray-500">mitchell · 2024.02.22</span>
                </div>
              </div>
              <div className="mt-2 text-sm">
                https://mitchell-up.github.io/mitchell-dictionary/blog
                <br />
                주니어 프로필에디트 개발자에게도 기본적인 리눅스 쉘커맨드의 활용을 바탕시 ...
              </div>
            </CardContent>
          </Card>
          <Card className="col-span-1 shadow-md">
            <CardContent>
              <div className="flex items-center space-x-3">
                <Avatar>
                  <AvatarFallback>CN</AvatarFallback>
                </Avatar>
                <div className="flex flex-col">
                  <p className="font-medium">여어장착</p>
                  <span className="text-sm text-gray-500">게시물 · 2022.07.17</span>
                </div>
              </div>
              <div className="mt-2 text-sm">
                프로필에디트 알게되었네에
                <br />
                밖에도 심어놓으 다시 시작하고싶은 ...
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>

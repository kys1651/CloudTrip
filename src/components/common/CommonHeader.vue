<script setup lang="ts">
import { signOutRequest } from '@/api/auth'
import HomeNavigator from '@/components/common/HomeNavigator.vue'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import { useAuthenticationStore } from '@/stores/authentication'
import { useMutation } from '@tanstack/vue-query'
import { LogOut, Settings, User } from 'lucide-vue-next'
import { storeToRefs } from 'pinia'
import { RouterLink, useRouter } from 'vue-router'

const authentication = useAuthenticationStore()

const router = useRouter()

const { mutate } = useMutation({
  mutationKey: ['sign-out'],
  mutationFn: signOutRequest,
  onSuccess: () => {
    authentication.clearAuthentication()
    sessionStorage.removeItem('accessToken')
    sessionStorage.removeItem('refreshToken')
    router.go(0)
  }
})

const hiddenRoute = ['sign-in', 'sign-up', 'oauth2-redirect', 'confirm-email', 'planning']

const ROUTES: { pathname: string; name: string }[] = [
  {
    pathname: 'user',
    name: '회원관리'
  },
  {
    pathname: 'notice',
    name: '공지사항'
  },
  {
    pathname: 'trip',
    name: '여행지 추천'
  },
  {
    pathname: 'review',
    name: '여행지 후기'
  }
]
const authenticationStore = useAuthenticationStore()
const { isLogin, profile } = storeToRefs(authenticationStore)
</script>

<template>
  <header
    v-show="!hiddenRoute.includes($route.name?.toString() || 'home')"
    class="flex items-center justify-between bg-white px-4 py-3 shadow-sm dark:bg-gray-900 sm:px-6 lg:px-8"
  >
    <div class="flex items-center">
      <HomeNavigator />
    </div>
    <div class="flex items-center space-x-4">
      <nav class="hidden space-x-4 md:flex">
        <RouterLink
          v-for="(route, index) in ROUTES"
          :key="index"
          :to="{ name: route.pathname }"
          class="text-gray-600 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-100"
          >{{ route.name }}</RouterLink
        >
      </nav>

      <DropdownMenu v-if="isLogin && profile">
        <DropdownMenuTrigger as-child>
          <button
            class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2"
            type="button"
            aria-haspopup="menu"
            aria-expanded="false"
            data-state="closed"
          >
            <Avatar class="w-8 h-8" v-if="profile.profileImage">
              <AvatarImage :src="profile.profileImage" :alt="profile.username" />
              <AvatarFallback>{{ profile.nickname }}</AvatarFallback>
            </Avatar>
            <Avatar v-else>
              <AvatarImage
                :src="`https://api.multiavatar.com/user-${profile.username}.svg`"
                :alt="profile.username"
              />
              <AvatarFallback>{{ profile.nickname }}</AvatarFallback>
            </Avatar>
          </button>
        </DropdownMenuTrigger>
        <DropdownMenuContent class="w-56">
          <DropdownMenuLabel>{{ profile.username }}</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuGroup>
            <DropdownMenuItem>
              <User class="mr-2 h-4 w-4" />
              <span><RouterLink :to="{ name: 'mypage' }">MyPage</RouterLink></span>
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Settings class="mr-2 h-4 w-4" />
              <span>Settings</span>
            </DropdownMenuItem>
          </DropdownMenuGroup>
          <DropdownMenuSeparator />
          <DropdownMenuItem>
            <LogOut class="mr-2 h-4 w-4" />
            <span @click="mutate">Log out</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
      <RouterLink
        v-else
        :to="{ name: 'sign-in' }"
        class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2"
        type="button"
      >
        <span
          class="relative flex shrink-0 overflow-hidden w-full cursor-pointer items-center justify-center"
        >
          Login
        </span>
      </RouterLink>
    </div>
  </header>
</template>

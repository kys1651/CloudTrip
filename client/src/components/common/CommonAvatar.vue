<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
defineProps<{
  src?: string;
  username: string;
}>();
</script>

<template>
  <Avatar class="w-8 h-8">
    <AvatarImage v-if="src" alt="이미지" :src="src" />
    <AvatarImage
      v-else
      alt="이미지"
      :src="`https://api.multiavatar.com/user-${username}.svg`"
    />
    <AvatarFallback>{{ username }}</AvatarFallback>
  </Avatar>
</template>

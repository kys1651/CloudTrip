<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Card, CardContent } from "@/components/ui/card";
import { imageOrDefault } from "@/lib/image-load";

import { useRoute, useRouter } from "vue-router";

const props = defineProps<{
  userInfo: {
    userId: number;
    profileImage?: string;
    email: string;
    nickname: string;
  };
}>();

const route = useRoute();
const router = useRouter();

const goSocialDetail = () => {
  router.push({ name: "social-detail", params: { socialId: props.userInfo.userId } });
};
</script>

<template>
  <Card className="col-span-1 shadow-md">
    <CardContent>
      <div className="flex items-center space-x-3">
        <Avatar>
          <AvatarImage :src="imageOrDefault(userInfo.profileImage)" alt="No Img" />
          <AvatarFallback></AvatarFallback>
        </Avatar>
        <div className="flex flex-col">
          <p className="font-medium">{{ userInfo.nickname }}</p>
          <span className="text-sm text-gray-500">[이메일]</span>
          <span className="text-sm text-gray-500">{{ userInfo.email }}</span>
        </div>
      </div>
    </CardContent>
  </Card>
</template>

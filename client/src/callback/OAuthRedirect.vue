<script setup lang="ts">
import { userDataRequest } from "@/api/user";
import CircularLoading from "@/components/common/CircularLoading.vue";
import { useToast } from "@/components/ui/toast";
import { useAuthenticationStore } from "@/stores/authentication";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const toast = useToast();
if (route.query.access_token) {
  const { setAccessToken } = useAuthenticationStore();
  setAccessToken(route.query.access_token as string);
  userDataRequest().then(() => {
    router.push({ name: "home" });
  });
} else {
  const isNew: boolean = JSON.parse(route.query.isNew as string);

  toast.toast({
    title: isNew ? "회원가입 성공" : "로그인 실패",
    description: route.query.error as string,
    duration: 2000,
    variant: isNew ? "success" : "destructive",
  });
  router.push({ name: "sign-in" });
}
</script>
<template>
  <CircularLoading />
</template>

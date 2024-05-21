<script setup lang="ts">
import { emailConfirmRequest } from "@/api/auth";
import { scheduleInviteConfirmRequest } from "@/api/schedule";
import CircularLoading from "@/components/common/CircularLoading.vue";
import { useToast } from "@/components/ui/toast";
import { useMutation } from "@tanstack/vue-query";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const toast = useToast();

const confirmType = route.query.type as string;
const code = route.query.code as string;
const email = route.query.email as string;
const id = route.query.id as string;

const { mutate } = useMutation({
  mutationKey: ["confirm-email"],
  mutationFn: emailConfirmRequest,
  onSuccess: () => {
    toast.toast({
      title: "이메일 인증 성공",
      description: "이메일 인증에 성공했습니다. 로그인해주세요.",
      duration: 2000,
      variant: "success",
    });
    router.push({ name: "sign-in" });
  },
  onError: (error: any) => {
    toast.toast({
      title: "이메일 인증 실패",
      description: error.response.data.message,
      duration: 2000,
      variant: "destructive",
    });
    router.push({ name: "sign-in" });
  },
});

const { mutate: inviteMutate } = useMutation({
  mutationKey: ["confirm-email"],
  mutationFn: scheduleInviteConfirmRequest,
  onSuccess: () => {
    toast.toast({
      title: "일정 초대 성공",
      description: "일정 초대에 성공했습니다. 계획에 참여하여 멋진 계획을 만들어주세요!",
      duration: 2000,
      variant: "success",
    });
    router.push({ name: "sign-in" });
  },
  onError: (error: any) => {
    toast.toast({
      title: "일정 초대 실패 실패",
      description: error.response.data.message,
      duration: 2000,
      variant: "destructive",
    });
    router.push({ name: "sign-in" });
  },
});

if (confirmType === "sign-up") {
  mutate({ code, email, type: confirmType });
} else if (confirmType === "schedule-invite") {
  inviteMutate({ scheduleId: Number(id), code: code, email: email });
} else {
  router.push({ name: "home" });
}
</script>
<template>
  <CircularLoading />
</template>

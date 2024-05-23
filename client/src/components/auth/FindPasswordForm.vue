<script setup lang="ts">
import { findPasswordRequest } from "@/api/auth";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useToast } from "@/components/ui/toast";
import { cn } from "@/lib/utils";
import { FindPasswordStatus, type FindPasswordForm } from "@/types/auth.type";
import { useMutation } from "@tanstack/vue-query";
import { LoaderCircle } from "lucide-vue-next";
import { useForm } from 'vee-validate';
import * as yup from 'yup';

import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from '@/components/ui/form';
import router from "@/router";
import { useAuthenticationStore } from "@/stores/authentication";
import { storeToRefs } from "pinia";

// 이미 로그인 되어있는 유저라면 바로 홈으로 이동
const authentication = useAuthenticationStore()
const {isLogin} = storeToRefs(authentication);

if (isLogin.value) {
  router.push({ name: 'home' })
}

const emit = defineEmits(["passwordChange", "searchEmail"]);
const setConfirmForm = FindPasswordStatus.CONFIRM;

const toast = useToast();

// 비밀번호 찾기 이메일 전송
const formSchema = yup.object({
  email: yup.string().required().email(),
})

const {handleSubmit, values} = useForm<FindPasswordForm>({
  validationSchema: formSchema
})

const {mutate, isPending: isLoading} = useMutation({
  mutationKey: ['find-password'],
  mutationFn: findPasswordRequest,
  onSuccess: () => {
    toast.toast({
      title: '비밀번호 찾기',
      description: "인증번호를 발송했습니다. 인증번호를 확인해주세요.",
      duration: 2000,
    })
    emit('passwordChange', setConfirmForm)
  },
  onError: (error: any) => {
    const { response: {data: { message, code }} } = error;
    if (code === 1002) {
      emit('passwordChange', setConfirmForm)
      toast.toast({
        title: '비밀번호 찾기',
        description: "이미 인증번호를 발송했습니다. 인증번호를 확인해주세요.",
        duration: 2000,
      })
    return
    }
    toast.toast({
      title: '비밀번호 찾기 실패',
      description: message,
      duration: 2000,
      variant: "destructive"
    })
  }
});



const onSubmit = handleSubmit((values) => {
  mutate(values)
  emit('searchEmail', values.email)
})
</script>

<template>
  <div class="flex flex-col space-y-2 text-center">
    <h1 class="text-2xl font-semibold tracking-tight">비밀번호 찾기</h1>
    <p class="text-sm text-muted-foreground">
      찾으시려는 아이디의 이메일을 입력해주세요.
    </p>
  </div>
  <div :class="cn('grid gap-6', $attrs.class ?? '')">
    <!-- 비밀번호 찾기 이메일 전송 -->
    <form @submit.prevent="onSubmit" class="space-y-4">
      <FormField v-slot="{ componentField }" name="email">
        <FormItem>
          <FormLabel for="email">이메일</FormLabel>
          <FormControl>
            <Input
              id="email"
              placeholder="name@example.com"
              type="email"
              v-bind="componentField"
              auto-capitalize="none"
              auto-complete="email"
              auto-correct="off"
              required
            />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
      <Button class="w-full flex justify-center" :disabled="isLoading" @click="onSubmit">
        <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
        <span v-else>인증번호 발송</span>
      </Button>
    </form>
  </div>
</template>

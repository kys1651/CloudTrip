<script setup lang="ts">
import { emailConfirmRequest } from "@/api/auth";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useToast } from "@/components/ui/toast";
import { FindPasswordStatus, type EmailConfirmForm } from "@/types/auth.type";
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
const authentication = useAuthenticationStore();
const { isLogin } = storeToRefs(authentication);

if (isLogin.value) {
  router.push({ name: "home" });
}

const props = defineProps<{
  email: string;
}>();

const emit = defineEmits(["passwordChange", "searchEmail"]);
const setResetForm = FindPasswordStatus.RESET;

const toast = useToast();

// 비밀번호 찾기 인증 코드 확인
const { mutate: mutate, isPending: isLoading } = useMutation({
  mutationKey: ["confirm-password"],
  mutationFn: emailConfirmRequest,
  onSuccess: () => {
    emit("passwordChange", setResetForm);
  },
  onError: (error: any) => {
    const {
      response: {
        data: { message },
      },
    } = error;
    toast.toast({
      title: "비밀번호 찾기 실패",
      description: message,
      duration: 2000,
      variant: "destructive",
    });
  },
});

const formSchema = yup.object({
  type: yup.string().required(),
  email: yup.string().required().email(),
  code: yup.string().required(),
});

const { handleSubmit, setValues } = useForm<EmailConfirmForm>({
  validationSchema: formSchema,
});

setValues({
  type: 'find-password',
  email: props.email,
  code: '',
});

const onSubmit = handleSubmit((values) => {
  mutate(values);
});
</script>
<template>
  <div class="flex flex-col space-y-2 text-center">
    <!-- 이메일 인증번호 인증 -->
    <form @submit.prevent="onSubmit" class="space-y-4">
      <FormField v-slot="{ componentField }" name="email">
        <FormItem>
          <FormLabel for="email">이메일</FormLabel>
          <FormControl>
            <Input
              id="email"
              readonly
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
      <FormField v-slot="{ componentField }" name="code">
        <FormItem>
          <FormLabel for="code">인증 코드</FormLabel>
          <FormControl>
            <Input
              id="code"
              type="text"
              v-bind="componentField"
              auto-correct="off"
              required
            />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
      <Button class="w-full flex justify-center" :disabled="isLoading" @click="onSubmit">
        <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
        <span v-else>인증하기</span>
      </Button>
    </form>
  </div>
</template>

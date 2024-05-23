<script setup lang="ts">
import { resetPasswordRequest } from "@/api/auth";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useToast } from "@/components/ui/toast";
import { FormStatus, type ResetPasswordForm } from "@/types/auth.type";
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

const toast = useToast();

const props = defineProps<{
  email: string;
}>();

const emit = defineEmits(["changeStatus"]);
const setSignIn = FormStatus.SIGN_IN;

// 비밀번호 찾기 인증 코드 확인
const { mutate: mutate, isPending: isLoading } = useMutation({
  mutationKey: ["reset-password"],
  mutationFn: resetPasswordRequest,
  onSuccess: () => {
    toast.toast({
      title: "비밀번호 변경",
      description: "비밀번호가 변경되었습니다.",
      duration: 2000,
      variant: "success",
    });
    emit("changeStatus", setSignIn);
  },
  onError: (error: any) => {
    const {
      response: {
        data: { message },
      },
    } = error;
    toast.toast({
      title: "비밀번호 변경",
      description: message,
      duration: 2000,
      variant: "destructive",
    });
  },
});

const formSchema = yup.object({
  password: yup.string().required().matches(/^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/, '영문, 숫자를 포함한 8~16자 비밀번호를 입력해주세요.'),
  passwordConfirm: yup.string().required().oneOf([yup.ref('password')], '비밀번호가 일치하지 않습니다.'),
});

const { handleSubmit, setValues } = useForm<ResetPasswordForm>({
  validationSchema: formSchema,
});

setValues({
  email: props.email,
  password: '',
});


const onSubmit = handleSubmit((values) => {
  mutate(values);
});
</script>
<template>
  <div class="flex flex-col space-y-2 text-center">
    <!-- 이메일 인증번호 인증 -->
    <form @submit.prevent="onSubmit" class="space-y-4">
      <FormField v-slot="{ componentField }" name="password">
        <FormItem>
          <FormLabel for="password">비밀번호</FormLabel>
          <FormControl>
            <Input id="password" type="password" v-bind="componentField" required />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" name="passwordConfirm">
        <FormItem>
          <FormLabel for="passwordConfirm">비밀번호 확인</FormLabel>
          <FormControl>
            <Input
              id="passwordConfirm"
              type="password"
              v-bind="componentField"
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

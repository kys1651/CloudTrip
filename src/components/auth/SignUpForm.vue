<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useToast } from '@/components/ui/toast'
import { FormStatus, type SignUpForm } from '@/types/auth.type'
import { cn } from '@/lib/utils'
import { LoaderCircle } from 'lucide-vue-next'
import { useMutation } from '@tanstack/vue-query'
import { signUpRequest } from '@/api/auth'
import { useForm } from 'vee-validate'
import * as yup from 'yup'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import router from '@/router'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'

// 이미 로그인 되어있는 유저라면 바로 홈으로 이동
const authentication = useAuthenticationStore()
const { isLogin } = storeToRefs(authentication)

if (isLogin.value) {
  router.push({ name: 'home' })
}

const emit = defineEmits(['changeStatus'])
const setSignIn = FormStatus.SIGN_IN

const toast = useToast()

const { mutate, isPending: isLoading } = useMutation({
  mutationKey: ['sign-up'],
  mutationFn: signUpRequest,
  onSuccess: () => {
    toast.toast({
      title: '회원가입 성공',
      description: '이메일 인증을 완료해주세요.',
      duration: 2000,
      variant: 'success'
    })
    emit('changeStatus', setSignIn)
  },
  onError: (error: any) => {
    const {
      response: {
        data: { message }
      }
    } = error
    toast.toast({
      title: '회원가입 실패',
      description: message,
      duration: 2000,
      variant: 'destructive'
    })
  }
})

const formSchema = yup.object({
  username: yup.string().required().email(),
  password: yup
    .string()
    .required()
    .matches(
      /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/,
      '영문, 숫자를 포함한 8~16자 비밀번호를 입력해주세요.'
    ),
  passwordConfirm: yup
    .string()
    .required()
    .oneOf([yup.ref('password')], '비밀번호가 일치하지 않습니다.'),
  nickname: yup.string(),
  email: yup.string().required().email()
})

const { handleSubmit, handleReset } = useForm<SignUpForm>({
  validationSchema: formSchema
})

const onSubmit = handleSubmit((values) => {
  mutate(values)
  handleReset()
})
</script>

<template>
  <div :class="cn('grid gap-6', $attrs.class ?? '')">
    <form @submit.prevent="onSubmit" class="space-y-4">
      <FormField v-slot="{ componentField }" name="username">
        <FormItem>
          <FormLabel for="username">아이디</FormLabel>
          <FormControl>
            <Input
              id="username"
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
            <Input id="passwordConfirm" type="password" v-bind="componentField" required />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" name="nickname">
        <FormItem>
          <FormLabel for="username">닉네임</FormLabel>
          <FormControl>
            <Input
              id="nickname"
              placeholder="닉네임을 입력해주세요.(선택)"
              type="text"
              v-bind="componentField"
              auto-capitalize="none"
              auto-correct="off"
              required
            />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
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
      <Button class="mt-4 w-full" :disabled="isLoading" @click="onSubmit">
        <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
        회원가입
      </Button>
    </form>
  </div>
</template>

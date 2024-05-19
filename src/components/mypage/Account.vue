<script setup lang="ts">
import { useForm } from 'vee-validate'
import { FormControl, FormItem, FormLabel, FormMessage, FormField } from '@/components/ui/form'
import Input from '@/components/ui/input/Input.vue'
import Button from '@/components/ui/button/Button.vue'
import Separator from '@/components/ui/separator/Separator.vue'
import * as yup from 'yup'
import type { ChangePwForm } from '@/types/user.type'
import { changePasswordRequest, deleteRequest } from '@/api/user'
import { toast } from '@/components/ui/toast'
import { useAuthenticationStore } from '@/stores/authentication'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'

const authentication = useAuthenticationStore()
const router = useRouter()

const authenticationStore = useAuthenticationStore()
const { profile } = storeToRefs(authenticationStore)

const formSchema = yup.object({
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
    .oneOf([yup.ref('password')], '비밀번호가 일치하지 않습니다.')
})
const { handleSubmit, handleReset } = useForm<ChangePwForm>({
  validationSchema: formSchema
})

const onSubmit = handleSubmit(async (values) => {
  // 로그인 추가 로직 작성
  const isSuccess = await changePasswordRequest(values.password)
  if (isSuccess) {
    toast({
      title: '비밀번호 변경',
      description: '변경이 완료되었습니다.',
      variant: 'success'
    })
    handleReset()
    return
  } else {
    toast({
      title: '비밀번호 변경 실패',
      description: '변경이 실패했습니다. 다시 시도해주세요.',
      variant: 'destructive'
    })
  }
})

const onDelete = async () => {
  if (confirm('정말 탈퇴하시겠습니까?') && confirm('확인을 누르시면 탈퇴가 완료됩니다.')) {
    const isSuccess = await deleteRequest(profile.value!.id)
    if (isSuccess) {
      toast({
        title: '회원 탈퇴',
        description: '회원이 탈퇴되었습니다.',
        variant: 'success'
      })
      authentication.clearAuthentication()
      sessionStorage.removeItem('accessToken')
      sessionStorage.removeItem('refreshToken')
      router.replace({ name: 'home' })
      return
    } else {
      toast({
        title: '회원 탈퇴 실패',
        description: '회원 탈퇴가 실패했습니다. 다시 시도해주세요.',
        variant: 'destructive'
      })
    }
  }
}
</script>

<template>
  <div class="container w-full" style="max-width: 1200px; padding: 20px">
    <div>
      <h3 class="text-3xl font-bold mb-5">계정 관리</h3>
    </div>
    <Separator />
    <div>
      <h3 class="text-xl font-bold mb-5 mt-5">비밀번호 변경</h3>
    </div>

    <form @submit.prevent="onSubmit" class="space-y-4">
      <FormField v-slot="{ componentField }" name="password">
        <FormItem class="mt-5">
          <FormLabel class="text-l font-semibold">비밀번호</FormLabel>
          <FormControl>
            <Input type="password" v-bind="componentField" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ componentField }" name="passwordConfirm">
        <FormItem class="mt-5">
          <FormLabel class="text-l font-semibold">비밀번호 확인</FormLabel>
          <FormControl>
            <Input type="password" v-bind="componentField" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <div class="flex justify-start">
        <Button type="submit" class="mt-3 mb-5">비밀번호 변경</Button>
      </div>
    </form>
    <Separator />

    <div>
      <div>
        <h3 class="text-xl font-bold mb-5 mt-8">계정 탈퇴하기</h3>
      </div>
      <Button type="submit" variant="destructive" class="mt-3 mb-5" @click="onDelete">탈퇴</Button>
    </div>
  </div>
</template>

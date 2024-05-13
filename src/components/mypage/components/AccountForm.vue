<script setup lang="ts">
import { h, ref } from 'vue'
import * as yup from 'yup'
import { toDate } from 'radix-vue/date'
import { Check, ChevronsUpDown } from 'lucide-vue-next'
import { CalendarDate, getLocalTimeZone, today } from '@internationalized/date'
import { cn } from '@/lib/utils'

import { FormControl, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { FormField } from '@/components/ui/form'
import Input from '@/components/ui/input/Input.vue'
import Separator from '@/components/ui/separator/Separator.vue'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList
} from '@/components/ui/command'
import Button from '@/components/ui/button/Button.vue'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { Calendar } from 'lucide-vue-next'
import { toast } from '@/components/ui/toast/use-toast'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { useForm } from 'vee-validate'

const open = ref(false)
const dateValue = ref()
const placeholder = ref()

// TODO : 지역으로 변경
const languages = [] as const

const formSchema = yup.object({
  username: yup.string().required(),
  nickname: yup.string().required(),
  image: yup.string()
})

const { handleSubmit, setFieldValue } = useForm<{
  username: string
  nickname: string
}>({
  validationSchema: formSchema
})

const onSubmit = handleSubmit((values) => {})

const authenticationStore = useAuthenticationStore()
const { isLogin, profile } = storeToRefs(authenticationStore)

setFieldValue('username', profile.value?.username || '')
setFieldValue('nickname', profile.value?.nickname || '')

console.log(profile)
</script>

<template>
  <div class="container w-full" style="max-width: 1200px; padding: 20px">
    <div>
      <h3 class="text-3xl font-medium mb-5">계정 관리</h3>
    </div>
    <Separator />
    <form @submit.prevent="onSubmit" class="space-y-8" @submit="onSubmit">
      <FormField v-slot="{ componentField }" name="image">
        <FormItem class="mt-5">
          <FormLabel class="text-xl">프로필 이미지</FormLabel>
          <FormControl>
            <div class="flex flex-col items-center">
              <img
                v-bind="componentField"
                src="https://generated.vusercontent.net/placeholder.svg"
                alt="프로필 이미지"
                class="w-40 h-40 rounded-full object-cover shadow-lg mb-4"
              />
              <div class="flex justify-center space-x-4 w-full">
                <Button class="px-4 py-2 rounded" variant="secondary">삭제</Button>
                <Button class="px-4 py-2 rounded">수정</Button>
              </div>
            </div>
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ componentField }" name="nickname">
        <FormItem class="mt-5">
          <FormLabel class="text-xl">닉네임</FormLabel>
          <FormControl>
            <Input type="text" v-bind="componentField" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ componentField }" name="username">
        <FormItem class="mt-5">
          <FormLabel class="text-xl">ID</FormLabel>
          <FormControl>
            <Input type="text" v-bind="componentField" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ field, value }" name="dob">
        <FormItem class="flex flex-col">
          <FormLabel class="text-xl">생년월일</FormLabel>
          <Popover>
            <PopoverTrigger as-child>
              <FormControl>
                <Button
                  variant="outline"
                  :class="
                    cn(
                      'w-[240px] justify-start text-left font-normal',
                      !value && 'text-muted-foreground'
                    )
                  "
                >
                  <RadixIconsCalendar class="mr-2 h-4 w-4 opacity-50" />
                  <span>{{
                    value ? df.format(toDate(dateValue, getLocalTimeZone())) : 'Pick a date'
                  }}</span>
                </Button>
              </FormControl>
            </PopoverTrigger>
            <PopoverContent class="p-0">
              <Calendar
                v-model:placeholder="placeholder"
                v-model="dateValue"
                calendar-label="Date of birth"
                initial-focus
                :min-value="new CalendarDate(1900, 1, 1)"
                :max-value="today(getLocalTimeZone())"
                @update:model-value="
                  (v) => {
                    if (v) {
                      dateValue = v
                      setFieldValue('dob', toDate(v).toISOString())
                    } else {
                      dateValue = undefined
                      setFieldValue('dob', undefined)
                    }
                  }
                "
              />
            </PopoverContent>
          </Popover>
          <FormMessage />
        </FormItem>
        <input type="hidden" v-bind="field" />
      </FormField>

      <FormField v-slot="{ value }" name="language">
        <FormItem class="flex flex-col">
          <FormLabel class="text-xl">지역 설정</FormLabel>

          <Popover v-model:open="open">
            <PopoverTrigger as-child>
              <FormControl>
                <Button
                  variant="outline"
                  role="combobox"
                  :aria-expanded="open"
                  :class="cn('w-[200px] justify-between', !value && 'text-muted-foreground')"
                >
                  {{
                    value
                      ? languages.find((language) => language.value === value)?.label
                      : 'Select language...'
                  }}

                  <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </FormControl>
            </PopoverTrigger>
            <PopoverContent class="w-[200px] p-0">
              <Command>
                <CommandInput placeholder="Search language..." />
                <CommandEmpty>No language found.</CommandEmpty>
                <CommandList>
                  <CommandGroup>
                    <CommandItem
                      v-for="language in languages"
                      :key="language.value"
                      :value="language.label"
                      @select="
                        () => {
                          setFieldValue('language', language.value)
                          open = false
                        }
                      "
                    >
                      <Check
                        :class="
                          cn('mr-2 h-4 w-4', value === language.value ? 'opacity-100' : 'opacity-0')
                        "
                      />
                      {{ language.label }}
                    </CommandItem>
                  </CommandGroup>
                </CommandList>
              </Command>
            </PopoverContent>
          </Popover>
          <FormMessage />
        </FormItem>
      </FormField>

      <div class="flex justify-start">
        <Button type="submit"> 수정 </Button>
      </div>
    </form>
  </div>
</template>

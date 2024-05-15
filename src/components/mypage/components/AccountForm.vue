<script setup lang="ts">
import { h, onMounted, onUpdated, ref, watch } from 'vue'
import * as yup from 'yup'
import { toDate } from 'radix-vue/date'
import { Check, ChevronsUpDown, Calendar } from 'lucide-vue-next'
import { CalendarDate, getLocalTimeZone, today } from '@internationalized/date'
import { cn } from '@/lib/utils'

import { FormControl, FormItem, FormLabel, FormMessage, FormField } from '@/components/ui/form'
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
import { toast } from '@/components/ui/toast/use-toast'
import { useAuthenticationStore } from '@/stores/authentication'
import { storeToRefs } from 'pinia'
import { useForm } from 'vee-validate'
import { useToast } from '@/components/ui/toast'
import { userDataModifyImageRequest, userDataModifyRequest } from '@/api/user'
import { useQuery } from '@tanstack/vue-query'
import { citiesRequest, townsRequest } from '@/api/trip'
import { type ITown } from '@/types/trip.type'
import type { IMyPage } from '@/types/user.type'

const authenticationStore = useAuthenticationStore()
const { isLogin, profile } = storeToRefs(authenticationStore)

const open = ref(false)
const dateValue = ref()
const fileInput = ref<HTMLInputElement | null>(null)

const { data: cities, isLoading } = useQuery({
  queryKey: ['cities'],
  queryFn: citiesRequest
})

const formSchema = yup.object({
  username: yup.string().required(),
  nickname: yup.string().required(),
  image: yup.string()
})

const selectedCity = ref(0)
const { handleSubmit, setFieldValue } = useForm<IMyPage>({
  validationSchema: formSchema,
  initialValues: {
    username: profile.value?.username,
    nickname: profile.value?.nickname,
    city: selectedCity.value,
    birth: ''
  }
})

const towns = ref<ITown[]>()
watch(selectedCity, async (newCityCode) => {
  console.log('상세 도시 불러오기')
  if (newCityCode) {
    towns.value = await townsRequest(newCityCode)
  } else {
    towns.value = []
  }
  console.log(towns.value)
})

const triggerFileInput = () => {
  fileInput.value?.click()
}

const image = ref<File | null>(null)
const imageSrc = ref<string>('')
const changeImage = (event: Event) => {
  const files = (event.target as HTMLInputElement).files
  if (files && files.length > 0) {
    const file = files[0]
    image.value = file
    const reader = new FileReader()
    reader.onload = (e) => {
      if (profile.value) {
        imageSrc.value = e.target?.result as string
      }
    }
    reader.readAsDataURL(file)
  }
}

const deleteImage = () => {
  imageSrc.value = ''
  image.value = null
}

const onSubmit = handleSubmit(async (values) => {
  console.log(values)
  let isSuccess = false
  if (image.value) {
    isSuccess = await userDataModifyImageRequest(values, image.value)
  } else {
    isSuccess = await userDataModifyRequest(values)
  }

  if (isSuccess) {
    toast({
      title: '회원 정보 수정 성공',
      description: '회원 정보 수정이 완료되었습니다.',
      variant: 'success'
    })
  } else {
    toast({
      title: '회원 정보 수정 실패',
      description: '회원 정보 수정이 실패했습니다. 다시 시도해주세요.',
      variant: 'destructive'
    })
  }
})
</script>

<template>
  <div v-if="profile" class="container w-full" style="max-width: 1200px; padding: 20px">
    <div>
      <h3 class="text-3xl font-bold mb-5">계정 관리</h3>
    </div>
    <Separator />
    <form @submit.prevent="onSubmit" class="space-y-8 mb-3">
      <div class="mt-5">
        <div class="text-xl font-semibold">프로필 이미지</div>
        <div class="relative flex flex-col items-center">
          <div class="relative w-40 h-40">
            <img
              :src="imageSrc || profile.profileImage"
              alt="프로필 이미지"
              class="w-40 h-40 rounded-full object-cover shadow-lg mb-4"
              @click="triggerFileInput"
            />
            <input
              ref="fileInput"
              id="image"
              accept="image/*"
              type="file"
              class="hidden"
              @change="changeImage"
            />
            <button
              class="absolute top-0 right-0 mt-1 mr-1 w-6 h-6 bg-white text-black rounded-full flex items-center justify-center border border-gray-300"
              @click.prevent="deleteImage"
            >
              &times;
            </button>
          </div>
        </div>
      </div>

      <FormField v-slot="{ field, value }" name="nickname">
        <FormItem class="mt-5">
          <FormLabel class="text-xl font-semibold">닉네임</FormLabel>
          <FormControl>
            <Input type="text" v-bind="field" :model-value="value" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ field, value }" name="username">
        <FormItem class="mt-5">
          <FormLabel class="text-xl font-semibold">ID</FormLabel>
          <FormControl>
            <Input type="text" v-bind="field" :model-value="value" readonly />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ field, value }" name="birth">
        <FormItem class="flex flex-col">
          <FormLabel class="text-xl font-semibold">생년월일</FormLabel>
          <Popover>
            <PopoverTrigger as-child>
              <FormControl>
                <Button
                  variant="outline"
                  :class="
                    cn(
                      'w-[280px] justify-start text-left font-normal',
                      !value && 'text-muted-foreground'
                    )
                  "
                >
                  <CalendarIcon class="mr-2 h-4 w-4" />
                  {{ value ? df.format(value.toDate(getLocalTimeZone())) : 'Pick a date' }}
                </Button>
              </FormControl>
            </PopoverTrigger>
            <PopoverContent class="p-0">
              <Calendar
                v-model="dateValue"
                calendar-label="Date of birth"
                initial-focus
                :min-value="new CalendarDate(1900, 1, 1)"
                :max-value="today(getLocalTimeZone())"
                @update:model-value="
                  (v) => {
                    if (v) {
                      dateValue = v
                      setFieldValue('birth', toDate(v).toISOString())
                    } else {
                      dateValue = undefined
                      setFieldValue('birth', undefined)
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

      <FormField v-slot="{ value }" name="city">
        <FormItem class="flex flex-col">
          <FormLabel class="text-xl font-semibold">지역 설정</FormLabel>
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
                    value && cities
                      ? cities.find((city) => city.cityCode === value)?.name
                      : '-- 지역 선택 --'
                  }}
                  <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </FormControl>
            </PopoverTrigger>
            <PopoverContent class="w-[200px] p-0">
              <Command>
                <CommandInput placeholder="지역검색" />
                <CommandEmpty>지역을 찾지 못했습니다.</CommandEmpty>
                <CommandList>
                  <CommandGroup>
                    <CommandItem
                      v-for="city in cities"
                      :key="city.cityCode"
                      :value="city.name"
                      @select="
                        () => {
                          selectedCity = city.cityCode
                          setFieldValue('city', city.cityCode)
                          open = false
                        }
                      "
                    >
                      <Check
                        :class="
                          cn('mr-2 h-4 w-4', value === city.cityCode ? 'opacity-100' : 'opacity-0')
                        "
                      />
                      {{ city.name }}
                    </CommandItem>
                  </CommandGroup>
                </CommandList>
              </Command>
            </PopoverContent>
          </Popover>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ value }" name="town">
        <FormItem class="flex flex-col">
          <Popover>
            <PopoverTrigger as-child>
              <FormControl>
                <Button
                  variant="outline"
                  role="combobox"
                  :aria-expanded="open"
                  :class="cn('w-[200px] justify-between', !value && 'text-muted-foreground')"
                >
                  {{
                    value && towns
                      ? towns.find((town) => town.townCode === value)?.name
                      : '-- 상세 지역 선택 --'
                  }}
                  <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </FormControl>
            </PopoverTrigger>
            <PopoverContent class="w-[200px] p-0">
              <Command>
                <CommandInput placeholder="상세지역검색" />
                <CommandEmpty>상세지역을 찾지 못했습니다.</CommandEmpty>
                <CommandList>
                  <CommandGroup>
                    <CommandItem
                      v-for="town in towns"
                      :key="town.townCode"
                      :value="town.name"
                      @select="
                        () => {
                          setFieldValue('town', town.townCode)
                          open = false
                        }
                      "
                    >
                      <Check
                        :class="
                          cn('mr-2 h-4 w-4', value === town.townCode ? 'opacity-100' : 'opacity-0')
                        "
                      />
                      {{ town.name }}
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
        <Button type="submit">수정</Button>
      </div>
    </form>

    <Separator />
    <div>
      <h3 class="text-xl font-semibold mt-5">비밀번호 변경</h3>
    </div>
  </div>
</template>

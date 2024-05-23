<script setup lang="ts">
import { Button } from "@/components/ui/button";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  CalendarDate,
  DateFormatter,
  getLocalTimeZone,
  today,
  type DateValue
} from '@internationalized/date';
import { Image, X } from "lucide-vue-next";
import { useForm } from "vee-validate";
import { ref, type Ref } from 'vue';
import * as yup from "yup";

import { scheduleCreateRequest } from "@/api/schedule";
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { RangeCalendar } from '@/components/ui/range-calendar';
import { cn } from '@/lib/utils';
import type { ScheduleForm } from "@/types/schedule.type";
import { Calendar as CalendarIcon } from 'lucide-vue-next';
import type { DateRange } from 'radix-vue';
import { useRouter } from "vue-router";
import FormDescription from "../ui/form/FormDescription.vue";
import Switch from "../ui/switch/Switch.vue";
import { useToast } from "../ui/toast";

const cities: { id: number; name: string; image: string }[] = [
  { id: 0, name: "전국", image: "junkok.png" },
  { id: 1, name: "서울", image: "seoul.jpg" },
  { id: 2, name: "인천", image: "incheon.jpg" },
  { id: 3, name: "대전", image: "daejeon.jpg" },
  { id: 4, name: "대구", image: "daegu.jpg" },
  { id: 5, name: "광주", image: "gwangju.jpg" },
  { id: 6, name: "부산", image: "busan.jpg" },
  { id: 7, name: "울산", image: "ulsan.jpg" },
  { id: 8, name: "세종특별자치시", image: "sejong.jpg" },
  { id: 31, name: "경기도", image: "gyeonggido.jpg" },
  { id: 32, name: "강원도", image: "gangwondo.jpg" },
  { id: 33, name: "충청북도", image: "chungcheongbukdo.jpg" },
  { id: 34, name: "충청남도", image: "chungcheongnamdo.jpg" },
  { id: 35, name: "경상북도", image: "gyeongsangbukdo.jpg" },
  { id: 36, name: "경상남도", image: "gyeongsangnamdo.jpg" },
  { id: 37, name: "전라북도", image: "jeollabukdo.jpg" },
  { id: 38, name: "전라남도", image: "jeollanamdo.jpg" },
  { id: 39, name: "제주도", image: "jejuisland.jpg" },
];

const router = useRouter()

const df = new DateFormatter('ko-kr', {
  dateStyle: 'medium',
})

const range = ref({
  start: undefined,
  end: undefined
}) as Ref<DateRange>

const toast = useToast()

const formSchema = yup.object({
  name: yup.string().required("계획 이름을 입력해주세요."),
  multi: yup.boolean(),
  private: yup.boolean(),
  city: yup.number(),
});

const { handleSubmit, setFieldValue, values } = useForm<ScheduleForm>({
  validationSchema: formSchema,
  initialValues: {
    name: "",
    multi: false,
    private: false,
    city: 0,
  },
});

const onSubmit = handleSubmit((values) => {
  if (!range.value.start || !range.value.end) {
    toast.toast({
      title: "여행 일자를 선택해주세요.",
      description: "여행 일자를 선택해주세요.",
      variant: "destructive",
    });
    return;
  }

  const startDate = range.value.start.toDate(getLocalTimeZone());
  const endDate = range.value.end.toDate(getLocalTimeZone());

  scheduleCreateRequest({
    ...values,
    start: startDate,
    end: endDate,
    day: Math.abs((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1,
  }, image.value).then((response) => {
    toast.toast({
      title: "계획 생성 성공",
      description: "계획이 생성되었습니다.",
      variant: "success",
    });
    router.push({ name: 'schedule-detail', params: {scheduleId: response} });
  }).catch(() => {
    toast.toast({
      title: "계획 생성 실패",
      description: "계획 생성에 실패했습니다. 다시 시도해주세요.",
      variant: "destructive",
    });
  });
});

const image = ref<File | undefined>(undefined);
const imageSrc = ref<string | null>(null);
const changeImage = (event: Event) => {
  const files = (event.target as HTMLInputElement).files;
  if (files && files.length > 0) {
    const file = files[0];
    const reader = new FileReader();
    reader.onload = (e) => {
      image.value = file;
      imageSrc.value = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }
};



const rangeLimit = ref<DateRange>({
  start: today(getLocalTimeZone()),
  end: today(getLocalTimeZone()).add({ years: 1 })
})

const setRange = (dateRange: DateRange) => {
  if (!dateRange.start || !dateRange.end) return
  range.value = dateRange
  rangeLimit.value = {
    start: today(getLocalTimeZone()),
    end: dateRange.end.add({ years: 1 })
  }
}

const changeRange = (date: DateValue | undefined) => {
  if (!date) return
  const { year, month, day } = date
  const newDate = new CalendarDate(year, month, day)
  range.value.start = newDate
  rangeLimit.value = {
    start: newDate,
    end: newDate.add({ days: 4 })
  }
}

const disabledDate = ({ year, month, day }: { year: number; month: number; day: number }) => {
  const date = new CalendarDate(year, month, day)
  if (!range.value.start) return false
  return !(rangeLimit.value.start!! <= date && date <= rangeLimit.value.end!!)
}
</script>

<template>
  <form class="flex flex-col rounded-lg max-w-[500px] justify-center">
    <div class="py-4 space-y-2">
      <FormField v-slot="{ componentField }" name="name">
        <FormItem>
          <FormLabel for="name">계획 이름</FormLabel>
          <FormControl>
            <Input
              id="name"
              placeholder="광주 3박 4일 먹거리 투어"
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
      <div class="flex flex-col space-y-2">
        <label class="text-sm font-medium dark:text-white"> 계획 기본 설정 </label>
        <Popover>
          <PopoverTrigger as-child>
            <Button
              variant="outline"
              :class="
                cn(
                  'w-full justify-start text-left font-normal',
                  !range && 'text-muted-foreground'
                )
              "
            >
              <CalendarIcon class="mr-2 h-4 w-4" />
              <template v-if="range.start">
                <template v-if="range.end">
                  {{ df.format(range.start.toDate(getLocalTimeZone())) }} -
                  {{ df.format(range.end.toDate(getLocalTimeZone())) }}
                </template>

                <template v-else>
                  {{ df.format(range.start.toDate(getLocalTimeZone())) }}
                </template>
              </template>
              <template v-else
                ><span class="text-sm">여행 일자를 선택해주세요.</span>
              </template>
            </Button>
          </PopoverTrigger>
          <PopoverContent class="w-auto p-0">
            <RangeCalendar
              v-model="range"
              initial-focus
              :locale="'ko-kr'"
              :hightlight-today="false"
              :number-of-months="2"
              @update:start-value="changeRange"
              @update:model-value="setRange"
              :isDateDisabled="disabledDate"
            />
          </PopoverContent>
        </Popover>
      </div>

      <div class="flex flex-col space-y-2 w-full py-2">
        <label class="text-sm font-medium dark:text-white" htmlFor="cover-image">
          커버 이미지
        </label>
        <div class="flex items-center w-full justify-center">
          <div
            v-if="imageSrc"
            class="relative w-24 h-24 border border-gray-400 z-10 rounded-md overflow-hidden flex flex-col items-center justify-center space-y-2 flex-shrink-0 mr-4 bg-cover bg-center"
            :style="{ backgroundImage: `url(${imageSrc})` }"
          >
            <X
              class="w-4 h-4 absolute top-2 right-2 cursor-pointer"
              @click="
                image = undefined;
                imageSrc = null;
              "
            />
          </div>
          <div
            v-else
            class="w-24 h-24 border border-gray-400 rounded-md overflow-hidden flex flex-col items-center justify-center space-y-2 flex-shrink-0 mr-4"
          >
            <label
              for="image"
              class="cursor-pointer flex flex-col items-center justify-center space-y-2"
            >
              <Image :size="26" class="text-gray-500 dark:text-gray-400" />
              <span class="text-xs text-gray-500 dark:text-gray-400"
                >이미지 추가하기</span
              >
              <input
                id="image"
                accept="image/*"
                type="file"
                class="hidden"
                @change="changeImage"
              />
            </label>
          </div>
        </div>
      </div>
      <div class="flex flex-col space-y-2 w-[450px] py-2">
        <label
          class="text-sm font-medium dark:text-white space-x-2"
          htmlFor="cover-image"
        >
          <span>계획 지역 설정</span>
          <span
            v-if="values"
            class="text-sm font-bold text-blue-600 dark:text-blue-400"
            >{{ cities.find((city) => city.id === values.city)?.name }}</span
          >
        </label>
        <div class="w-full flex flex-row overflow-scroll space-x-1 scrollbar-hide">
          <div :value="city.name" v-for="city in cities" :key="city.id" class="w-16 h-16">
            <div
              class="h-16 w-16 flex-shrink-0 border rounded-xl bg-center bg-cover relative isolate aftter:content-'' after:absolute after:inset-0 after:z-10"
              :class="{
                'border-blue-600 border-2': values.city === city.id,
                'border-gray-400': values.city !== city.id,
              }"
              :style="{
                backgroundImage: `url(/cities/${city.image})`,
              }"
              @click="setFieldValue('city', city.id)"
            >
              <div
                class="absolute inset-0 rounded-xl bg-black bg-opacity-20 z-20 flex w-full h-full flex-col items-center justify-center"
              >
                <h1 class="text-white text-xs font-bold pb-2">{{ city.name }}</h1>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="flex flex-col space-y-2">
        <label class="text-sm font-medium dark:text-white"> 계획 기본 설정 </label>
        <FormField v-slot="{ value, handleChange }" name="multi">
          <FormItem
            class="flex flex-row items-center justify-between rounded-lg border px-4 py-2"
          >
            <div class="space-y-0.5 flex flex-col text-xs">
              <FormLabel class="text-base"> 멀티 방식 사용 </FormLabel>
              <FormDescription class="text-xs">
                여러명이 동시에 여행 계획을 함께 관리할 수 있습니다.
              </FormDescription>
            </div>
            <FormControl>
              <Switch :checked="value" aria-readonly @update:checked="handleChange" />
            </FormControl>
          </FormItem>
        </FormField>
        <FormField v-slot="{ value, handleChange }" name="private">
          <FormItem
            class="flex flex-row items-center justify-between rounded-lg border px-4 py-2"
          >
            <div class="space-y-0.5 flex flex-col text-xs">
              <FormLabel class="text-base"> 비공개 모드 </FormLabel>
              <FormDescription class="text-xs">
                계획을 비공개로 설정하면 다른 사용자에게 공개되지 않습니다.
              </FormDescription>
            </div>
            <FormControl>
              <Switch :checked="value" aria-readonly @update:checked="handleChange" />
            </FormControl>
          </FormItem>
        </FormField>
      </div>
    </div>
    <Button class="w-full" variant="outline" @click="onSubmit">계획 생성하기</Button>
  </form>
</template>

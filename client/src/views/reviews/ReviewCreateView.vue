<script setup lang="ts">
import { reviewRecommendRequest } from "@/api/chat";
import { reviewCreateRequest } from "@/api/review";
import { searchTripRequest } from "@/api/trip";
import CreateRating from "@/components/review/CreateRating.vue";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import Button from "@/components/ui/button/Button.vue";
import Dialog from "@/components/ui/dialog/Dialog.vue";
import DialogContent from "@/components/ui/dialog/DialogContent.vue";
import DialogDescription from "@/components/ui/dialog/DialogDescription.vue";
import DialogTitle from "@/components/ui/dialog/DialogTitle.vue";
import DialogTrigger from "@/components/ui/dialog/DialogTrigger.vue";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/components/ui/toast";
import type { ReviewForm } from "@/types/board.type";
import { useMutation } from "@tanstack/vue-query";
import { Image, LoaderCircle, Locate, MapPin, WandSparkles } from "lucide-vue-next";
import { useForm } from "vee-validate";
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import * as yup from "yup";

const imageIndex = ref<number>(-1);
const images = ref<File[]>([]);
const imageSrcs = ref<string[]>([]);
const image = computed(() => imageSrcs.value[imageIndex.value]);
const MAX_IMAGES = 5; // 업로드 가능한 최대 이미지 개수 설정

const tours = ref<
  {
    tourId: number;
    name: string;
    address: string;
  }[]
>([]);

const open = ref(false);
const toast = useToast();
const router = useRouter();

const { mutate: searchLocation, isPending } = useMutation({
  mutationKey: ["reviews", "searchLocation"],
  mutationFn: async (query: string) => searchTripRequest({ query: query, city: 0 }),
  onSuccess: (data) => {
    tours.value = data.map((tour) => ({
      tourId: tour.tourId,
      name: tour.name,
      address: tour.address,
    }));
  },
});

const formSchema = yup.object({
  name: yup.string().required("방문한 주소를 적어주세요."),
  tourId: yup.number(),
  content: yup.string().required("방문 후기를 작성해주세요."),
  rating: yup.number().required("별점을 입력해주세요").min(1, "별점을 입력해주세요."),
});

const { handleSubmit, setFieldValue, values } = useForm<ReviewForm>({
  validationSchema: formSchema,
});

const onSubmit = handleSubmit(async (values) => {
  if (images.value.length === 0) {
    toast.toast({
      title: "이미지를 추가해주세요.",
      description: "후기 작성을 위해 이미지를 추가해주세요.",
      variant: "destructive",
      duration: 2000,
    });
    return;
  }

  // API 호출
  const isSuccess = await reviewCreateRequest(values, images.value);
  if (isSuccess) {
    router.push({ name: "review" });
    toast.toast({
      title: "후기 작성 성공",
      description: "후기 작성이 완료되었습니다.",
      variant: "success",
      duration: 2000,
    });
    return;
  } else {
    toast.toast({
      title: "후기 작성 실패",
      description: "후기 작성에 실패했습니다. 다시 시도해주세요.",
      variant: "destructive",
      duration: 2000,
    });
  }
});

const pickAddress = (name: string, tourId: number) => {
  setFieldValue("name", name);
  setFieldValue("tourId", tourId);
  open.value = false;
};

// 이미지가 추가 된 경우 호출하는 메서드
const changeImage = (event: Event) => {
  const files = (event.target as HTMLInputElement).files;
  if (files && files.length > 0) {
    if (images.value.length + files.length > MAX_IMAGES) {
      toast.toast({
        title: "이미지 업로드 개수 제한",
        description: `최대 ${MAX_IMAGES}개의 이미지만 업로드할 수 있습니다.`,
        variant: "destructive",
        duration: 2000,
      });
      return;
    }

    [...files].forEach((file: File) => {
      const reader = new FileReader();
      reader.onload = (e) => {
        images.value = [...images.value, file];
        imageSrcs.value = [...imageSrcs.value, e.target?.result as string];
        imageIndex.value = imageSrcs.value.length - 1;
      };
      reader.readAsDataURL(file);
    });
  }
};

// 이미지 삭제 기능
const removeImage = (index: number) => {
  images.value.splice(index, 1);
  imageSrcs.value.splice(index, 1);
  if (imageIndex.value >= imageSrcs.value.length) {
    imageIndex.value = imageSrcs.value.length - 1;
  }
};

const responseContent = ref("");
const isRecommend = ref(false);
const isLoading = ref(false);
const reviewRecommend = async () => {
  const location = values.name;
  const content = values.content;
  if (!location || location.trim().length == 0) {
    responseContent.value = "위치를 설정해주세요";
    isRecommend.value = false;
    return;
  }
  // API 호출
  isLoading.value = true; // 로딩 시작

  try {
    // API 호출
    const data = await reviewRecommendRequest(location, content);
    responseContent.value = data;
    isRecommend.value = true;
  } catch (error) {
    responseContent.value = "서버와의 통신이 원활하지 않습니다. 다시 시도해주세요.";
    isRecommend.value = false;
  } finally {
    isLoading.value = false; // 로딩 종료
  }
};

const changeContent = () => {
  setFieldValue(
    "content",
    (values.content ? values.content : "") + responseContent.value
  );
  initContent();
};

const initContent = () => {
  responseContent.value = "";
  isRecommend.value = false;
};

const currentRating = ref(Number(0));
const updateRating = (rating: number) => {
  currentRating.value = rating;
  setFieldValue("rating", rating);
};
</script>

<template>
  <form
    @submit.prevent="onSubmit"
    class="container flex flex-col py-10 space-y- w-1/2 space-y-4"
  >
    <div class="flex flex-row justify-between">
      <h2 class="text-2xl font-bold">여행 후기 작성</h2>
      <Button class="w-24 h-10" :disabled="isPending" variant="outline" type="submit">
        <LoaderCircle v-if="isPending" class="mr-2 h-4 w-4 animate-spin" />
        <span v-else>작성하기</span>
      </Button>
    </div>
    <div class="flex flex-row space-x-4">
      <!-- 위치 선택 부분 -->
      <div
        class="border border-gray-400 flex-grow flex items-center rounded-md px-4 py-2"
      >
        <Dialog v-model:open="open">
          <DialogTrigger>
            <FormField v-slot="{ componentField }" name="name">
              <FormItem>
                <FormLabel
                  for="name"
                  class="text-lg font-semibold flex flex-row items-center cursor-pointer space-x-3 py-1 w-full"
                >
                  <Locate :size="20" class="text-gray-500 dark:text-gray-400" />
                  <FormControl>
                    <Input
                      id="name"
                      class="ml-4 w-full border-none focus:border-none focus:ring-0 focus-visible:ring-0 focus-visible:ring-offset-0 flex-1"
                      autocomplete="off"
                      placeholder="방문한 주소를 적어주세요."
                      v-bind="componentField"
                    />
                  </FormControl>
                </FormLabel>
                <FormMessage />
              </FormItem>
            </FormField>
          </DialogTrigger>
          <DialogContent>
            <DialogTitle>위치 검색</DialogTitle>
            <DialogDescription>방문하신 위치를 검색해 입력해주세요.</DialogDescription>
            <div class="grid gap-4">
              <div class="grid grid-cols-4 items-center gap-4">
                <Input
                  class="col-span-4"
                  placeholder="Search location..."
                  @keyup.enter="searchLocation($event.target.value)"
                />
              </div>
            </div>
            <div class="grid gap-4">
              <div class="flex flex-col space-y-2 h-96 overflow-scroll scrollbar-hide">
                <div
                  class="flex gap-2 text-gray-500 dark:text-gray-600 hover:bg-gray-100 p-1 rounded-md cursor-pointer flex-col"
                  v-for="(tour, index) in tours"
                  :key="index"
                  @click="pickAddress(tour.name, tour.tourId)"
                >
                  <span class="text-sm text-black">{{ tour.name }}</span>
                  <div class="flex flex-row space-x-2 items-center">
                    <MapPin :size="14" />
                    <span class="text-xs">{{ tour.address }}</span>
                  </div>
                </div>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </div>
      <!-- 위치 출력 종료 -->

      <!-- 별점 출력 부분 -->
      <div
        class="flex items-center space-x-2 border border-gray-400 rounded-md mx-4 px-4 py-2 flex-grow"
      >
        <FormField v-slot="{ componentField }" name="rating">
          <FormItem>
            <FormControl>
              <CreateRating
                :rating="currentRating"
                @update:rating="updateRating"
                v-bind="componentField"
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>
    </div>
    <!-- 이미지 미리보기 -->
    <div
      class="border border-gray-400 flex flex-row items-center rounded-md px-4 py-2 h-96"
    >
      <div
        class="w-full h-full justify-center bg-contain bg-center bg-no-repeat"
        v-if="image"
        :style="{ backgroundImage: `url(${image})` }"
      />
    </div>
    <!-- 이미지 출력 -->
    <div class="flex flex-row">
      <div
        class="w-24 h-24 border border-gray-400 rounded-md overflow-hidden flex flex-col items-center justify-center space-y-2 flex-shrink-0 mr-4"
      >
        <label
          for="image"
          class="cursor-pointer flex flex-col items-center justify-center space-y-2"
        >
          <Image :size="26" class="text-gray-500 dark:text-gray-400" />
          <span class="text-xs text-gray-500 dark:text-gray-400">이미지 추가하기</span>
          <input
            id="image"
            accept="image/*"
            type="file"
            class="hidden"
            :multiple="true"
            @change="changeImage"
          />
        </label>
      </div>
      <div
        class="flex flex-row space-x-4 overflow-x-scroll w-full flex-nowrap scrollbar-hide"
      >
        <div
          class="relative w-24 h-24 border rounded-md overflow-hidden flex flex-col items-center justify-center space-y-2 cursor-pointer flex-shrink-0"
          v-for="(image, index) in imageSrcs"
          :key="index"
          :class="{
            'border-blue-500 border-2': imageIndex === index,
            'border-gray-400': imageIndex !== index,
          }"
          @click="imageIndex = index"
        >
          <img :src="image" class="w-full h-full object-cover" />
          <button
            class="absolute -top-2.5 -right-0.5 w-5 h-5 bg-black text-white border border-gray-300 flex items-center justify-center opacity-75 hover:opacity-100"
            @click.stop="removeImage(index)"
          >
            <span class="sr-only">Remove image</span>
            &times;
          </button>
        </div>
      </div>
    </div>
    <!-- 이미지 출력 종료 -->

    <!-- 글 작성 부분 -->
    <div class="flex flex-col border border-gray-400 rounded-md px-4 py-2 h-72">
      <FormField v-slot="{ componentField }" name="content">
        <FormItem>
          <div class="flex justify-between items-center py-1 w-full">
            <FormLabel
              for="content"
              class="text-lg font-semibold flex items-center cursor-pointer space-x-3"
            >
              후기 작성
            </FormLabel>
            <FormMessage />
            <div class="flex justify-end">
              <AlertDialog>
                <AlertDialogTrigger @click.prevent="reviewRecommend">
                  <Button> AI추천 작성 <WandSparkles class="size-4 ml-1" /></Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle class="mb-3">AI추천 작성</AlertDialogTitle>
                    <div v-if="isLoading" class="flex justify-center items-center h-40">
                      <LoaderCircle class="h-16 w-16 animate-spin" />
                    </div>
                    <AlertDialogDescription v-else>{{
                      responseContent
                    }}</AlertDialogDescription>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel @click="initContent">취소</AlertDialogCancel>
                    <AlertDialogAction v-if="isRecommend" @click="changeContent"
                      >결정</AlertDialogAction
                    >
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
            </div>
          </div>
          <FormControl>
            <Textarea
              class="h-52 resize-none border-none focus:border-none focus:ring-0 w-full focus-visible:ring-0 focus-visible:ring-offset-0"
              placeholder="Write a caption..."
              rows="{10}"
              v-bind="componentField"
            />
          </FormControl>
        </FormItem>
      </FormField>
    </div>
  </form>
</template>

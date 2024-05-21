<script setup lang="ts">
import {
  scheduleInviteRequest,
  schedulePublicRequest,
  scheduleRevokeRequest,
} from "@/api/schedule";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { imageOrDefault } from "@/lib/image-load";
import type { InviteUser } from "@/types/schedule.type";
import { useMutation, useQueryClient } from "@tanstack/vue-query";
import { computed, ref } from "vue";
import { useToast } from "../ui/toast";

const props = defineProps<{
  scheduleId: number;
  publicKey: string;
  name: string;
  owner: string;
  invitedUsers: InviteUser[];
}>();

const usePublic = computed(() => props.publicKey !== null);
const isOpen = ref<boolean>(false);
const isInviteOpen = ref<boolean>(false);

const message = computed(() => {
  if (!usePublic.value) {
    return {
      title: "정말 여행 계획을 공유하시겠습니까?",
      description: "확인 버튼을 누르면 해당 여행 계획을 공유할 수 있는 URL이 생성됩니다.",
    };
  }
  return {
    title: "정말 여행 계획을 비공개로 설정하시겠습니까?",
    description: "확인 버튼을 누르면 해당 URL은 더 이상 사용할 수 없게 됩니다.",
  };
});

const queryClient = useQueryClient();

const { mutate: publicMutate } = useMutation({
  mutationKey: ["schedule", props.scheduleId, "public"],
  mutationFn: () => schedulePublicRequest(props.scheduleId),
  onSuccess: () => {
    queryClient.invalidateQueries({
      queryKey: ["schedule", props.scheduleId],
    });
  },
});

const { mutate: remokeMutate } = useMutation({
  mutationKey: ["schedule", props.scheduleId, "revoke"],
  mutationFn: () => scheduleRevokeRequest(props.scheduleId),
  onSuccess: () => {
    queryClient.invalidateQueries({
      queryKey: ["schedule", props.scheduleId],
    });
  },
});

const updatePublic = () => {
  if (usePublic.value) {
    remokeMutate();
  } else {
    publicMutate();
  }
  isOpen.value = false;
};

console.log(props.invitedUsers);

const username = ref<string>("");

const toast = useToast();

const invite = () => {
  scheduleInviteRequest(props.scheduleId, {
    username: username.value,
    name: props.name,
    owner: props.owner,
  })
    .then(() => {
      isInviteOpen.value = false;

      toast.toast({
        title: "초대 성공",
        description: `${username.value}님을 여행 계획에 초대했습니다.`,
        variant: "success",
      });
    })
    .catch(() => {
      isInviteOpen.value = false;

      toast.toast({
        title: "초대 실패",
        description: `${username.value}님을 여행 계획에 초대하는데 실패했습니다.`,
        variant: "destructive",
      });
    });
};

const onSubmit = () => {
  isInviteOpen.value = true;
};
</script>
<template>
  <AlertDialog :open="isOpen">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle>{{ message.title }}</AlertDialogTitle>
        <AlertDialogDescription>
          {{ message.description }}
        </AlertDialogDescription>
      </AlertDialogHeader>
      <AlertDialogFooter>
        <AlertDialogCancel @click="isOpen = false">취소</AlertDialogCancel>
        <AlertDialogAction @click="updatePublic">확인</AlertDialogAction>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
  <AlertDialog :open="isInviteOpen">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle>친구 초대하기</AlertDialogTitle>
        <AlertDialogDescription>
          {{ username }}을/를 여행 계획에 초대하시겠습니까?
        </AlertDialogDescription>
      </AlertDialogHeader>
      <AlertDialogFooter>
        <AlertDialogCancel @click="isInviteOpen = false">취소</AlertDialogCancel>
        <AlertDialogAction @click="invite">확인</AlertDialogAction>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
  <div>
    <h1 class="text-xl font-semibold">유저 추가하기</h1>
    <span class="text-sm text-gray-400"
      >해당 여행 계획에 친구를 추가하여 함께 여행을 설계해보세요!</span
    >

    <div class="flex flex-col items-start justify-between space-y-1 space-x-2 my-4">
      <Label for="public-path">퍼블릭 URL 생성하기</Label>
      <div class="flex flex-row items-center w-full space-x-5 py-3">
        <Switch id="public-path" :checked="usePublic" @update:checked="isOpen = true" />
        <span
          class="flex w-4/5 px-2 items-center text-sm select-all h-8 border border-gray-400 rounded-md focus-visible:ring-0 focus-visible:ring-offset-0"
          >{{ publicKey }}</span
        >
      </div>
    </div>
    <form
      @submit.prevent="onSubmit"
      class="flex flex-col items-start justify-between space-y-1 space-x-2 my-4"
    >
      <Label for="public-path">유저 추가하기</Label>
      <div class="flex flex-row items-center w-full space-x-5 py-3">
        <Label for="public-path">이메일</Label>
        <Input type="text" class="flex w-3/5 h-8" v-model="username" />
        <Button type="submit" variant="outline" class="h-9">입력</Button>
      </div>
    </form>
    <Label for="invite-user">초대된 유저</Label>
    <div class="flex flex-row w-[400px] py-2 space-x-2">
      <div
        v-for="user in invitedUsers"
        :key="user.userId"
        class="flex flex-col space-y-1 w-12 h-16 items-center overflow-x-scroll scrollbar-hide"
      >
        <img
          :src="imageOrDefault(user.profileImage)"
          alt="profile"
          class="w-10 h-10 rounded-full"
        />
        <span class="text-xs">{{ user.nickname }}</span>
      </div>
    </div>
  </div>
</template>

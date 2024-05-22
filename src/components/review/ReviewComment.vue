<script setup lang="ts">
import { commentDeleteRequest, commentUpdateRequest } from "@/api/review";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAuthenticationStore } from "@/stores/authentication";
import type { IComment } from "@/types/board.type";
import { useQueryClient } from "@tanstack/vue-query";
import { Ellipsis, Eraser, PenLine } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useToast } from "../ui/toast";

const router = useRouter();
const props = defineProps<{
  comment: IComment;
  reviewId: number;
}>();

const toast = useToast();

const queryClient = useQueryClient();
const { profile } = storeToRefs(useAuthenticationStore());

const isUpdate = ref(false);
const updatedComment = ref(props.comment.content);

const cancle = () => {
  isUpdate.value = false;
  updatedComment.value = props.comment.content;
};

const onUpdate = async () => {
  if (!confirm("댓글을 수정하시겠습니까?")) {
    return;
  }
  const isSuccess = await commentUpdateRequest(
    props.comment.commentId,
    updatedComment.value
  );
  if (isSuccess) {
    toast.toast({
      title: "댓글 수정 성공",
      description: "댓글이 수정되었습니다.",
      duration: 2000,
      variant: "success",
    });
    queryClient.invalidateQueries({
      queryKey: ["reviews", props.reviewId, "comments"],
    });
    isUpdate.value = false;
  } else {
    toast.toast({
      title: "댓글 수정 실패",
      description: "댓글 수정 요청이 실패하였습니다. 잠시후 다시 시도해주세요",
      duration: 2000,
      variant: "destructive",
    });
  }
};

const onDelete = async () => {
  if (!confirm("댓글을 삭제하시겠습니까?")) {
    return;
  }
  const isSuccess = await commentDeleteRequest(props.comment.commentId);
  if (isSuccess) {
    toast.toast({
      title: "댓글 삭제 성공",
      description: "댓글이 삭제되었습니다.",
      duration: 2000,
      variant: "success",
    });
    queryClient.invalidateQueries({
      queryKey: ["reviews", props.reviewId, "comments"],
    });
  } else {
    toast.toast({
      title: "댓글 삭제 실패",
      description: "댓글 삭제 요청이 실패하였습니다. 잠시후 다시 시도해주세요",
      duration: 2000,
      variant: "destructive",
    });
  }
};
</script>

<template>
  <div class="flex items-center gap-1 pb-1">
    <Avatar class="w-7 h-7 border" @click="router.push(`/user/${comment.user.userId}`)">
      <AvatarImage alt="@shadcn" :src="comment.user.profileImage" />
      <AvatarFallback>{{ comment.user.userId }}</AvatarFallback>
    </Avatar>
    <div class="flex flex-grow flex-col space-y-1">
      <div class="flex items-center gap-2">
        <h5 class="font-medium text-sm">{{ comment.user.nickname }}</h5>
        <span class="text-xs text-gray-500 dark:text-gray-400">
          {{ new Date(comment.createdAt).toLocaleDateString() }}
        </span>
        <div
          v-if="
            profile &&
            (comment.user.userId === profile.id || profile.roleType === 'ADMIN')
          "
          class="ml-auto"
        >
          <DropdownMenu>
            <DropdownMenuTrigger>
              <Button class="w-8 h-8 rounded-full" size="icon" variant="ghost">
                <Ellipsis class="w-4 h-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem @click="isUpdate = true">
                <PenLine class="w-4 h-4 mr-2" />
                수정하기
              </DropdownMenuItem>
              <DropdownMenuItem @click="onDelete">
                <Eraser class="w-4 h-4 mr-2" />
                삭제하기
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
      <form
        v-if="isUpdate"
        @submit.prevent="onUpdate"
        class="flex flex-col space-y-1 px-2"
      >
        <textarea
          class="border p-2 text-sm text-gray-700"
          v-model="updatedComment"
          rows="2"
        />
        <div class="flex gap-1 mt-1">
          <Button type="submit" size="sm">수정</Button>
          <Button
            size="sm"
            variant="secondary"
            class="border border-gray-300"
            @click.prevent="cancle"
            >취소</Button
          >
        </div>
      </form>
      <p v-else class="px-2 text-xs text-gray-800">{{ comment.content }}</p>
    </div>
  </div>
</template>

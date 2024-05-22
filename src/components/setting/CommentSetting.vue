<script setup lang="ts">
import { userCommentsRequest } from "@/api/review";
import SimpleCommentCard from "@/components/card/SimpleCommentCard.vue";
import Separator from "@/components/ui/separator/Separator.vue";
import { useAuthenticationStore } from "@/stores/authentication";
import { useQuery } from "@tanstack/vue-query";
import { storeToRefs } from "pinia";
import Button from "../ui/button/Button.vue";
const authenticationStore = useAuthenticationStore();
const { profile } = storeToRefs(authenticationStore);

const { data: comments } = useQuery({
  queryKey: ["comments", profile.value?.id],
  queryFn: () => userCommentsRequest(profile.value!.id),
});
</script>

<template>
  <div class="container w-full max-w-6xl mx-auto p-5">
    <div>
      <h3 class="text-3xl font-bold mb-5">댓글 모아보기</h3>
    </div>
    <Separator />
    <div v-if="comments && comments?.length > 0">
      <SimpleCommentCard
        v-for="comment in comments"
        :key="comment.commentId"
        :comment="comment"
      />
    </div>
    <div v-else class="flex flex-col items-center justify-center min-h-screen">
      <h4 class="text-xl font-semibold mb-3 text-center">
        아직 작성한 댓글이 존재하지않습니다.
      </h4>
      <Button class="mt-4"
        ><RouterLink :to="{ name: 'review' }">리뷰 둘러보기</RouterLink></Button
      >
    </div>
  </div>
</template>

<style scoped></style>

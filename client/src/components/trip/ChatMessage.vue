<script setup lang="ts">
import { scheduleChatRequst } from "@/api/schedule";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { toTime } from "@/lib/formatter";
import { imageOrDefault } from "@/lib/image-load";
import { useAuthenticationStore } from "@/stores/authentication";
import { useScheduleSocket } from "@/stores/web-stomp";
import { ChatType } from "@/types/schedule.type";
import { useQuery } from "@tanstack/vue-query";
import { Maximize2, Minimize2 } from "lucide-vue-next";
import { storeToRefs } from "pinia";
import { computed, onUpdated, ref } from "vue";
const props = defineProps<{
  name: string;
  scheduleId: number;
}>();

const { authUserId } = storeToRefs(useAuthenticationStore());
const message = ref<string>("");

const { data: prevChats } = useQuery({
  queryKey: ["schedule", "chat", String(props.scheduleId)],
  queryFn: () => scheduleChatRequst(props.scheduleId),
});
const scrollRef = ref<HTMLElement | null>(null);

const { sendChat, toggleOpen } = useScheduleSocket();
const { chats: socketChats, isOpen, profileDictionary, unseen } = storeToRefs(
  useScheduleSocket()
);

const chats = computed(() => {
  if (!prevChats.value || !socketChats.value) return [];
  const newChat = [...prevChats.value, ...socketChats.value];
  return newChat.map((chat) => {
    return {
      ...chat,
      mine: chat.userId === authUserId.value,
    };
  });
});

onUpdated(() => {
  if (scrollRef.value) {
    scrollRef.value.lastElementChild?.scrollIntoView({ behavior: "smooth" });
  }
});

const onSubmit = () => {
  if (message.value.trim() === "") return;
  sendChat(props.scheduleId, message.value);
  message.value = "";
};
</script>

<template>
  <div v-if="!isOpen" class="flex flex-col h-10">
    <div
      class="flex bg-gray-900 text-white flex-row justify-between w-full px-3 h-full items-center"
    >
      <div class="flex flex-row space-x-1 items-center">
        <h2 class="text-md font-semibold">{{ name }}</h2>
        <div class="w-6 h-6 rounded-full bg-white">
          <span
            class="text-xs text-red-700 w-full h-full flex items-center justify-center"
            >{{ socketChats.length - unseen }}</span
          >
        </div>
      </div>
      <Maximize2 :size="18" class="cursor-pointer" @click="toggleOpen" />
    </div>
  </div>
  <div v-else class="flex flex-col h-[500px]">
    <header class="bg-gray-900 text-white p-2 flex items-center justify-between">
      <div class="flex flex-row justify-between w-full px-3 items-center">
        <div>
          <h2 class="text-sm font-semibold">{{ name }}</h2>
          <p class="text-xs text-gray-400">5 participants</p>
        </div>
        <Minimize2 :size="18" class="cursor-pointer" @click="toggleOpen" />
      </div>
      <div class="flex items-center space-x-2" />
    </header>
    <div class="flex-1 overflow-auto p-2">
      <div class="space-y-4" ref="scrollRef">
        <div v-for="(chat, index) in chats" :key="index">
          <div v-if="chat.type === ChatType.ENTER" class="flex justify-center py-1">
            <span class="bg-red-100 text-black text-xs py-1 px-3 rounded-lg">
              {{ profileDictionary[chat.userId]?.nickname }}님이 입장하였습니다.
            </span>
          </div>
          <div
            v-else-if="chat.type === ChatType.TALK && chat.mine"
            class="flex flex-row items-end space-x-2 justify-end"
          >
            <div class="flex flex-row h-full items-end space-x-1 max-w-[70%]">
              <p
                class="text-gray-500 flex h-full items-end dark:text-gray-400"
                style="font-size: 10px"
              >
                {{ toTime(new Date(chat.createdAt)) }}
              </p>
              <div class="bg-gray-100 dark:bg-gray-800 rounded-lg p-2 h-full">
                <p class="text-xs leading-tight">
                  {{ chat.message }}
                </p>
              </div>
            </div>
          </div>
          <div
            v-else-if="chat.type === ChatType.TALK && !chat.mine"
            class="flex flex-row items-start space-x-2"
          >
            <Avatar class="h-7 w-7">
              <AvatarImage
                :alt="profileDictionary[chat.userId]?.nickname"
                :src="imageOrDefault(profileDictionary[chat.userId]?.profileImage)"
              />
              <AvatarFallback>{{
                profileDictionary[chat.userId]?.nickname
              }}</AvatarFallback>
            </Avatar>
            <div class="flex flex-row h-full items-end space-x-1 max-w-[70%]">
              <div class="bg-gray-100 dark:bg-gray-800 rounded-lg p-2 h-full">
                <p class="text-xs leading-tight">
                  {{ chat.message }}
                </p>
              </div>
              <p
                class="text-gray-500 flex h-full items-end dark:text-gray-400"
                style="font-size: 10px"
              >
                {{ toTime(new Date(chat.createdAt)) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <form
      @submit.prevent="onSubmit"
      class="bg-gray-100 dark:bg-gray-800 p-2 flex items-center"
    >
      <Input
        class="flex-1 mr-2 text-xs"
        placeholder="채팅을 입력해주세요."
        type="text"
        v-model="message"
      />
      <Button class="text-xs" type="submit">전송</Button>
    </form>
  </div>
</template>

<script setup lang="ts">
import CommonHeader from "@/components/common/CommonHeader.vue";
import Toaster from "@/components/ui/toast/Toaster.vue";
import { VueQueryDevtools } from "@tanstack/vue-query-devtools";
import { storeToRefs } from "pinia";
import { RouterView } from "vue-router";
import { refreshRequest } from "./api/auth";
import { userDataRequest } from "./api/user";
import { useAuthenticationStore } from "./stores/authentication";

const { isLogin, profile } = storeToRefs(useAuthenticationStore());
const isRefreshRequest = sessionStorage.getItem("isRefreshRequest");
if (!isLogin.value && !isRefreshRequest) {
  refreshRequest()
    .then(() => {
      userDataRequest();
    })
    .finally(() => {
      sessionStorage.setItem("isRefreshRequest", "true");
    });
} else if (isLogin.value && !profile.value) {
  userDataRequest();
}
</script>

<template>
  <CommonHeader />
  <RouterView />
  <Toaster />
  <VueQueryDevtools :button-position="'top-right'" />
</template>

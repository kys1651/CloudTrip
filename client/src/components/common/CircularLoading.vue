<script setup lang="ts">
import { LoaderCircle } from "lucide-vue-next";
</script>

<template>
  <div class="h-screen w-screen flex flex-col justify-center items-center">
    <span class="text-md font-bold mb-2">잠시만 기다려주세요.</span>
    <LoaderCircle class="mr-2 h-16 w-16 animate-spin" />
  </div>
</template>

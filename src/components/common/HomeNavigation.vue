<script setup lang="ts">
import { RouterLink } from "vue-router";
</script>

<template>
  <div>
    <RouterLink :to="{ name: 'home' }" class="relative z-20 flex items-center">
      <img src="/logo.png" alt="logo" class="w-10 h-10" />
      <span class="ml-2 text-lg font-medium">CLOUD TRIP</span>
    </RouterLink>
  </div>
</template>

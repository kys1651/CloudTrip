<script setup lang="ts">
import SidebarNav from './SidebarNav.vue'
import Separator from '@/components/ui/separator/Separator.vue'

defineProps(['title', 'description'])
</script>

<template>
  <div class="hidden space-y-6 p-10 pb-16 md:block">
    <div class="space-y-0.5">
      <h2 class="text-2xl font-bold tracking-tight">{{ title }}</h2>
      <p class="text-muted-foreground">{{ description }}</p>
    </div>
    <Separator class="my-6" />
    <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-12 lg:space-y-0">
      <aside class="-mx-4 lg:w-1/5">
        <SidebarNav />
      </aside>
      <div class="flex-1">
        <div class="space-y-6">
          <slot />
        </div>
      </div>
    </div>
  </div>
</template>

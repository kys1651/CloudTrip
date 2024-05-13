<script setup lang="ts">
import {
  Pagination,
  PaginationEllipsis,
  PaginationFirst,
  PaginationLast,
  PaginationList,
  PaginationListItem,
  PaginationNext,
  PaginationPrev
} from '@/components/ui/pagination'

import { Button } from '@/components/ui/button'
import { ITEM_NAME } from 'node_modules/radix-vue/dist/Menu/utils'
import { ref, computed } from 'vue'

const emit = defineEmits(['pageNumber'])
</script>

<template>
  <Pagination
    v-slot="{ page }"
    :total="100"
    :sibling-count="1"
    show-edges
    :default-page="1"
    class="place-items-center"
  >
    <PaginationList v-slot="{ items }" class="flex items-center gap-1">
      <PaginationFirst />
      <PaginationPrev @click="$emit('pageNumber', page - 1)" />

      <template v-for="(item, index) in items">
        <PaginationListItem v-if="item.type === 'page'" :key="index" :value="item.value" as-child>
          <Button
            @click="$emit('pageNumber', item.value)"
            class="w-10 h-10 p-0"
            :variant="item.value === page ? 'default' : 'outline'"
          >
            {{ item.value }}
          </Button>
        </PaginationListItem>
        <PaginationEllipsis v-else :key="item.type" :index="index" />
      </template>

      <PaginationNext @click="$emit('pageNumber', page + 1)" />
      <PaginationLast />
    </PaginationList>
  </Pagination>
</template>

<template>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    class="h-6 w-6 cursor-pointer"
    :class="filled ? 'text-yellow-500' : 'text-gray-400'"
    viewBox="0 0 20 20"
    fill="currentColor"
  >
    <path
      d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.186 3.637a1 1 0 00.95.69h3.813c.969 0 1.371 1.24.588 1.81l-3.087 2.24a1 1 0 00-.364 1.118l1.186 3.637c.3.921-.755 1.688-1.54 1.118l-3.087-2.24a1 1 0 00-1.175 0l-3.087 2.24c-.785.57-1.84-.197-1.54-1.118l1.186-3.637a1 1 0 00-.364-1.118L2.17 8.064c-.783-.57-.38-1.81.588-1.81h3.812a1 1 0 00.95-.69L9.049 2.927z"
    />
  </svg>
</template>

<script setup lang="ts">
defineProps({
  filled: {
    type: Boolean,
    required: true
  }
})
</script>

<template>
  <div class="flex items-center space-x-1">
    <IconReviewRating
      v-for="(star, index) in 5"
      :key="index"
      :filled="currentRating >= index + 1"
      @mouseenter="() => setRating(index + 1)"
    />
    <span v-if="currentRating" class="font-semibold">({{ currentRating }})</span>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, watch } from 'vue'
import IconReviewRating from '@/components/icons/IconReviewRating.vue'
const props = defineProps(['rating'])
const emit = defineEmits(['update:rating'])
const currentRating = ref(props.rating)

const setRating = (rating: number) => {
  currentRating.value = rating
  emit('update:rating', rating)
}

watch(
  () => props.rating,
  (newValue) => {
    currentRating.value = newValue
  }
)
</script>

<style scoped></style>

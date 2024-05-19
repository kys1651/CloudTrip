<template>
  <div class="flex items-center space-x-1">
    <IconReviewRating
      v-for="(star, index) in 5"
      :key="index"
      :filled="currentRating >= index + 1"
      @click="() => setRating(index + 1)"
    />
    <span v-if="currentRating" class="font-semibold">({{ currentRating }})</span>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, watch } from 'vue'
import IconReviewRating from '@/components/icons/IconReviewRating.vue'
const props = defineProps({
  modelValue: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue'])
const currentRating = ref(props.modelValue)

const setRating = (rating: number) => {
  currentRating.value = rating
  emit('update:modelValue', currentRating.value)
}

watch(
  () => props.modelValue,
  (newValue) => {
    currentRating.value = newValue
  }
)
</script>

<style scoped></style>

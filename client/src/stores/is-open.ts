import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useOpenStore = defineStore('is-open', () => {
  const isOpen = ref<boolean>(false)
  const updateOpen = (open: boolean) => {
    isOpen.value = open
  }
  const close = () => isOpen.value = false;
  const open = () => isOpen.value = true;
  return { isOpen, updateOpen, open, close }
})

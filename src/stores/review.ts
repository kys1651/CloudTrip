import type { IReview } from "@/types/board.type";
import { defineStore } from "pinia";
import { ref } from "vue";

const INIT_REVIEW: IReview = {
  reviewId: 0,
  user: {
    userId: 0,
    nickname: "",
    profileImage: "",
  },
  isLiked: false,
  images: [],
  content: "",
  tourId: 0,
  address: "",
  createdAt: 0,
};

export const useReview = defineStore('review', () => {
  const review = ref<IReview>(INIT_REVIEW)

  const updateReview = (newReview: IReview) => {
    review.value = newReview
  }

  return { review, updateReview }
})

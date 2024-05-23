<template>
  <div>
    <a ref="kakaoTalkSharing" href="javascript:void(0)">
      <Send :size="20" />
    </a>
  </div>
</template>

<script setup lang="ts">
import { FRONTEND_URL } from '@/api/kakao';
import { Send } from 'lucide-vue-next';
import { onMounted, ref, type Ref } from 'vue';

const props = defineProps(['review'])
const review = props.review
const likeCount = Number(review.likeCount)

// Kakao SDK 초기화 설정을 모듈로 분리
const useKakao = (elementRef: Ref<HTMLAnchorElement | null>) => {
  const initializeKakao = () => {
    if (window.Kakao && !window.Kakao.isInitialized()) {
      window.Kakao.init(`${import.meta.env.VITE_APP_KAKAO_JS_API_KEY}`)
    }

    if (window.Kakao) {
      window.Kakao.Share.createDefaultButton({
        container: elementRef.value,
        objectType: 'feed',
        itemContent: {
          titleImageText: `💌리뷰 후기를 공유했습니다!`,
          titleImageCategory: `📍위치: ${review.tourName}`
        },
        content: {
          title: `✏️ 작성자: ${review.user.nickname}`,
          description: `내용: ${review.content}`,
          imageUrl: `${review.images[0]}`,
          link: {
            mobileWebUrl: FRONTEND_URL,
            webUrl: FRONTEND_URL
          }
        },
        social: {
          likeCount: likeCount
        },
        buttons: [
          {
            title: '리뷰 바로가기',
            link: {
              mobileWebUrl: `${FRONTEND_URL}/review/${review.reviewId}`,
              webUrl: `${FRONTEND_URL}/review/${review.reviewId}`
            }
          },
          {
            title: '사이트',
            link: {
              mobileWebUrl: `${FRONTEND_URL}`,
              webUrl: `${FRONTEND_URL}`
            }
          }
        ]
      })
    }
  }

  onMounted(() => {
    initializeKakao()
  })
}
const kakaoTalkSharing = ref<HTMLAnchorElement | null>(null)
useKakao(kakaoTalkSharing)
</script>

<style scoped></style>

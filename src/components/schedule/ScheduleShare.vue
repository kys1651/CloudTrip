<template>
  <div>
    <a ref="kakaoTalkSharing" href="javascript:void(0)">
      <Share2 />
    </a>
  </div>
</template>

<script setup lang="ts">
const cities: { id: number; name: string; image: string }[] = [
  { id: 0, name: "전국", image: "junkok.png" },
  { id: 1, name: "서울", image: "seoul.jpg" },
  { id: 2, name: "인천", image: "incheon.jpg" },
  { id: 3, name: "대전", image: "daejeon.jpg" },
  { id: 4, name: "대구", image: "daegu.jpg" },
  { id: 5, name: "광주", image: "gwangju.jpg" },
  { id: 6, name: "부산", image: "busan.jpg" },
  { id: 7, name: "울산", image: "ulsan.jpg" },
  { id: 8, name: "세종특별자치시", image: "sejong.jpg" },
  { id: 31, name: "경기도", image: "gyeonggido.jpg" },
  { id: 32, name: "강원도", image: "gangwondo.jpg" },
  { id: 33, name: "충청북도", image: "chungcheongbukdo.jpg" },
  { id: 34, name: "충청남도", image: "chungcheongnamdo.jpg" },
  { id: 35, name: "경상북도", image: "gyeongsangbukdo.jpg" },
  { id: 36, name: "경상남도", image: "gyeongsangnamdo.jpg" },
  { id: 37, name: "전라북도", image: "jeollabukdo.jpg" },
  { id: 38, name: "전라남도", image: "jeollanamdo.jpg" },
  { id: 39, name: "제주도", image: "jejuisland.jpg" },
];
import { FRONTEND_URL } from '@/api/kakao';
import type { ISchedule } from '@/types/schedule.type';
import { Share2 } from 'lucide-vue-next';
import { onMounted, ref, type Ref } from 'vue';

const props = defineProps<{schedule: ISchedule}>()

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
          titleImageText: '💌여행 계획을 공유했습니다!',
          titleImageCategory: `📍위치: ${props.schedule.cityName}`
        },
        content: {
          title: `✏️ 작성자: ${props.schedule.user.nickname}`,
          description: `일정: ${props.schedule.startDate} ~ ${props.schedule.endDate}`,
          imageUrl: `${props.schedule.thumbnailImage || cities.find((city) => city.id === props.schedule.cityCode)!!.image }`,
          link: {
            mobileWebUrl: FRONTEND_URL,
            webUrl: FRONTEND_URL
          }
        },
        buttons: [
          {
            title: '리뷰 바로가기',
            link: {
              mobileWebUrl: `${FRONTEND_URL}/schedule/${props.schedule.scheduleId}`,
              webUrl: `${FRONTEND_URL}/schedule/${props.schedule.scheduleId}`
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

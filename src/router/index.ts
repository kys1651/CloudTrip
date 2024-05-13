import HomeView from '@/views/HomeView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/sign-in',
      name: 'sign-in',
      component: () => import('@/views/SignInView.vue')
    },
    {
      path: '/social',
      name: 'social',
      component: () => import('@/views/social/SocialView.vue')
    },
    {
      path: '/notice',
      name: 'notice',
      component: () => import('@/views/notice/NoticeListView.vue')
    },
    {
      path: '/trip',
      name: 'trip',
      component: () => import('@/views/TripView.vue')
    },
    {
      path: '/review',
      name: 'review',
      component: () => import('@/views/ReviewView.vue')
    },
    {
      path: '/review/create',
      name: 'review-create',
      component: () => import('@/views/ReviewCreateView.vue')
    },
    {
      path: '/reviewDetail/:id',
      name: 'reviewDetail',
      component: () => import('@/components/review/ReviewDetail.vue')
    },
    {
      path: '/reviewWrite',
      name: 'reviewWrite',
      component: () => import('@/components/review/ReviewWrite.vue')
    },
    {
      path: '/oauth2/redirect',
      name: 'oauth2-redirect',
      component: () => import('@/callback/OAuthRedirect.vue')
    },
    {
      path: '/confirm/email',
      name: 'confirm-email',
      component: () => import('@/callback/ConfirmEmail.vue')
    },
    {
      path: '/notice/view/:noticeId',
      name: 'notice-view',
      component: () => import('@/views/notice/NoticeDetailView.vue')
    },
    {
      path: '/notice/modify/:noticeId',
      name: 'notice-modify',
      component: () => import('@/views/notice/NoticeUpdateView.vue')
    },
    {
      path: '/notice/create/',
      name: 'notice-create',
      component: () => import('@/views/notice/NoticeCreateView.vue')
    },
    {
      path: '/planning/:cityId',
      name: 'planning',
      component: () => import('@/views/PlanningView.vue')
    }
  ]
})

export default router

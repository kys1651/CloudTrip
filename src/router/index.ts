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
      component: () => import('@/views/social/SocialListView.vue')
    },
    {
      path: '/social/:socialId',
      name: 'social-detail',
      component: () => import('@/views/social/SocialView.vue')
    },
    {
      path: '/social/:socialId',
      name: 'social-modify',
      component: () => import('@/views/social/SocialUpdateView.vue')
    },
    {
      path: '/social/create',
      name: 'social-create',
      component: () => import('@/views/social/SocialCreateView.vue')
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
      path: '/notice/create/',
      name: 'notice-create-quill',
      component: () => import('@/views/notice/NoticeCreateQuillView.vue')
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('@/views/user/UserListView.vue')
    },
    {
      path: '/planning/:cityId',
      name: 'planning',
      component: () => import('@/views/PlanningView.vue')
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/MyPageView.vue')
    },
    {
      path: '/account',
      name: 'account',
      component: () => import('@/views/MyAccountView.vue')
    },
    {
      path: '/myReview',
      name: 'myReview',
      component: () => import('@/views/MyReviewView.vue')
    },
    {
      path: '/myComment',
      name: 'myComment',
      component: () => import('@/views/MyCommentView.vue')
    },
    {
      path: '/likeReview',
      name: 'likeReview',
      component: () => import('@/views/MyLikeReviewView.vue')
    }
    // {
    //   path: '/admin',
    //   name: 'admin',
    //   component: () => import('@/components/mypage/Notifications.vue')
    // }
  ]
})

export default router

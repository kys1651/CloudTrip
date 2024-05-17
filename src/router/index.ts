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
      component: () => import('@/views/reviews/ReviewView.vue')
    },
    {
      path: '/review/create',
      name: 'review-create',
      component: () => import('@/views/reviews/ReviewCreateView.vue')
    },
    {
      path: '/review/:id',
      name: 'review-detail',
      component: () => import('@/views/reviews/ReviewDetailView.vue')
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
      path: '/user',
      name: 'user',
      component: () => import('@/views/user/UserListView.vue')
    },
    {
      path: '/user/:userId',
      name: 'userDetail',
      component: () => import('@/views/UserView.vue'),
      children: [
        {
          path: 'reviews',
          name: 'userReviews',
          component: () => import('@/components/user/UserReviews.vue')
        },
        {
          path: 'comments',
          name: 'userComments',
          component: () => import('@/components/user/UserComments.vue')
        },
        {
          path: 'liked-reviews',
          name: 'userLikedReviews',
          component: () => import('@/components/user/UserLikedReviews.vue')
        }
      ]
    },
    {
      path: '/planning/:cityId',
      name: 'planning',
      component: () => import('@/views/PlanningView.vue')
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/MyPageView.vue'),
      children: [
        {
          path: '',
          name: 'myProfile',
          component: () => import('@/components/mypage/Profile.vue')
        },
        {
          path: 'account',
          name: 'myAccount',
          component: () => import('@/components/mypage/Account.vue')
        },
        {
          path: 'reviews',
          name: 'myReviews',
          component: () => import('@/components/mypage/Review.vue')
        },
        {
          path: 'comments',
          name: 'myComments',
          component: () => import('@/components/mypage/Comment.vue')
        },
        {
          path: 'likedReview',
          name: 'myLikedReview',
          component: () => import('@/components/mypage/LikedReview.vue')
        },
        {
          path: 'admin/user',
          name: 'adminUser',
          component: () => import('@/views/user/UserListView.vue')
        }
      ]
    }

    // {
    //   path: '/admin',
    //   name: 'admin',
    //   component: () => import('@/components/mypage/Notifications.vue')
    // }
  ]
})

export default router

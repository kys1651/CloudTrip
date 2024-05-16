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
      component: () => import('@/views/mypage/MyPageView.vue')
    },
    {
      path: '/account',
      name: 'account',
      component: () => import('@/views/mypage/MyAccountView.vue')
    },
    {
      path: '/myReview',
      name: 'myReview',
      component: () => import('@/views/mypage/MyReviewView.vue')
    },
    {
      path: '/myComment',
      name: 'myComment',
      component: () => import('@/views/mypage/MyCommentView.vue')
    },
    {
      path: '/likeReview',
      name: 'likeReview',
      component: () => import('@/views/mypage/MyLikeReviewView.vue')
    },
    {
      path: '/custom',
      name: 'custom',
      component: () => import('@/components/mypage/custom.vue'),
      children: [
        {
          path: '',
          name: 'customMyPage',
          component: () => import('@/components/mypage/MyPage.vue')
        },
        {
          path: 'account',
          name: 'customAccount',
          component: () => import('@/components/mypage/MyAccount.vue')
        },
        {
          path: 'reviews',
          name: 'customReviews',
          component: () => import('@/components/mypage/MyReview.vue')
        },
        {
          path: 'comments',
          name: 'customComments',
          component: () => import('@/components/mypage/MyComment.vue')
        },
        {
          path: 'likedReview',
          name: 'customLikedReview',
          component: () => import('@/components/mypage/MyLikeReview.vue')
        },
        {
          path: 'user/manage',
          name: 'userManage',
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

import { useAuthenticationStore } from '@/stores/authentication'
import HomeView from '@/views/HomeView.vue'
import { storeToRefs } from 'pinia'
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
      path: '/search',
      name: 'search',
      component: () => import('@/views/search/SearchView.vue')
    },
    {
      path: '/searchReview',
      name: 'search-review',
      component: () => import('@/views/search/tag/SearchReview.vue')
    },
    {
      path: '/searchSchedule',
      name: 'search-schedule',
      component: () => import('@/views/search/tag/SearchSchedule.vue')
    },
    {
      path: '/searchNotice',
      name: 'search-notice',
      component: () => import('@/views/search/tag/SearchNotice.vue')
    },
    {
      path: '/searchProfile',
      name: 'search-profile',
      component: () => import('@/views/search/tag/SearchProfile.vue')
    },
    {
      path: '/sign-in',
      name: 'sign-in',
      component: () => import('@/views/SignInView.vue')
    },
    {
      path: '/search/:searchId',
      name: 'search-detail',
      component: () => import('@/views/search/SearchView.vue')
    },

    {
      path: '/notice',
      name: 'notice',
      component: () => import('@/views/notice/NoticeListView.vue')
    },
    {
      path: '/review',
      name: 'review',
      component: () => import('@/views/reviews/ReviewView.vue')
    },
    {
      path: '/review/create',
      name: 'review-create',
      component: () => import('@/views/reviews/ReviewCreateView.vue'),
      beforeEnter: (to, from, next) => {
        if (signInCheck()) {
          next()
        } else {
          next({ name: 'sign-in' })
        }
      }
    },
    {
      path: '/review/update/:reviewId',
      name: 'review-update',
      component: () => import('@/views/reviews/ReviewUpdateView.vue'),
      beforeEnter: (to, from, next) => {
        if (signInCheck()) {
          next()
        } else {
          next({ name: 'sign-in' })
        }
      }
    },
    {
      path: '/review/:id',
      name: 'review-detail',
      component: () => import('@/views/reviews/ReviewDetailView.vue')
    },
    {
      path: '/redirect/oauth2',
      name: 'redirect-oauth',
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
      component: () => import('@/views/notice/NoticeUpdateView.vue'),
      beforeEnter: (to, from, next) => {
        if (adminCheck()) {
          next()
        } else {
          next({ name: 'sign-in' })
        }
      }
    },
    {
      path: '/notice/create/',
      name: 'notice-create',
      component: () => import('@/views/notice/NoticeCreateView.vue'),
      beforeEnter: (to, from, next) => {
        if (adminCheck()) {
          next()
        } else {
          next({ name: 'sign-in' })
        }
      }
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
        },
        {
          path: 'schedule',
          name: 'userSchedule',
          component: () => import('@/components/user/UserSchedule.vue')
        }
      ]
    },
    {
      path: '/schedule',
      name: 'schedule',
      component: () => import('@/views/schedule/ScheduleView.vue')
    },
    {
      path: '/schedule/:scheduleId',
      name: 'schedule-detail',
      component: () => import('@/views/schedule/ScheduleDetailView.vue')
    },
    {
      path: '/schedule/explore',
      name: 'schedule-explore',
      component: () => import('@/views/schedule/ScheduleExploreView.vue')
    },
    {
      path: '/setting',
      name: 'setting',
      component: () => import('@/views/SettingView.vue'),
      redirect: { name: 'myProfile' }, // 기본 경로로 리디렉션 추가
      beforeEnter: (to, from, next) => {
        if (signInCheck()) {
          next()
        } else {
          next({ name: 'sign-in' })
        }
      },
      children: [
        {
          path: '',
          name: 'myProfile',
          component: () => import('@/components/setting/ProfileSetting.vue')
        },
        {
          path: 'account',
          name: 'myAccount',
          component: () => import('@/components/setting/AccountSetting.vue')
        },
        {
          path: 'reviews',
          name: 'myReviews',
          component: () => import('@/components/setting/ReviewSetting.vue')
        },
        {
          path: 'comments',
          name: 'myComments',
          component: () => import('@/components/setting/CommentSetting.vue')
        },
        {
          path: 'likedReview',
          name: 'myLikedReview',
          component: () => import('@/components/setting/LikedReview.vue')
        },
        {
          path: 'mySchedule',
          name: 'mySchedule',
          component: () => import('@/components/setting/ScheduleSetting.vue')
        },
        {
          path: 'admin/user',
          name: 'adminUser',
          component: () => import('@/views/user/UserListView.vue'),
          beforeEnter: (to, from, next) => {
            if (adminCheck()) {
              next()
            } else {
              next({ name: 'sign-in' })
            }
          }
        },
        {
          path: 'admin/notice',
          name: 'adminNotice',
          component: () => import('@/views/notice/AdminNoticeView.vue'),
          beforeEnter: (to, from, next) => {
            if (adminCheck()) {
              next()
            } else {
              next({ name: 'sign-in' })
            }
          }
        },
        {
          path: 'admin/review',
          name: 'adminReview',
          component: () => import('@/views/notice/AdminReviewView.vue'),
          beforeEnter: (to, from, next) => {
            if (adminCheck()) {
              next()
            } else {
              next({ name: 'sign-in' })
            }
          }
        }
      ]
    }
  ]
})

const signInCheck = (): boolean => {
  const authenticationStore = useAuthenticationStore()
  const { isLogin, profile } = storeToRefs(authenticationStore)
  if (!isLogin.value || profile.value === undefined) {
    return false
  }
  return true
}

const adminCheck = (): boolean => {
  const authenticationStore = useAuthenticationStore()
  const { isLogin, profile } = storeToRefs(authenticationStore)
  if (!isLogin.value || profile.value === undefined || profile.value.roleType !== 'ADMIN') {
    return false
  }
  return true
}

export default router

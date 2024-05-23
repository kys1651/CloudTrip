import type { IScheduleSearch } from '@/types/schedule.type'

export interface ISearch {
  reviews: {
    reviewId: number
    user: {
      userId: number
      nickname: string
      profileImage: string
    }
    isLiked: boolean
    isFollowing: boolean
    likeCount: number
    images: string[]
    content: string
    tourId: number
    tourName: string
    address: string
    createdAt: number
    rating: number
  }[]
  notices: {
    noticeId: number
    title: string
    content: string
    createdAt: string
  }[]
  users: {
    userId: number
    email: string
    profileImage?: string
    nickname: string
  }[]
  schedules: IScheduleSearch[]
}

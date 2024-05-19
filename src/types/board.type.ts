export interface BoardListProps {
  title: string
}

export interface INotice {
  noticeId: number
  title: string
  content: string
  createdAt: Date
}

export interface IReview {
  reviewId: number
  user: {
    userId: number
    nickname: string
    profileImage: string
  }
  isLiked: boolean
  images: string[]
  content: string
  tourId: number
  tourName: string
  address: string
  createdAt: number
  rating: number
}

export interface ReviewForm {
  content: string
  tourId: number
}

export interface SumaryReview {
  reviewId: number
  content: string
  createdAt: string
  images: string
}

export interface ReviewCardProps {
  review: IReview
}

export interface IReviewComment {
  author: string
  content: string
  createdAt: string
}

export interface ReviewCommentForm {
  reviewId: number
  userId: number
  content: string
}

export interface ReviewCommentDetail {
  commentId: number
  userId: number
  nickname: string
  content: string
  createdAt: string
  profileImage: string
}

export interface SimpleCommentDetail {
  commentId: number
  reviewId: number
  createdAt: string
  content: string
  reviewContent: string
}

export interface ReviewForm {
  name: string
  tourId: number
  content: string
  images: File[]
  rating: number
}

export interface ReviewDetail {
  reviewId: number
  content: string
  tourName: string
  tourId: number
  createdAt: number
  updatedAt: number
  likeCount: number
  rating: number
  address: string
  isLiked: boolean
  images: string[]
  user: {
    userId: number
    nickname: string
    profileImage: string
  }
}

export interface IComment {
  commentId: number
  user: {
    userId: number
    nickname: string
    profileImage: string
  }
  content: string
  createdAt: string
}

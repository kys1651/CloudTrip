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
  reviewId: number;
  user: {
    userId: number;
    nickname: string;
    profileImage: string;
  };
  isLiked: boolean;
  images: string[];
  content: string;
  tourId: number;
  address: string;
  createdAt: number;
}

export interface ReviewDetail {
  reviewId: number
  content: string
  tourId: number
  createdAt: string
  updatedAt: string
  likeCount: number
  imgUrls: string[]
  userId: number
  nickname: string
  profileImage: string
}

export interface ReviewForm {
  content: string
  tourId: number
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

export interface ReviewForm {
  name: string
  tourId: number
  content: string
  images: File[]
}

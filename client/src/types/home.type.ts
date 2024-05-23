export interface IHome {
    numberData: {
        reviewCount: number
        noticeCount: number
        usersCount: number
        tourCount: number
        scheduleCount: number
    }
        topTours: {
        tourId: number
        name: string
        description: string
        cityName: string
        townName: string
        rating: number
        backgroundImage: string | null
    }[]
        topReviews: {
        reviewId: number
        content: string
        createdAt: string
        name: string
        image: string | null
        likeCount: number
    }[]
}
export interface MetricProps {
  count: number
  name: string
}

export interface TripSummaryProps {
  backgroundImage: string
  name: string
  description: string
  location: string
  rating: number
}

export interface PostingSummaryProps {
  backgroundImage: string
  name: string
  description: string
  createdAt: Date
  views: number
}

export interface TripCategory {
  contentId: number
  name: string
  korName: string
}

export interface SearchQuery {
  city: number
  query: string
  // page: number
  // limit: number
}

export interface SearchTrip {
  tourId: number;
  contentType: string;
  name: string;
  address: string;
  backgroundImage?: string;
  description: string;
  latitude: number;
  longitude: number;
}

export interface ICity {
  cityId: number
  name: string
  latitude: number
  longitude: number
}

export enum TripStep {
  CALENDAR = 1,
  PLAN = 2,
  REVIEW = 3,
}
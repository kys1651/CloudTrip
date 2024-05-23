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
  contentTypeId: number;
  name: string;
  address: string;
  backgroundImage?: string;
  description: string;
  latitude: number;
  longitude: number;
  scheduleCount: number;
  reviewCount: number;
  rating: number;
}

export interface ICity {
  cityId: number
  name: string
  latitude: number
  longitude: number
}

export interface ICity {
  cityCode: number
  name: string
}

export interface ITown {
  townCode: number
  name: string
}

export enum TripStep {
  PLAN = 1,
  STAY = 2,
  VEHICLE = 3,
}

export interface SearchTripWithDistance extends SearchTrip {
  distance: number;
}

export interface ISimpleTour {
  tourId: number
  name: string
  backgroundImage: string
  latitude: number
  longitude: number
  address: string
}
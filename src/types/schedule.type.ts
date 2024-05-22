import type { IDirection } from "@/api/trip";
import type { ITripPlace } from "@/stores/trip-plan";
import type { ISimpleTour } from "./trip.type";

export interface ScheduleForm {
  name: string;
  multi: boolean;
  private: boolean;
  start: Date;
  end: Date;
  day: number;
  city: number;
}

export interface IScheduleSearch {
  scheduleId: number
  name: string
  user: {
    userId: number
    nickname: string
    profileImage: string
  }
  count: number
  cityCode: number
  cityName: string
  thumbnailImage?: string
  startDate: string
  endDate: string
  private: boolean
  multi: boolean
  finished: boolean
  usernames: string[]
  day: number
}

export interface ISchedule {
  scheduleId: number
  name: string
  user: {
    userId: number
    nickname: string
    profileImage: string
  }
  cityCode: number
  cityName: string
  thumbnailImage: string
  startDate: string
  endDate: string
  private: boolean
  multi: boolean
  finished: boolean
  publicKey: string
  day: number
  invitedUsers: InviteUser[]
}

export interface InviteUser {
  userId: number
  nickname: string
  profileImage: string
  username: string
}

export interface ScheduleTripCreate {
  trips: IScheduleTrip[]
  vehicles: IScheduleVechile[]
}

export interface IScheduleTrip {
  tourId: number
  day: number
  order: number
  room: boolean
}

export interface IScheduleVechile {
  vehicleId: number
  type: string
  fromTourId: number
  toTourId: number
  day: number
  order: number
}

export enum ChatType {
  ENTER = "ENTER",
  TALK = "TALK",
}

export interface IChat {
  type: ChatType
  userId: number
  scheduleId: number
  message: string
  createdAt: number
}

export interface IVehicle extends IScheduleVechile {
  name: string
}

export interface ITripAndVehicle {
  trips: ITripPlace[][]
  vehicles: IVehicle[]
  scheduleId: number
  day: number
  userId: number
}

export interface ScheduleTripResponse {
  tour: ISimpleTour
  day: number
  order: number
  room: boolean
}

export interface ScheduleVehicleResponse {
  vehicle: IDirection
  type: string
  fromTourId: number
  toTourId: number
  day: number
  order: number
}

export interface PathResponse {
  trips: ScheduleTripResponse[]
  vehicles: ScheduleVehicleResponse[]
}

export interface InviteForm {
  username: string;
  name: string;
  owner: string;
}

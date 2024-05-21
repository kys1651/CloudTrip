import type { ITripPlace } from "@/stores/trip-plan";

export interface ScheduleForm {
  name: string;
  multi: boolean;
  private: boolean;
  start: Date;
  end: Date;
  day: number;
  city: number;
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
  usernames: string[]
  private: boolean
  multi: boolean
  day: number
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
  vehicleId?: number
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
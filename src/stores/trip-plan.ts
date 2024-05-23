import { type IDirection, type MobilityResponse } from '@/api/trip'
import { calculateDistance, calculateSum, type Point } from '@/lib/distance'
import type { IVehicle } from '@/types/schedule.type'
import type { SearchTrip } from '@/types/trip.type'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const HOVER_COLORS = [
  'bg-blue-300 hover:bg-blue-200 border-blue-600',
  'bg-green-300 hover:bg-green-200 border-green-600',
  'bg-yellow-300 hover:bg-yellow-200 border-yellow-600',
  'bg-red-300 hover:bg-red-200 border-red-600',
  'bg-indigo-300 hover:bg-indigo-200 border-indigo-600',
  'bg-purple-300 hover:bg-purple-200  border-purple-600',
  'bg-pink-300 hover:bg-pink-200 border-pink-600'
]

export const COLORS = [
  'bg-blue-300',
  'bg-green-300',
  'bg-yellow-300',
  'bg-red-300',
  'bg-indigo-300',
  'bg-purple-300',
  'bg-pink-300'
]

export const BORDER_COLORS = [
  'border-blue-300',
  'border-green-300',
  'border-yellow-300',
  'border-red-300',
  'border-indigo-300',
  'border-purple-300',
  'border-pink-300'
]

export const TEXT_COLORS = [
  'text-blue-300',
  'text-green-300',
  'text-yellow-300',
  'text-red-300',
  'text-indigo-300',
  'text-purple-300',
  'text-pink-300'
]

export interface ITripPlace {
  tourId: number
  latitude: number
  longitude: number
  name: string
  address: string
  backgroundImage?: string
  room: boolean
}

interface DirectionDictionary {
  [key: string]: MobilityResponse
}

export interface ITripVehicle {
  fromTourId: number
  toTourId?: number
  name: string
  vehicleType: string
  vehicleId?: number
}

interface TripVehicleDictionary {
  [key: string]: IVehicle
}

export const useTripPlanStore = defineStore('trip-plan', () => {
  const trips = ref<ITripPlace[][]>([])
  const tripVehicles = ref<IVehicle[]>([])

  const tripVehicleDictionary = computed<TripVehicleDictionary>(() => {
    const dictionary: TripVehicleDictionary = {}
    tripVehicles.value.forEach((vehicle) => {
      dictionary[dictionaryKey(vehicle.day, vehicle.order)] = vehicle
    })
    return dictionary
  })

  const tripVehicleList = computed<IVehicle[][]>(() => {
      return dayTrips.map((_, index) => tripVehicleDictionary.value[dictionaryKey(day, index)])
    }))
    return trips.value.map((dayTrips, day) => {
      return dayTrips.map((_, index) => tripVehicleDictionary.value[dictionaryKey(day, index)])
    })
  })

  const dictionaryKey = (day: number, order: number) => {
    return day + ":" + order;
  }

  const updateVehicles = () => {
    const newTripVehicles: IVehicle[] = []
    trips.value.forEach((dayTrips, day) => {
      [...dayTrips].sort((prev, next) => Number(prev.room) - Number(next.room)).forEach((trip, index) => {
        const findVehicle = tripVehicles.value.find((find) => find.day === day && find.order === index && find.fromTourId === trip.tourId)
        const newVehicle: IVehicle = {
          fromTourId: trip.tourId,
          name: trip.name,
          toTourId: 0,
          vehicleId: 0,
          type: 'none',
          day: day,
          order: index
        }

        if (index !== dayTrips.length - 1) {
          newVehicle.toTourId = dayTrips[index + 1].tourId
        }

        if (findVehicle) {
          newVehicle.vehicleId = findVehicle.vehicleId
          newVehicle.type = findVehicle.type
        }

        newTripVehicles.push(newVehicle)
      })
    })
    tripVehicles.value = newTripVehicles
  }

  const initTripAndVehicle = (initTrips: ITripPlace[][], initVehicles:  IVehicle[]) => {
    trips.value = [...initTrips]
    tripVehicles.value = [...initVehicles]
  }

  const setTripVehicles = (day: number, index: number, vehicleType: string) => {
    const tripVehicle = tripVehicleDictionary.value[dictionaryKey(day, index)]
    if (!tripVehicle) return;
    const vehicleIndex = tripVehicles.value.indexOf(tripVehicle)
    const newVehicle = {...tripVehicle}
    newVehicle.type = vehicleType
    const fromTourId = newVehicle.fromTourId
    const toTourId = newVehicle.toTourId
    const vehicle = findVehicle(fromTourId, toTourId, vehicleType)
    if (vehicle) {
      newVehicle.vehicleId = vehicle.vehicleId
    }
    tripVehicles.value[vehicleIndex] = newVehicle
  }

  const findVehicle = (fromTourId: number, toTourId: number | undefined, vehicleType: string): IDirection | undefined => {
    const vehicle = vehicles.value[`${fromTourId}-${toTourId}`]
    if (!vehicle) return
    switch (vehicleType) {
      case 'walk':
        return vehicle.walk
      case 'bike':
        return vehicle.bike
      case 'bus':
        return vehicle.bus
      case 'metro':
        return vehicle.metro
      case 'car':
        return vehicle.car
    }
  }

  const setDefaultTripVehicles = (vehicleType: string) => {
    tripVehicles.value = tripVehicles.value.map((tripVehicle) => {
      const newVehicle = {...tripVehicle}
      newVehicle.type = vehicleType;
      const fromTourId = newVehicle.fromTourId
      const toTourId = newVehicle.toTourId
      const vehicle = findVehicle(fromTourId, toTourId, vehicleType)
      if (vehicle) {
        newVehicle.vehicleId = vehicle.vehicleId
      }
      return newVehicle
    })
  }

  const distances = computed(() => {
    return trips.value.map((dayTrips) => {
      return {
        count: dayTrips.length,
        distance: calculateSum(
          dayTrips.map((trip) => ({
            latitude: trip.latitude,
            longitude: trip.longitude,
            tourId: trip.tourId
          }))
        )
      }
    })
  })

  const tripsExceptRooms = computed<ITripPlace[][]>(() => {
    return trips.value.map((dayTrips) => {
      return dayTrips.filter((trip) => !trip.room)
    })
  })

  const rooms = computed<(ITripPlace | undefined)[]>(() => {
    return trips.value.map((dayTrips) => {
      return dayTrips.find((trip) => trip.room)
    })
  })

  const removeRoom = (day: number) => {
    trips.value[day] = trips.value[day].filter((trip) => !trip.room)
  }

  const setTripDay = (day: number) => {
    if (trips.value.length !== day) {
      trips.value = Array.from({ length: day }, () => [])
    }
  }

  const addTrip = (day: number, trip: SearchTrip) => {
    trips.value[day] = [
      ...trips.value[day],
      {
        tourId: trip.tourId,
        latitude: trip.latitude,
        longitude: trip.longitude,
        name: trip.name,
        address: trip.address,
        backgroundImage: trip.backgroundImage,
        room: false
      }
    ]
  }

  const addRoom = (day: number, trip: SearchTrip) => {
    trips.value[day] = [
      ...trips.value[day],
      {
        tourId: trip.tourId,
        latitude: trip.latitude,
        longitude: trip.longitude,
        name: trip.name,
        address: trip.address,
        backgroundImage: trip.backgroundImage,
        room: true
      }
    ]
  }

  const exists = (tripId: number): boolean => {
    return trips.value.flat().some((trip) => trip.tourId === tripId)
  }

  const removeTrip = (day: number, tourId: number) => {
    trips.value[day] = trips.value[day].filter((trip) => trip.tourId !== tourId)
  }

  const vehicles = ref<DirectionDictionary>({})

  const coordinates = computed(() => {
    return tripsExceptRooms.value
      .map((dayTrips, index) => {
        return dayTrips.map((trip) => {
          return {
            day: index + 1,
            latitude: trip.latitude,
            longitude: trip.longitude,
            color: COLORS[index % COLORS.length]
          }
        })
      })
      .reduce((array, trips) => [...array, ...trips], [])
  })

  const centercoordinate = computed(() => {
    if (coordinates.value.length === 0) return
    const bounds = new window.kakao.maps.LatLngBounds()
    coordinates.value.forEach((coordinate) => {
      bounds.extend(new window.kakao.maps.LatLng(coordinate.latitude, coordinate.longitude))
    })
    return bounds
  })

  const addVehicles = (mobility: MobilityResponse, startTourId: number, endTourId: number) => {
    const key = `${startTourId}-${endTourId}`
    vehicles.value[key] = mobility
  }


  const calculateOptimalRoute = () => {
    const newTrips = trips.value.flat()
    const tripDictionary = newTrips.reduce(
      (acc, trip) => {
        acc[trip.tourId] = trip
        return acc
      },
      {} as Record<number, ITripPlace>
    )
    const numRoutes = trips.value.length
    const routes: Point[][] = Array(numRoutes).fill([])

    for (let i = 0; i < numRoutes; i++) {
      if (newTrips.length === 0) break

      const route: Point[] = [newTrips[0]]
      newTrips.splice(0, 1)

      while (route.length < newTrips.length / numRoutes) {
        let closestTripIndex = 0
        let closestTripDistance = calculateDistance(route[route.length - 1], newTrips[0])

        for (let i = 1; i < newTrips.length; i++) {
          const distance = calculateDistance(route[route.length - 1], newTrips[i])
          if (distance < closestTripDistance) {
            closestTripDistance = distance
            closestTripIndex = i
          }
        }

        route.push(newTrips[closestTripIndex])
        newTrips.splice(closestTripIndex, 1)
      }

      routes[i] = route
    }
    trips.value = routes.map((route) => route.map((point) => tripDictionary[point.tourId]))
  }

  return {
    trips,
    coordinates,
    distances,
    rooms,
    centercoordinate,
    tripVehicles,
    tripsExceptRooms,
    tripVehicleList,
    initTripAndVehicle,
    setTripDay,
    addVehicles,
    addTrip,
    addRoom,
    updateVehicles,
    findVehicle,
    removeTrip,
    exists,
    removeRoom,
    setTripVehicles,
    setDefaultTripVehicles,
    calculateOptimalRoute,
  }
})

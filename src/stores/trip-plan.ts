import { calculateDistance, calculateSum, type Point } from '@/lib/distance'
import type { SearchTrip } from '@/types/trip.type'
import { CalendarDate, getLocalTimeZone, today, type DateValue } from '@internationalized/date'
import { defineStore } from 'pinia'
import type { DateRange } from 'radix-vue'
import { computed, ref } from 'vue'

export const COLORS = [
  'bg-blue-300',
  'bg-green-300',
  'bg-yellow-300',
  'bg-red-300',
  'bg-indigo-300',
  'bg-purple-300',
  'bg-pink-300'
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

export const useTripPlanStore = defineStore('trip-plan', () => {
  const pickedTrips = ref<SearchTrip[][]>([[], [], []])
  const range = ref<DateRange>({
    start: undefined,
    end: undefined
  })
  const limit = ref(today(getLocalTimeZone()).add({ years: 1 }))

  const isRangeSetted = computed<boolean>(
    () => range.value.start !== undefined && range.value.end !== undefined
  )

  const tourIds = computed(() => pickedTrips.value.flat().map((trip) => trip.tourId))

  const coordinates = computed(() => {
    return pickedTrips.value
      .map((trips, index) => {
        return trips.map((trip) => {
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

  const dailyTrip = computed<
    {
      count: number
      distance: number
    }[]
  >(() => {
    return pickedTrips.value.map((trips) => ({
      count: trips.length,
      distance: calculateSum(
        trips.map((trip) => ({
          latitude: trip.latitude,
          longitude: trip.longitude,
          tourId: trip.tourId
        }))
      )
    }))
  })

  // 여행 일정 설정 부분
  const setRange = (dateRange: DateRange) => {
    if (!dateRange.start || !dateRange.end) return
    range.value = dateRange
    const day = dateRange.end!!.compare(dateRange.start!!) + 1
    pickedTrips.value = Array.from({ length: day }, () => [])
  }

  const changeRange = (date: DateValue | undefined) => {
    if (!date) return
    const { year, month, day } = date
    const newDate = new CalendarDate(year, month, day)
    range.value.start = newDate
    limit.value = newDate.add({ days: 4 })
  }

  const disabledDate = ({ year, month, day }: { year: number; month: number; day: number }) => {
    const startDate = range.value.start ?? today(getLocalTimeZone())
    const date = new CalendarDate(year, month, day)
    return !(startDate <= date && date <= limit.value)
  }

  const resetRange = () => {
    range.value = {
      start: undefined,
      end: undefined
    }
    limit.value = today(getLocalTimeZone()).add({ years: 1 })
  }

  const exists = (tripId: number): boolean => {
    return tourIds.value.includes(tripId)
  }

  const addTrip = (day: number, trip: SearchTrip) => {
    const prev = pickedTrips.value[day - 1]
    pickedTrips.value[day - 1] = [...prev, trip]
  }

  const removeTrip = (day: number, tourId: number) => {
    pickedTrips.value[day] = pickedTrips.value[day].filter((trip) => trip.tourId !== tourId)
  }

  const calculateOptimalRoute = () => {
    const trips = pickedTrips.value.flat()
    const tripDictionary = trips.reduce((acc, trip) => {
        acc[trip.tourId] = trip
        return acc
    }, {} as Record<number, SearchTrip>)
    const numRoutes = pickedTrips.value.length
    const routes: Point[][] = Array(numRoutes).fill([])

    for (let i = 0; i < numRoutes; i++) {
      if (trips.length === 0) break

      const route: Point[] = [trips[0]]
      trips.splice(0, 1)

      while (route.length < trips.length / numRoutes) {
        let closestTripIndex = 0
        let closestTripDistance = calculateDistance(route[route.length - 1], trips[0])

        for (let i = 1; i < trips.length; i++) {
          const distance = calculateDistance(route[route.length - 1], trips[i])
          if (distance < closestTripDistance) {
            closestTripDistance = distance
            closestTripIndex = i
          }
        }

        route.push(trips[closestTripIndex])
        trips.splice(closestTripIndex, 1)
      }

      routes[i] = route
    }
    const newTrips = routes.map((route) => route.map((point) => tripDictionary[point.tourId]))
    pickedTrips.value = newTrips
  }

  return {
    pickedTrips,
    coordinates,
    dailyTrip,
    isRangeSetted,
    range,
    centercoordinate,
    setRange,
    addTrip,
    removeTrip,
    exists,
    changeRange,
    resetRange,
    disabledDate,
    calculateOptimalRoute
  }
})

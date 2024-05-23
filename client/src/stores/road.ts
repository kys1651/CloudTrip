import axios from 'axios'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { KakaoMapLatLngItem } from 'vue3-kakao-maps'

export const STORKE_COLORS = [
  // 각 색보다 한 3배 더 진하게 표현해줘
  { main: '#92C5FD', highlight: '#4F8CF7', highlight2: '#2D74DA', highlight3: '#1A66C6' },
  { main: '#87EEAB', highlight: '#2ED573', highlight2: '#1AAE6F', highlight3: '#0E923A' },
  { main: '#FDE046', highlight: '#FFC048', highlight2: '#FFA801', highlight3: '#FF922B' },
  { main: '#FCA5A5', highlight: '#FF6B6B', highlight2: '#FF3B3B', highlight3: '#FF1E1E' },
  { main: '#A5B4FB', highlight: '#818CF8', highlight2: '#5C7CFA', highlight3: '#3B69F2' }
]

interface Position {
  lat: number
  lng: number
}

interface KakaoRoadResponse {
  routes: {
    sections: {
      distance: number
      duration: number
      guides: {
        x: number
        y: number
      }[]
    }[]
  }[]
}

interface KakaoRoadRequest {
  origin: {
    x: number
    y: number
  }
  destination: {
    x: number
    y: number
  }
  waypoints: {
    name: string
    x: number
    y: number
  }[]
  priority: string
  car_fuel: string
  car_hipass: false
  alternatives: false
  road_details: false
}

export interface LatLng {
  latitude: number
  longitude: number
}

export const useRoadStore = defineStore('road', () => {
  const roads = ref<Position[][][]>([])
  const isLoading = ref<boolean>(false)

  const initRoads = async (scheduleId: number, positions: LatLng[][]) => {
    if (sessionStorage.getItem(`roads:${scheduleId}`)) {
      roads.value = JSON.parse(sessionStorage.getItem(`roads:${scheduleId}`)!)
      return
    }
    isLoading.value = true

    const requests: KakaoRoadRequest[] = positions.map((position) => {
      return {
        origin: {
          x: position[0].longitude,
          y: position[0].latitude
        },
        destination: {
          x: position[position.length - 1].longitude,
          y: position[position.length - 1].latitude
        },
        waypoints: position.slice(1, -1).map((waypoint, idx) => {
          return {
            name: 'name' + idx,
            x: waypoint.longitude,
            y: waypoint.latitude
          }
        }),
        priority: 'RECOMMEND',
        car_fuel: 'GASOLINE',
        car_hipass: false,
        alternatives: false,
        road_details: false
      }
    })

    roads.value = await Promise.all(
      requests.map(async (request) => {
        const { data: response } = await axios.post<KakaoRoadResponse>(
          'https://apis-navi.kakaomobility.com/v1/waypoints/directions',
          request,
          {
            headers: {
              'Content-Type': 'application/json',
              Authorization: 'KakaoAK 07c31433dd7f0a38f33b16dd2ac90a40'
            }
          }
        )
        return response.routes[0].sections.map((section) => {
          return section.guides.map((guide) => {
            return {
              lat: guide.y,
              lng: guide.x
            } as KakaoMapLatLngItem
          })
        })
      })
    )

    isLoading.value = false
    sessionStorage.setItem(`roads:${scheduleId}`, JSON.stringify(roads.value))
  }

  return { roads, isLoading, initRoads }
})

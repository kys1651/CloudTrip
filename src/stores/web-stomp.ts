import { userDataByUserIdRequest } from '@/api/user'
import { ChatType, type IChat, type ITripAndVehicle } from '@/types/schedule.type'
import { Client } from '@stomp/stompjs'
import { defineStore, storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useAuthenticationStore } from './authentication'
import { useTripPlanStore } from './trip-plan'

interface ProfileDictionary {
  [key: number]: {
    userId: number,
    nickname: string
    profileImage?: string
  }
}

export const useScheduleSocket = defineStore('schedule-socket', () => {
  const client = ref<Client | null>(null)
  const isOpen = ref<boolean>(false);
  const updateLock = ref<{
    isLock: boolean
  }>({
    isLock: false
  });
  const updatedUser = ref<number>(0)
  const unseen = ref<number>(0);
  const {trips, tripVehicles} = storeToRefs(useTripPlanStore())
  const profileDictionary = ref<ProfileDictionary>({
    '0' : {
      userId: 0,
      nickname: '익명 유저',
      profileImage: '/anonymous.png'
    }
  })
  const { initTripAndVehicle } = useTripPlanStore()
  const { authUserId } = storeToRefs(useAuthenticationStore())
  const chats = ref<IChat[]>([])

  const toggleOpen = () => {
    isOpen.value = !isOpen.value
    if (!isOpen.value) {
      unseen.value = chats.value.length;
    }
  }

  const lock = () => {
    updateLock.value.isLock = true
  }

  const unLock = () => {
    updateLock.value.isLock = false
  }

  const fetchUser = async (userId?: number) => {
    if (userId === 0) return { userId: 0, nickname: '익명 유저', }
    if (!userId) return
    if (!Object.keys(profileDictionary.value).includes(userId.toString()))  {
      const profile = await userDataByUserIdRequest(userId)
      profileDictionary.value[userId] = {
        userId: profile.id,
        nickname: profile.nickname!!,
        profileImage: profile.profileImage
      }
    }
  }

  const connect = (scheduleId: number, day: number) => {
    client.value = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      onConnect: () => {
        client.value!!.subscribe(`/sub/schedule/${scheduleId}/chat`, (message) => {
          const chat = JSON.parse(message.body) as IChat
          fetchUser(chat.userId)
          chats.value.push(chat)
        })
        client.value!!.subscribe(`/sub/schedule/${scheduleId}/update`, (message) => {
          const tripAndVehicle = JSON.parse(message.body) as ITripAndVehicle
          updatedUser.value = tripAndVehicle.userId
          if (authUserId.value === 0 || tripAndVehicle.userId !== authUserId.value) {
            initTripAndVehicle(tripAndVehicle.trips, tripAndVehicle.vehicles)
          }
        })

        client.value!!.publish({
          destination: `/pub/schedule/${scheduleId}/chat`,
          body: JSON.stringify({
            type: 'ENTER',
            userId: authUserId.value,
            scheduleId,
          })
        })
      }
    })
    client.value.activate()
  }

  function sendUpdate(scheduleId: number, day: number) {
    if (!(client.value && client.value.connected)) return
    client.value.publish({
      destination: `/pub/schedule/${scheduleId}/update`,
      body: JSON.stringify({
        scheduleId,
        day,
        userId: authUserId.value,
        trips: trips.value,
        vehicles: tripVehicles.value
      })
    })
  }

  const sendChat = (scheduleId: number, message: string) => {
    if (!(client.value && client.value.connected)) return
    client.value.publish({
      destination: `/pub/schedule/${scheduleId}/chat`,
      body: JSON.stringify({
        type: ChatType.TALK,
        userId: authUserId.value,
        scheduleId,
        message,
        createdAt: new Date().getTime()
      })
    })
  }

  return {
    client,
    chats,
    isOpen,
    unseen,
    profileDictionary,
    updateLock,
    updatedUser,
    lock,
    unLock,
    connect,
    toggleOpen,
    fetchUser,
    sendUpdate,
    sendChat
  }
})

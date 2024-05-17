<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
</script>

<template>
  <div class="flex flex-col h-[500px]">
    <header class="bg-gray-900 text-white p-2 flex items-center justify-between">
      <div>
        <h2 class="text-sm font-semibold">Project Brainstorm</h2>
        <p class="text-xs text-gray-400">5 participants</p>
      </div>
      <div class="flex items-center space-x-2" />
    </header>
    <div class="flex-1 overflow-auto p-2">
      <div class="space-y-4">
        <div class="flex items-start space-x-2">
          <Avatar class="h-5 w-5">
            <AvatarImage alt="Avatar" src="/placeholder.svg?height=20&width=20" />
            <AvatarFallback>JD</AvatarFallback>
          </Avatar>
          <div class="bg-gray-100 dark:bg-gray-800 rounded-lg p-2 max-w-[70%]">
            <p class="text-xs leading-tight">
              Hey everyone, let's brainstorm some ideas for the new project.
            </p>
            <p class="text-xs text-gray-500 dark:text-gray-400 mt-2">10:15 AM</p>
          </div>
        </div>
        <div class="flex items-start space-x-2 justify-end">
          <div class="bg-blue-500 text-white rounded-lg p-2 max-w-[70%]">
            <p class="text-xs leading-tight">
              I think we should focus on the user experience and make it as intuitive as
              possible.
            </p>
            <p class="text-xs text-gray-200 mt-2">10:16 AM</p>
          </div>
          <Avatar class="h-5 w-5">
            <AvatarImage alt="Avatar" src="/placeholder.svg?height=20&width=20" />
            <AvatarFallback>OL</AvatarFallback>
          </Avatar>
        </div>
        <div class="flex items-start space-x-2">
          <Avatar class="h-5 w-5">
            <AvatarImage alt="Avatar" src="/placeholder.svg?height=20&width=20" />
            <AvatarFallback>MK</AvatarFallback>
          </Avatar>
          <div class="bg-gray-100 dark:bg-gray-800 rounded-lg p-2 max-w-[70%]">
            <p class="text-xs leading-tight">
              Good idea! We should also consider the latest design trends and
              technologies.
            </p>
            <p class="text-xs text-gray-500 dark:text-gray-400 mt-2">10:17 AM</p>
          </div>
        </div>
        <div class="flex items-start space-x-2 justify-end">
          <div class="bg-blue-500 text-white rounded-lg p-2 max-w-[70%]">
            <p class="text-xs leading-tight">
              Absolutely, let's make sure we stay on top of the latest developments.
            </p>
            <p class="text-xs text-gray-200 mt-2">10:18 AM</p>
          </div>
          <Avatar class="h-5 w-5">
            <AvatarImage alt="Avatar" src="/placeholder.svg?height=20&width=20" />
            <AvatarFallback>JS</AvatarFallback>
          </Avatar>
        </div>
      </div>
    </div>
    <div class="bg-gray-100 dark:bg-gray-800 p-2 flex items-center">
      <Input class="flex-1 mr-2 text-xs" placeholder="Type your message..." type="text" />
      <Button class="text-xs">Send</Button>
    </div>
  </div>
</template>

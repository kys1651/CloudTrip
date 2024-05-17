<script setup lang="ts">
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'

import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import Quill from 'quill'
import 'quill/dist/quill.snow.css'
import { Files } from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()

const notice = ref({
  title: '',
  content: ''
})

let quill: Quill

onMounted(() => {
  quill = new Quill('#editor', {
    theme: 'snow',
    placeholder: '내용을 입력해주세요.',
    modules: {
      toolbar: [
        [{ header: [1, 2, false] }],
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote'],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ color: [] }, { background: [] }],
        ['image', 'link']
      ]
    }
  })

  quill.on('text-change', () => {
    notice.value.content = quill.root.innerHTML
  })

  quill.getModule('toolbar').addHandler('image', () => {
    getLocalImage()
  })
})

const images = ref<File[]>([])
const imageSrcs = ref<string[]>([])

const getLocalImage = () => {
  const fileInput = document.createElement('input')
  fileInput.setAttribute('type', 'file')
  fileInput.setAttribute('accept', 'image/*')

  fileInput.click()

  fileInput.onchange = () => {
    const files = fileInput.files
    if (files && files.length > 0) {
      for (let i = 0; i < files.length; i++) {
        const file = files[i]
        images.value = [...images.value, file]
        const reader = new FileReader()

        reader.onload = (e) => {
          const base64ImageSrc = e.target!.result as string
          imageSrcs.value = [...imageSrcs.value, base64ImageSrc]
          const range = quill.getSelection()
          quill.insertEmbed(range!.index, 'image', base64ImageSrc)
        }

        reader.readAsDataURL(file)
      }
    }
  }
}

const goHome = () => {
  router.push({ name: 'notice' })
}

const createAddr = `http://localhost:8080/api/v1/notice/create`
const createNotice = () => {
  notice.value.content = quill.root.innerHTML
  const key = 'image-replace-key'
  imageSrcs.value.forEach((src, index) => {
    notice.value.content = notice.value.content.replace(src, `${key}-${index + 1}`)
  })
  console.log(notice.value.content)

  const form = new FormData()
  images.value.forEach((image) => {
    form.append('images', image)
  })
  form.append('title', notice.value.title)
  form.append('content', notice.value.content)

  axios
    .post(createAddr, form, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    .then((response) => {
      console.log('등록 성공', response)

      router.push({ name: 'notice' })
    })
    .catch((error) => {
      console.log('등록 실패', error)
    })
}
</script>

<template>
  <div class="flex items-center justify-center h-screen">
    <Card class="w-[1200px] h-[800px]">
      <CardHeader>
        <CardTitle>공지사항 등록</CardTitle>
        <CardDescription>글 작성자 : Admin</CardDescription>
      </CardHeader>
      <CardContent class="overflow-y-auto">
        <form>
          <div class="grid items-center w-full gap-4">
            <div class="flex flex-col space-y-1.5 mb-3">
              <Label for="name">제목</Label>
              <Input
                class="h-[70px] p-2"
                id="name"
                placeholder="제목을 입력해주세요."
                v-model="notice.title"
              />
            </div>
          </div>

          <Label for="name">내용</Label>
          <div id="editor-container" class="h-[400px] mb-5 mt-2">
            <div id="editor"></div>
          </div>
        </form>
      </CardContent>
      <div class="flex flex-col space-y-1.5 mt-10 px-10">
        <Label for="framework">#공지사항</Label>
      </div>
      <CardFooter class="flex justify-between px-6 pb-6 mt-10">
        <Button variant="outline" @click="goHome"> 목록으로 </Button>
        <Button @click="createNotice">글 작성하기</Button>
      </CardFooter>
    </Card>
  </div>
</template>

<style scoped></style>

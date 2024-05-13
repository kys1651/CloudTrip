import './assets/index.css';

import { VueQueryPlugin } from '@tanstack/vue-query';
import { createPinia } from 'pinia';
import { createApp } from 'vue';
import { useKakao } from 'vue3-kakao-maps/@utils';
import {QuillEditor} from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';

import App from '@/App.vue';
import router from './router';

const app = createApp(App)

useKakao(import.meta.env.VITE_APP_KAKAO_MAP_API_KEY);

app.use(createPinia())
app.use(router)
app.use(VueQueryPlugin)


app.mount('#app')
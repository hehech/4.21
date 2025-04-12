import './assets/main.scss'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import Particles from 'particles.vue3'
import { createPinia } from 'pinia'
import App from './App.vue'
import { createPersistedState } from 'pinia-persistedstate-plugin'
import router from '@/router'
import zhCn from 'element-plus/es/locale/lang/zh-cn'


const app = createApp(App);
const pinia = createPinia();
const persist = createPersistedState();
pinia.use(persist);
app.use(router);
app.use(Particles);
app.use(pinia);
app.use(ElementPlus, {
    locale: zhCn
  })
app.use(ElementPlus);
app.mount('#app');



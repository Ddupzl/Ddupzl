import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/router";
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'  // 导入icon图标
import 'element-plus/theme-chalk/dark/css-vars.css'
import axios from "axios";  //登陆成功或失败提示样式
import locale from 'element-plus/es/locale/lang/zh-cn'


// createApp(App).mount('#app')
// app是vue的实例对象,App是根组件。在一个vue项目中，有且只有一个vue的实例对象
axios.defaults.baseURL='http://localhost:8888'


const app=createApp(App)
//icon图标全局注册
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(router)
app.use(ElementPlus,{locale})
app.mount('#app')

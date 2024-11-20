<template>

      <div class="a">
          <div class="img">
              <img style="width: 71vw;" :src="url" alt="gg">
          </div>
       <div class="a1">
           <div style="color: #666;font-size: 26px;font-weight: 600;line-height: 40px;">
                {{name}}
           </div>
           <div>
               <el-button @click="bm(a)" style="color: white;font-size: 17px;
           font-weight: 600;background-color:#409EFF;width: 150px;height: 48px;float: right;">
                   点击报名</el-button>
           </div>
           <div class="zt">
                   主办单位: &nbsp;
               <span style="font-size: 24px;color: rgb(60,150,255)">{{sponsor}}</span>
           </div>
           <div class="zt">
               竞赛介绍: &nbsp;
           </div>
           <span style="font-size: 1.5rem">{{desc}}</span>
           <div class="zt">
             竞赛日程: &nbsp;<span style="color: rgb(60,150,255)">{{compete}}</span>
           </div>
           <div class="zt">
               报名时间: &nbsp;<span style="color: rgb(60,150,255)">{{start}}~{{end}}</span>
           </div>
           <div class="zt">
               竞赛地点: &nbsp;<span style="color: rgb(60,150,255)">{{loc}}</span>
           </div>
           <h3>本竞赛赛事信息(报名时间，比赛时间，竞赛描述,pdf下载)</h3>
       </div>
      </div>

</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import { useRoute } from 'vue-router';
import axios from "axios";
import router from "../../../router/router.js"

const url=ref()
const name=ref()
const sponsor=ref()
const compete=ref()
const start=ref()
const end=ref();
const loc=ref();
const desc=ref();
const route = useRoute();
const a=ref(route.query.cid)
const bm=(id)=>{
    change('/bmPage') //跳转页面不显示下划线
    router.push('/bmPage?cid='+id)
}
//父传子的
const props = defineProps({change: Function});
const change=(z)=>{
    props.change(z)
}
const getDetails=()=>{
    axios.get('/info/getDetails?id='+a.value)
        .then((response)=>{
           url.value="http://localhost:8888/info/file/download/"+response.data.url
            name.value=response.data.competitionName
            sponsor.value=response.data.sponsor
            compete.value=response.data.competitionTime
            start.value=response.data.registrationTimeStart
           end.value=response.data.registrationTimeEnd
            loc.value=response.data.location
            desc.value=response.data.description
        })
}
onMounted(()=>{
    getDetails()
})
</script>

<style scoped>
.zt{
    font-weight: 600;
    line-height: 60px;
    font-size: 2rem;
    color:#666
}
.a{
    position: absolute;
    top: 90px;
    left: 0;
    width: 100%;
    box-sizing: border-box;
}
.img{
    width: 72%;
    margin: 0 auto;/*让盒子居中*/
    box-sizing: border-box;
    /*border: 1px skyblue solid;*/
}
.a1{
 width: 72%;
 margin: 0 auto;/*让盒子居中*/
 box-sizing: border-box;
}
</style>

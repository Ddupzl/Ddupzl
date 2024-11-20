<template>
     <div style="position: absolute;top: 96px;width: 100%;left: 0">
          <div>
              <div class="pls">
                  <div style="width: 79vw;margin: 0 auto;display: flex">
                      <h3 style="margin-left: 5px;color:#409EFF;font-size: 1.5rem">欢迎来到学科竞赛的讨论区。在这里，你可以畅所欲言!</h3>
                      <span><el-button style="margin-top: 12px;margin-left: 20px" @click="router.push('/postPage')">我要发帖</el-button></span>
                  </div>
                  <div v-for="(item,index) in posts" :key="index" style="margin: 5px auto 0;width: 80%;color: #666;
                  font-size: 1.5rem;background-color: white;">
                      <div style="font-size: 20px;font-weight: 600;text-align: center">
                      <span @click="router.push('/plPage?id='+item.postId+'&a='+item.username+'&b='+item.url)
                         "style="cursor: pointer;">
                          标题:{{item.title}}
                      </span>
                      </div>
                  <div style="border: 3px solid rgb(153,153,153);width: 60px;height: 60px;border-radius: 50%;margin-left: 10px;margin-top: 10px">
                     <img w-full :src="a+item.url" style="width: 60px;height: 60px;border-radius: 50%;"  alt="gg"/>
                  </div>

                  <div style="margin-top: -55px;margin-left: 85px">{{item.username}}</div>
                  <div style="margin-left: 84px">发布时间:{{time.formatDate(item.postDate)}}</div>
                  <div  class="pl" style="
                  margin-top: 5px;
                      display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2; /* 设置显示的行数 */
                    overflow: hidden; /* 如果内容太多，可以添加滚动条 auto*/
                    text-overflow: ellipsis; /* 当文本溢出容器时显示省略号 */
                    margin-left: 84px;
                    background-color: #f6f4f4;
                    line-height: 20px;
                    word-wrap: break-word; /* 或者使用 overflow-wrap: break-word; */
                    border-radius: 5px;
                    color: #666;
                    /*border: 1px skyblue solid;*/
                    margin-bottom: 5px;
                    cursor: pointer;"
                       @click="router.push('/plPage?id='+item.postId+'&a='+item.username+'&b='+item.url)"
                  >
                      <div v-html="item.content"></div>
<!--                      {{item.content}} -->
                  </div>
               </div>
             </div>

              <div style="display: flex; justify-content: center; align-items: center;">
                  <el-pagination
                          style="display: flex; justify-content: center; align-items: center;width:350px;
                    flex-wrap: wrap;margin-top: 20px;margin-bottom: 10px;"
                          layout="prev, pager, next,jumper,total"
                          prev-text="上一页"
                          next-text="下一页"
                          big background  v-model:current-page="currentPage" :total="totalCount" :page-size=8  @current-change="getPost"/>
              </div>
          </div>
     </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import router from "../../../router/router.js"
import time from "../../../utils/time.js";
interface Post {
     postId: string;
     title: string;
     content: string;
     postDate: string;
     username: string;
     url: string;
}
const a=ref("http://localhost:8888/comm/tx/")
const posts= ref<Post[]>([]);//数据集合
const currentPage=ref(1)
const totalCount=ref() //数据总条数

const getPost=()=>{
     axios.get('/comm/postList?currentPage='+currentPage.value)
         .then((res)=>{
             // console.log(res.data)
             totalCount.value=res.data.totalCount
             posts.value=res.data.list
         })
}

onMounted(()=>{
    getPost()
})
</script>

<style scoped>
@media screen and (min-width: 1470px) {
    .pls{
        width:1470px;
        margin: 0 auto;
        padding-bottom: 15px;
        background-color: #f6f4f4;
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
    }
    .pl{
        width: 50vw;
    }
}
@media screen and (max-width: 1470px) {
    .pls{
        width:100vw;
        margin: 0 auto;
        padding-bottom: 15px;
        background-color: #f6f4f4;
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
    }
    .pl{
        width: 50vw;
    }
}
@media screen and (max-width: 960px) {
    .pls{
        width:100vw;
        margin: 0 auto;
        padding-bottom: 15px;
        background-color: #f6f4f4;
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
    }
    .pl{
        width: 50vw;
    }
}
@media screen and (max-width: 750px) {
    .pls{
        width:100vw;
        margin: 0 auto;
        padding-bottom: 15px;
        background-color: #f6f4f4;
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
    }
    .pl{
        width: 50vw;
    }
}
</style>

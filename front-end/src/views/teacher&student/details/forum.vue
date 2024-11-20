<template>
    <div style="position: absolute;top: 100px;width: 100%;left: 0">

        <div class="lt">
            <div style="margin-bottom: 40px;margin-top: 10px;">
                <div style="border: 3px solid rgb(153,153,153);width: 60px;height: 60px;border-radius: 50%;margin-left: 10px;">
                    <img w-full :src="a+url" style="width: 60px;height: 60px;border-radius: 50%;"  alt="gg"/>
                </div>
                <div style="margin-top: -55px;margin-left: 85px">{{username}}</div>
                <div style="margin-left: 84px">发布时间:{{time.formatDate(date)}}</div>
                <div style="margin-left: 300px;margin-top: -36px;font-size: 25px;font-weight: 600">
                    <span style="color: #666666">{{infoTitle}} </span>
                </div>
                <div
                    style="margin-top: 10px;
                    overflow: auto; /* 如果内容太多，可以添加滚动条 auto*/
                    margin-left: 84px;
                    background-color: #f6f4f4;
                    width: 680px;
                    line-height: 28px;
                    word-wrap: break-word; /* 或者使用 overflow-wrap: break-word; */
                    border-radius: 5px;
                    color: #666;"
                >
                    <div v-html="infoContent" style="font-size: 1.5rem"/>
                </div>
            </div>

            <el-button style="float: right;margin-top: -120px" @click="router.push('/forumPage')" type="primary">返回帖子区</el-button>
            <div style="width: 800px;margin: 0 auto;">
                <el-input
                    v-model="textarea"
                    style="width: 640px;"
                    :rows="5"
                    resize="none"
                    type="textarea"
                    placeholder="期待你的评论"
                    maxlength="100"
                />
                <el-button type="primary" @click="submit()">发布</el-button>
            </div>
            <div style="width: 800px;margin: 10px auto 0;">
            <div v-for="(item, index) in comments" :key="index">
            <div style="border-top: 1px solid #666666">
                <div style="border: 3px solid rgb(153,153,153);width: 60px;height: 60px;border-radius: 50%;margin-left: 10px;margin-top: 10px">
                    <img w-full :src="a+item.url" style="width: 60px;height: 60px;border-radius: 50%;"  alt="gg"/>
                </div>
                <div style="margin-top: -55px;margin-left: 85px">{{item.username}}</div>
                <div style="margin-left: 84px">{{time.formatDate(item.time)}}</div>
                <div style="margin-top: 10px;
                    margin-left: 84px;
                    /*border: 1px solid #666;*/
                    width: 500px;
                    line-height: 25px;
                    max-height: 120px;
                    word-wrap: break-word; /* 或者使用 overflow-wrap: break-word; */
                    overflow: auto; /* 如果内容太多，可以添加滚动条 */
                    border-radius: 5px;
                    color: #666;
                    /*user-select: none; !* 防止用户选择文本 *!*/
                ">
                    {{item.content}}
                </div>
                <div>
                    <el-popover trigger="click" :width="400">
                        <template #reference>
                            <el-button style="margin-top: -22px;float: right" link type="primary">回复</el-button>
                        </template>
                        <el-input v-model="textarea" :rows="4" type="textarea"></el-input>
                        <el-button @click="submit(item.commentId,item.userId)">提交</el-button>
                    </el-popover>
                </div>
            </div>
            <div v-for="(item, index) in item.children" :key="index">
                <div style="margin-left: 70px;margin-top: 5px;border-top: 1px skyblue solid">
                    <div style="border: 3px solid rgb(153,153,153);width: 60px;height: 60px;border-radius: 50%;margin-left: 10px;margin-top: 5px">
                        <img w-full :src="a+item.url" style="width: 60px;height: 60px;border-radius: 50%;"  alt="gg"/>
                    </div>
                    <div style="margin-top: -55px;margin-left: 85px">{{item.username}} 回复
                        <span style="color: cornflowerblue">@{{item.replyUsername}}</span></div>
                    <div style="margin-left: 84px">{{time.formatDate(item.time)}}</div>
                    <div style="margin-top: 10px;
                    margin-left: 84px;
                    /*border: 1px solid #666;*/
                    width: 500px;
                    line-height: 25px;
                    max-height: 120px;
                    word-wrap: break-word; /* 或者使用 overflow-wrap: break-word; */
                    overflow: auto; /* 如果内容太多，可以添加滚动条 */
                    border-radius: 5px;
                    color: #666;
                ">
                        {{item.content}}
                    </div>
                    <div>
                      <el-popover trigger="click" :width="400">
                        <template #reference>
                            <el-button style="margin-top: -22px;float: right" link type="primary">回复</el-button>
                        </template>
                        <el-input v-model="textarea" :rows="4" type="textarea"></el-input>
                        <el-button @click="submit(item.pid,item.userId)">提交</el-button>
                      </el-popover>
                    </div>
                </div>
            </div>
         </div>
        </div>

       </div>

    </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import axios2 from "../../../axios/axios.js";
import {ElMessage} from "element-plus";
import router from  "../../../router/router.js"
import { useRoute } from 'vue-router';
import time from "../../../utils/time.js";

const route = useRoute();
const postId=ref(route.query.id)
const username=ref(route.query.a)
const url=ref(route.query.b)
const textarea = ref('')
const a=ref("http://localhost:8888/comm/tx/")
const uid=ref()
const props = defineProps({change: Function});
const infoTitle=ref()
const infoContent=ref()
const date=ref()

class Comment {
    url: string
    username: string
    replyUsername: string
    children: Comment
    commentId: string
    content: string;  //内容
    pid: string
    postId: string
    replyId: string
    time: string;   //评论时间
    userId: string
}
// interface Post {
//     title: string;
//     content: string;
//     postDate: string;
// }
const comments = ref<Comment[]>([]);//评论的数组
//获取登录用户的uid
const getId=()=>{
        axios.get("/personal/getUserInfo",{headers:axios2.accessHeader()})
        .then((res)=>{
            // console.log(res.data)
            uid.value=res.data.data.uid
        })
}
//提交评论
const submit=(id?: Number,replyUserId?: Number)=>{
    if (axios2.accessHeader().Authorization==null)
    {
        ElMessage.error("请登录后再发表吧")
        router.push("/registerLoginPage/loginPage")
        props.change("/registerLoginPage/loginPage")
    }
    else if (textarea.value.length==0)
        ElMessage.warning("请填写内容再发表吧")
    else
    {
        const entity=new Comment();
        entity.content=textarea.value;
        console.log(entity.content)
        entity.pid=id;               //id是这层楼的父id
        entity.userId=uid.value
        entity.replyId=replyUserId  //你要回复的那个人的uid
        entity.postId=postId.value
        console.log("实体")
        console.log(entity)
        axios({
            method: 'post',
            url: '/comm/save',
            data:entity,
            headers:{
                'Content-Type': 'application/json'
            }
        })
          .then((res)=>{
              console.log(res)
             if (res.data.code===200)
                 ElMessage.success("发布成功")
              pl()
              textarea.value=null
              // open.value=false
          })
          .catch((e)=>{
           console.log(e)
              textarea.value=null
              // open.value=false
           ElMessage.error("发表失败")
         })
    }
}
const pl=()=>{                   //根据postId查找所有评论表
    axios.get("/comm/list?postId="+postId.value)
        .then((response)=>{
            // console.log(response)
            comments.value=response.data.data
        })
}
const postInfo=()=>{      //根据帖子id查询帖子信息
    axios.get("/comm/postInfo?postId="+postId.value)
        .then((res)=>{
           infoTitle.value=res.data.title
            infoContent.value=res.data.content
            date.value=res.data.postDate
        })
}
onMounted(()=>{
    pl(),getId(),postInfo()
})
</script>

<style>
.lt{
    margin: 0 auto;
    width: 64%;
    background-color: white;
    box-shadow: 0 0 5px 0 rgba(0,0,0,0.1);
    border-radius: 5px;
    border: 1px solid white;
}
</style>

<template>
    <header>
            <div style="margin-top: 20px;">
                <el-link :underline="false" @click="to()">
                    <img src="../assets/1.png" title="点我回到首页" style="width: 30rem;margin-left: 10px">
                </el-link>
            </div>

            <div class="top2">
                <el-link :underline="false" :class="{ 'underlined': isUnderlined }"  @click="to()" style="height:35px;">
                    <el-icon style="margin-left: -3px;margin-top: -5px" size="29"><House /></el-icon>首页
                </el-link>
                <el-link :underline="false" :class="{ 'underlined': isUnderlined2 }" @click="to2()" style="height: 35px;">
                    <el-icon style="margin-left: -3px;margin-top: -5px" size="29"><TrophyBase /></el-icon>竞赛
                </el-link>
                <el-link :underline="false" :class="{ 'underlined': isUnderlined3 }" @click="to3()" style="height: 35px;">
                    <el-icon style="margin-left: -2px;margin-top: -5px" size="28"><User /></el-icon>个人中心
                </el-link>
                <el-link :underline="false" :class="{ 'underlined': isUnderlined4 }" @click="to4()" style="height: 35px;">
                    <el-icon style="margin-left: -3px;margin-top: -1px" size="29"><ChatLineSquare /></el-icon>论坛
                </el-link>
                <el-link v-if="show2()" :underline="false" :class="{ 'underlined': isUnderlined5 }" @click="to5()" style="height: 35px;">
                    <el-icon size="28"><Tools /></el-icon>后台管理
                </el-link>
                <el-link v-if="show()" :underline="false" :class="{ 'underlined': isUnderlined6 }" @click="to6()" style="height: 35px;">
                    <el-icon style="margin-left: -2px;" size="28"><Tickets /></el-icon>成绩管理
                </el-link>
            </div>

            <div class="top3">
                <component  ref="child2" :is="tabComponent" :to7="to7" :to3="to3"  :to="to" :show="show" :tab="tab"></component>
            </div>
<!--        <div>-->
<!--            <span class="top4"><el-icon size="35"><Fold /></el-icon>-->
<!--                <div :class="{ open: isSidebarOpen }">-->
<!--                    <ul>-->
<!--                         <component  ref="child2" :is="tabComponent" :to7="to7" :to3="to3"  :to="to" :show="show" :tab="tab"></component>-->
<!--                        <li @click="closeSidebar"><el-link @click="to">首页</el-link></li>-->
<!--                        <li @click="closeSidebar"><el-link @click="to2">竞赛</el-link></li>-->
<!--                        <li @click="closeSidebar"><el-link @click="to3">个人中心</el-link></li>-->
<!--                        <li @click="closeSidebar"><el-link @click="to4">论坛</el-link></li>-->
<!--                        <li @click="closeSidebar"><el-link v-if="show2()" @click="to5">后台管理</el-link></li>-->
<!--                        <li @click="closeSidebar"><el-link v-if="show()" @click="to6">成绩管理</el-link></li>-->
<!--                    </ul>-->
<!--                </div>-->
<!--            </span>-->
        <span class="top4">
    <el-icon size="35"  class="nav-icon" @click="toggleSidebar"><Fold /></el-icon>
    <div :class="{ open: isSidebarOpen }">
      <ul>
        <component @click="handleItemClick(to7)" ref="child2" :is="tabComponent" :to7="to7" :to3="to3" :to="to" :show="show" :tab="tab"></component>
        <li @click="handleItemClick(to)">首页</li>
        <li @click="handleItemClick(to2)">竞赛</li>
        <li @click="handleItemClick(to3)">个人中心</li>
        <li @click="handleItemClick(to4)">论坛</li>
        <li v-if="show2()" @click="handleItemClick(to5)">后台管理</li>
        <li v-if="show()" @click="handleItemClick(to6)">成绩管理</li>
      </ul>
    </div>
  </span>
        <div v-if="isSidebarOpen" class="overlay" @click="closeSidebar"></div>
<!--        </div>-->
    </header>

</template>
<script setup>
import One from "./headSculpture2.vue"
import Two from "./headSculpture.vue"
import router from "../router/router.js";
import {ref, defineExpose,onMounted} from 'vue'
import axios from "../axios/axios.js";
import {useRoute} from 'vue-router';
import {ElMessage} from "element-plus";


const isSidebarOpen = ref(false);
const closeSidebar = () => {
    isSidebarOpen.value = false;
};
const toggleSidebar = () => {
    isSidebarOpen.value = !isSidebarOpen.value;
};

const handleItemClick = (navigateTo) => {
    if (navigateTo.name==="to7"&&axios.unauthorized()===false)
        console.log(navigateTo.name==="to7")
    else
       isSidebarOpen.value = false;
    navigateTo(); // 调用导航函数
};
const child2 = ref();
const fetchImage=()=>{
    if (child2.value)
        child2.value.fetchImage()
}
const tabComponent=ref(axios.unauthorized() ? One:Two)//判断是否登录了。未登录就跳one组件，否则two组件
const tab=()=>{
        tabComponent.value=One; //退出登录？切换成注册/登录
}
const tab2=()=>{
      tabComponent.value=Two;   //登录成功后切换头像
}

const role=ref('')
const getRole=()=>{
    axios.getRole((zl)=>{
        role.value=zl
    })
}
onMounted(()=>{
    getRole()
})
//每次刷新页面,输入路径,跳转到相应的位置
const route = useRoute();
let isUnderlined= ref(route.path === '/homePage');
let isUnderlined2= ref(route.path === '/competitionPage');
let isUnderlined3= ref(route.path === '/personalPage/teacherPersonal' || route.path === '/personalPage/studentPersonal'|| route.path === '/personalPage');
let isUnderlined4= ref(route.path === '/forumPage');
let isUnderlined5= ref(route.path === '/adminPage');
let isUnderlined6= ref(route.path === '/competitionManage');

const change=(path)=>{
    isUnderlined.value = isUnderlined2.value=isUnderlined3.value=isUnderlined4.value=isUnderlined5.value=isUnderlined6.value=false;
    if (path==='/homePage')
        isUnderlined.value=true;
    else if (path==='/competitionPage')
        isUnderlined2.value=true;
    else if (path===('/personalPage'))
        isUnderlined3.value=true;
    else if (path==='/forumPage')
        isUnderlined4.value=true;
    else if (path==='/adminPage')
        isUnderlined5.value=true;
    else if (path==='/competitionManage')
        isUnderlined6.value=true
}
const to = () => {
    // router.push({name: 'homePage'})
    router.push('/homePage')
    change('/homePage')
};
const to2 = () => {
    router.push('/competitionPage')
    change('/competitionPage')
};
const to3= ()=>{
    if (axios.unauthorized())
    {
        ElMessage.warning("未登录，请登录后操作")
        return  to7()
    }
    else if (role.value==="student")
        {
            router.push("/personalPage/studentPersonal")
            change('/personalPage')
        }
    else if (role.value==="teacher")
        {
            router.push("/personalPage/teacherPersonal")
            change("/personalPage")
        }
    else
       {
           router.push("/personalPage")
           change("/personalPage")
       }

}
const to4=()=>{
        router.push("/forumPage")
        change("/forumPage")
}
const to5=()=>{
        router.push('/adminPage')
        change("/adminPage")
}
const to6=()=> {
        router.push("/competitionManage")
        change("/competitionManage")
    }
const to7=()=> {
    router.push("/registerLoginPage")
    change("")
}
defineExpose({
    to2,to,tab2,change,fetchImage,getRole
});
const show=(zl)=>{
    if (zl!=null)
        role.value=zl
    return role.value === "teacher";
}
const show2=()=>{
    return role.value === "admin";
}

</script>

<style scoped>
.nav-icon {
    cursor: pointer;
    color: #fff;
    background: linear-gradient(45deg, #6a11cb 0%, #2575fc 100%);
    padding: 5px;
    border-radius: 50%;
}
.top2{
    /*border: 1px skyblue solid;*/
    display: flex;
    width: 30%;/*之前是40全包裹*/
    height: 55px;
    margin: -42px auto 0;
    justify-content: space-between;
}
.top3{
    width: 76px;
    height:60px;
    float: right;
    margin-right: 40px;
    margin-top: -56px;
}
@media screen and (min-width: 1489px){
    .top4{
        display: none;
    }
    header{
        width: 1488px;
        left: 50%;
        transform: translateX(-50%);
        top:0;/*使滑动时不会改变视口当前的相对位置*/
    }
}
@media screen and (min-width: 960px)and (max-width: 1488px){
    header{
        top:0;/*使滑动时不会改变视口当前的相对位置*/
    }
    .top4{
        display: none;
    }
    header{
        width: 100%;
        margin: 0 auto;
        left: 0;
    }
}
@media screen and (max-width: 960px) and (min-width: 750px){
    header {
        width: 100vw;
        left: 0;
        top: -1px;
    }

    .top3, .top2 {
        display: none;
    }

    .top4 {
        display: block;
        float: right;
        margin-right: 5px;
        margin-top: -44px;
    }

    .top4 > div {
        height: 100vh;
        width: 50%; /* 扩展栏占屏幕的50%*/
        background: linear-gradient(135deg, #87c5da 0%, #546ddc 100%);
        position: fixed;
        top: 0;
        right: -100%;
        border-left: 1px solid skyblue;
        box-shadow: -2px 0 5px rgba(0, 0, 0, 0.5);
        transition: right 0.3s ease;
        overflow-y: auto; /* 增加滚动条 */
        z-index: 1000;
    }

    .top4 > div.open {
        right: 0;
    }

    .top4 ul {
        padding: 0;
        margin: 0;
        list-style-type: none;
    }

    .top4 ul li {
        padding: 15px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        cursor: pointer;
        color: #fff;
        font-weight: bold;
        transition: background-color 0.3s, transform 0.3s;
    }

    .top4 ul li:hover {
        background-color: rgba(255, 255, 255, 0.1);
        transform: translateX(10px);
    }

    .overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 999;
    }
}
@media screen and (max-width: 750px) {
    header {
        width: 100vw;
        left: 0;
        top: -1px;
    }

    .top3, .top2 {
        display: none;
    }

    .top4 {
        display: block;
        float: right;
        margin-right: 5px;
        margin-top: -44px;
    }

    .top4 > div {
        height: 100vh;
        width: 50%; /* 扩展栏占屏幕的50%*/
        background: linear-gradient(135deg, #87c5da 0%, #546ddc 100%);
        position: fixed;
        top: 0;
        right: -100%;
        border-left: 1px solid skyblue;
        box-shadow: -2px 0 5px rgba(0, 0, 0, 0.5);
        transition: right 0.3s ease;
        overflow-y: auto; /* 增加滚动条 */
        z-index: 1000;
    }

    .top4 > div.open {
        right: 0;
    }

    .top4 ul {
        padding: 0;
        margin: 0;
        list-style-type: none;
    }

    .top4 ul li {
        padding: 15px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        cursor: pointer;
        color: #fff;
        font-weight: bold;
        transition: background-color 0.3s, transform 0.3s;
    }

    .top4 ul li:hover {
        background-color: rgba(255, 255, 255, 0.1);
        transform: translateX(10px);
    }

    .overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 999;
    }
}

header{
    position: fixed;
    z-index: 2;/* main里面z-index是1 */
    background-color: white;
    /*width: 100%;*/
    height: 90px;
    box-sizing: border-box;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);/* 0水平偏移量;2px垂直偏移量;6px阴影的模糊半径,决定了阴影的模糊程度;最后是阴影的颜色和透明度*/
}
.underlined{
    border-bottom: 3px solid skyblue;
}

</style>

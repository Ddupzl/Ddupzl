<template>
<!--<Main>-->
<!--    <template #main1>-->
    <div style="position: absolute;top: 94px;width: 100%;left: -1px">

        <div class="block">
            <el-carousel trigger="click" height="350px">
                <el-carousel-item v-for="item in downList" :key="item">
                    <el-avatar :src="url+item.image" shape="square" fit="fill" style="height: 350px;width: 100%;"></el-avatar>
                </el-carousel-item>
            </el-carousel>
        </div>
        <div class="block2">
            <div style="margin-top: 6px;margin-left: 5px;font-weight:600;font-size: 15px;color: #409EFF;">竞赛类型:</div>
            <div>
                <el-link style="margin-top: -30px;margin-left: 97px;width: 60px;height: 28px;border-radius: 6px;" :underline="false"  :class="{ 'clicked': buttonStyle==='全部' }" @click="all('全部')">全部</el-link>
            </div>
            <div  style="display: flex;flex-direction: column; justify-content: flex-start;gap: 24px;margin-left: 23px;">
                <div v-for="(item, index) in subjectInfo" style="display: flex;align-items: center;" :key="index" > <!-- 根据大类查询-->
                    <el-link @click="getSubjectByType(item.bigType)" style="width: 60px;height: 28px;border-radius: 6px;" :underline="false" :class="{ 'clicked': buttonStyle===item.bigType}" >
                        {{item.bigType}}
                    </el-link>                      <!--item.smallType也可以 -->
                    <div style="" v-for="item2 in subjectInfo[index].smallType" >  <!-- 根据小类查询-->
                        <el-link @click="getSubjectByType(item2)" style="margin-left: 15px;width: 60px;height: 28px;border-radius: 6px;" :underline="false" :class="{ 'clicked': buttonStyle===item2 }">
                            {{item2}}
                        </el-link>
                    </div>
                </div>
            </div>
            <div>
                <div style="margin-top: 30px;margin-left: 5px;font-weight:600;font-size: 15px;color: #409EFF;">排序方式:</div>
                <el-link style="margin-top: -36px;margin-left: 97px;width: 60px;height: 28px;border-radius: 6px;" :underline="false" :class="{ 'clicked': buttonStyle==='最近更新' }" @click="latest2('最近更新')" >最近更新</el-link>
                <el-link style="margin-top: -36px;margin-left:20px;width: 60px;height: 28px;border-radius: 6px;" :underline="false" :class="{ 'clicked': buttonStyle==='开赛时间' }" @click="latest3('开赛时间')">开赛时间</el-link>
            </div>
        </div>

<!--    </template>-->
<!--    <template #main2>-->
<!--        <div>-->
            <div class="cmain2">
                    <div v-for="(item, index) in competitionInfo2" :key="index" class="comp">
                        <div style="float: right;margin-top: 10px;">
                            <el-button v-if="role=='student'||role==''"  @click="bm(item.competitionId)" style="color: white;font-size: 17px;
                            font-weight: 600;background-color:#409EFF;width: 140px;height: 48px">点击报名</el-button>
                            <div v-if="daysLeft(parseDateString(item.registrationTimeEnd))>0" class="zt">距离报名截止还有 {{ daysLeft(parseDateString(item.registrationTimeEnd)) }} 天</div>
                            <div v-else-if="daysLeft(parseDateString(item.registrationTimeEnd))==0" class="zt">报名今晚12点截止 ! ! !</div>
                            <div v-else class="zt">报名已结束!</div>
                            <el-link  @click="details(item.competitionId)" :underline="false" style="font-weight: 600;font-size: 15px;margin-top: 10px; ">
                                竞赛详情<div style="color:#409EFF">&nbsp;></div>
                            </el-link>
                        </div>
                        <div style="width: 100vw;margin-top: 10px;">
                                <div style="color: #409EFF;font-size: 1.5rem;font-weight: 600">正在报名</div>
                                <div style="margin-left: 70px;margin-top: -23px;display: flex">
                                    <el-link @click="details(item.competitionId)" style="font-size: 21px" :underline="false">{{ item.competitionName }}</el-link>
                                </div>
                                <div>
                                    <div class="zl">主办方</div>
                                    <div class="zl2">{{ item.sponsor }}</div>
                                </div>
                                <div>
                                    <div class="zl">报名时间 </div>
                                    <div class="zl2">{{item.registrationTimeStart}}~{{item.registrationTimeEnd}}</div>
                                </div>
                                <div>
                                    <div class="zl">比赛时间 </div>
                                    <div class="zl2" style="margin-bottom: 15px">{{item.competitionTime}}</div>
                                </div>
                            </div>
                    </div>

            </div>
            <div style="display: flex; justify-content: center;margin-bottom: 10px;margin-top: 10px">
                <el-pagination
                    style="display: flex; justify-content: center; align-items: center;width:350px;
                    flex-wrap: wrap;"
                    layout="prev, pager, next,jumper,total"
                    prev-text="上一页"
                    next-text="下一页"
                    big background
                    v-model:current-page="currentPage" :total="totalCount" :page-size=8  @current-change="changPage"/>
            </div>
<!--        </div>-->
<!--    </template>-->
<!--</Main>-->
    </div>
</template>

<script lang="ts" setup>
import {onMounted,  ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "../../../axios/axios.js"
import router from "../../../router/router.js"
import axios2 from "axios";
import time from "../../../utils/time.js"

const role=ref('')
const getRole=()=>{
    axios.getRole((zl)=>{
        if (zl.code===401)
            role.value=''
        else
            role.value=zl
    })
}
onMounted(()=>{
    getRole()
})
const url=ref(time.http+"/lbt/download/")
const currentPage=ref(1)
const jz=ref("全部")
const totalCount=ref() //数据总条数
const buttonStyle=ref('全部')
const downList=ref()
//下载轮播图
const downPList=()=>{
    axios2.get("/lbt/download/downloadList",{headers: axios.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
                downList.value=res.data.data
            else
                ElMessage.error(res.data.message)
        })
}

interface CompetitionInfoItem2 {
    competitionId: string;
    competitionName: string;
    sponsor: string;
    registrationTimeStart: string;
    registrationTimeEnd: string;
    competitionTime: string;
}
const competitionInfo2 = ref<CompetitionInfoItem2[]>([]);//数据集合

interface SubjectItem{
   bigType:string;
   smallType:string[];
}
const subjectInfo = ref<SubjectItem[]>([])

// 解析日期字符串为Date对象
const parseDateString=(dateString)=> {
    const parts = dateString.split('-');
    return new Date(parseInt(parts[0]), parseInt(parts[1]) - 1, parseInt(parts[2])); // 注意月份是从0开始的，所以减1
}
// 计算距离约定日期还有多少天
const daysLeft=(appointmentDate)=> {
    const now = new Date();
    const appointmentTime = appointmentDate.getTime();
    const nowTime = now.getTime();
    const diff = appointmentTime - nowTime;
    const days = Math.ceil(diff / (1000 * 60 * 60 * 24)); // 将毫秒转换为天，并向上取整
    return days;
}

const changPage=()=>{
    if (buttonStyle.value==='全部')
        all(buttonStyle.value)
    else if (buttonStyle.value==='最近更新')
        latest2(buttonStyle.value)
    else if (buttonStyle.value==='开赛时间')
        latest3(buttonStyle.value)
    else
        getSubjectByType(buttonStyle.value)
}
const latest2= (name)=>{
    if (buttonStyle.value!=name)
        currentPage.value=1
    buttonStyle.value=name
    axios.get("/info/getLatest2?currentPage="+currentPage.value,(zl)=>{
            competitionInfo2.value=zl.list
            totalCount.value=zl.totalCount
        }, ()=>ElMessage.error("获取信息失败,出现了一点问题")
    )
}
const latest3= (name)=>{
    if (buttonStyle.value!=name)
        currentPage.value=1
    buttonStyle.value=name
    axios.get("/info/getLatest3?currentPage="+currentPage.value,(zl)=>{
            competitionInfo2.value=zl.list
            totalCount.value=zl.totalCount
        }, ()=>ElMessage.error("获取信息失败,出现了一点问题")
    )
}
const getSubjectByType=(name)=>{
    if (buttonStyle.value!=name)
        currentPage.value=1
    buttonStyle.value=name
    axios.get("/info/getSubByType?type="+name+"&currentPage="+currentPage.value,(zl)=>{
        competitionInfo2.value=zl.list
        totalCount.value=zl.totalCount
    })
}
const getSubjectType=()=>{
    axios.get("/info/getSubType",(list)=>{
        subjectInfo.value=list //获取竞赛大的科目
    }, ()=>ElMessage.error("获取信息失败,出现了一点问题"))
}

const all=(name)=>{
    if (buttonStyle.value!=name)
        currentPage.value=1
    buttonStyle.value=name
    axios.get("/info/getAllClassify?currentPage="+currentPage.value,(zl)=>{
            competitionInfo2.value=zl.list
            totalCount.value=zl.totalCount
        }, ()=>ElMessage.error("获取信息失败,出现了一点问题")
    )
}
onMounted(() => {
    all(buttonStyle.value);getSubjectType(); downPList()
});

const bm=(id)=>{
    change('/bmPage') //跳转页面不显示下划线
    router.push('/bmPage?cid='+id)
}
const details=(id)=>{
    change('/detailPage') //跳转页面不显示下划线
    router.push('/detailPage?cid='+id)
}
//父传子的
const props = defineProps({change: Function});
const change=(z)=>{
    props.change(z)
}
</script>

<style scoped>
.zt{
    font-weight: 600;
    font-size: 14px;
    color: #606266;
    margin-top: 10px;
    /*margin-right: 10px;*/
}
.clicked {
    background-color: #ecf5ff;
    color: #409EFF; /* 文字颜色 */
}
.zl{
    margin-top: 6px;
    font-size: 1.5rem;
    font-weight:600;
    color: #aaa
}
.zl2{
    margin-left: 80px;
    font-size: 1.5rem;
    margin-top: -20.2px;
    color: #aaa
}
.cmain2{
    left: 0;
    margin: 5px auto 0;
    padding-bottom: 5px;
    width: 831px;
    background-color: #f6f4f4;
    display: flex;
    flex-wrap: wrap;
}
@media screen and (min-width: 1300px) {
    .main1{
        /*display: flex;*/
        width: 1470px;
        margin: 0 auto;
        /*border: 1px skyblue solid;*/
    }
    .block{
        /*border: 1px solid red;*/
        /*width: 1050px;*/
        width: 1000px;
        height: 350px;
    }
    .block2{
       margin-top: -348px;height: 345px;margin-left: 1020px
    }
    .comp{
        width: 820px;margin-top: 5px;margin-left: 5px;background-color: white;
    }
}
@media screen and (max-width: 1300px){
    .main1{
        width: 100vw;
        margin: 0 auto;
        /*border: 1px skyblue solid;*/
        /*display: flex;*/
    }
    .block{
        /*border: 1px solid red;*/
        /*display: none;*/
        width: 700px;
    }
    .block2{
        margin-top: -348px;height: 345px;margin-left: 710px
    }
    .comp{
        width: 820px;margin-top: 5px;margin-left: 5px;background-color: white;
    }
}
@media screen and (max-width: 960px){
    .main1{
        width: 100vw;
        margin: 0 auto;
        /*border: 1px skyblue solid;*/
        /*display: none;*/
    }
    .block{
        /*border: 1px solid red;*/
        /*display: none;*/
        width: 100vw;
    }
    .block2{
        margin-top: 0;
        margin-left: 10px;
    }
    .cmain2{
        padding-bottom: 5px;
        width: 100vw;
    }
    .comp{
        width: 99vw;
        background-color: white;
    }
}
</style>

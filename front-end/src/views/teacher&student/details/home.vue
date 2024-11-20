<template>
<!--    这是用的main.vue 再用插槽往里面填内容-->
<Main>
    <template #main1>  <!-- 具名插槽v-slot的简写# -->
        <section class="picture" ></section>
    </template>
    <template v-slot:main2>
        <div class="dh">
            <el-button size="default" :class="{ 'clicked': isButtonClicked }"  @click="tj">热门竞赛</el-button>
            <el-button size="default" :class="{ 'clicked': isButtonClicked2 }"  @click="latest">最近更新</el-button>
            <el-button size="default" @click="to2">更多竞赛详情...</el-button>
        </div>
        <div>
            <div style="margin-top: 10px;">
                <div class="home">
                    <!-- 动态生成的盒子将会放在这里 -->
                    <div v-for="(item, index) in competitionInfo" :key="index" style="border: 1px solid grey; width: 282px;border-radius: 10px;">
                        <div>
                            <img  @click="details(item.competitionId)" style="cursor: pointer;border-radius: 10px 10px 0 0;width: 282px;height: 190px" :src="a+item.url">
                        </div>
                        <div style="width: 282px;">
                            <div style="height: 40px">
                                <el-link @click="details(item.competitionId)" :underline="false">{{ item.competitionName }}</el-link>
                            </div>
                            <div>主办方: {{ item.sponsor }}</div>
                            <div>比赛时间: {{item.competitionTime }}</div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="isButtonClicked" style="display: flex; justify-content: center; align-items: center;">
                <el-pagination
                    style="display: flex; justify-content: center; align-items: center;width:350px;
                  flex-wrap: wrap;margin-top: 20px;"
                    layout="prev, pager, next,jumper,total"
                    prev-text="上一页"
                    next-text="下一页"
                    big background   v-model:current-page="currentPage"  :total="totalCount" :page-size=8  @current-change="tj"/>
            </div>
            <div v-else style="display: flex; justify-content: center; align-items: center;">
                <el-pagination
                    style="display: flex; justify-content: center; align-items: center;width:350px;
                    flex-wrap: wrap;margin-top: 20px;"
                    layout="prev,pager,next,jumper,total"
                    prev-text="上一页"
                    next-text="下一页"
                    big background   v-model:current-page="currentPage2"  :total="totalCount" :page-size=8  @current-change="latest"/>
            </div>
        </div>

        <div class="footer">
            <img src="../../../assets/备案图标.png" alt="" style="width: 2rem">
            <a href="https://beian.mps.gov.cn/#/query/webSearch?code=43072502000344" style="text-decoration: none;margin-left: 10px;color: #666" rel="noreferrer" target="_blank">湘公网安备43072502000344</a>
            <a href="https://beian.miit.gov.cn/" style="text-decoration: none;margin-left: 20px;color: #666;" target="_blank">湘ICP备2024071241号</a>
        </div>
    </template>
</Main>

</template>

<script lang="ts" setup>
import Main from "../../../components/main.vue"
import {defineProps, onMounted,ref} from 'vue';
import axios from "../../../axios/axios.js"
import {ElMessage} from "element-plus";
import router from "../../../router/router.js"

const a=ref("http://localhost:8888/info/file/download/")
interface CompetitionInfoItem {
    competitionId: string;
    competitionName: string;
    sponsor: string;
    competitionTime: string
    url: string;
}

const competitionInfo = ref<CompetitionInfoItem[]>([]); //数据集合
const totalCount=ref() //数据总条数
const currentPage=ref(1)
const currentPage2=ref(1)
const isButtonClicked=ref(true)
const isButtonClicked2=ref(false)

const details=(id)=>{
    change('/detailPage')//跳转页面不显示下划线
    router.push('/detailPage?cid='+id)
}
const tj= ()=>{
    if (isButtonClicked.value==false)
        currentPage.value=1
    isButtonClicked.value = true
    isButtonClicked2.value=false
    axios.get("/info/getHot?currentPage="+currentPage.value,(zl)=>{
          competitionInfo.value=zl.list
          totalCount.value=zl.totalCount
            // console.log(zl.list)
            // console.log(zl.list[0].competitionName)
            // console.log(competitionInfo.value)
            // console.log(competitionInfo.value[0].competitionName)
        }, ()=>ElMessage.error("获取信息失败,出现了一点问题")
    )
}
onMounted(() => {
    tj()
});

const latest= ()=>{
    if (isButtonClicked2.value==false)
        currentPage2.value=1
    isButtonClicked.value = false
    isButtonClicked2.value=true
    axios.get("/info/getLatest?currentPage="+currentPage2.value,(zl)=>{
        competitionInfo.value=zl.list
        totalCount.value=zl.totalCount
    }, ()=>ElMessage.error("获取信息失败,出现了一点问题")
    )
}
//父传子的
const props = defineProps({to2: Function,change: Function});
const to2 = () => {
    props.to2();
};
const change=(z)=>{
    props.change(z)
}
</script>

<style scoped>
.footer {
    display: flex;
    /*width: 100%;*/
    font-size: 1.5rem;
    margin-top: 10px;
    margin-bottom: 20px;
    justify-content: center;
}

.clicked {
    background-color: #ecf5ff; /* Element UI按钮点击后的默认背景色 */
    color: #409EFF; /* 文字颜色 */
}
@media screen and (min-width: 1488px){
    .picture{
        width: 1488px;
        height: 350px;
        margin: 0 auto;
        background: url("../../../assets/3.png")  center;
        background-size: 100% 350px;
    }
    .dh{
        width: 1488px;
        display: flex;
        justify-content: flex-start;
        margin: 10px auto 0;
    }
    .home{
        /*border: 1px skyblue solid;*/
        font-size: 1.5rem;
        display: flex;
        flex-wrap: wrap;
        width: 1488px;
        margin: 0 auto;
        gap: 42.5px;
        justify-content: flex-start;
    }
}
@media screen and (min-width: 960px) and (max-width: 1488px) {
    .picture{
        width: 100%;
        height: 350px;
        margin: 0 auto;
        background: url("../../../assets/3.png")  center;
        background-size: 100% 350px;
    }
    .dh{
        width: 1488px;
        display: flex;
        justify-content: flex-start;
        margin: 10px auto 0 ;
    }
    .home{
        /*border: 1px skyblue solid;*/
        display: flex;
        flex-wrap: wrap;
        width: 100%;
        font-size: 1.5rem;
        /*margin: 0 auto;*/
        gap: 42.5px;
        justify-content: flex-start;
    }

}
@media screen and (max-width: 960px) and (min-width: 670px){
    .dh{
        width: 100%;
        display: flex;
        justify-content: space-evenly;
        margin: 10px auto 0;
    }
    .picture{
        width: 100%;
        height: 350px;
        background: url("../../../assets/4.png")  center;
        background-size: 100% 350px;
    }
    .home{
        font-size: 1.5rem;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-evenly;
    }
}
@media screen and (max-width: 670px){
    .dh{
        width: 100%;
        display: flex;
        justify-content: space-evenly;
        margin: 10px auto 0;
    }
    .picture{
        width: 100%;
        height: 350px;
        background: url("../../../assets/5.png")  center;
        background-size: 42rem 350px;
    }
    .home{
        font-size: 1.5rem;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-evenly;
    }
}

</style>

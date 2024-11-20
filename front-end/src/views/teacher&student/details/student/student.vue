<template>

    <div class="sm">
       <div class="tb">
           <el-link :underline="false" class="zys"  :class="{ 'select': a===1 }" @click="select(1)" >我报名的竞赛</el-link>
           <el-link :underline="false" class="zys" style="margin-left: 10px;" :class="{ 'select': a===2  }" @click="select(2)">竞赛成绩</el-link>
       </div>
       <div class="overFlow">
            <div v-if="a==1" style="height: 375px;margin-top: 3px;overflow: auto">
                        <div v-if="b.length==0">
                            <h1 style="color: #8c939d">{{notfound}}</h1>
                        </div>
                <div v-for="(item,index) in b" :key="index" style="margin-bottom: 15px;background-color: #f6f3f3">
                            <div><el-link @click="details(item.competitionId)" style="font-size: 21px" :underline="false">{{ item.cname}}</el-link></div>
                    <div style="margin-top: 8px;font-size: 18px;color: #8c939d">报名时间为: <span>{{time.formatDate(item.registrationTime)}}</span></div>
                    <div class="zp">
                        <el-button  @click="gmh(index)" style="background-color: #00BFFF;color: white">报名信息查看</el-button>
                        <el-button @click="dktj(index)" style="background-color: #00BFFF;color: white">作品提交</el-button>
                    </div>
                    <div  style="font-size: 18px;color: #409EFF;width: 400px;line-height: 30px" :hidden="item.hidden">
                        <div>
                            姓名: {{item.name}}
                        </div>
                        <div>
                            学校: {{item.school}}
                        </div>
                        <div>
                            院系: {{item.dep}}
                        </div>
                        <div>
                            电话: {{item.tel}}
                        </div>
                        <div>
                            邮箱: {{item.email}}
                        </div>
                    </div>
                </div>
                <el-dialog  v-model="dk" title="提交作品" :modal="false" z-index:999 :append-to-body="true">
                    <el-form :model="form">
                        <el-form-item label="竞赛名称:">
                            {{form.cname}}
                        </el-form-item>
                        <h3>报名信息</h3>
                        <div>
                            姓名: {{form.name}}
                        </div>
                        <div>
                            学校: {{form.school}}
                        </div>
                        <div>
                            院系: {{form.dep}}
                        </div>
                        <div>
                            电话: {{form.tel}}
                        </div>
                        <div>
                            邮箱: {{form.email}}
                        </div>
                        <el-form-item v-if="form.zpUrl==null" label="上传作品:" style="margin-top: 20px">
                            <el-upload
                                style="margin-top: 30px;margin-left: -71px"
                                v-model:file-list="fileList"
                                class="upload-demo"
                                :auto-upload="false"
                                :on-remove="handleRemove"
                                limit="1"
                                span="10"
                            >
                                <template #tip>
                                    <div class="el-upload__tip" style="">
                                        只能上传一个压缩包、pdf、word等文件
                                    </div>
                                </template>
                                <el-button type="success">选择上传的文件</el-button>
                            </el-upload>
                        </el-form-item>
                        <el-form-item v-else>
                            <div>
                                <span style="font-weight: 600;font-size: 16px">已上传文件:</span>
                                <el-link @click="downZp(form.zpUrl)">{{form.zpUrl}}</el-link>
                            </div>
                        </el-form-item>
                        <el-button  v-if="form.zpUrl==null" type="primary" @click="submit(form.competitionId)">提交</el-button>
                    </el-form>
                </el-dialog>
            </div>
            <div v-else style="height: 375px;margin-top: 3px;">
                <h1 v-if="c.length==0" style="color: #8c939d">{{notfound2}}</h1>
                <div v-for="(item,index) in c" :key="index" style="margin-bottom: 15px;background-color:#f6f3f3">
                    <div style="font-size: 21px">{{ item.competitionName}}</div>
                    <div style="margin-top: 8px;font-size: 18px;">
                        竞赛时间: <span>{{time.formatDate(item.competitionTime)}}</span>
                        竞赛成绩: <span>{{item.grade}}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup lang="ts">
import router from "../../../../router/router.js"
import time from "../../../../utils/time.js"
import {onMounted,ref, watch,reactive} from "vue";
import axios from "axios";
import { defineProps } from 'vue';
import axios2 from "../../../../axios/axios.js"
import {ElMessage, ElMessageBox, UploadProps} from "element-plus";

const props = defineProps({
    uid: {
        type: Number,
        required: true,
    },change:{
        type: Function
    }
});
interface bmInfo{
    zpUrl:string,
    name: string,
    school: string,
    dep: string,
    email: string,
    tel: string,
    competitionId: string,
    cname: string,
    registrationTime: string
    hidden: boolean
}
interface grade{
    competitionName: string
    competitionTime: string
    grade:string
}
const fileList=ref()
const dk=ref(false)
const b=ref<bmInfo[]>([])
const c=ref<grade[]>([])
const a=ref(1)
const notfound=ref()
const notfound2=ref()
const form =reactive({
    cname:'',
    dep:'',
    email:'',
    tel:'',
    name:'',
    school:'',
    competitionId:'',
    zpUrl:''
})
//.......................
const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {
    console.log(file, uploadFiles)
}
const submit=(zl)=>{
    if (zl.length==null)
        ElMessage.error("请上传文件在提交")
    const data=new FormData;
    data.append("file",fileList.value[0].raw)
    axios.post("/personal/zpUpload?cid="+zl+"&uid="+props.uid,data,{headers:axios2.accessHeader()})
        .then((res)=>{
            console.log(res)
            if (res.data.code===0)
            {
                dk.value=false
                ElMessage.success("上传成功")
                getInfo()
                fileList.value=[]
            }
            else
                ElMessage.error(res.data.message)
        })
}
const dktj=(index)=>{
    dk.value=true
    form.competitionId=b.value[index].competitionId
    form.cname=b.value[index].cname
    form.dep=b.value[index].dep
    form.tel=b.value[index].tel
    form.school=b.value[index].school
    form.name=b.value[index].name
    form.email=b.value[index].email
    form.zpUrl=b.value[index].zpUrl
}
const downZp=(zl)=>{
    axios({
        method: 'get',
        url: '/personal/zpDownload/'+zl,
        responseType: 'blob',
        headers:axios2.accessHeader()
    })
        .then((response) => {
            console.log(response)
            const url = URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', zl);
            document.body.appendChild(link);
            link.click();
        })
        .catch((error) => {
            console.error(error);
        });
}
const gmh=(index)=>{
    console.log(b.value[index].hidden)
     b.value[index].hidden=!b.value[index].hidden
}
const select=(zl)=>{
     a.value=zl
    if (a.value===1)
         getInfo()
    else
        getGrade()
}
//父传子的
const change=(z)=>{
    props.change(z)
}
const details=(id)=>{
    change('/detailPage') //跳转页面不显示下划线
    router.push('/detailPage?cid='+id)
}
const getInfo=()=>{
        axios.get("/personal/bmInfo?uid="+props.uid,{headers: axios2.accessHeader()})
            .then((res)=>{
                if (res.data.code===200)
                    b.value = res.data.data
                else
                    notfound.value=res.data.message
            })
}
const getGrade=()=>{
     axios.get("/personal/getGrade",{headers: axios2.accessHeader()})
         .then((res)=>{
             console.log(res)
             if (res.data.code===200)
                 c.value = res.data.data
             else
                 notfound2.value=res.data.message
         })
}
// 使用watch监听props.message的变化
watch(() => props.uid, (newVal, oldVal) => {
    if (newVal !== oldVal) {
        getInfo();
    }
});
// onMounted(()=>{
//     getInfo()
// })
</script>

<style scoped>
.sm{
    margin: 20px auto 0;
    display: flex;
    flex-wrap: wrap;
    /*width: 100vw;*/
    /*border: 1px solid red;*/
}
.select{
    border-bottom: 3px solid skyblue;
}
.zys{
    width: 150px;
    height: 50px;
    border-radius: 2px;
    background-color: white;
    font-size: 18px
}
/*@media screen and (min-width: 1470px){*/
/*    .tb{*/
/*        !*width: 89vw;*!*/
/*        width: 81.2vw;*/
/*        margin: 0 auto;*/
/*    }*/
/*    .sm{*/
/*        width: 1488px;*/
/*    }*/
/*    .zp{*/
/*        float: right;margin-top: -35px*/
/*    }*/
/*}*/
@media screen and (min-width: 960px){
    .overFlow{
        height: 48vh;width: 80vw;
        margin-top: 100px;
        margin-left: -310px;
    }
    .sm{
        width: 1488px;
    }
    .tb{
        /*width: 80.2vw;*/
        /*margin: 0 auto*/
        /*border: 1px skyblue solid;*/
        margin-left: 140px;
        margin-top: 35px;
    }

}
@media screen and (min-width: 750px) and (max-width: 960px){
    .overFlow{
        height: 48vh;width: 80vw;margin: 10px auto 0;
    }
    .tb{
        width: 80vw;
        margin: 0 auto
    }

}
@media screen and (max-width: 750px) {
    .overFlow{
        height: 48vh;width: 80vw;margin: 10px auto 0;
    }
    .tb{
        width: 82vw;
        margin: 0 auto
    }

}
</style>

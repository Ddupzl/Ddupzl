<template>
<!--竞赛审核-->
    <div>
      <div style="margin-top: 1px">
        <el-input v-model="search" placeholder="姓名搜索 " style="width: 180px;"/>
        <el-input v-model="search2" placeholder="电话搜索 " style="width: 180px;"/>
        <el-button style="margin-left: 20px" @click="handleClick" >搜索</el-button>
    </div>
    <el-table  :data="tableData" stripe style="width: 100%;height: 540px" >
        <el-table-column prop="applicationTime" label="竞赛申请时间" width="180">
            <template #default="scope">
                <div>
                    {{time.formatDate(scope.row.applicationTime)}}
                </div>
            </template>
        </el-table-column>
        <el-table-column  prop="competitionId" label="竞赛id" width="150" />
        <el-table-column prop="url" label="竞赛图片" width="280">
            <template  #default="scope">
                <div style="border: 1px solid rgb(153,153,153);width: 260px;height:140px;">
                    <img
                            style="cursor: pointer;width: 100%;height: 100%;"
                            :src="imageSrc+scope.row.url"
                    >
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="competitionName" label="竞赛名称" width="180"/>
        <el-table-column prop="sponsor" label="主办方" width="180"/>
        <el-table-column prop="subject" label="竞赛分类" width="180"/>
        <el-table-column prop="competitionTime" label="竞赛时间" width="180">
            <template #default="scope">
                <div>
                    {{time.formatDate(scope.row.competitionTime)}}
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="registrationTimeStart" label="竞赛报名开始时间" width="180">
            <template #default="scope">
                <div>
                    {{time.formatDate(scope.row.registrationTimeStart)}}
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="registrationTimeEnd" label="竞赛报名结束时间" width="180">
            <template #default="scope">
                <div>
                    {{time.formatDate(scope.row.registrationTimeEnd)}}
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="location" label="竞赛地址" width="180"/>
        <el-table-column prop="description" label="竞赛描述" width="280">
            <template #default="scope">
                <div class="cell-ellipsis">
                    {{ scope.row.description }}
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="count" label="竞赛人数" width="180"/>
        <el-table-column fixed="right" label="操作" width="150">
            <template #default="scope">
                <el-button text @click="dialogFormVisible(tableData[scope.$index])">审核</el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-dialog v-model="dialogFormVisible1" title="竞赛信息审核" :modal="false" z-index:999 :append-to-body="true" >
        <el-form ref="ruleFormRef" :model="form"  label-width="120px"  status-icon >
            <el-form-item label="竞赛id:">
                <div>{{form.competitionId}}</div>
            </el-form-item>
            <el-form-item label="竞赛申请时间:">
                {{time.formatDate(form.applicationTime)}}
            </el-form-item>
            <el-form-item label="竞赛图片">
                <div class="account-avatar">
                    <img v-if="imageUrl" :src="imageUrl" class="avatart" />
                </div>
            </el-form-item>
            <el-form-item  label="竞赛名称:">
                <div>{{form.competitionName}}</div>
            </el-form-item>
            <el-form-item label="主办方:">
                <div>{{form.sponsor}}</div>
            </el-form-item>
            <el-form-item label="竞赛类别:">
                <div>{{form.subject}}</div>
            </el-form-item>
            <el-form-item label="竞赛时间:">
                {{time.formatDate(form.competitionTime)}}
            </el-form-item>
            <el-form-item label="报名时间:">
                {{time.formatDate(form.registrationTimeStart)}} - {{time.formatDate(form.registrationTimeEnd)}}
            </el-form-item>
            <el-form-item label="竞赛地址:">
                <div>{{form.location}}</div>
            </el-form-item>
            <el-form-item label="竞赛描述:">
                <el-input disabled resize="none" :rows="4" type="textarea"  v-model="form.description" />
            </el-form-item>
            <el-form-item label="竞赛人数:">
                <div>{{form.count}}</div>
            </el-form-item>
            <el-form-item label="审核意见:">
                <el-input v-model="sh.suggest" resize="none" :rows="4" type="textarea"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="yes">
                    通过
                </el-button>
                <el-button type="danger" @click="no">
                    不通过
                </el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <div style="display: flex; justify-content: center; align-items: center;height: 80px;">
        <el-pagination
                layout="prev, pager, next,jumper"
                prev-text="上一页"
                next-text="下一页"
                big background   v-model:current-page="currentPage" :total="totalCount" :page-size=7  @current-change="getInfo"/>
        <span style="margin-left: 10px;color: #606266">共{{totalCount}}条记录</span>
    </div>

  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import axios2 from "../../axios/axios.js";
import {ElMessage, FormInstance} from "element-plus";
import time from "../../utils/time.js"

const dialogFormVisible1=ref(false)
const imageSrc=ref("http://localhost:8888/info/file/download/")
const search=ref()
const search2=ref()
const tableData=ref([])
const totalCount=ref()
const currentPage=ref(1)
const ruleFormRef = ref<FormInstance>()
const form =reactive({
    competitionId:'',
    url:'',
    competitionName:'',
    sponsor:'',
    subject:'',

    publishTime:'',
    competitionTime:'',
    registrationTimeStart:'',
    registrationTimeEnd:'',
    location:'',
    isHot:'',
    status:'',
    awardTime:'',
    description:'',
    count:'',
    applicationTime:''
})
const sh =reactive({
    approvverId:'',
    result:'',
    suggest:''
})
//....
const imageUrl=ref()
const handleClick=()=>{
    console.log("搜索7")
}
const uid=ref()
const getUid=()=>{
    axios2.getUserInfo((res)=>{
        uid.value=res.data.uid
    })
}
onMounted(()=>{
    getUid()
})
const yes=()=>{
    sh.result=1
    sh.approvverId=uid.value
    axios.post("/admin/sh?c="+form.competitionId,sh,{headers: axios2.accessHeader()})
        .then((res)=>{
            console.log(res)
            if (res.data.code===200)
            {
                dialogFormVisible1.value=false
                ElMessage.success("提交审核成功")
                getInfo()
            }
            else
                ElMessage.error(res.data.message)
        })
        .catch((e)=>{
            console.log(e)
            ElMessage.error("提交审核信息失败")
        })
}
const no=()=>{
    sh.result=0
    sh.approvverId=uid.value
    axios.post("/admin/sh?c="+form.competitionId,sh,{headers: axios2.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
            {
                dialogFormVisible1.value=false
                ElMessage.success("提交审核成功")
            }
        })
}
const getInfo=()=>{
    axios.get("/admin/shCp?currentPage="+currentPage.value,{headers:axios2.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
            {
                tableData.value=res.data.data.list
                totalCount.value=res.data.data.totalCount
            }
            else
                ElMessage.warning(res.data.message)
        })
        .catch((e)=>{
            console.log(e)
        })
}
const  dialogFormVisible= (info) => {
    dialogFormVisible1.value = true//弹出对话框
    form.competitionId=info.competitionId
    form.url=info.url
    imageUrl.value=imageSrc.value+form.url

    form.competitionName=info.competitionName
    form.sponsor=info.sponsor
    form.subject=info.subject
    form.competitionTime=info.competitionTime
    form.registrationTimeStart=info.registrationTimeStart
    form.registrationTimeEnd=info.registrationTimeEnd
    form.location=info.location
    form.description=info.description
    form.count=info.count
    form.applicationTime=info.applicationTime
}
onMounted(()=>{
    getInfo()
})
</script>

<style scoped>
.cell-ellipsis {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 5;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 20px;
    height: 100px;
    word-wrap: break-word;
    /*overflow-wrap: break-word;*/
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}
.avatar-uploader .el-upload:hover {
    border-color: #409eff;
}
.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 260px;
    height: 140px;
    line-height: 178px;
    text-align: center;
}
.avatart {
    display: block;
    cursor: pointer;
    width: 260px;
    height:140px;
    border: 1px gray solid;
}
</style>

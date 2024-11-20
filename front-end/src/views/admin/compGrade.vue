<template>
  <!--竞赛成绩管理-->
    <div>
        <div style="margin-top: 1px">
            <el-input v-model="search" placeholder="竞赛名称搜索 " style="width: 180px;"/>
            <el-button style="margin-left: 20px" @click="handleClick" >搜索</el-button>
            <el-button style="margin-left: 20px" @click="exportGrade(cp.competitionId,cp.competitionName)" >竞赛成绩导出 &nbsp;<el-icon><Download /></el-icon></el-button>
        </div>
        <div>
            <div style="float: left;margin-top: 5px;color:#606266">竞赛id: {{cp.competitionId}}&nbsp;&nbsp;&nbsp;&nbsp; 竞赛名称: {{cp.competitionName}}&nbsp;&nbsp;&nbsp;&nbsp;竞赛分类: {{cp.subject}}
                <br>竞赛时间: {{time.formatDate(cp.competitionTime)}}&nbsp;&nbsp;&nbsp;&nbsp;成绩公布时间: {{time.formatDate(cp.awardTime)}}</div>
        </div>

        <el-table  :data="tableData" stripe style="width: 100%;height: 490px" >
            <el-table-column  prop="uid" label="用户id" width="180" />
            <el-table-column prop="name" label="学生姓名" width="180"/>
            <el-table-column prop="grade" label="成绩" width="180"/>
            <el-table-column prop="zpUrl" label="作品下载" width="300">
                <template  #default="scope">
                    <div>
                        <el-link @click="downZp(scope.row.zpUrl)">{{scope.row.zpUrl}}</el-link>
                    </div>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="200">
                <template #default="scope">
                    <el-popconfirm
                            confirm-button-text="确定"
                            cancel-button-text="取消"
                            icon-color="#626AEF"
                            title="你确定要删除该学生成绩吗"
                            @confirm="confirmEvent(tableData[scope.$index])"
                            @cancel="cancelEvent"
                    >
                        <template #reference>
                            <el-button link>删除</el-button>
                        </template>
                    </el-popconfirm>
                    <el-button text @click="dialogFormVisible(tableData[scope.$index])">修改成绩</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogFormVisible1" title="成绩修改" :modal="false" z-index:999 :append-to-body="true" >
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px"  status-icon >
                <el-form-item  label="用户id:">
                    <div>{{form.uid}}</div>
                </el-form-item>
                <el-form-item label="学生姓名:">
                    <div>{{form.name}}</div>
                </el-form-item>
                <el-form-item label="成绩:"prop="grade">
                    <el-input v-model="form.grade" style="width: 100px" />
                </el-form-item>
                <el-form-item label="作品下载:">
                    <el-icon><Download /></el-icon>
                    <el-link @click="downZp(form.zpUrl)">{{form.zpUrl}}</el-link>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submit()">
                        提交修改
                    </el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <div style="display: flex; justify-content: center; align-items: center;height: 80px;">
            <el-pagination
                    layout="prev, pager, next,jumper"
                    prev-text="上一页"
                    next-text="下一页"
                    big background   v-model:current-page="currentPage" :total="totalCount" :page-size=size  @current-change="getCompInfo"/>
            <span style="margin-left: 10px;color: #606266">共{{totalCount}}条记录</span>
        </div>

    </div>
</template>

<script lang="ts" setup>
import {Download} from "@element-plus/icons";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import axios2 from "../../axios/axios.js";
import {ElMessage, FormInstance} from "element-plus";
import time from "../../utils/time.js"
const cancelEvent=()=>console.log("取消")
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
//删除该学生成绩
const confirmEvent=(zl)=>{
    axios.get("/admin/deleteGrade?cid="+cp.competitionId+"&uid="+zl.uid,{headers:axios2.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
            {
                ElMessage.success("删除成功")
                getCompInfo()
            }
            else
                ElMessage.error(res.data.message)
        })
        .catch((e)=>{
            console.log(e)
        })
}
const exportGrade=async (id,name)=>{
    try {
        const response = await axios({
            method: 'get',
            url: '/admin/export-results?id='+id,
            responseType: 'blob',
            headers: axios2.accessHeader()
        });
        const url = URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', name+'竞赛成绩.xlsx');
        // 创建一个不可见的容器来放置a标签
        const tempLinkContainer = document.createElement('div');
        tempLinkContainer.style.display = 'none';
        document.body.appendChild(tempLinkContainer);
        tempLinkContainer.appendChild(link);
        link.click();
        // 清理创建的a标签和容器
        document.body.removeChild(tempLinkContainer);

        // 释放URL对象
        URL.revokeObjectURL(url);

    } catch (error) {
        console.error('下载失败:', error);
    }
}

const dialogFormVisible1=ref(false)
const search=ref()
const size=ref()
const tableData=ref([])
const totalCount=ref()
const currentPage=ref(1)
const ruleFormRef = ref<FormInstance>()
const form =reactive({
    uid:'',
    name:'',
    grade:'',
    zpUrl:''
})
const cp =reactive({
     competitionId:'',
     competitionName:'',
     subject:"",
     competitionTime:'',
     awardTime:''
})
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
const submit=()=>{
    if (!ruleFormRef.value) return;
    ruleFormRef.value.validate((valid) => {
        if (valid) {
            axios.post("/admin/updateGrade?cid="+cp.competitionId,form,{headers:axios2.accessHeader()})
                .then((res)=>{
                    console.log(res)
                    if (res.data.code===200)
                    {
                        ElMessage.success("修改成功")
                        dialogFormVisible1.value=false
                        getCompInfo()
                    }
                    else
                        ElMessage.error(res.data.message)
                })
                .catch((e)=>console.log(e))
        }
        else {
            ElMessage.error("请按要求填写完整表单")
            console.log('请填写完整表单!');
        }
    })
}
//获取竞赛信息
const getCompInfo=()=>{
    axios.get("/admin/selectGradeComp?currentPage="+currentPage.value,{headers:axios2.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
            {
                cp.competitionId =res.data.data.list[0].competitionId
                cp.subject=res.data.data.list[0].subject
                cp.awardTime=res.data.data.list[0].awardTime
                cp.competitionName=res.data.data.list[0].competitionName
                cp.competitionTime=res.data.data.list[0].competitionTime
                totalCount.value=res.data.data.totalCount
                size.value=res.data.data.size
                //获取对应竞赛的学生成绩
                    axios.get("/admin/selectGrade?cid="+cp.competitionId,{headers:axios2.accessHeader()})
                        .then((res)=>{
                            console.log(res)
                            if (res.data.code===200)
                                tableData.value=res.data.data
                            else
                            {
                                tableData.value=null
                                ElMessage.error(res.data.message)
                            }
                        })
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
    form.uid=info.uid
    form.name=info.name
    form.grade=info.grade
    form.zpUrl=info.zpUrl
}
const rules = reactive({
    grade:[
        {required:true,message:'请输入成绩', trigger: 'blur'},
        {
            validator: (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入成绩'));
                } else if (!Number.isInteger(Number(value)) || Number(value) < 0 || Number(value) > 100) {
                    callback(new Error('成绩必须在0-100之间'));
                } else {
                    callback();
                }
            },
            trigger: 'change'
        }
    ]
})
onMounted(()=>{
    getCompInfo()
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

<template>
    <div>
        <div>
            <el-link :underline="false"  class="zys2" style="margin-left: 20px;" :class="{ 'select': a===1 }" @click="select(1)">竞赛申请</el-link>
            <el-link :underline="false" class="zys2" style="margin-left: 10px;" :class="{ 'select': a===2 }" @click="select(2)">竞赛审核情况</el-link>
        </div>
        <div style="width: 1100px;height: 373px;margin: 0 auto;background-color: white">
            <div>
                <div v-if="a===1" style="height: 375px;margin-top: 5px;overflow: auto">
                    <div>
                        <el-form
                            ref="ruleFormRef"
                            style="max-width: 600px"
                            :model="ruleForm"
                            :rules="rules"
                            label-width="auto"
                            class="demo-ruleForm"
                            :size="formSize"
                            status-icon
                        >
                            <el-form-item label="竞赛名称" prop="competitionName">
                                <el-input v-model="ruleForm.competitionName" />
                            </el-form-item>
                            <el-form-item label="主办方" prop="organizerName">
                                <el-input v-model="ruleForm.organizerName" />
                            </el-form-item>
                            <el-form-item label="竞赛类别" prop="categoryName">
                                <div>
                                    <el-select v-model="selectedBigType" placeholder="请选择竞赛大类" style="width: 200px">
                                        <el-option
                                                v-for="(item, index) in subjectInfo"
                                                :key="index"
                                                :label="item.bigType"
                                                :value="item.bigType"
                                        />
                                    </el-select>
                                    <el-select  v-model="ruleForm.categoryName" placeholder="请选择竞赛小类" style="width: 200px">
                                        <el-option
                                                v-for="(smallType, index) in smallTypes"
                                                :key="index"
                                                :label="smallType"
                                                :value="smallType"
                                        />
                                    </el-select>
                                </div>

                            </el-form-item>
                            <el-form-item label="竞赛地点" prop="location">
                                <el-input v-model="ruleForm.location" />
                            </el-form-item>
                            <el-form-item label="竞赛人数" prop="count">
                                <el-select-v2
                                    v-model="ruleForm.count"
                                    placeholder="人数"
                                    :options="option"
                                />
                            </el-form-item>
                            <el-form-item label="竞赛时间" prop="competitionTime">
                                    <el-date-picker
                                        v-model="ruleForm.competitionTime"
                                        type="datetime"
                                        placeholder="请选择日期时间"
                                        format="YYYY-MM-DD HH:mm:ss"
                                        date-format="MMM DD, YYYY"
                                        time-format="HH:mm"
                                    />
                            </el-form-item>
                            <el-form-item label="报名时间" required>
                                    <el-form-item prop="registrationTime">
                                        <el-date-picker
                                            v-model="ruleForm.registrationTime"
                                            type="datetimerange"
                                            start-placeholder="Start date"
                                            end-placeholder="End date"
                                            format="YYYY-MM-DD HH:mm:ss"
                                            date-format="YYYY/MM/DD ddd"
                                            time-format="A hh:mm:ss"
                                        />
                                    </el-form-item>
                            </el-form-item>
                            <el-form-item label="竞赛图片" prop="url">
                                    <div class="account-avatar">
                                        <el-upload
                                            :auto-upload="false"
                                            class="avatar-uploader"
                                            :show-file-list="false"
                                            :before-upload="beforeAvatarUpload"
                                            @change="handleFileChange"
                                        >
                                            <img v-if="imageUrl" :src="imageUrl" class="avatart" />
                                            <i v-else class="el-icon-plus avatar-uploader-icon">
                                                <el-icon><Plus /></el-icon>
                                            </i>
                                        </el-upload>
                                    </div>
                            </el-form-item>

                            <el-form-item label="竞赛描述" prop="description">
                                <el-input resize="none" v-model="ruleForm.description" type="textarea" />
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="submitForm(ruleFormRef)">
                                    提交
                                </el-button>
                                <el-button @click="resetForm(ruleFormRef)">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
                <div v-else style="height: 375px;margin-top: 5px;overflow: auto">
                    <div v-if="b.length==0">
                        <h1 style="color: #8c939d">{{notfound}}</h1>
                    </div>
                    <div v-for="(item,index) in b" :key="index" style="margin-bottom: 15px;border: 2px silver solid;border-radius: 1px">
                        <div style="font-size: 18px;color: #8c939d">
                            <div> 竞赛id:{{item.competitionId}} &nbsp;&nbsp;&nbsp;竞赛名称:{{item.competitionName}}</div>
                           <div>
                               申请id:{{item.applicationId}}&nbsp;&nbsp;&nbsp;
                               申请时间为:<span>{{time.formatDate(item.applicationTime)}}</span>
                           </div>
                            <div style="float: right;margin-top: -10px">
                                <el-button  @click="ck(index)" style="background-color: #00BFFF;color: white">审核详情查看</el-button>
                            </div>
                            <div>审核结果: &nbsp;&nbsp;
                            <span v-if="item.result=='1'">审核通过</span>
                            <span v-else-if="item.result=='0'">审核未通过</span>
                            <span v-else>正在审核中</span>
                            </div>
                        </div>
                    </div>
                        <el-dialog  v-model="dk" title="审核详情信息" :modal="false" z-index:999 :append-to-body="true">
                            <el-form :model="form">
                                <h4>竞赛名称:{{form.competitionName}}</h4>
                                   <h4>竞赛申请时间: {{time.formatDate(form.applicationTime)}} </h4>
                                    <h4>申请id: {{form.applicationId}}</h4>
                                <h4>
                                审核结果:
                                <span v-if="form.result=='1'">审核通过</span>
                                <span v-else-if="form.result=='0'">审核未通过</span>
                                <span v-else>正在审核中</span>
                                 </h4>
                                <h4>审核人id: {{form.approvverId}}</h4>
                                <h4>审核建议: {{form.suggest}}</h4>
                            </el-form>
                        </el-dialog>

                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import time from "../../../../utils/time.js"
interface shInfo{
    applicationId: string;
    competitionId: string;
    applicationTime: string;
    approvverId: string;
    result: string
    approvalTime: string
    suggest: string;
    competitionName: string
}
const ck=(index)=>{
        dk.value=true
        form.competitionName=b.value[index].competitionName
        form.competitionId=b.value[index].competitionId
        form.approvalTime=b.value[index].approvalTime
        form.suggest=b.value[index].suggest
        form.approvverId=b.value[index].approvverId
        form.applicationId=b.value[index].applicationId
        form.applicationTime=b.value[index].applicationTime
        form.result=b.value[index].result
}
const dk=ref(false)
const b=ref<shInfo[]>([])
const notfound=ref("查询不到审核记录")
const form=reactive<shInfo>({})
const shSelect=()=>{
    console.log("ssssssss")
    axios2.get("/personal/shSelect",{headers:axios.accessHeader()})
        .then((res)=>{
            console.log(res)
            if (res.data.code===200)
                b.value=res.data.data
        })
}
//.........
import {computed, onMounted, reactive, ref, watch} from 'vue'
import type {ComponentSize, FormInstance, FormRules, UploadProps} from 'element-plus'
import {ElMessage} from "element-plus";
import axios  from "../../../../axios/axios.js";
import axios2 from "axios";
import 'vue-cropper/dist/index.css'
import {Plus} from "@element-plus/icons";

const imageUrl=ref()
const zlData=ref()
// 选择文件后图片显示在前端
const handleFileChange = (file: any) => {
    if (file.raw) {
        zlData.value=file.raw  //把图片数据传给变量
        const reader = new FileReader();
        reader.readAsDataURL(file.raw);
        reader.onload = (e: any) => {
            imageUrl.value= e.target.result; // 读取完成后更新图片 URL
        };
    }
};

const beforeAvatarUpload = (file: any) => {
    const isJPG = file.type === 'image/jpeg';
    const isPNG = file.type === 'image/png';
    const isLt2M = file.size / 1024 / 1024 < 2;

    if (!isJPG && !isPNG) {
        ElMessage.error('上传头像图片只能是 JPG或PNG 格式!');
        return false;
    }
    if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!');
        return false;
    }
    return true;
};
//.....................................
interface SubjectItem{
    bigType:string;
    smallType:string[];
}
const subjectInfo = ref<SubjectItem[]>([])
const selectedBigType = ref('');
const smallTypes = computed(() => {
    const selected = subjectInfo.value.find(item => item.bigType === selectedBigType.value);
    return selected ? selected.smallType : [];
});
// 监听 selectedBigType 的变化，当变化时重置 selectedSmallType
watch(selectedBigType, (newValue, oldValue) => {
    if (newValue !== oldValue) {
        ruleForm.categoryName = ''; // 重置 selectedSmallType
    }
});

const xz=()=>{
    axios2.get("/info/getSubType").then((res)=>{
        console.log(res)
        subjectInfo.value=res.data
    })
}
onMounted(()=>{
    xz()
})
//id不填自己生成(可以返回生成),竞赛名称,分类(从后端拿？),组织者(从后端拿，然后选),发布时间先不填(等审核过后填)
//竞赛时间，报名开始和结束，地点（线下），状态，热门,货架时间，图片路径，竞赛描述，人数。
interface RuleForm {
    competitionName: string
    categoryName: string  //分类
    organizerName: string  //组织者
    competitionTime: string
    registrationTimeStart: string
    registrationTimeEnd: string
    location: string
    url: string
    description: string
    count: string
    registrationTime:[]
}

// const registrationTime=ref([])
const formSize = ref<ComponentSize>('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    competitionName: '',
    categoryName:'',
    organizerName:'',
    competitionTime:'',
    registrationTimeStart: '',
    registrationTimeEnd:'',
    location: '',
    url2:'',
    description: '',
    count: '',
    registrationTime:[]
    // selectedBigType:''
})

const rules = reactive<FormRules<RuleForm>>({
    registrationTime:[
        {
            required: true,
            message: '请选择报名开始和截止时间',
            trigger: ['blur','change']
        },
    ],
    competitionName: [
        { required: true, message: '请输入竞赛名称', trigger:['blur','change']},
        { required:true, min: 1, max: 35, message: '字符1到35之间', trigger:['blur','change'] },
    ],
    categoryName: [
        {required: true, message: '请输入竞赛类别', trigger: ['blur','change']},
    ],
    organizerName: [
        {required: true, message: '请输入主办方', trigger: ['blur','change']},
    ],
    count: [
        {
            required: true,
            message: '请选择竞赛人数',
            trigger: 'change',
        },
    ],
    competitionTime:[
        {type:'date',required:true,message:"请输入竞赛时间",trigger: ['blur','change']}
    ],
    url:[{required: true,message:"请上传竞赛图片",trigger:'change'}],
    location: [
        {
            required: true,
            message: '请输入竞赛地址',
            trigger: 'change',
        },
    ],
    description: [
        { required: true, message: '请输入对竞赛内容的描述', trigger: 'blur' },
    ],
})
//竞赛提交
const submitForm = async (formEl: FormInstance | undefined) => {
    if (imageUrl.value!=null)
        ruleForm.url=1
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (!valid) {
            return ElMessage.error("提交失败,请填写完信息", fields)
        }
        const formData = new FormData();
        formData.append("image",zlData.value)
        axios2({
            method: 'post',
            url: '/competition/comp/upload',
            data:formData,
            headers:axios.accessHeader()
        }).then((res)=> {
            console.log("ssssssssssss")
            console.log(res.data.data.url)
            if (res.data.code === 0)
            {
                ruleForm.url2=res.data.data.url
                console.log(ruleForm.url2)
                ruleForm.registrationTimeStart=ruleForm.registrationTime[0]
                ruleForm.registrationTimeEnd=ruleForm.registrationTime[1]
                axios2.post("/competition/insertComp",ruleForm,{headers:axios.accessHeader()})
                    .then((res)=>{
                        console.log(res)
                        if (res.data.code==200)
                        {
                            ElMessage.success("竞赛申请成功，耐心等待管理员审核")
                            resetForm(ruleFormRef.value)
                        }
                        else
                            ElMessage.error("提交过程中出了点错误,请稍后再试")
                    })
            }
            else
                ElMessage.error("上传图片失败")
        })

    })
}
const resetForm = (formEl: FormInstance | undefined) => {
    imageUrl.value=''
    selectedBigType.value=''
    if (!formEl) return
    formEl.resetFields()
}

const option = Array.from({ length: 100 }).map((_, idx) => ({
    value: `${idx + 1}`,
    label: `${idx + 1}`,
}))
//.....................................................
const a=ref(1)
const select=(zl)=>{
    a.value=zl
    if (a.value===1)
        console.log("竞赛申请")
    else
        shSelect()
}

</script>

<style scoped>
.select{
    border-bottom: 3px solid skyblue;
}
.zys2{
    margin-top: 40px;
    width: 150px;
    height: 50px;
    border-radius: 2px;
    background-color: white;
    font-size: 18px
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
    width: 282px;
    height: 190px;
    line-height: 178px;
    text-align: center;
}
.avatart {
    width: 282px;
    height: 190px;
    display: block;
}
</style>

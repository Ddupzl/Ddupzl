<template>
    <div>
        <!--     竞赛息查询-->
        <div style="margin-top: 1px">
            <el-input v-model="search" placeholder="姓名搜索 " style="width: 180px;"/>
            <el-input v-model="search2" placeholder="电话搜索 " style="width: 180px;"/>
            <el-button style="margin-left: 20px" @click="handleClick" >搜索</el-button>
            <el-button style="margin-left: 20px" @click="tjjs" ><el-icon><Plus /></el-icon>&nbsp;竞赛</el-button>
            <el-dialog v-model="dk" title="添加竞赛" :modal="false" z-index:999 :append-to-body="true" >
                <div>
                    <el-form
                        ref="ruleFormRef2"
                        style="max-width: 600px"
                        :model="ruleForm"
                        :rules="rules2"
                        label-width="auto"
                        class="demo-ruleForm"
                        :size="formSize"
                        status-icon
                    >
                        <el-form-item label="竞赛名称" prop="competitionName">
                            <el-input v-model="ruleForm.competitionName" />
                        </el-form-item>
                        <el-form-item label="报名时间" prop="registrationTime">
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
                        <el-form-item label="主办方" prop="organizerName">
                            <el-input v-model="ruleForm.organizerName" />
                        </el-form-item>
                        <el-form-item label="竞赛类别" prop="categoryName">
                            <div>
                                <el-select v-model="selectedBigType2" placeholder="请选择竞赛大类" style="width: 200px">
                                    <el-option
                                        v-for="(item, index) in subjectInfo2"
                                        :key="index"
                                        :label="item.bigType"
                                        :value="item.bigType"
                                    />
                                </el-select>
                                <el-select  v-model="ruleForm.categoryName" placeholder="请选择竞赛小类" style="width: 200px">
                                    <el-option
                                        v-for="(smallType, index) in smallTypes2"
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
                        <el-form-item label="竞赛图片" prop="url2">
                            <div class="account-avatar">
                                <el-upload
                                    :auto-upload="false"
                                    class="avatar-uploader"
                                    :show-file-list="false"
                                    :before-upload="beforeAvatarUpload2"
                                    @change="handleFileChange2"
                                    style="border: 1px slategrey solid;width: 260px;height:140px;"
                                >
                                    <img v-if="imageUrl2" :src="imageUrl2" class="avatart"/>
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
                            <el-button type="primary" @click="submitForm2(ruleFormRef2)">
                                提交
                            </el-button>
                            <el-button @click="resetForm(ruleFormRef2)">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-dialog>
        </div>
        <el-table  :data="tableData" stripe style="width: 100%;height: 540px" >
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
            <el-table-column prop="publishTime" label="竞赛发布时间" width="180">
                <template #default="scope">
                    <div>
                        {{time.formatDate(scope.row.publishTime)}}
                    </div>
                </template>
            </el-table-column>
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
            <el-table-column prop="isHot" label="是否热门" width="180">
                <template #default="scope">
                    <div v-if="scope.row.isHot==1">
                        热门
                    </div>
                    <div v-else>
                        否
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="是否发布" width="180">
                <template #default="scope">
                    <div v-if="scope.row.status==1">
                        已发布
                    </div>
                    <div v-else>
                        未发布
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="awardTime" label="成绩公布时间" width="180"/>
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
                    <el-popconfirm
                            confirm-button-text="确定"
                            cancel-button-text="取消"
                            icon-color="#626AEF"
                            title="你确定要删除该竞赛吗"
                            @confirm="confirmEvent(tableData[scope.$index])"
                            @cancel="cancelEvent"
                    >
                        <template #reference>
                            <el-button link>删除</el-button>
                        </template>
                    </el-popconfirm>
                    <el-button text @click="dialogFormVisible1(tableData[scope.$index])">修改</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogFormVisible" title="竞赛信息修改" :modal="false" z-index:999 :append-to-body="true" >
                <el-form ref="ruleFormRef" :model="form" :rules="rules2" label-width="120px"  status-icon >
                    <el-form-item label="竞赛id">
                        <el-input v-model="form.competitionId" disabled  style="width: 180px" />
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
                    <el-form-item  label="竞赛名称" prop="competitionName">
                        <el-input resize="none" :rows="3" type="textarea"  v-model="form.competitionName" style="width: 200px;" />
                    </el-form-item>
                    <el-form-item label="主办方" prop="sponsor">
                        <el-input v-model="form.sponsor" style="width: 350px" />
                    </el-form-item>
                    <el-form-item label="竞赛类别" prop="subject">
                        <div>
                            <el-select v-model="selectedBigType" placeholder="请选择竞赛大类" style="width: 200px">
                                <el-option
                                    v-for="(item, index) in subjectInfo"
                                    :key="index"
                                    :label="item.bigType"
                                    :value="item.bigType"
                                />
                            </el-select>
                            <el-select  v-model="form.subject" placeholder="请选择竞赛小类" style="width: 200px">
                                <el-option
                                    v-for="(smallType, index) in smallTypes"
                                    :key="index"
                                    :label="smallType"
                                    :value="smallType"
                                />
                            </el-select>
                        </div>
                    </el-form-item>

                    <el-form-item label="竞赛发布时间" prop="publishTime">
                        <el-date-picker
                            v-model="form.publishTime"
                            type="datetime"
                            placeholder="请选择日期时间"
                            format="YYYY-MM-DD HH:mm:ss"
                            date-format="MMM DD, YYYY"
                            time-format="HH:mm"
                        />
                    </el-form-item>
                    <el-form-item label="竞赛时间" prop="competitionTime">
                        <el-date-picker
                            v-model="form.competitionTime"
                            type="datetime"
                            placeholder="请选择日期时间"
                            format="YYYY-MM-DD HH:mm:ss"
                            date-format="MMM DD, YYYY"
                            time-format="HH:mm"
                        />
                    </el-form-item>
                    <el-form-item label="报名时间" prop="registrationTimeStart">
                        <el-date-picker
                            v-model="form.registrationTimeStart"
                            type="datetime"
                            placeholder="请选择日期时间"
                            format="YYYY-MM-DD HH:mm:ss"
                            date-format="MMM DD, YYYY"
                            time-format="HH:mm"
                        />
                    </el-form-item>
                    <el-form-item label="-" prop="registrationTimeEnd" style="margin-top: -50px;margin-left: 250px">
                        <el-date-picker
                            v-model="form.registrationTimeEnd"
                            type="datetime"
                            placeholder="请选择日期时间"
                            format="YYYY-MM-DD HH:mm:ss"
                            date-format="MMM DD, YYYY"
                            time-format="HH:mm"
                        />
                    </el-form-item>
                    <el-form-item label="竞赛地址" prop="location">
                        <el-input v-model="form.location" />
                    </el-form-item>
                    <el-form-item label="是否热门" prop="isHot">
                        <el-input v-model="form.isHot" style="width: 100px"/>
                    </el-form-item>
                    <el-form-item label="是否发布" prop="status">
                        <el-input v-model="form.status" style="width: 100px"/>
                    </el-form-item>
                    <el-form-item label="成绩公布时间" prop="awardTime">
                        <el-date-picker
                            v-model="form.awardTime"
                            type="datetime"
                            placeholder="请选择日期时间"
                            format="YYYY-MM-DD HH:mm:ss"
                            date-format="MMM DD, YYYY"
                            time-format="HH:mm"
                        />
                    </el-form-item>
                    <el-form-item label="竞赛描述" prop="description">
                        <el-input resize="none" :rows="4" type="textarea"  v-model="form.description" />
                    </el-form-item>
                    <el-form-item label="竞赛人数" prop="count">
                        <el-select-v2
                            v-model="form.count"
                            placeholder="选择人数"
                            :options="option"
                            style="width:200px"
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm()">
                            确认修改
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
import type {ComponentSize, FormRules} from 'element-plus'

const imageUrl2=ref()
const dk=ref(false)
interface SubjectItem{
    bigType:string;
    smallType:string[];
}
const tjjs = () => {
    dk.value=true
    resetForm(ruleFormRef2.value)
}
const zlData2=ref()
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
    url2: string
    description: string
    count: string
    registrationTime:[]
}
const formSize = ref<ComponentSize>('default')
const ruleFormRef2 = ref<FormInstance>()
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

const rules2 = reactive<FormRules<RuleForm>>({
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
    url:[{required: true,message:"请上传竞赛图片11111111",trigger:'change'}],
    url2:[{required: true,message:"请上传竞赛图片",trigger:'change'}],
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
    isHot: [
        { required: true, message: '不能为空', trigger: 'blur' },
    ],
    status: [
        { required: true, message: '不能为空', trigger: 'blur' },
    ],
    registrationTimeEnd: { required: true, message: '不能为空', trigger: 'blur' },
    // awardTime: { required: true, message: '不能为空', trigger: 'blur' },
    // publishTime: { required: true, message: '不能为空', trigger: 'blur' },
    subject: { required: true, message: '不能为空', trigger: 'blur' },
    sponsor: { required: true, message: '不能为空', trigger: 'blur' },
})
//竞赛提交
const submitForm2 = async () => {
    axios.get("/admin/selectBigType2",{headers: axios2.accessHeader()})
        .then((res)=>{
            if(res.data.code===200)
                selectedBigType2.value=res.data.data
        })
        .catch((e)=>console.log(e))
    if (imageUrl2.value!=null)
        ruleForm.url2=1
    if (!ruleFormRef2.value) return
    await ruleFormRef2.value.validate((valid, fields) => {
        if (!valid) {
            return ElMessage.error("提交失败,请填写完信息", fields)
        }
        const formData = new FormData();
        formData.append("image",zlData2.value)
        axios({
            method: 'post',
            url: '/competition/comp/upload',
            data:formData,
            headers:axios2.accessHeader()
        }).then((res)=> {
            console.log(res)
            if (res.data.code === 0)
            {
                ruleForm.url2=res.data.data.url
                console.log("返回的图片路径")
                console.log(ruleForm.url2)
                ruleForm.registrationTimeStart=ruleForm.registrationTime[0]
                ruleForm.registrationTimeEnd=ruleForm.registrationTime[1]
                axios.post("/competition/insertComp",ruleForm,{headers:axios2.accessHeader()})
                    .then((res)=>{
                        if (res.data.code==200)
                        {
                            ElMessage.success("竞赛申请成功，耐心等待管理员审核")
                            resetForm(ruleFormRef2.value)
                            dk.value=false
                            getInfo()
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
const handleFileChange2 = (file: any) => {
    if (file.raw) {
        flag.value=true
        zlData2.value=file.raw  //把图片数据传给变量
        const reader = new FileReader();
        reader.readAsDataURL(file.raw);
        reader.onload = (e: any) => {
            imageUrl2.value= e.target.result; // 读取完成后更新图片 URL
        };
    }
};

const beforeAvatarUpload2 = (file: any) => {
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
const resetForm = (formEl: FormInstance | undefined) => {
    imageUrl.value=''
    imageUrl2.value=''
    selectedBigType.value=''
    selectedBigType2.value=''
    if (!formEl) return
    formEl.resetFields()
}
//................................................
import {computed, onMounted, reactive, ref} from "vue";
import axios from "axios";
import axios2 from "../../axios/axios.js";
import {ElMessage, FormInstance} from "element-plus";
import time from "../../utils/time.js"
import {Plus} from "@element-plus/icons";

// interface SubjectItem{
//     bigType:string;
//     smallType:string[];
// }
const option = Array.from({ length: 100 }).map((_, idx) => ({
    value: `${idx + 1}`,
    label: `${idx + 1}`,
}))
const subjectInfo = ref<SubjectItem[]>([])
const subjectInfo2 = ref<SubjectItem[]>([])
const selectedBigType = ref('');
const selectedBigType2 = ref('');
const smallTypes2 = computed(() => {
    const selected = subjectInfo2.value.find(item => item.bigType === selectedBigType2.value);
    return selected ? selected.smallType : [];
});
const smallTypes = computed(() => {
    const selected = subjectInfo.value.find(item => item.bigType === selectedBigType.value);
    return selected ? selected.smallType : [];
});
const imageSrc=ref("http://localhost:8888/info/file/download/")
const search=ref()
const search2=ref()
const tableData=ref([])
const totalCount=ref()
const currentPage=ref(1)
const ruleFormRef = ref<FormInstance>()
let dialogFormVisible = ref(false)
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
    count:''
})
//....
const imageUrl=ref()
const flag=ref(false)
const zlData=ref()
// 选择文件后图片显示在前端
const handleFileChange = (file: any) => {
    if (file.raw) {
        flag.value=true
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
//....
const handleClick=()=>{
    console.log("搜索7")
}
const xz=()=>{
    axios.get("/info/getSubType").then((res)=>{
        subjectInfo.value=res.data
        subjectInfo2.value=res.data
    })
}
onMounted(()=>{
    xz()
})
const confirmEvent=(zl)=>{
    axios.get("/admin/deleteCp?id="+zl.competitionId,{headers:axios2.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
            {
                ElMessage.success("删除成功")
                getInfo()
            }
            else
                ElMessage.error(res.data.message)
        })
        .catch((e)=>{
            console.log(e)
        })
}
const submitForm=()=>{
    if (!ruleFormRef.value) return;
       ruleFormRef.value.validate((valid) => {
        if (valid) {
            dialogFormVisible.value=false
            if (!flag.value) //图片未修改
            {
                axios.post("/admin/updateCp",form,{headers: axios2.accessHeader()})
                    .then((res)=>{
                        if (res.data.code===200)
                        {
                            ElMessage.success(res.data.data)
                            getInfo()
                        }
                        else
                            ElMessage.error(res.data.message)
                    })
                    .catch((e)=> console.log(e)
                    )
            }
            else
            {
                const formData = new FormData();
                formData.append("image",zlData.value)
                axios({
                    method: 'post',
                    url: '/competition/comp/update?id='+form.competitionId,
                    data:formData,
                    headers:axios2.accessHeader()
                }).then((res)=>{
                        if (res.data.code === 0)
                        {
                            form.url=res.data.data.url
                            axios.post("/admin/updateCp",form,{headers: axios2.accessHeader()})
                                .then((res)=>{
                                    if (res.data.code===200)
                                    {
                                        ElMessage.success(res.data.data)
                                        getInfo()
                                    }
                                    else
                                        ElMessage.error(res.data.message)
                                })
                                .catch((e)=>{
                                    console.log(e)
                                })
                        }
                        else
                            ElMessage.error("修改图片失败，请重试!")
                    })
            }
        }
        else {
            ElMessage.error("请填写完整表单")
            console.log('表单验证失败!');
        }
    })
}
const cancelEvent=()=>console.log("取消")
const getInfo=()=>{
    axios.get("/admin/getCp?currentPage="+currentPage.value,{headers:axios2.accessHeader()})
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
const  dialogFormVisible1= (info) => {
    dialogFormVisible.value = true//弹出对话框
    form.competitionId=info.competitionId
    form.url=info.url
    imageUrl.value=imageSrc.value+form.url


    form.competitionName=info.competitionName
    form.sponsor=info.sponsor
    form.subject=info.subject
    form.publishTime=info.publishTime
    form.competitionTime=info.competitionTime
    form.registrationTimeStart=info.registrationTimeStart
    form.registrationTimeEnd=info.registrationTimeEnd
    form.location=info.location
    form.isHot=info.isHot
    form.status=info.status
    form.awardTime=info.awardTime
    form.description=info.description
    form.count=info.count
    axios.get("/admin/selectBigType?s="+info.categoryId,{headers: axios2.accessHeader()})
        .then((res)=>{
            if(res.data.code===200)
               selectedBigType.value=res.data.data
        })
        .catch((e)=>console.log(e))
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
    border: 2px gray solid;
}
</style>

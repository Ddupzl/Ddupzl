<template>
 <div>
<!--     学生信息查询-->
     <div style="margin-top: 1px">
         <el-input v-model="search3" placeholder="姓名搜索 " style="width: 180px;"/>
         <el-input v-model="search4" placeholder="电话搜索 " style="width: 180px;"/>
         <el-button style="margin-left: 20px" @click="handleClick" >搜索</el-button>
     </div>
     <div style="margin-top: 2px">
         <el-button type="danger" @click="pldelete">批量删除</el-button>
         <el-button type="primary" @click="plexport">批量导出</el-button>
     </div>
     <el-table  :data="tableData" stripe style="width: 100%;height: 500px;" row-key="id" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center"/>
         <el-table-column  prop="uid" label="用户id" width="150" />
         <el-table-column prop="url" label="头像" width="180">
             <template  #default="scope">
                 <div style="border: 1px solid rgb(153,153,153);width: 53px;height:53px;overflow: hidden;border-radius: 50%;">
                     <img
                         style="cursor: pointer;width: 100%;height: 100%;border-radius: 50%;"
                         :src="imageSrc+scope.row.url"
                         alt="用户头像"
                     >
                 </div>
             </template>
         </el-table-column>
         <el-table-column prop="username" label="用户名" width="150"/>
         <el-table-column prop="tel" label="电话" width="180"/>
         <el-table-column prop="email" label="邮件" width="180"/>
         <el-table-column prop="registerTime" label="注册时间" width="180">
             <template #default="scope">
                 <div>
                     {{time.formatDate(scope.row.registerTime)}}
                 </div>
             </template>
         </el-table-column>
         <el-table-column fixed="right" label="操作" width="150">
             <template #default="scope">
                 <el-popconfirm
                         confirm-button-text="确定"
                         cancel-button-text="取消"
                         :icon="InfoFilled"
                         icon-color="#626AEF"
                         title="你确定要删除吗"
                         @confirm="confirmEvent(tableData[scope.$index])"
                         @cancel="cancelEvent"
                 >
                     <template #reference>
                         <el-button link>删除</el-button>
                     </template>
                 </el-popconfirm>
                 <el-button text @click="dialogFormVisible1(tableData[scope.$index])">详情</el-button>
             </template>
         </el-table-column>
     </el-table>
     <el-dialog v-model="dialogFormVisible" title="学生信息修改" :modal="false" z-index:999 :append-to-body="true">
         <el-form>
             <el-form ref="ruleFormRef" :model="form" :rules="rule" label-width="120px"  status-icon >
                 <el-form-item label="用户id:" prop="uid">
                     {{form.uid}}
                 </el-form-item>
                 <el-form-item label="注册时间:" >
                     {{time.formatDate(form.registerTime)}}
                 </el-form-item>
                 <el-form-item label="用户名" prop="username">
                     <el-input v-model="form.username" style="width: 180px" />
                 </el-form-item>
                 <el-form-item label="电话" prop="tel">
                     <el-input v-model="form.tel" />
                 </el-form-item>
                 <el-form-item label="邮件" prop="email">
                     <el-input v-model="form.email" />
                 </el-form-item>
                 <el-form-item>
                     <el-button type="primary" @click="submit(form.uid)">
                         确认修改
                     </el-button>
                 </el-form-item>
             </el-form>
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
import {InfoFilled} from "@element-plus/icons";
import time from "../../utils/time.js"

const a=ref()
const handleSelectionChange=(val)=> {
    a.value= val.map(item => item.uid);
}
const pldelete=()=>{
    axios.post("/admin/pldelete",
            a.value,
        {headers:axios2.accessHeader()})
        .then((res)=>{
             if (res.data.code==200)
             {
                 ElMessage.success("删除成功")
                 getInfo()
             }
        })

}
const plexport=() => {
  console.log("导出")
}
const handleClick=ref()
const imageSrc=ref("http://localhost:8888/comm/tx/")
//..................................
const tableData=ref([])
const totalCount=ref()
const currentPage=ref(1)
//删除用户信息
const confirmEvent=(zl)=>{
    console.log(zl.uid)
    axios.get("/admin/deleteUser?id="+zl.uid,{headers:axios2.accessHeader()})
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
const cancelEvent=()=>console.log("取消")
const getInfo=()=>{
    axios.get("/admin/getStudentInfo?currentPage="+currentPage.value,{headers:axios2.accessHeader()})
        .then((res)=>{
            console.log(res)
            tableData.value=res.data.data.list
            totalCount.value=res.data.data.totalCount
        })
}
//详情框
const ruleFormRef = ref<FormInstance>()
const dialogFormVisible = ref(false)
interface Student{
    uid:'',
    registerTime:'',
    username: '',
    tel:'',
    email:'',
    url:''
}
const form =reactive<Student>()
const  dialogFormVisible1= (info: any) => {
    dialogFormVisible.value = true//弹出对话框
    form.uid=info.uid
    form.registerTime=info.registerTime
    form.username=info.username
    form.tel=info.tel
    form.email=info.email
}
const submit=(zl)=>{
    ruleFormRef.value.validate((valid)=>{
        if (valid){
            axios.post('/personal/updateInfo',{
                username: form.username,
                email:form.email,
                tel:form.tel,
                uid: zl,
            },{headers:axios2.accessHeader()})
                .then((res)=>{
                    console.log(res)
                    if (res.data.code==200){
                        ElMessage.success("修改信息成功")
                        dialogFormVisible.value = false
                       getInfo()
                    }
                    else
                        ElMessage.error(res.data.message)
                })
                .catch((e)=>console.log(e))
        }
        else {
            ElMessage.warning("用户名或邮箱不能为空")
        }
    })
}
const rule={
    username:[
        {required:true,message: '请输入用户名',trigger: 'blur'},
        {min:2,max:20,message:'用户名不能为空',trigger:['blur','change']}
    ],
    email:[
        {required:true,message: '请输入邮件地址',trigger: 'blur'},
        {type:'email',message:'请输入合法的邮件地址 ',trigger:['blur','change']}
    ],
    tel: [
        // 如果电话号码是必填项
        // { required: true, message: '请输入电话号码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (!value) {
                    // 如果不是必填项，并且没有输入值，则验证通过
                    callback();
                } else if (!/^1[3-9]\d{9}$/.test(value)) {
                    // 使用正则表达式进行电话号码验证
                    // 这里的正则表达式仅作为示例，可能需要根据实际情况调整
                    callback(new Error('请输入合法的电话号码'));
                } else {
                    // 验证通过
                    callback();
                }
            },
            trigger: ['blur', 'change']
        },
        { required: false, message: '请输入电话号码', trigger: 'blur' },
        { min: 11, max: 11, message: '电话号码应为11位数字', trigger: ['blur', 'change'] }
    ]
};
onMounted(()=>{
    getInfo()
})
</script>

<style scoped>

</style>

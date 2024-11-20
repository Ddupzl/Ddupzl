<template>
    <div class="bm">
        <div style="font-weight: 500;font-size: 21px;margin-top: 10px;cursor: pointer;color: #606266" @click="details(a)">
            {{name}}
        </div>
        <div style="margin-top: 10px">
            <div style="color:#666666;font-size: 20px;font-weight: 600;border-left: 6px skyblue solid;padding: 0 15px 0 5px;">
                <span style="color: skyblue;">报名</span>
            </div>
            <el-steps :active="active" finish-status="success" style="margin:0 auto;width: 320px">
                <el-step title="填写个人信息"></el-step>
                <el-step title="确认信息"></el-step>
                <el-step title="报名成功"></el-step>
            </el-steps>
        </div>

        <div v-if="active === 0">
            <div style="margin: 60px auto 0;width: 400px;">
                <el-form :model="form" :rules="rule" ref="formRef">
                    <el-form-item prop="name" style="width: 400px;">
                        <el-input v-model="form.name" type="text" placeholder="请输入真实姓名">
                            <template #prefix>
                                <el-icon><User /></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="school" style="width: 400px;">
                        <el-input v-model="form.school" type="text" placeholder="请输入学校">
                            <template #prefix>
                                <el-icon><School /></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="dep" style="width: 400px;">
                        <el-input v-model="form.dep" type="text" placeholder="请输入所在院系">
                            <template #prefix>
                                <el-icon><OfficeBuilding /></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="email" style="width: 400px;">
                        <el-input v-model="form.email" type="email" placeholder="请输入电子邮件">
                            <template #prefix>
                                <el-icon><Message/></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="tel" style="width: 400px;">
                        <el-input v-model="form.tel" type="text" placeholder="请输入电话">
                            <template #prefix>
                                <el-icon><Phone /></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div style="margin:0 auto ;width: 200px;">
                <div style="margin-top: 40px">
                    <el-button plain @click="next" style="width: 180px" type="primary">下一步</el-button>
                </div>
                <div style="margin-top: 25px">
                    <el-button plain @click="router.go(-1)" style="width: 180px" type="warning">取消报名</el-button>
                </div>
            </div>
        </div>

        <div v-if="active === 1">
            <div style="font-size: 20px;width: 400px;margin: 40px auto 0;line-height: 30px">
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
            </div>
            <div style="margin:0 auto;width: 280px;">
                <div style="margin-top: 40px">
                    <el-button plain @click="dialogFormVisible = true" type="primary" style="width: 180px">
                        报名
                    </el-button>
                </div>
                <el-dialog v-model="dialogFormVisible" title="确定信息无误？报名后不可修改" width="500">

                        <div style="margin: 0 auto;width: 150px;">
                            <el-button @click="dialogFormVisible = false">取消</el-button>
                            <el-button type="primary" @click="bm">确认</el-button>
                        </div>

                </el-dialog>
                <div style="margin-top: 25px">
                    <el-button plain @click="active--" style="width: 180px" type="warning">返回上一步</el-button>
                </div>
            </div>
        </div>
        <div v-if="active === 3">
            <div style="margin-top: 130px">
                <div style="font-size: 25px;font-weight: bold;text-align: center;color: #67C23A">报名成功!</div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import { useRoute } from 'vue-router';
import axios from "../../../axios/axios.js";
import axios2 from "axios";
import router from "../../../router/router.js"
import {ElMessage} from "element-plus";

const dialogFormVisible = ref(false)
const formRef=ref()
const active=ref(0)
const route = useRoute();
const a=ref(route.query.cid)
const name=ref()
const uid=ref()
const getUid=()=>{
    axios.getUserInfo((zl)=>{
        if (zl.code == 200)
        uid.value=zl.data.uid
    })
}
const form=reactive({
    name: '',
    school:'',
    dep:'',
    email:'',
    tel:''
})
const rule={

    name:[
        {required:true,message: '请输入姓名',trigger: 'blur'},
        {required:true,min:2,max:20,message:'姓名不能为空',trigger:['blur','change']}
    ],
    school:[
        {required:true,message: '请输入学校',trigger: 'blur'}
    ],
    dep:[
        {required:true,message: '请输入所在院系',trigger: 'blur'}
    ],
    email:[
        {required:true,message: '请输入邮件地址',trigger: 'blur'},
        {type:'email',message:'请输入合法的邮件地址 ',trigger:['blur','change']}
    ],
    tel:[
        {required:true,message:'请输入电话号码',trigger:'blur'},
        {min:11,max:11,message: '请输入合法的电话号码'}
    ]
};
const next=()=>{
    formRef.value.validate((valid)=>{
        if (valid){
           active.value++
        }
    })
}
const bm=()=>{
    dialogFormVisible.value = false
    axios2.post('/bm/addRegister',{
        name: form.name,
        school:form.school,
        dep: form.dep,
        email:form.email,
        tel:form.tel,
        competitionId: a.value,
        uid: uid.value,
    },{headers:axios.accessHeader()})
        .then((res)=>{
            console.log(res)
            if (res.data.code==200){
                active.value=3
            }
            else
                ElMessage.error(res.data.message)
        })
}
const props = defineProps({change: Function});//父传子的

const change=(z)=>{
    props.change(z)
}
const getCName=()=>{
    axios2.get('/info/getDetails?id='+a.value)
        .then((response)=>{
            name.value=response.data.competitionName
        })
}
const details=(id)=>{
    change('/detailPage') //跳转页面不显示下划线
    router.push('/detailPage?cid='+id)
}
onMounted(()=>{
    getCName(),getUid()
})
</script>
<style scoped>
.input{
    height: 40px;
    width: 300px;
}
.bm{
    width: 66%;
    height: 86vh;
    box-sizing: border-box;
    margin: 96px auto 0;
    /*text-align: center;*/

    background-color: white;
    box-shadow: 0 5px 10px 0 rgba(0,0,0,0.1);
    border-radius: 10px;
    border: 1px solid white;
}
</style>

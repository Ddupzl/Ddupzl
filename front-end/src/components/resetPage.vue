<template>
    <div class="reset">
       <el-steps :active="active" finish-status="success" align-center style="margin-top: 40px">
            <el-step title="验证电子邮件"></el-step>
            <el-step title="重新设定密码"></el-step>
       </el-steps>
        <div v-if="active === 0">
            <div style="margin-top: 30px">
                  <div style="font-size: 25px;font-weight: bold">重置密码</div>
                  <div style="font-size: 14px;color: grey">请输入要重置账号的电子邮件</div>
            </div>
            <div style="margin: 60px auto 0;width: 400px;">
                <el-form :model="form" :rules="rule" ref="formRef">
                    <el-form-item prop="email" style="width: 400px;">
                        <el-input v-model="form.email" type="email" placeholder="请输入电子邮件">
                            <template #prefix>
                                <el-icon><Message/></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item style="width: 400px;margin-top: 40px" prop="code">
                        <el-row :gutter="10" style="width: 100%">
                            <el-col :span="17">
                                <el-input v-model="form.code" maxlength="4" type="text" placeholder="请输入验证码">
                                    <template #prefix>
                                        <el-icon><EditPen/></el-icon>
                                    </template>
                                </el-input>
                            </el-col>
                            <el-col :span="5">
                                <el-button @click="askCode" :disabled="!isEmailValid ||coldTime>0" type="success" style="width: 100px">
                                    {{ coldTime>0 ? `请稍后${coldTime}秒` : '获取验证码'}}
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-form>
            </div>
            <div style="margin-top: 60px">
                <el-button @click="confirmReset" style="width: 180px" type="warning">开始重置密码</el-button>
            </div>
            <div style="margin-top: 25px">
                <el-button @click="router.go(-1)" style="width: 180px" type="primary">点错了，返回上一步</el-button>
            </div>
        </div>

        <div v-if="active === 1">
            <div style="margin-top: 30px">
                <div style="font-size: 25px;font-weight: bold">重置密码</div>
                <div style="font-size: 14px;color: grey">请输入你的新密码，务必牢记，防止丢失</div>
                <div style="margin: 60px auto 0;width: 400px">
                    <el-form :model="form" :rules="rule" ref="formRef">
                    <el-form-item style="width: 400px" prop="password">
                        <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
                            <template #prefix>
                                <el-icon><Lock/></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item style="width: 400px" prop="password_repeat">
                        <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
                            <template #prefix>
                                <el-icon><Lock/></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    </el-form>
                </div>
            </div>
            <div style="margin-top: 60px">
                <el-button @click="doReset" style="width: 180px" type="success">重置密码</el-button>
            </div>
        </div>

    </div>
</template>

<script setup>
import {ref, reactive, computed} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import axios from "@/axios/axios";
import {ElMessage} from "element-plus";
import router from "@/router/router";

const formRef=ref()
const coldTime=ref(0)
const active=ref(0)
const isEmailValid =computed(()=>/^[\w.-]+@[\w.-]+\.\w+$/.test(form.email))
function confirmReset(){
    formRef.value.validate((valid)=>{
        if (valid){
            axios.post('/zl/reset-confirm',{
                email: form.email,
                code: form.code
            }, ()=>active.value++)
        }
    })
}
function doReset(){
    formRef.value.validate((valid)=>{
        if (valid){
            axios.post('/zl/reset-password',{...form}, ()=>{
                ElMessage.success("密码重置成功，请重新登录")
                router.push("/registerLoginPage/loginPage")
            })
        }
    })
}
function askCode(){
    if (isEmailValid.value)
    {
        coldTime.value=60
        axios.get('/zl/ask-code?email='+form.email+'&type=reset',()=>{
            ElMessage.success('验证码已发送,请注意查收')
            setInterval(()=> coldTime.value--,1000)
        },(message)=>{
            ElMessage.warning(message)
            coldTime.value=0
        })
    }
    else
        ElMessage.warning("请输入正确的电子邮件")
}
const validPassword=(rule,value,callback)=>{
    if(value==='')
        callback(new Error('请再次输入密码'))
    else if( value!== form.password){
        callback(new Error('两次输入的密码不一致'))
    }else
        callback()
}
const form=reactive({
    email: '',
    code:'',
    password:'',
    password_repeat:'',
})
const rule={
    password:[
        {required:true,message: '请输入密码',trigger: 'blur'},
        {min:6,max:20,message:'密码的长度在6-20个字符之间',trigger:['blur','change']}
    ],
    password_repeat:[
        { validator: validPassword,trigger:['blur','change']},
    ],
    email:[
        {required:true,message: '请输入邮件地址',trigger: 'blur'},
        {type:'email',message:'请输入合法的邮件地址 ',trigger:['blur','change']}
    ],
    code:[
        {required:true,message:'请输入验证码',trigger:'blur'},
        {min:4,max:4,message: '请输入4位验证码'}
    ]
};
</script>
<style scoped>
.reset{
    width: 56%;
    height: 86vh;
    box-sizing: border-box;
    margin: 96px auto 0;
    text-align: center;

    background-color: white;
    box-shadow: 0 5px 10px 0 rgba(0,0,0,0.1);
    border-radius: 10px;
    border: 1px solid white;
}
</style>

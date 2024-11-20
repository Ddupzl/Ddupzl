<template>
  <div style="text-align: center;">
       <div>
           <div style="font-size: 25px;font-weight: bold">注册新用户</div>
           <div style="font-size: 14px;color: grey">欢迎注册，请在下方填写信息</div>
       </div>
      <div style="margin-top: 30px;margin-left: 150px">
          <el-form :model="form" :rules="rule" ref="formRef"> <!-- formRef用于邮件验证-->
               <el-form-item style="width: 190px" prop="role">
                   <span style="font-size: 15px;color: grey;">您注册的角色：</span>
                   <el-select v-model="form.role" placeholder="请选择" style="margin-left: 100px;margin-top: -32px">
                       <el-option label="学生" value="student" />
                       <el-option label="老师" value="teacher" />
                   </el-select>
               </el-form-item>
              <el-form-item style="width: 400px" prop="username">
                  <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
                      <template #prefix>
                           <el-icon><User/></el-icon>
                      </template>
                  </el-input>
              </el-form-item>
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
              <el-form-item style="width: 400px" prop="email">
                  <el-input v-model="form.email" maxlength="20" type="email" placeholder="电子邮件">
                      <template #prefix>
                          <el-icon><Message/></el-icon>
                      </template>
                  </el-input>
              </el-form-item>
              <el-form-item style="width: 200px" prop="code">
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
          <div>
               <div style="margin-top: 10px;width:250px;">
                   <el-button @click="register" style="width: 250px;">立即注册</el-button>
               </div>
              <div style="margin-top: 10px;width: 140px">
                    <span style="font-size: 14px;line-height: 15px;color: grey;margin-left: -20px">已有账号?</span>
                    <el-link style="translate: 0 -1px" @click="router.push('/registerLoginPage/loginPage')">立即登录</el-link>
              </div>
          </div>
      </div>
  </div>

</template>

<script setup>
import router from "../router/router.js"
import axios from "../axios/axios.js";
import {computed, reactive, ref} from "vue";
import {Lock, User, Message, EditPen} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";

const formRef=ref()
const coldTime=ref(0)
const form=reactive({
    role:'',
    username:'',
    password:'',
    password_repeat:'',
    email:'',
    code:''
})
const validateUsername=(rule,value,callback)=>{
    if(value==='')
        callback(new Error("请输入用户名 "))
    else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
        callback(new Error('用户名不能包含特殊字符，只能用中/英文'))
    }else
        callback()
}
const validPassword=(rule,value,callback)=>{
    if(value==='')
        callback(new Error('请再次输入密码'))
    else if( value!== form.password){
        callback(new Error('两次输入的密码不一致'))
    }else
        callback()
}
const isEmailValid =computed(()=>/^[\w.-]+@[\w.-]+\.\w+$/.test(form.email))
function askCode(){
    console.log(coldTime.value)
    if (isEmailValid.value)
    {
        coldTime.value=60
        axios.get('/zl/ask-code?email='+form.email+'&type=register',()=>{
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
const register=()=>{
    formRef.value.validate((isValid) =>{
        if(isValid){
           axios.post('/zl/register',{
                role:form.role,
                username:form.username,
                password:form.password,
                email:form.email,
                code:form.code
            },()=>{
                ElMessage.success("注册成功，请重新登录")
               router.push("/registerLoginPage/loginPage")
            })
        }else {
            ElMessage.warning('请完整填写注册表单内容')
        }
    })
}
const rule={
    username:[
        { validator: validateUsername,trigger:['blur','change']},
        {min:3,max:10,message:'用户名在3-10个字符之间',trigger:['blur','change']}
    ],
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

</style>

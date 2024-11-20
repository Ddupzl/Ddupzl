<template>
<!--<div style="width: 100%;border: 1px purple solid;display: flex">-->

            <div class="left">
<!--                <div ref="dx" style="margin-left: 60px;position:absolute;top: 0;width:100px;font-size: 20px;cursor: pointer;"  @click="bs1">管理员登录</div>-->
<!--                <div ref="zh" style="margin-left: 200px;position:absolute;top: 0;width: 80px;font-size: 20px;cursor: pointer;border-bottom: 3px solid skyblue" @click="bs2">用户登录</div>-->
                <div ref="zh" style="font-size: 22px;margin:0 auto ;width: 90px;">用户登录</div>
<!--                cursor: pointer;-->

<!--                <div v-if="showElement">-->
<!--                    <el-form :model="form" :rules="rule" ref="formRef" style="margin-left: 30px;margin-top: 30px;width: 292px;height: 180px;">-->
<!--                        <el-form-item prop="tel">-->
<!--                            <el-input v-model="form.tel" type="text" style="width: 291px;height: 42px;font-size: 16px;margin-top: 40px;" placeholder="请输入手机号" >-->
<!--                            </el-input>-->
<!--                        </el-form-item>-->
<!--                        <el-form-item prop="yzm" style="width:290px;height: 40px;">-->
<!--                            <el-input style="width: 130px;height: 42px;" v-model="form.yzm" :maxlength="4" type="email" placeholder="请输入验证码" @keyup.enter="login">-->
<!--                            </el-input>-->
<!--                            <el-button style="margin-left: 50px" :size="'large'" type="success"  :disabled="!isEmailValid || coldTime>0" @click="">-->
<!--                                {{coldTime>0 ? coldTime+'秒' : '获取验证码'}}-->
<!--                            </el-button>-->
<!--                        </el-form-item>-->
<!--                    </el-form>-->
<!--                    <el-button style="position:absolute;top:215px;width: 290px;height: 40px;margin-left: 30px;" @click="login()">登录/注册</el-button>-->
<!--                </div>-->
                <div>
                    <el-form :model="form2" :rules="rule2" ref="formRef2" style="margin:0 auto;width: 292px;height: 380px;">
                        <el-form-item prop="username">
                            <el-input v-model="form2.username" type="text" style="width: 291px;height: 42px;font-size: 16px;margin-top: 40px;" placeholder="请输入手机号/QQ邮箱" @keyup.enter="login2">
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input v-model="form2.password" type="password" style="width: 291px;height: 42px;font-size: 16px;" placeholder="请输入密码" @keyup.enter="login2">
                            </el-input>
                        </el-form-item>
                        <el-row style="width: 292px;margin-top: -10px">
                            <el-col :span="12" style="text-align: left;">
                                <el-form-item>
                                    <el-checkbox v-model="form2.remember" label="记住我"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" style="text-align: right">
                                <el-link @click="router.push('/resetPage')">忘记密码?</el-link>
                            </el-col>
                        </el-row>
                        <el-button style="width: 290px;height: 40px;" @click="login2()" type="primary">登录</el-button>
                        <el-divider style="margin-top: 4rem">
                            <span style="font-size: 13px;color: grey">没有账号</span>
                        </el-divider>
                        <el-button style="width: 290px;height: 40px;margin: 0 auto" @click="router.push('/registerLoginPage/registerPage')">
                            立即注册
                        </el-button>
                        <el-divider style="margin-top: 2rem">
                            <span style="font-size: 13px;color: grey">其他方式快捷登录</span>
                        </el-divider>
                        <div class="login-button clickable">
                            <svg @click="QQlogin" cursor="pointer"
                                t="1721983847796" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                                 p-id="5106" width="40" height="40"><path d="M512.268258 64.433103c-247.183323 0-447.569968
                           200.380501-447.569968 447.563825 0 247.189467 200.385621 447.570992 447.569968 447.570992s447.569968-200.380501
                           447.569968-447.569968c0-247.184347-200.386645-447.564849-447.569968-447.564849z m252.85872 584.692787c-18.997168
                           16.287968-43.668709-53.628042-47.2134-42.875198-8.642616 26.161294-12.695154 43.646184-38.148944 72.127602-1.35972
                            1.521494 29.43056 12.647032 38.148944 36.396051 8.346713 22.756875 24.596797 58.811973-81.725503 70.125906-62.389428
                            6.635801-107.471099-33.244533-111.964932-32.85648-8.325212 0.734126-4.618747 0-13.568528 0-7.321804 0-7.807126
                            0.534468-14.69685 0-1.899307-0.140272-22.632985 32.85648-115.364231 32.85648-71.878798 0-90.48177-45.243445-76.032701-70.125906
                            14.464428-24.877342 38.579999-32.122354 35.176604-36.06636-16.73643-19.39546-28.287904-40.1404-35.176604-58.882621-1.705793-4.666869-3.135137-9.209848-4.262434-13.574672-2.611931-10.008479-22.627866
                            58.76385-44.111028 42.875198-21.483162-15.883533-19.567472-56.309597-5.659014-95.003248 14.033372-39.006959 49.37687-76.562049
                            49.771065-84.854496 1.412962-30.849665-3.044011-35.975235 0-44.078263 6.780169-18.149391
                            15.034732-11.190043 15.034733-20.609788 0-118.64476 88.172909-214.829571 196.933079-214.829571 108.755051 0 196.928984 96.184811
                             196.928984 214.829571 0 4.554242 11.815637 0 17.474651 20.609788 1.165181 4.256291 1.968931 20.684531 0.58771 44.078263-0.658358
                              11.238165 29.954789 24.914202 45.777913 84.854496 15.845649 59.945414 0 88.215912-7.909514 95.003248z" fill="#68A5E1" p-id="5107">
                            </path></svg>
                        </div>

                    </el-form>

                </div>
            </div>
<!--            <div class="right">-->
<!--                二维码-->
<!--            </div>-->

<!--</div>-->
</template>

<script lang="ts" setup>
import {defineProps, onMounted, onUpdated, reactive, ref, watchEffect} from "vue";
import axios from "../axios/axios.js";
import router from "../router/router.js"
import {ElMessage} from "element-plus";
import axios2 from "axios";

const props = defineProps({to: Function,tab2: Function,getRole: Function});
const to = () => {
    props.to();
};
const dx=ref(null)
const zh=ref(null)
const showElement = ref(false);
const formRef=ref()
const formRef2=ref()
const coldTime=ref(0)
const isEmailValid=ref(true)//禁止邮箱按钮
const form=reactive({
    tel:"",
    yzm:"",
})
const form2=reactive({
    username:"",
    password:"",
    remember:false
})
// async function QQlogin() {
//     const response = await axios2.get("/zl/getState");
//     console.log(response)
//     const stateValue = response.data;
//     const route = useRoute();
//     const AuthorizationCode = `https://graph.qq.com/oauth2.0/authorize?display=pc&response_type=code&client_id=102154394` +
//         `&redirect_uri=https://www.cami1e.online/homePage&scope=get_user_info%20list_album%20upload_pic%20do_like` +
//         `&state=${encodeURIComponent(stateValue)}`;
//     window.location.href = AuthorizationCode;
//
//     const code=ref(route.query.code)
//     const AccessToken = `https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=102154394&client_secret=
//     uqjWEqNjvwmQxIVX&code=`+code+`&redirect_uri=https://www.cami1e.online/homePage&need_openid=1`;
//     axios2.get(AccessToken)
//         .then((res)=>{
//             const access_token=res.data.access_token
//             const OpenID="https://graph.qq.com/oauth2.0/me?access_token="+access_token
//             axios2.get(OpenID)
//                 .then((res)=>{
//                     const openId=res.data.openid
//                      axios2.post("/zl/qqLogin",{sj: openId})
//                          .then((res)=>{
//                              if (res.data.code===200)
//                              {
//                                  axios.storeAccessToken(res.data.data.token,form2.remember)
//                                  ElMessage.success(`登陆成功`)
//                                  to()
//                                  tab2()
//                                  props.getRole()
//                              }
//                              else
//                                  ElMessage.error(res.data.message)
//                          })
//                 })
//
//         })
// }
async function QQlogin() {
    window.location.href = `https://graph.qq.com/oauth2.0/authorize?display=pc&response_type=code&client_id=102154394&redirect_uri=https://www.cami1e.online/homePage&scope=get_user_info%20list_album%20upload_pic%20do_like`;

}



// watchEffect(async () => {
//     if (window.location.search.includes('code=')) {
//         try {
//             const urlParams = new URLSearchParams(window.location.search);
//             const code = urlParams.get('code');
//             console.log("ssssssssss"+code)
//             if (code) {
//                 const response = await axios2.post('/zq/callbackHandler', {code: code });
//                 if (response.data.code===200) {
//                     console.log(response)
//                     router.push('/homePage')
//                     axios.storeAccessToken(response.data.token,true)
//                     ElMessage.success(`登陆成功`)
//                     to()
//                     tab2()
//                     props.getRole()
//                 } else {
//                     ElMessage.error(response.data.message)
//                 }
//             }
//         } catch (error) {
//             errorMessage.value = '发生错误：' + (error as Error).message;
//             error.value = true;
//         } finally {
//             isLoading.value = false;
//         }
//     }
// });



const tab2 = () => {
    props.tab2()
};
function login2(){
    formRef2.value.validate((valid)=>{
      if (valid){
          axios.login(form2.username,form2.password,form2.remember,()=>{
              to()
              tab2()
              props.getRole()
          })
      }
    })
}
const rule={
    tel:[
        {required:true,message:"请输入手机号",min:11,max:11}
    ],
    yzm:[
        {required:true,message:"请输入4位验证码",min:4,max:4}
    ],
}
const rule2={
    username:[
        {required:true,message:"请输入用户名",max:20}
    ],
    password:[
        {required:true,message:"请输入密码",max:20}
    ]
}

// const bs1=()=>{
//     dx.value.style.borderBottom='3px solid skyblue'
//     zh.value.style.borderBottom=null
//     showElement.value = true;
// }
// const bs2=()=>{
//     zh.value.style.borderBottom='3px solid skyblue'
//     dx.value.style.borderBottom=null
//     showElement.value = false;
// }
</script>

<style scoped>
/*!*.left{*!*/
/*!*    height: 480px;*!*/
/*!*    margin:0 auto;*!*/
/*!*    border: 1px solid red;*!*/
/*!*}*!*/

/*.right{*/
/*    width: 308px;*/
/*    height: 350px;*/
/*    border: 1px solid skyblue;*/
/*    margin-left: 450px;*/
/*    margin-top: 30px;*/
/*}*/

</style>

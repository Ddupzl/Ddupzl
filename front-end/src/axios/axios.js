import axios from "axios";
import {ElMessage} from "element-plus";

//axios的一些封装
const authItemName="access_token"
const defaultFailure=(message,code,url)=>{
    console.warn(`请求地址:${url},状态码: ${code},错误信息: ${message}`)
    ElMessage.warning(message)
}
const defaultError=(err)=>{
    console.log(err)
    ElMessage.error("发生了一些错误，请联系管理员")
}
function deleteAccessToken(){
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}
//从浏览器存储里面拿token
function takeAccessToken(){
    const str=localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
    if (!str) return null;
    const obj=JSON.parse(str)//重新封回obj?
    return obj.token
}
//从浏览器存储里面获取token，然后加上他的前缀，放在请求头header里面
function accessHeader(){
    const token=takeAccessToken();
    return token ? {'Authorization':`Bearer ${token}`} : {}
}
//判断用户是否登录       要和存在redis里面的token进行比较？还是api接口对比查看是否登录
function unauthorized(){
    return !takeAccessToken();
}
function getUserInfo(success){
    axios.get("/personal/getUserInfo",{headers: accessHeader()})
        .then( ({data})=>{
            success(data)
        })
        .catch(err=>defaultError(err))
}
function getUserInfo2(){
   return axios.get("/personal/getUserInfo",{headers: accessHeader()})
        .then( ( response=>response.data))
        .catch(err=>defaultError(err))
}

// //获取电话和邮箱
// function getTelEmail(success){
//     axios.get("/personal/getTelEmail",{headers: accessHeader()})
//         .then( ({data})=>{
//             success(data)
//         })
//         .catch(err=>defaultError(err))
// }
// //获取用户名
// function getUsername(success){
//     // 在header组件里面判断用户是否登录了,这里无需在判断是否为空
//     axios.get("/personal/getUsername",{headers: accessHeader()})
//         .then( ({data})=>{
//             success(data)
//         })
//         .catch(err=>defaultError(err))
// }
// //获取用户uid
// function getUid(success){
//     // 在header组件里面判断用户是否登录了,这里无需在判断是否为空
//     axios.get("/personal/getUid",{headers: accessHeader()})
//         .then( ({data})=>{
//             success(data)
//         })
//         .catch(err=>defaultError(err))
// }

//判断用户权限
function getRole(success){
    // 在header组件里面判断用户是否登录了,这里无需在判断是否为空
    axios.get("/personal/getRole",{headers: accessHeader()})
        .then( ({data})  =>{
            success(data)
        })
        .catch(err=>defaultError(err))
}

function storeAccessToken(token,remember){
    const obj={token:token}
    const str=JSON.stringify(obj)
    if (remember)
        localStorage.setItem(authItemName,str)
    else
        sessionStorage.setItem(authItemName,str)
}

function internalGet(url,header,success,failure,error=defaultError) {
    axios.get(url,{headers: header})
        .then( ({data})=>{
            success(data)
       })
        .catch(err=>error(err))
}
// axios.get 的响应对象通常包含以下属性：
// data：服务器返回的数据。
// status：HTTP 状态码。
// statusText：HTTP 状态消息。
// headers：服务器响应头。
// config：用于生成请求的配置信息。
// ... 其他属性
//下面函数中的{data}相当于直接获取响应对象中的响应体部分的数据(也就是上述的data)，可以写成.then((response)=>{success(response.data)})
function internalPost(url,data,header,success,failure,error=defaultError) {
    axios.post(url,data,{headers: header})
        .then(({data})=>{
        if(data.code===200)
            success(data.data)
        else
            failure(data.message,data.code,url)
    }).catch(err => error(err))
}
function get(url,success,failure=defaultFailure){
      internalGet(url,accessHeader(),success,failure)
}
function post(url,data,success,failure=defaultFailure){
    internalPost(url,data,accessHeader(),success,failure)
}
function login(username,password,remember,success,failure=defaultFailure){
     internalPost('/zl/login',{
        username:username,
        password:password
    },{
        'Content-Type':'application/x-www-form-urlencoded',
    },(data)=>{
        storeAccessToken(data.token,remember)
        ElMessage.success(`登陆成功`)
        success()
         // success(data)
    },failure)
}
function logout(success,failure=defaultFailure){
    get('/zl/logout',(res)=>{
        console.log(res)
        if (res.code===200)
        {
            deleteAccessToken()
            success()
            ElMessage.success("退出登录成功")
        }
        else
            ElMessage.error(res.message)
    },failure)
}
export default {login,logout,get,post,unauthorized,getRole,accessHeader,getUserInfo,getUserInfo2,storeAccessToken}

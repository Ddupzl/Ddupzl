<template>
    <div  class="personal" style="position: absolute;top: 95px;left: 0">
        <div class="info">
            <div class="tx" style="border: 3px solid rgb(153,153,153);width: 180px;height: 180px;border-radius: 50%;">
                <img w-full :src="imageSrc" style="width: 180px;height: 180px;border-radius: 50%;"  alt="gg"/>
            </div>
            <div class="nameId">
                <div style="font-size: 24px;font-weight: 600;color: #555">{{username}}</div>
               <div style="color: #409EFF;font-size: 1.5rem">uid: <span>{{uid}}</span></div>
            </div>
            <el-button class="xgtx"  @click="editCropper">修改头像</el-button>
            <el-dialog
                    v-model="open" :title="title"
                    align-center width="780px" append-to-body @opened="modalOpened" @close="closeDialog"
            >

                <el-row>
                    <el-col :xs="24" :md="12" :style="{ height: '350px' }">
                        <vue-cropper
                                v-if="visible"
                                ref="cropper"
                                :img="options.img"
                                :info="true"
                                :auto-crop="options.autoCrop"
                                :auto-crop-width="options.autoCropWidth"
                                :auto-crop-height="options.autoCropHeight"
                                :fixed-box="options.fixedBox"
                                :output-type="options.outputType"
                                @realTime="realTime"
                        />
                    </el-col>
                    <el-col :xs="24" :md="12" :style="{ height: '350px' }">
                        <div class="cj">
                            <img :src="options.previews.url" :style="options.previews.img" />
                        </div>
                    </el-col>
                </el-row>
                <br />
                <el-row>
                    <el-col :lg="2" :md="2">
                        <el-upload
                                action="#"
                                :http-request="requestUpload"
                                :show-file-list="false"
                                :before-upload="beforeUpload"
                        >
                            <el-button>
                                选择
                                <el-icon class="el-icon--right"><Upload /></el-icon>
                            </el-button>
                        </el-upload>
                    </el-col>

                    <el-col :lg="{ span: 1, offset: 2 }" :md="2">
                        <el-button icon="Plus" @click="changeScale(1)"></el-button>
                    </el-col>
                    <el-col :lg="{ span: 1, offset: 1 }" :md="2">
                        <el-button icon="Minus" @click="changeScale(-1)"></el-button>
                    </el-col>
                    <el-col :lg="{ span: 1, offset: 1 }" :md="2">
                        <el-button icon="RefreshLeft" @click="rotateLeft()"></el-button>
                    </el-col>
                    <el-col :lg="{ span: 1, offset: 1 }" :md="2">
                        <el-button icon="RefreshRight" @click="rotateRight()"></el-button>
                    </el-col>
                    <el-col :lg="{ span: 2, offset: 6 }" :md="2">
                        <el-button type="primary" @click="uploadImg()">提 交</el-button>
                    </el-col>
                </el-row>
            </el-dialog>

            <div class="xg">
                <el-link @click="bjzl" :underline="false">
                    <el-icon size="22" ><EditPen /></el-icon>修改资料
                </el-link>
                <el-link @click="router.push('/resetPage')" :underline="false" style="margin-left: 28px">
                     <el-icon size="22"><Key /></el-icon>修改密码
                </el-link>
                <el-dialog
                    v-model="open2" title="编辑资料"
                    align-center width="780px" append-to-body>
                    <el-form ref="ruleFormRef" :model="form" :rules="rule" label-width="120px"  status-icon >
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
                            <el-button type="primary" @click="submit">
                                确认修改
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-dialog>
            </div>
            <router-view :uid="uid" :change="change"/>
        </div>

    </div>
</template>

<script setup lang="ts">
import axios from "../axios/axios.js";
import router from "../router/router.js"
import axios2 from "axios";
import {ref, defineProps, onMounted, reactive, onUpdated} from "vue";
import {ElMessage} from "element-plus";
import {VueCropper} from 'vue-cropper'
import 'vue-cropper/dist/index.css'
import {EditPen} from "@element-plus/icons-vue";

const ruleFormRef=ref()
const imageSrc=ref('')
const props = defineProps({fetchImage2: Function,change: Function});//父传子的
const username=ref()
const uid=ref()
const form=reactive({
    username: '',
    email:'',
    tel:'',
})
const change=(z)=>{
    props.change(z)
}
const submit=()=>{
    ruleFormRef.value.validate((valid)=>{
        if (valid){
            axios2.post('/personal/updateInfo',{
                username: form.username,
                email:form.email,
                tel:form.tel,
                uid: uid.value,
            },{headers:axios.accessHeader()})
                .then((res)=>{
                    console.log(res)
                    if (res.data.code==200){
                        ElMessage.success("修改信息成功")
                        open2.value=false
                        getUsernameUid()
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
const getUsernameUid=()=>{
    axios.getUserInfo((zl)=>{
        if (zl.code==200)
        {
            username.value=zl.data.username
            uid.value=zl.data.uid
        }
    })
}

const open2=ref(false)
const bjzl=()=>{
    open2.value=true
    axios.getUserInfo((zl)=>{
        console.log(zl)
        if (zl.code==200)
            form.tel=zl.data.tel
        form.email =zl.data.email
        form.username=zl.data.username
    })
}
// function closeDialog2() {
//     // options.img = imageSrc.value
//     open2.value=false
// }

//.................................................................
const open = ref(false);
const visible = ref(false);
const title = ref("修改头像");
const cropper = ref(null);
// 图片裁剪数据
const options = reactive({
    img: imageSrc.value,
    autoCrop: true, // 是否默认生成截图框
    autoCropWidth: 200, // 默认生成截图框宽度
    autoCropHeight: 200, // 默认生成截图框高度
    fixedBox: true, // 固定截图框大小 不允许改变
    outputType: "png", // 默认生成截图为PNG格式
    previews: {} // 预览数据
});
/** 编辑头像 */
function editCropper() {
    options.img = imageSrc.value
    open.value = true;
}
/** 打开弹出层结束时的回调 */
function modalOpened() {
    visible.value = true;
}
/** 覆盖默认上传行为 */
function requestUpload() {}
/** 向左旋转 */
function rotateLeft() {
    cropper.value.rotateLeft();
}
/** 向右旋转 */
function rotateRight() {
    cropper.value.rotateRight();
}
/** 图片缩放 */
function changeScale(num) {
    num = num || 1;
    cropper.value.changeScale(num);
}
/** 上传预处理 */
function beforeUpload(file) {
    if (file.type.indexOf("image/") == -1) {
        ElMessage.error("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
    } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
            options.img = reader.result;
        };
    }
}
/** 上传图片 */
function uploadImg() {
    const formData = new FormData();
    cropper.value.getCropBlob(data => {
        console.log(data)
        formData.append("image",data)
        open.value = false;
        axios2({
            method: 'post',
            url: '/personal/file/upload',
            data:formData,
            headers:axios.accessHeader()
        }).then((res)=> {
            if (res.data.code === 0)
            {
                // console.log(res)
                tx()
                props.fetchImage2()
                ElMessage.success("上传图片成功")
                visible.value = false;
            }
            else
                ElMessage.error("上传图片失败")
        })
    });
}
/** 实时预览 */
function realTime(data) {
    options.previews = data;
}
/** 关闭窗口 */
function closeDialog() {
    visible.value = false;
}
//.....................................
const tx=()=>{
    axios2({
        method: 'get',
        url: '/personal/file/download',
        headers: axios.accessHeader(),
        responseType: 'blob' // 设置为blob来接收二进制数据
    }).then( (response) => {
        // console.log(response)
           const objectUrl=URL.createObjectURL(new Blob([response.data]))
            imageSrc.value = objectUrl;
    }).catch((e)=>{
        // console.log(e.response.status)
        console.log(e)
    })
}
// onUpdated(()=>{
//     tx()
// })
onMounted(()=>{
    tx(),getUsernameUid()
})

</script>

<style lang="scss" scoped>
.navbar {
  overflow: hidden;
  position: relative;
  height: 50px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  .breadcrumb-container {
    float: left;
  }
  .right-menu {
    display: flex;
    float: right;
    align-items: center;
    padding-right: 16px;
    ::v-deep .right-menu-item {
      display: inline-block;
      padding: 0 18px 0 0;
      vertical-align: text-bottom;
      font-size: 24px;
      color: #5a5e66;
      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;
        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }
    ::v-deep .avatar-container {
      cursor: pointer;
      .avatar-wrapper {
        display: flex;
        position: relative;
        align-items: center;
        margin-top: 5px;
        height: 50px;
        line-height: 50px;

      }
    }
  }
  ::v-deep .avatar {
    margin-right: 5px;
    border-radius: 50%;
    width: 30px;
    height: 30px;
  }
}

</style>
<style>
.personal{
    background-color: #f6f3f3;
    /*border: 1px red solid;*/
    box-sizing: border-box;
    height: 100vh;
    /*margin-left: 160px*/

}
@media screen and (min-width: 960px) {
    .nameId{
        margin-left: 395px;margin-top: -140px;
    }
    .xgtx{
        margin-top: 10px;margin-left: 395px;background-color:deepskyblue;color: white
    }
    .info{
        width: 1480px;
        height: 100vh;
        background-color: white;
        margin: 0 auto;
    }
    .tx{
        margin-left: 160px;
    }
    .xg{
        margin-top: -28px;
        margin-left: 540px;
    }
    .personal{
        width: 100vw;
    }
}
@media screen and (min-width: 750px) and (max-width: 960px) {
    .nameId{
        margin-left: 365px;margin-top: -140px;
    }
    .xgtx{
        margin-top: 10px;margin-left: 365px;background-color:deepskyblue;color: white
    }
    .info{
        background-color: white;
    }
    .tx{
        margin-left: 90px;
    }
    .xg{
        margin-top: 50px;
        margin-left: 100px;
    }
    .personal{
        width: 100vw;
    }
}
@media screen and (max-width: 750px) {
    .nameId{
       margin-left: 285px;margin-top: -140px;
    }
    .xgtx{
        margin-top: 10px;margin-left: 275px;background-color:deepskyblue;color: white
    }
    .info{
        background-color: white;
    }
    .tx{
        margin-left: 60px;
    }
    .xg{
        margin-top: 50px;
        margin-left: 70px;
    }
    .personal{
        width: 100vw;
    }
}
.cj{
    border-radius: 50%;
    width: 200px;
    height:200px;
    overflow: hidden;
    border: 1px solid skyblue;
    margin-left: 100px;
    margin-top: 62px;
}
.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}
</style>

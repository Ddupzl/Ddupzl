<template>
    <div style="border: 2px solid rgb(153,153,153);width: 53px;height:53px;border-radius: 50%;">
        <el-dropdown>
            <img style="cursor: pointer;width: 53px;height:53px;border-radius: 50%;" :src="imageSrc" @click="to3" alt="">
<!--            <img src="../assets/img.png" alt="aa">-->
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item @click="to3">用户:{{username}}</el-dropdown-item>
                    <el-dropdown-item @click="logout" style="align-items: center; justify-content: center;">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </div>
</template>

<script setup lang="ts">
import {defineProps, onMounted, ref, defineExpose, onUpdated} from "vue";
import axios from "../axios/axios.js";
import axios2 from "axios";
import {ElMessage} from "element-plus";

const props = defineProps({to3: Function,to: Function,tab: Function,show: Function});
const imageSrc = ref('');
const imageFail=ref("src/assets/img.png")
const username=ref()
const getUsername=()=>{
    axios.getUserInfo((zl)=>{
        if (zl.code==200)
        username.value=zl.data.username
    })
}
//加载头像
const fetchImage = () => {
    axios2({
        method: 'get',
        url: '/personal/file/download',
        headers: axios.accessHeader(),
        responseType: 'blob' // 设置为blob来接收二进制数据
    }).then( (response) => {
        if (response.data.size)
            imageSrc.value = URL.createObjectURL(new Blob([response.data]));
        else
            imageSrc.value=imageFail.value
    }).catch((e)=>{
        console.log(e)
    })
};
defineExpose({
    fetchImage
});
onMounted(() => {
    fetchImage(),getUsername()
});
const to3 = () => {
    props.to3();
};
function logout(){
    axios.logout(()=>{
            props.tab()
            props.to()
            props.show("zl")
        }
    )
}
</script>

<style scoped>

</style>

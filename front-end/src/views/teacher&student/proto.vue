<template>
<!--    这是老师和学生端的原型组件-->
        <Header ref="child"></Header>
       <router-view  :to2="to2" :to="to" :tab2="tab2" :change="change" :fetchImage2="fetchImage2"  :getRole="getRole"/>
</template>

<script setup lang="ts">
import Header from "../../components/header.vue";
import {onMounted, onUpdated, ref} from "vue";
import axios2 from "axios";
import axios from "../../axios/axios.js";
import {ElMessage} from "element-plus";

const child = ref();
const fetchImage2=()=>{
        if (child.value)
                child.value.fetchImage()
}
const getRole=()=>{
        if (child.value)
                child.value.getRole()
}
//这是登录跳转的方法
const tab2=()=>{
        if (child.value)
                child.value.tab2()
}
const to=()=>{
        if (child.value)
                child.value.to()
}
//这是更多竞赛的方法
const to2 = () => {
        if (child.value)
              child.value.to2()
};
const change = () => {
        if (child.value)
                child.value.change();
};
const isLoading = ref(true);
const error = ref(false);
const errorMessage = ref('');
onMounted(async () => {

    try {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        if (code) {
            const response = await axios2.post('/zq/callbackHandler', {code});
            if (response.data.code === 200) {
                console.log(response)
                axios.storeAccessToken(response.data.token, true)
                ElMessage.success(`登陆成功`)
                to()
                tab2()
                getRole()
            } else {
                ElMessage.error(response.data.message)
            }
        }
    } catch (error) {
        errorMessage.value = '发生错误：' + (error as Error).message;
        error.value = true;
    } finally {
        isLoading.value = false;
    }

})
</script>

<style>

</style>

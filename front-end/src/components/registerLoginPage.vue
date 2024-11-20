<template>
<!--    这是 注册/登录 页面，下面的路由用来跳转登录还是注册-->
    <div class="login">
        <div class="z1">
            <router-view :tab2="props.tab2" :to="props.to" :getRole="props.getRole"/>
        </div>
    </div>
</template>

<script lang="ts" setup>
import {defineProps} from 'vue';

const props = defineProps({to: Function,tab2: Function,getRole: Function});
</script>
<style scoped>
@media screen and (min-width: 960px){
    .z1{
        width: 587px;
    }
}
@media screen and (max-width: 960px) and (min-width: 750px){
    .z1{
        width: 382px;
    }
}
@media screen and (max-width: 750px){
    .z1{
        width: 100vw;
    }
}
.login{
    position: absolute;
    top:160px;
    left: 0;
    width: 100%;
    box-sizing: border-box;
    /*border: 1px black solid;*/
}
.z1{
    text-align: center;
    height: 48rem;
    /*border: 1px solid black;*/
    margin: 0 auto;
    background-color: white;
    box-shadow: 0 5px 10px 0 rgba(0,0,0,0.1);
    border-radius: 10px;
}
</style>

<template>
        <el-link :underline="false" v-if="true" style="font-size: 17px;margin-top: 10px" @click="to7()">注册/登录</el-link>
</template>

<script setup>
import {defineProps} from "vue";

const props = defineProps({to7: Function});
const to7 = () => {
    props.to7();
};
</script>

<style scoped>

</style>

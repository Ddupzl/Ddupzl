<template>
    <div class="main">
        <div class="main1">
            <slot name="main1"></slot>
        </div>
        <div class="main2">
            <slot name="main2"></slot>
        </div>
    </div>
</template>

<style scoped>

.main{
    width: 100%;
    /*border: 1px solid red;*/
    box-sizing: border-box;
    z-index: 1;
    position: absolute;
    left: 0;/* left和top,这是距离视口为0*/
    top: 90px;
}
.main1{
    width: 100%;
    height: 350px;
    box-sizing: border-box;
    /*border: 1px solid black;*/
    box-shadow: 0 1px 6px rgba(0, 0, 0, 0.1);/* 0水平偏移量;2px垂直偏移量;6px阴影的模糊半径,决定了阴影的模糊程度;最后是阴影的颜色和透明度*/
}
.main2{
    width: 88%;
    box-sizing: border-box;
    margin: 0 auto ;
    /*border: 1px solid red;*/
}

</style>

<script setup>
</script>

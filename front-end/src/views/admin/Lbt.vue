<template>
  <div>
      <div style="width: 1050px;height: 350px;background-color: #ccc;">
          <el-carousel trigger="click" height="350px">
              <el-carousel-item v-for="(item,index) in downList" :key="index">
                  <el-avatar :src="url+item.image" shape="square" fit="fill" style="height: 350px;width: 1050px;"></el-avatar>
              </el-carousel-item>
<!--              <h1 v-if="downList.value==null" style="text-align: center">暂无图片数据</h1>-->
          </el-carousel>
      </div>
      <h3>轮播图:</h3>
      <div class="image-wrapper">
          <div class="image-container" v-for="(item2,index) in downList" :key="index"  @mouseover="showDeleteIcon(item2.id)"
               @mouseleave="a=null"  >
              <img :src="url+item2.image" alt="" style="width: 300px;height: 100px">
              <div class="delete-icon" v-if="a==item2.id" @click="deleteItem(item2.image)">
                  <el-icon size="28"><Delete /></el-icon>
              </div>
          </div>
      </div>
     <h3>图片上传:</h3>
      <div>
          <el-upload
              v-model:file-list="fileList"
              list-type="picture-card"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
              :auto-upload="false"
              multiple
              style="margin-top: 10px;"
          >
              <el-icon><Plus /></el-icon>
          </el-upload>
          <el-dialog v-model="dialogVisible">
              <img w-full :src="dialogImageUrl" alt="Preview Image" />
          </el-dialog>
          <el-button @click="submit" style="width: 150px;" type="primary">提交</el-button>
      </div>

  </div>
</template>

<script lang="ts" setup>
import { Plus } from '@element-plus/icons-vue'
import type { UploadProps } from 'element-plus'
import {onMounted, onUpdated, ref} from "vue";
import axios2 from "axios";
import axios from  "../../axios/axios.js";
import {ElMessage} from "element-plus";
import {Delete} from "@element-plus/icons";

const a=ref()
const showDeleteIcon=(zl)=>{
    a.value=zl
}
const url=ref("http://localhost:8888/lbt/download/")
const downList=ref()
const fileList = ref()
const  dialogImageUrl = ref('')
const dialogVisible = ref(false)

const deleteItem=(zl)=>{
    axios2.get("/lbt/delete?name="+zl,{headers:axios.accessHeader()})
        .then((res)=>{
            if (res.data.code===200)
            {
                ElMessage.success("删除图片成功")
                downPList()
            }
            else
                ElMessage.error("删除图片失败")
        })
}
const downPList=()=>{
    axios2.get("/lbt/download/downloadList",{headers: axios.accessHeader()})
        .then((res)=>{
            console.log(res)
            if (res.data.code===200)
                downList.value=res.data.data
            else
                ElMessage.error(res.data.message)
        })
}
onMounted(()=>{
    downPList()
})

const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
    console.log(uploadFile, uploadFiles)
}

const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
    dialogImageUrl.value = uploadFile.url!
    dialogVisible.value = true
}
const submit=()=>{
    const data=new FormData();
    if (fileList.value==null)
        ElMessage.warning("请不要提交空图片")
    fileList.value.forEach(file=>{
        let size=0
        size=size+file.size
        if (size>10* 1024 *1024)
            ElMessage.error("传的图片数据太大了！")
    })
    fileList.value.forEach(file=>{
        data.append("imageList",file.raw);
    })
    axios2({
        method: 'post',
        url: '/lbt/uploadList',
        data: data,
        headers: axios.accessHeader()
    })
        .then((res)=>{
            if (res.data.code===200)
            {
                fileList.value=[]
                downPList()
                ElMessage.success("提交成功!")
            }
            else
                ElMessage.error(res.data.message)
        })

}

</script>

<style scoped>
.image-wrapper {
    display: flex;
    flex-wrap: wrap; /* 允许内容换行 */
    justify-content: flex-start; /* 水平对齐方式，这里设为左对齐 */
    align-items: flex-start; /* 垂直对齐方式，这里设为顶部对齐 */
    margin-top: 10px;
    /* 你可以添加额外的样式来控制间距等 */
}

.image-container {
    border-right: 3px solid #ccc; /* 右侧边框作为分界线 */
    /* 其他样式 */
}

/* 移除最后一个盒子的右侧边框 */
.image-container:last-child {
    border-right: none;
}
.image-container {
    position: relative; /* 使内部元素可以相对于它定位 */
    display: inline-block; /* 根据需要调整 */
}
.image-container img {
    display: block; /* 确保图片占满整个容器 */
}
.delete-icon {
    position: absolute; /* 绝对定位 */
    top: 50%; /* 从顶部开始，偏移 50% 的自身高度 */
    left: 50%; /* 从左边开始，偏移 50% 的自身宽度 */
    transform: translate(-50%, -50%); /* 使用 transform 来调整位置，使图标的中心与容器的中心对齐 */
    cursor: pointer; /* 鼠标悬停时显示手形图标 */
}
</style>

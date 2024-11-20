<template>
    <div class="post">
        <div style="width: 62%;margin: 0 auto">
            <div>
                <el-input placeholder="请输入文章标题" v-model="title"></el-input>
            </div>
            <div style="border: 1px solid #ccc">
                <Toolbar
                        style="border-bottom: 1px solid #ccc"
                        :editor="editorRef"
                        :mode="mode"
                />
                <Editor
                        style="height: 300px; overflow-y: hidden;font-size: 1.5rem"
                        v-model="valueHtml"
                        :defaultConfig="editorConfig"
                        :mode="mode"
                        @onCreated="handleCreated"
                        @onChange="handleChange"
                />
            </div>

            <div>
                <el-button style="float:right;" type="primary" @click="tz">发布帖子</el-button>
            </div>

            <div>
                <el-button style="float: left;" @click="router.push('/forumPage')" type="danger">返回帖子区</el-button>
            </div>

        </div>
    </div>

</template>

<script lang="ts" setup>
import "@wangeditor/editor/dist/css/style.css";
import router from "../router/router.js"
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IEditorConfig } from "@wangeditor/editor";
import {reactive, ref, shallowRef} from "vue";
import { onBeforeUnmount } from 'vue';
import axios from "axios";
import axios2 from "../axios/axios.js";
import {ElMessage} from "element-plus";

const props = defineProps({change: Function});
interface Post{
    userId: string
    title: string
    content: string
}
const title=ref('')
const text=ref('')
// 绑定数据
const valueHtml = ref();

// 初始化 MENU_CONF 属性
const editorConfig: Partial<IEditorConfig> = {
    MENU_CONF: {}
};
const mode = "default";

// 编辑器实例，必须用 shallowRef，重要！
const editorRef = shallowRef();

const handleCreated = editor => {
    // console.log("created", editor);
    editorRef.value = editor; // 记录 editor 实例，重要！
};

// 组件销毁时，也及时销毁编辑器，重要！
onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
});
// 配置上传地址
editorConfig.MENU_CONF["uploadImage"] = {
    // form-data fieldName ，默认值 'wangeditor-uploaded-image'
    fieldName: "image",
    server: "/personal/file/upload",
    // 小于该值就插入 base64 格式（而不上传），默认为 0
    base64LimitSize: 5 * 1024 // 5kb
};
// const handleChange = editor => {
//     // console.log("content", editor.getHtml());
//     text.value=editor.getHtml()
// };
const handleChange = (editor) => {
    // 只在编辑器内容实际发生变化时更新 text.value
    const newContent = editor.getHtml();
    if (newContent !== text.value) {
        text.value = newContent;
    }
};
const a=reactive<Post>({
    userId: '', // 初始值可以设为空字符串或其他默认值
    title: '',
    content: '',
})
const tz = async () => {
    if (axios2.accessHeader().Authorization == null) {
        ElMessage.error("请登录后再发表吧");
        router.push("/registerLoginPage/loginPage");
        props.change("/registerLoginPage/loginPage");
    } else if (text.value === "<p><br></p>" || title.value.length === 0) {
        ElMessage.warning("请填写内容或标题再发表吧");
    } else {
        try {
            const userData = await axios2.getUserInfo2();
            a.userId = userData.data.uid;
            a.content = text.value;
            a.title = title.value;
            const response = await axios.post("/post/posts", a, { headers: axios2.accessHeader() });
            if (response.data.code === 200) {
                ElMessage.success("帖子发布成功");
                // 清空状态变量
                text.value = '';
                valueHtml.value = '';
                title.value = '';
            } else {
                ElMessage.error("帖子发布失败");
            }
        } catch (error) {
            console.error(error);
            ElMessage.error("发生错误，请稍后再试");
        }
    }
};
// const tz= async () => {
//     console.log(text.value.length)
//     console.log(valueHtml.value.length)
//     if (axios2.accessHeader().Authorization == null) {
//         ElMessage.error("请登录后再发表吧")
//         router.push("/registerLoginPage/loginPage")
//         props.change("/registerLoginPage/loginPage")
//     }
//     else if (text.value=="<p><br></p>" || title.value.length==0)
//         ElMessage.warning("请填写内容或标题再发表吧")
//     else {
//         // axios2.getUserInfo((data)=>{
//         //     // console.log(data.data.uid)
//         //     a.userId=data.data.uid
//         //     console.log(a.userId)
//         // })
//         const userData = await axios2.getUserInfo2(); //等待异步
//         a.userId = userData.data.uid;
//         a.content = text.value
//         a.title = title.value
//         axios.post("/post/posts", a, {headers: axios2.accessHeader()})
//             .then((res) => {
//                 if (res.data.code == 200)
//                 {
//                     ElMessage.success("帖子发布成功")
//                     text.value=null
//                     valueHtml.value=null
//                     title.value=null
//                 }
//                 else
//                     ElMessage.error("帖子发布失败")
//             })
//             .catch((err) => console.log(err))
//     }
//
// }
</script>

<style scoped>
@media screen and (min-width: 1488px) {
    .post{
        width: 1488px;
        /*position: absolute;top: 100px;*/
        /*border: 1px skyblue solid*/
    }
}
@media screen and (max-width: 1488px) {
    .post{
        width: 100%;
        position: absolute;
        top: 95px;
    }
}
</style>

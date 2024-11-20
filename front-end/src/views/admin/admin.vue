<template>
        <div style="position: absolute;top: 95px;left: 0">
            <div>
              <el-menu
                default-active="1"
                class="el-menu-vertical-demo"
              >
                  <el-menu-item  index="1" @click="router.push('/adminPage/lbt')">
                      <el-icon><icon-menu /></el-icon>
                      <template #title>轮播图管理</template>
                  </el-menu-item>
                <el-sub-menu index="2">
                    <template #title>
                        <el-icon><Operation /></el-icon>
                        <span>竞赛管理</span>
                    </template>
                    <el-menu-item-group>
                        <el-menu-item index="2-1" @click="router.push('/adminPage/comp1')">竞赛审核</el-menu-item>
                    </el-menu-item-group>
                    <el-menu-item-group>
                        <el-menu-item index="2-2"  @click="router.push('/adminPage/comp2')">竞赛信息管理</el-menu-item>
                    </el-menu-item-group>
                    <el-menu-item-group>
                        <el-menu-item index="2-3" @click="router.push('/adminPage/comp3')">竞赛成绩管理</el-menu-item>
                    </el-menu-item-group>
                </el-sub-menu>
                  <el-sub-menu index="3">
                      <template #title>
                          <el-icon><Avatar /></el-icon>
                          <span>教师管理</span>
                      </template>
                      <el-menu-item-group>
                          <el-menu-item index="3-1"  @click="router.push('/adminPage/zl2')">教师信息管理</el-menu-item>
                      </el-menu-item-group>
                  </el-sub-menu>
                  <el-sub-menu index="4">
                      <template #title>
                          <el-icon><UserFilled /></el-icon>
                          <span>学生管理</span>
                      </template>
                      <el-menu-item-group>
                          <el-menu-item index="4-1"  @click="router.push('/adminPage/zl1')">学生信息管理</el-menu-item>
                      </el-menu-item-group>
                  </el-sub-menu>

             </el-menu>
            </div>
            <div class="ll">
                <router-view />
            </div>
        </div>
</template>

<script lang="ts" setup>
import router from "../../router/router.js"
import {Menu as IconMenu,} from '@element-plus/icons-vue'

</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
}
.ll{
    width: 1272px;
    margin-left: 204px;
    position: absolute;
    top: 0;
    /*border: 1px red solid;*/
}
</style>

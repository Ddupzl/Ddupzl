import {createRouter, createWebHistory} from "vue-router";
import proto from "../views/teacher&student/proto.vue"
import homePage from "../views/teacher&student/details/home.vue"
import axios from "../axios/axios.js"
import {ElMessage} from "element-plus";

const router= createRouter({
    // 路由历史信息控制(浏览器url前进后退的控制方式)
    history: createWebHistory(),
    routes: [
        //教师学生端(这一个大括号就是App.vue，所以里面引的是App里面要显示的内容)
        {
            path:'/',
            redirect:'/homePage',
            name:"proto",
            component:proto,
            children:[
                {
                    path:"homePage",
                    name:"homePage",
                    component:homePage,
                },
                {
                    path:"detailPage",
                    name:"detailPage",
                    component:()=>import("../views/teacher&student/details/detail.vue")
                },
                {
                    path:"bmPage",
                    name:"bmPage",
                    component:()=>import("../views/teacher&student/details/bm.vue")
                },
                {
                    path:"competitionPage",
                    name:"competitionPage",
                    component:()=>import("../views/teacher&student/details/competitions.vue"),
                },
                {
                    path:'personalPage',
                    name:'personal',
                    component:()=>import("../components/personal.vue"),
                    children:[
                        {
                            path:"teacherPersonal",
                            name:"teacherPersonal",
                            component:()=>import("../views/teacher&student/details/teacher/teacher.vue"),
                        },
                        {
                            path:"studentPersonal",
                            name:"studentPersonal",
                            component:()=>import("../views/teacher&student/details/student/student.vue")
                        },
                    ]
                },
                {
                    path:"forumPage",
                    name:"forumPage",
                    component:()=>import("../views/teacher&student/details/forums.vue"),
                },
                {
                    path:"plPage",
                    name:"plPage",
                    component:()=>import("../views/teacher&student/details/forum.vue"),
                },
                {
                    path:"postPage",
                    name:"postPage",
                    component:()=>import("../components/post.vue")
                },
                {
                    path:"competitionManage",
                    name:"cm",
                    component:()=>import("../views/teacher&student/details/teacher/competitionManage.vue")
                },
                {
                    path:"registerLoginPage",
                    redirect: '/registerLoginPage/loginPage',
                    name:"registerLoginPage",
                    component:()=>import("../components/registerLoginPage.vue"),
                    children:[
                        {
                            path:"loginPage",
                            component:()=>import("../components/login.vue")
                        },
                        {
                            path:"registerPage",
                            component:()=>import("../components/registerPage.vue")
                        }
                    ]
                },
                {
                    path:"resetPage",
                    name:"resetPage",
                    component:()=>import("../components/resetPage.vue")
                },
                // 管理员端
                {
                    path:"/adminPage",
                    redirect: '/adminPage/lbt',
                    name:"adminPage",
                    component:()=>import("../views/admin/admin.vue"),
                    children:[
                        {
                            path: "lbt",
                            name:"lbt",
                            component:()=>import("../views/admin/Lbt.vue")
                        },
                        {
                            path: "zl1",
                            name: "zl1",
                            component:()=>import("../views/admin/content1.vue")
                        },
                        {
                            path: "zl2",
                            name: "zl2",
                            component:()=>import("../views/admin/content2.vue")
                        },
                        {
                            path: "comp3",
                            name: "comp3",
                            component:()=>import("../views/admin/compGrade.vue")
                        },
                        {
                            path: "comp1",
                            name: "comp1",
                            component:()=>import("../views/admin/comp1.vue")
                        },
                        {
                            path: "comp2",
                            name: "comp2",
                            component:()=>import("../views/admin/comp2.vue")
                        }
                    ]
                }
            ]
        }

    ]
})
router.beforeEach((to,from,next)=>{
    const validRoutes
        = ['/homePage', '/competitionPage','/detailPage','/personalPage','/personalPage/teacherPersonal',
        '/bmPage','/personalPage/studentPersonal','/forumPage','/competitionManage', '/registerLoginPage/loginPage',
        '/registerLoginPage/registerPage','/adminPage/lbt','/adminPage/zl1','/adminPage/zl2','/adminPage/comp1',
        '/adminPage/comp2','/adminPage/comp3','/resetPage','/plPage','/postPage'];
    //这里adminPage还没设置未登录不允许跳转，或者直接跳转到登录页面
    const isUnauthorized=axios.unauthorized()
    if ( (to.path.startsWith('/registerLoginPage') && !isUnauthorized) || !validRoutes.includes(to.path) )
        next('/homePage')  //当随便输入路径或者已经登录再进入登录页面的,直接跳转到首页
    else if ( to.path.startsWith('/bmPage') && isUnauthorized)//未登录报名的
    {
        next('/registerLoginPage/loginPage')
        ElMessage.warning("未登录，请登录后操作")
    }
    else if ((to.path.startsWith('/studentPersonal') || to.path.startsWith('/teacherPersonal'))&& isUnauthorized)
    {
        next('/registerLoginPage/loginPage')
        ElMessage.warning("未登录，请登录后操作")
    }
    // else if(!isUnauthorized && !(a==="student") && to.path.startsWith('/studentPersonal'))
    //     next('/registerLoginPage/loginPage')
    // else if(!isUnauthorized && !(a==="teacher") && to.path.startsWith('/teacherPersonal'))
    //     next('/registerLoginPage/loginPage')
    else
        next()
})
//向外共享路由实例
export default router

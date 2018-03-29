import Users from '@/components/Users.vue'
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      component: Users,
      name: 'users',
      path: '/',
    },
  ],
})

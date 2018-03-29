
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import colors from 'vuetify/es5/util/colors'

import App from './App.vue'
import router from './router'
import store from './store'

Vue.use(Vuetify, {
  theme: {
    accent: colors.pink.accent2,
    primary: colors.indigo.base,
  },
})
Vue.config.productionTip = false

// tslint:disable-next-line:no-unused-expression
new Vue({
  components: { App },
  el: '#app',
  router,
  store,
  template: '<App/>',
})

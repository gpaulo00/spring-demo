
import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

const state = {
  users: [],
  usersError: null,
  usersLoading: false,
  usersRequested: false,
}

const mutations = {
  setUsers(store, users) {
    store.users = users
    store.usersLoading = false
  },
  setError(store, msg) {
    store.usersError = msg
    store.usersLoading = false
  },
  setLoading(store) {
    store.usersLoading = true
  },
}

const actions = {
  async search({ commit }, query) {
    commit('setLoading')
    try {
      const res = await axios.get('/users', { params: { query } })
      commit('setUsers', res.data)
    } catch (e) {
      commit('setError', e.message)
    }
  },
}

export default new Vuex.Store({ state, actions, mutations })

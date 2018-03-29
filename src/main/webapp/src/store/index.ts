
import Vue from 'vue'
import Vuex from 'vuex'
import { MutationTree, ActionTree } from 'vuex'
import axios from 'axios'

import { User, RootState } from '../types'

Vue.use(Vuex)

const state: RootState = {
  users: [],
  usersError: null,
  usersLoading: false,
  usersRequested: false,
}

const mutations: MutationTree<RootState> = {
  setUsers(store, users: User[]) {
    store.users = users
    store.usersLoading = false
  },
  setError(store, msg: string) {
    store.usersError = msg
    store.usersLoading = false
  },
  setLoading(store) {
    store.usersLoading = true
  },
}

const actions: ActionTree<RootState, any> = {
  async search({ commit }, query: string) {
    commit('setLoading')
    try {
      const res = await axios.get('/users', { params: { query } })
      commit('setUsers', res.data)
    } catch (e) {
      commit('setError', e.message)
    }
  },
}

export default new Vuex.Store<RootState>({
  state,
  actions,
  mutations,
})

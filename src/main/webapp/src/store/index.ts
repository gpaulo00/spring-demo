
import axios from 'axios'
import Vue from 'vue'
import Vuex from 'vuex'
import { ActionTree, MutationTree } from 'vuex'

import { IRootState, IUser } from '../types'

Vue.use(Vuex)

const state: IRootState = {
  users: [],
  usersError: null,
  usersLoading: false,
  usersRequested: false,
}

const mutations: MutationTree<IRootState> = {
  setUsers(store, users: IUser[]) {
    store.users = users
    store.usersLoading = false
  },
  setError(store, msg: string) {
    store.usersError = msg
    store.usersLoading = false
  },
  setLoading(store) {
    store.usersLoading   = true
  },
}

const actions: ActionTree<IRootState, any> = {
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

export default new Vuex.Store<IRootState>({
  actions,
  mutations,
  state,
})

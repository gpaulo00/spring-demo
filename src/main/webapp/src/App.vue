<template>
  <v-app>
    <v-toolbar app dark color="primary">
      <v-toolbar-title v-text="title"></v-toolbar-title>
      <v-btn icon @click="solo = !solo">
        <v-icon>autorenew</v-icon>
      </v-btn>
      <v-spacer></v-spacer>
      <v-text-field
        label="Buscar"
        v-model="query"
        prepend-icon="search"
        hide-details
        single-line
        :solo="solo"
        :light="solo"
      ></v-text-field>
    </v-toolbar>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions } from 'vuex'

export default Vue.extend({
  data() {
    return {
      title: 'SpringBoot Demo',
      query: null,
      solo: true,
    }
  },
  watch: {
    query(val) {
      this.search(val)
    },
  },
  methods: {
    ...mapActions(['search']),
  },
  name: 'App',
})
</script>

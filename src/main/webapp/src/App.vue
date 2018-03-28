<template>
  <v-app>
    <v-toolbar app dark color="primary">
      <v-toolbar-title v-text="title"></v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      title: 'SpringBoot Demo',
    }
  },
  name: 'App',
}
</script>

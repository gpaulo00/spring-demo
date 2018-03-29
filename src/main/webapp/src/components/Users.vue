<template>
  <v-container fluid>
    <v-data-table :headers="headers" :items="users" :loading="loading" hide-actions class="elevation-1">
      <v-progress-linear slot="progress" color="blue" indeterminate></v-progress-linear>
      <template slot="items" slot-scope="props">
        <td>{{ props.item.id }}</td>
        <td>{{ props.item.firstName }}</td>
        <td>{{ props.item.lastName }}</td>
        <td>{{ props.item.age }}</td>
      </template>
      <template slot="no-data">
        <span class="text-xs-center" v-if="error === null">No hay ningún usuario en el sistema.</span>
        <v-alert v-else type="error" :value="true">
          <strong>Error al obtener datos</strong>: {{error.message}}
        </v-alert>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      headers: [
        { text: '#', value: 'id' },
        { text: 'Nombre', value: 'firstName' },
        { text: 'Apellido', value: 'lastName' },
        { text: 'Edad', value: 'age' },
      ],
      users: [],
      error: null,
      loading: true,
    }
  },
  methods: {
    query(input) {
      axios.get('/users', { params: { input } })
        .then((res) => { this.users = res.data })
        .catch((err) => { this.error = err })
        .then(() => { this.loading = false })
    },
  },
  created() {
    this.query()
  },
}
</script>

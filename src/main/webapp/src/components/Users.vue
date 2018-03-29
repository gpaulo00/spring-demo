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
          <strong>Error al obtener datos</strong>: {{error}}
        </v-alert>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  data() {
    return {
      headers: [
        { text: '#', value: 'id' },
        { text: 'Nombre', value: 'firstName' },
        { text: 'Apellido', value: 'lastName' },
        { text: 'Edad', value: 'age' },
      ],
    }
  },
  computed: mapState({
    users: store => store.users,
    loading: store => store.usersLoading,
    error: store => store.usersError,
    requested: store => store.usersRequested,
  }),
  methods: {
    ...mapActions(['search']),
  },
  created() {
    if (!this.requested) {
      this.search()
    }
  },
}
</script>

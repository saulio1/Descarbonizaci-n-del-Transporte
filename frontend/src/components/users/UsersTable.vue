<template>
  <DataTable
    :value="usersWithRoleNames"
    paginator
    ref="dt"
    table-style="min-width: 50rem;"
    size="large"
    data-key="id"
    selectionMode="single"
    :filters="filters"
    :rows="5"
    :loading="usersStore.loading"
    :rowsPerPageOptions="[5, 10, 20, 50]"
    paginatorTemplate="FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink RowsPerPageDropdown"
    currentPageReportTemplate="{first} al {last} de {totalRecords}"
    removableSort
    resizableColumns
    columnResizeMode="fit"
  >
    <template #header>
      <div class="flex flex-wrap gap-2 items-center justify-between">
        <IconField>
          <InputIcon>
            <i class="pi pi-search" />
          </InputIcon>
          <InputText variant="filled" v-model="filters['global'].value" placeholder="Buscar..." />
        </IconField>
        <Button
          severity="warn"
          outlined
          label="Crear"
          icon="pi pi-plus"
          class="mr-2"
          @click="createDialog()"
        />
      </div>
    </template>
    <template #empty> No fueron encontrados usuarios. </template>
    <template #loading> Cargando datos de usuarios. Por favor espere... </template>
    <template #paginatorstart>
      <Button
        v-tooltip.bottom="'Refrescar listado'"
        type="button"
        icon="pi pi-refresh"
        text
        @click="usersStore.fetchUsers()"
      />
    </template>
    <template #paginatorend>
      <Button
        v-tooltip.bottom="'Descargar listado'"
        type="button"
        icon="pi pi-download"
        text
        @click="exportCSV()"
      />
    </template>
    <Column
      v-for="col in columns"
      :field="col.field"
      :key="col.field"
      :header="col.header"
      :exportable="col.exportable"
      resizableColumns
      columnResizeMode="expand"
      :sortable="col.sortable"
      style="min-width: 12rem"
    >
      <template #body="slotProps">
        <div v-if="col.field === 'roleNames'">
          <Tag
            v-for="role in [...slotProps.data.roles].sort((a, b) =>
              getRoleName(a).localeCompare(getRoleName(b)),
            )"
            :key="role"
            :value="getRoleName(role)"
            :severity="getSeverityRole(role)"
            class="mr-1 mb-1"
          />
        </div>
        <div v-else-if="col.field === 'actions'">
          <Button
            size="small"
            v-tooltip.bottom="'Editar'"
            @click="updateDialog(slotProps.data.id)"
            icon="pi pi-pencil"
            outlined
            rounded
            class="mr-2"
            severity="warn"
          />
          <Button
            size="small"
            v-tooltip.bottom="'Eliminar'"
            @click="confirmDelete($event, slotProps.data.id)"
            icon="pi pi-trash"
            outlined
            rounded
            severity="danger"
          />
        </div>
        <span v-else>
          {{ col.format ? col.format(slotProps.data[col.field]) : slotProps.data[col.field] }}
        </span>
      </template>
    </Column>
  </DataTable>
  <users-modal />
  <ConfirmPopup />
</template>

<script setup lang="ts">
import {
  Button,
  Column,
  ConfirmPopup,
  DataTable,
  IconField,
  InputIcon,
  InputText,
  Tag,
} from 'primevue'
import { useConfirm } from 'primevue/useconfirm'
import { FilterMatchMode } from '@primevue/core/api'
import { computed, onMounted, ref } from 'vue'
import { useUsersStore } from '@/stores/users.ts'
import Role from '@/api/common/interfaces/roles.ts'
import UsersModal from '@/components/users/UsersModal.vue'

const dt = ref()

const usersStore = useUsersStore()
const confirm = useConfirm()

const usersWithRoleNames = computed(() => {
  return usersStore.users.map((user) => ({
    ...user,
    roleNames: user.roles.map(getRoleName).join(', '),
  }))
})

const createDialog = () => {
  usersStore.openDialog('create')
}

const updateDialog = (id: any) => {
  usersStore.openDialog(
    'edit',
    usersStore.users.find((x) => x.id === id),
  )
}

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

const getRoleName = (role: Role) => {
  switch (role) {
    case Role.ROLE_USER:
      return 'Visitante'
    case Role.ROLE_MODERATOR:
      return 'Moderador'
    case Role.ROLE_ADMIN:
      return 'Admin'
  }
}

const getSeverityRole = (role: Role) => {
  switch (role) {
    case Role.ROLE_USER:
      return 'info'
    case Role.ROLE_MODERATOR:
      return 'warn'
    case Role.ROLE_ADMIN:
      return 'primary'
  }
}

const columns = [
  {
    field: 'username',
    header: 'Nombre de usuario',
    sortable: true,
    format: null,
    exportable: true,
  },
  { field: 'email', header: 'Correo', sortable: true, format: null, exportable: true },
  { field: 'roleNames', header: 'Roles', sortable: true, format: null, exportable: true },
  {
    field: 'actions',
    header: 'Acciones',
    sortable: false,
    format: (data: string) => {
      return data
    },
    exportable: false,
  },
]

onMounted(async () => {
  await usersStore.fetchUsers()
})

const exportCSV = () => {
  dt.value.exportCSV()
}

const confirmDelete = (event: any, id: any) => {
  confirm.require({
    target: event.currentTarget,
    message: '¿Está seguro que desea eliminar este usuario?',
    icon: 'pi pi-exclamation-triangle',
    rejectProps: {
      label: 'Cancelar',
      severity: 'secondary',
      outlined: true,
    },
    acceptProps: {
      label: 'Eliminar',
      severity: 'danger',
    },
    accept: async () => {
      try {
        await usersStore.deleteUser(id)
      } catch {}
    },
  })
}
</script>

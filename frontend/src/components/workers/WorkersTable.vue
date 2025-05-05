<template>
  <DataTable
    :value="workersData"
    paginator
    ref="dt"
    table-style="min-width: 50rem;"
    size="large"
    data-key="id"
    selectionMode="single"
    :filters="filters"
    :rows="5"
    :loading="workersStore.loading"
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
          v-if="auth.isModerator"
          severity="warn"
          outlined
          label="Crear"
          icon="pi pi-plus"
          class="mr-2"
          @click="createDialog()"
        />
      </div>
    </template>
    <template #empty> No fueron encontrados trabajadores.</template>
    <template #loading> Cargando datos de trabajadores. Por favor espere...</template>
    <template #paginatorstart>
      <Button
        v-tooltip.bottom="'Refrescar listado'"
        type="button"
        icon="pi pi-refresh"
        text
        @click="workersStore.fetchWorkers()"
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
      style="min-width: 10rem"
    >
      <template #body="slotProps">
        <div v-if="col.field === 'actions'">
          <Button
            v-if="auth.isModerator"
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
            v-if="auth.isModerator"
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
  <workers-modal />
  <ConfirmPopup />
</template>

<script setup lang="ts">
import WorkersModal from '@/components/workers/WorkersModal.vue'
import { Button, Column, ConfirmPopup, DataTable, IconField, InputIcon, InputText } from 'primevue'
import { computed, onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/auth.ts'
import { useConfirm } from 'primevue/useconfirm'
import { FilterMatchMode } from '@primevue/core/api'
import { useWorkersStore } from '@/stores/workers.ts'

const dt = ref()

const auth = useAuthStore()
const workersStore = useWorkersStore()
const confirm = useConfirm()

const workersData = computed(() =>
  workersStore.workers.map((x) => ({
    ...x,
    positionName: x.position.name,
  })),
)

const createDialog = () => {
  workersStore.openDialog('create')
}

const updateDialog = (id: any) => {
  workersStore.openDialog(
    'edit',
    workersStore.workers.find((x) => x.id === id),
  )
}

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

const columns = [
  {
    field: 'names',
    header: 'Nombre',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'lastNames',
    header: 'Apellidos',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'cid',
    header: 'Carné de identidad',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'phoneNumber',
    header: 'Número de teléfono',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'email',
    header: 'Correo',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'positionName',
    header: 'Ocupación',
    sortable: true,
    format: null,
    exportable: true,
  },
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
  await workersStore.fetchWorkers()
})

const exportCSV = () => {
  dt.value.exportCSV()
}

const confirmDelete = (event: any, id: any) => {
  confirm.require({
    target: event.currentTarget,
    message: '¿Está seguro que desea eliminar este trabajador?',
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
        await workersStore.deleteWorker(id)
      } catch {}
    },
  })
}
</script>

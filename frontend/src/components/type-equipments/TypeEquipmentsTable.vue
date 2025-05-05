<template>
  <DataTable
    :value="typeEquipmentsStore.typeEquipments"
    paginator
    ref="dt"
    table-style="min-width: 50rem;"
    size="large"
    data-key="id"
    selectionMode="single"
    :filters="filters"
    :rows="5"
    :loading="typeEquipmentsStore.loading"
    :rowsPerPageOptions="[5, 10, 20, 50]"
    paginatorTemplate="FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink RowsPerPageDropdown"
    currentPageReportTemplate="{first} al {last} de {totalRecords}"
    removableSort
    resizableColumns
    columnResizeMode="fit"
    v-model:expandedRows="expandedRows"
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
          v-if="useAuthStore().isModerator"
          severity="warn"
          outlined
          label="Crear"
          icon="pi pi-plus"
          class="mr-2"
          @click="createDialog()"
        />
      </div>
    </template>
    <template #empty> No fueron encontrados tipos de equipamientos.</template>
    <template #loading> Cargando datos de tipos de equipamientos. Por favor espere...</template>
    <template #paginatorstart>
      <Button
        v-tooltip.bottom="'Refrescar listado'"
        type="button"
        icon="pi pi-refresh"
        text
        @click="typeEquipmentsStore.fetchTypeEquipments()"
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
    <Column expander style="width: 5rem" />
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
            v-if="auth.isModerator && slotProps.data.editable"
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
    <template #expansion="slotProps">
      <div class="p-4">
        <h3>Campos de datos de {{ slotProps.data.name }}</h3>
        <DataFieldTypeEquipmentsTable :data-fields="slotProps.data.dataFields"/>
      </div>
    </template>
  </DataTable>
  <type-equipments-modal />
  <ConfirmPopup />
</template>

<script setup lang="ts">
import TypeEquipmentsModal from '@/components/type-equipments/TypeEquipmentsModal.vue'
import {
  Button,
  Column,
  ConfirmPopup,
  DataTable,
  IconField,
  InputChips,
  InputIcon,
  InputText
} from 'primevue'
import { onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/auth.ts'
import { useConfirm } from 'primevue/useconfirm'
import { FilterMatchMode } from '@primevue/core/api'
import { useTypeEquipmentsStore } from '@/stores/typeEquipments.ts'
import DataFieldTypeEquipmentsTable
  from '@/components/type-equipments/DataFieldTypeEquipmentsTable.vue'

const dt = ref()

const auth = useAuthStore()
const typeEquipmentsStore = useTypeEquipmentsStore()
const confirm = useConfirm()
const expandedRows = ref({});

const createDialog = () => {
  typeEquipmentsStore.openDialog('create')
}

const updateDialog = (id: any) => {
  typeEquipmentsStore.openDialog(
    'edit',
    typeEquipmentsStore.typeEquipments.find((x) => x.id === id),
  )
}

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

const columns = [
  {
    field: 'name',
    header: 'Nombre',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'description',
    header: 'Descripción',
    sortable: true,
    format: (val: string | null | undefined) => (val ? val : '---'),
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
  await typeEquipmentsStore.fetchTypeEquipments()
})

const exportCSV = () => {
  dt.value.exportCSV()
}

const confirmDelete = (event: any, id: any) => {
  confirm.require({
    target: event.currentTarget,
    message: '¿Está seguro que desea eliminar este tipo de equipamiento?',
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
        await typeEquipmentsStore.deleteTypeEquipment(id)
      } catch {}
    },
  })
}

</script>

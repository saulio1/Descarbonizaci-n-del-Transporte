<template>
  <DataTable
    :value="equipmentsStore.usageDatas"
    paginator
    ref="dt"
    table-style="min-width: 50rem;"
    size="large"
    data-key="id"
    selectionMode="single"
    :filters="filters"
    :rows="5"
    :loading="equipmentsStore.loadingUsageData"
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
          :disabled="!equipmentsStore.selectedEquipment"
          severity="warn"
          outlined
          label="Crear"
          icon="pi pi-plus"
          class="mr-2"
          @click="createDialog()"
        />
      </div>
    </template>
    <template #empty> No fueron encontrados datos de uso.</template>
    <template #loading> Cargando datos de uso. Por favor espere...</template>
    <template #paginatorstart>
      <Button
        v-tooltip.bottom="'Refrescar listado'"
        type="button"
        icon="pi pi-refresh"
        text
        @click="equipmentsStore.fetchUsageData()"
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
  <usage-data-modal />
  <ConfirmPopup />
</template>

<script setup lang="ts">
import { Button, Column, ConfirmPopup, DataTable, IconField, InputIcon, InputText } from 'primevue'
import UsageDataModal from '@/components/usage-data/UsageDataModal.vue'
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth.ts'
import { useConfirm } from 'primevue/useconfirm'
import { FilterMatchMode } from '@primevue/core/api'
import { useEquipmentsStore } from '@/stores/equipments.ts'

function formatDate(date: Date): string {
  const pad = (n: number) => n.toString().padStart(2, '0')

  const day = pad(date.getDate())
  const month = pad(date.getMonth() + 1)
  const year = date.getFullYear()

  const hours = pad(date.getHours())
  const minutes = pad(date.getMinutes())
  const seconds = pad(date.getSeconds())

  return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`
}

const dt = ref()

const auth = useAuthStore()
const equipmentsStore = useEquipmentsStore()
const confirm = useConfirm()

const createDialog = () => {
  equipmentsStore.openUsageDataDialog('create')
}

const updateDialog = (id: String) => {
  equipmentsStore.openUsageDataDialog(
    'edit',
    equipmentsStore.usageDatas.find((x) => x.id === id),
  )
}

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

const columns = [
  {
    field: 'date',
    header: 'Fecha',
    sortable: true,
    format: (date: Date) => formatDate(new Date(date)),
    exportable: true,
  },
  {
    field: 'usageStart',
    header: 'Inicio del uso',
    sortable: true,
    format: (date: Date) => formatDate(new Date(date)),
    exportable: true,
  },
  {
    field: 'usageEnd',
    header: 'Fin del uso',
    sortable: true,
    format: (date: Date) => formatDate(new Date(date)),
    exportable: true,
  },
  {
    field: 'operatingHours',
    header: 'Horas de operabilidad',
    sortable: true,
    format: (val: number) => new Intl.NumberFormat('en-US').format(val),
    exportable: true,
  },
  {
    field: 'energyConsumed',
    header:
      'Energía consumida (' +
      (equipmentsStore.selectedEquipment?.usageDataEnergyConsumedMeasurementUnit ?? '') +
      ')',
    sortable: true,
    format: (val: number) => new Intl.NumberFormat('en-US').format(val),
    exportable: true,
  },
  {
    field: 'loadOrCapacityUsed',
    header:
      'Carga o capacidad usada (' +
      (equipmentsStore.selectedEquipment?.usageDataLoadOrCapacityUsedMeasurementUnit ?? '') +
      ')',
    sortable: true,
    format: (val: number) => new Intl.NumberFormat('en-US').format(val),
    exportable: true,
  },
  {
    field: 'operator',
    header: 'Operador',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'location',
    header: 'Localización',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'notes',
    header: 'Notas',
    sortable: true,
    format: (val: any) => (val ? '' : '---'),
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

const exportCSV = () => {
  dt.value.exportCSV()
}

const confirmDelete = (event: any, id: String) => {
  confirm.require({
    target: event.currentTarget,
    message: '¿Está seguro que desea eliminar este dato de uso?',
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
        await equipmentsStore.deleteUsageData(id)
      } catch {}
    },
  })
}
</script>

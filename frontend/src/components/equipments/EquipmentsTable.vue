<template>
  <DataTable
    :value="equipmentsStore.equipments"
    paginator
    ref="dt"
    table-style="min-width: 50rem;"
    size="large"
    data-key="id"
    selectionMode="single"
    :filters="filters"
    :rows="5"
    :loading="equipmentsStore.loading || typeEquipmentsStore.loading"
    :rowsPerPageOptions="[5, 10, 20, 50]"
    paginatorTemplate="FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink RowsPerPageDropdown"
    currentPageReportTemplate="{first} al {last} de {totalRecords}"
    removableSort
    resizableColumns
    columnResizeMode="expand"
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
          v-if="auth.isModerator"
          :disabled="typeEquipmentsStore.typeEquipments.length === 0"
          outlined
          label="Crear"
          icon="pi pi-plus"
          class="mr-2"
          @click="createDialog()"
        />
      </div>
    </template>
    <template #empty> No fueron encontrados equipamientos.</template>
    <template #loading> Cargando datos de equipamientos. Por favor espere...</template>
    <template #paginatorstart>
      <Button
        v-tooltip.bottom="'Refrescar listado'"
        type="button"
        icon="pi pi-refresh"
        text
        @click="equipmentsStore.fetchEquipments(); typeEquipmentsStore.fetchTypeEquipments()"
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
            size="small"
            v-tooltip.bottom="typeEquipmentsStore.typeEquipments.length === 0 ? 'No existen tipos de equipamientos' : 'Ver datos'"
            @click="viewDialog(slotProps.data.id)"
            icon="pi pi-eye"
            outlined
            rounded
            class="mr-2"
            severity="help"
          />
          <Button
            v-if="auth.isModerator"
            size="small"
            :disabled="typeEquipmentsStore.typeEquipments.length === 0"
            v-tooltip.bottom="typeEquipmentsStore.typeEquipments.length === 0 ? 'No existen tipos de equipamientos' : 'Editar'"
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
  <equipments-modal :type-equipments="typeEquipmentsStore.typeEquipments" />
  <ConfirmPopup />
</template>

<script setup lang="ts">
import EquipmentsModal from '@/components/equipments/EquipmentsModal.vue'
import { Button, Column, ConfirmPopup, DataTable, IconField, InputIcon, InputText } from 'primevue'
import { onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/auth.ts'
import { useConfirm } from 'primevue/useconfirm'
import { FilterMatchMode } from '@primevue/core/api'
import { useEquipmentsStore } from '@/stores/equipments.ts'
import type TypeEquipmentInputDto from '@/api/typeEquipments/interfaces/input/type-equipment.input.dto.ts'
import { useTypeEquipmentsStore } from '@/stores/typeEquipments.ts'

const dt = ref()

const auth = useAuthStore()
const equipmentsStore = useEquipmentsStore()
const typeEquipmentsStore = useTypeEquipmentsStore()
const confirm = useConfirm()

const createDialog = () => {
  equipmentsStore.openDialog('create')
}

const updateDialog = (id: any) => {
  equipmentsStore.openDialog(
    'edit',
    equipmentsStore.equipments.find((x) => x.id === id),
  )
}

const viewDialog = (id: any) => {
  equipmentsStore.openDialog(
    'view',
    equipmentsStore.equipments.find((x) => x.id === id),
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
    format: (val: string | any) => val || '---',
    exportable: true,
  },
  {
    field: 'typeEquipment',
    header: 'Tipo de equipamiento',
    sortable: true,
    format: (val: TypeEquipmentInputDto) => val.name,
    exportable: true,
  },
  {
    field: 'usageDataEnergyConsumedMeasurementUnit',
    header: 'UM de energía consumida (DU)',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'usageDataLoadOrCapacityUsedMeasurementUnit',
    header: 'UM de carga o capacidad usada (DU)',
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
  await equipmentsStore.fetchEquipments()
  await typeEquipmentsStore.fetchTypeEquipments()
})

const exportCSV = () => {
  dt.value.exportCSV()
}

const confirmDelete = (event: any, id: any) => {
  confirm.require({
    target: event.currentTarget,
    message: '¿Está seguro que desea eliminar este equipamiento?',
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
        await equipmentsStore.deleteEquipment(id)
      } catch {}
    },
  })
}
</script>

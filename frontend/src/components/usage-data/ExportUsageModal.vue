<template>
  <Dialog
    v-model:visible="equipmentsStore.isDialogExportOpen"
    :style="{ width: '40rem' }"
    :header="'Exportar datos de uso de los equipamientos'"
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Selecciona los equipamientos a exportar (por defecto todos)
        </label>
        <DataTable
          :value="equipmentsStore.equipments"
          paginator
          ref="dt"
          table-style="min-width: 50rem;"
          data-key="id"
          v-model:selection="selectedRows"
          :filters="filters"
          :rows="5"
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
                <InputText
                  variant="filled"
                  size="small"
                  v-model="filters['global'].value"
                  placeholder="Buscar..."
                />
              </IconField>
              <Button
                size="small"
                severity="primary"
                outlined
                label="Exportar"
                icon="pi pi-external-link"
                class="mr-2"
                @click="save()"
              />
            </div>
          </template>
          <template #empty> No fueron encontrados equipamientos.</template>
          <template #loading>
            Cargando campos de datos de equipamientos. Por favor espere...
          </template>
          <template #paginatorstart>
            <Button
              v-tooltip.bottom="'Refrescar listado'"
              type="button"
              icon="pi pi-refresh"
              text
              @click="equipmentsStore.fetchEquipments()"
            />
          </template>
          <template #paginatorend>
            <Button
              size="small"
              v-tooltip.bottom="'Descargar listado'"
              type="button"
              icon="pi pi-download"
              text
              @click="exportCSV()"
            />
          </template>
          <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
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
              <span>
                {{ col.format ? col.format(slotProps.data[col.field]) : slotProps.data[col.field] }}
              </span>
            </template>
          </Column>
        </DataTable>
      </div>
    </div>
    <template #footer>
      <Button
        label="Cancelar"
        icon="pi pi-times"
        variant="outlined"
        severity="secondary"
        text
        @click="close()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { useEquipmentsStore } from '@/stores/equipments.ts'
import { ref } from 'vue'
import { FilterMatchMode } from '@primevue/core/api'
import type TypeEquipmentInputDto from '@/api/typeEquipments/interfaces/input/type-equipment.input.dto.ts'
import { Button, Column, DataTable, Dialog, IconField, InputIcon, InputText } from 'primevue'
import type EquipmentOutputDto from '@/api/equipments/interfaces/output/equipment.output.dto.ts'

const equipmentsStore = useEquipmentsStore()
const selectedRows = ref<EquipmentOutputDto[]>([])

const close = () => {
  equipmentsStore.closeExportDialog()
}

function onDialogVisibilityChange(val: boolean) {
  equipmentsStore.isDialogExportOpen = val
  if (!val) {
    close()
  }
}

const save = async () => {
  try {
    const ids = selectedRows.value.length ? selectedRows.value.map(x => x.id) : undefined
    await equipmentsStore.export(ids)
    close()
  } catch {
  } finally {
  }
}

const dt = ref()

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

const exportCSV = () => {
  dt.value.exportCSV()
}

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
]
</script>

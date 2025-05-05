<template>
  <DataTable
    :value="dataFieldsData"
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
  >
    <template #header>
      <div class="flex flex-wrap gap-2 items-center justify-between">
        <IconField>
          <InputIcon>
            <i class="pi pi-search" />
          </InputIcon>
          <InputText variant="filled" v-model="filters['global'].value" placeholder="Buscar..." />
        </IconField>
      </div>
    </template>
    <template #empty> No fueron encontrados campos de datos.</template>
    <template #loading>
      Cargando campos de datos de tipos de equipamientos. Por favor espere...</template
    >
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
        <div v-if="col.field === 'required'">
          <Tag
            :value="col.format!(slotProps.data.required)"
            :severity="slotProps.data.required ? 'warn' : 'secondary'"
            class="mr-1 mb-1"
          />
        </div>
        <span v-else>
          {{ col.format ? col.format(slotProps.data[col.field]) : slotProps.data[col.field] }}
        </span>
      </template>
    </Column>
  </DataTable>
</template>

<script setup lang="ts">
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import { computed, ref } from 'vue'
import { FilterMatchMode } from '@primevue/core/api'
import { Button, Column, DataTable, IconField, InputIcon, InputText, Tag } from 'primevue'
import { useTypeEquipmentsStore } from '@/stores/typeEquipments.ts'
import TypeField from '@/api/common/interfaces/typeFields.ts'

const typeEquipmentsStore = useTypeEquipmentsStore()

const props = defineProps<{
  dataFields: DataFieldInputDto[]
}>()

const dataFieldsData = computed(() => {
  return props.dataFields.map(x => ({
    ...x,
    tf: getTypeFieldsName(x.typeField)
  }))
})

const getTypeFieldsName = (typeField: TypeField) => {
  switch (typeField){
    case TypeField.MULTIPLESELECT:
      return 'Selección múltiple'
    case TypeField.COLOR:
      return 'Color'
    case TypeField.DATE:
      return 'Fecha'
    case TypeField.DATETIME:
      return 'Fecha y Hora'
    case TypeField.TEXTFIELD:
      return 'Campo de texto'
    case TypeField.EMAIL:
      return 'Correo electrónico'
    case TypeField.NUMBER:
      return 'Campo numérico'
    case TypeField.TIME:
      return 'Hora'
    case TypeField.TEXTAREA:
      return 'Área de texto'
    case TypeField.SELECT:
      return 'Selección única'

  }
}

const dt = ref()

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

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
    format: (val: string | null | undefined | any) => (val ? val : '---'),
    exportable: true,
  },
  {
    field: 'tf',
    header: 'Tipo de campo',
    sortable: true,
    format: null,
    exportable: true,
  },
  {
    field: 'required',
    header: 'Requerido',
    sortable: true,
    format: (val: boolean) => (val ? 'Sí' : 'No'),
    exportable: true,
  },
  {
    field: 'minimum',
    header: 'Mínimo',
    sortable: true,
    format: (val: number | null | undefined | any) => (val ? new Intl.NumberFormat('en-US').format(val) : '---'),
    exportable: true,
  },
  {
    field: 'maximum',
    header: 'Máximo',
    sortable: true,
    format: (val: number | null | undefined | any) => (val ? new Intl.NumberFormat('en-US').format(val) : '---'),
    exportable: true,
  },
  {
    field: 'minimumDateTime',
    header: 'Fecha/Hora mínima',
    sortable: true,
    format: (val: Date | null | undefined | any) => (val ? formatDate(val) : '---'),
    exportable: true,
  },
  {
    field: 'maximumDateTime',
    header: 'Fecha/Hora máxima',
    sortable: true,
    format: (val: Date | null | undefined | any) => (val ? formatDate(val) : '---'),
    exportable: true,
  },
  {
    field: 'options',
    header: 'Opciones',
    sortable: true,
    format: (options: string[] | null | undefined | any) => (options ? options.join(', ') : '---'),
    exportable: true,
  },
]

const exportCSV = () => {
  dt.value.exportCSV()
}
</script>

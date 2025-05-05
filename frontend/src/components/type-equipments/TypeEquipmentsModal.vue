<template>
  <Dialog
    v-model:visible="typeEquipmentsStore.isDialogOpen"
    :style="{ width: '40rem' }"
    :header="
      typeEquipmentsStore.dialogMode === 'create'
        ? 'Crear tipo de equipamiento'
        : 'Editar tipo de equipamiento'
    "
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Nombre <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="name" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && name.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="
              typeEquipmentsStore.typeEquipments.find((x) => x.name === name.trim()) &&
              typeEquipmentsStore.selectedTypeEquipment?.name !== name.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El tipo de equipamiento ya existe
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Descripción
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="description" fluid />
      </div>

      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Campos de datos <span class="text-error-500">*</span>
        </label>
        <DataTable
          :value="dataFields"
          paginator
          ref="dt"
          table-style="min-width: 50rem;"
          data-key="id"
          selectionMode="single"
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
                <InputText variant="filled" size="small" v-model="filters['global'].value" placeholder="Buscar..." />
              </IconField>
              <Button
                size="small"
                severity="warn"
                outlined
                label="Agregar"
                icon="pi pi-plus"
                class="mr-2"
                @click="createDialog()"
              />
            </div>
          </template>
          <template #empty> No fueron encontrados campos de datos.</template>
          <template #loading>
            Cargando campos de datos de tipos de equipamientos. Por favor espere...
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
              <div v-else-if="col.field === 'actions'">
                <Button
                  size="small"
                  v-tooltip.bottom="'Actualizar'"
                  @click="updateDialog(slotProps.data.name)"
                  icon="pi pi-pencil"
                  outlined
                  rounded
                  class="mr-2"
                  severity="warn"
                />
                <Button
                  size="small"
                  v-tooltip.bottom="'Eliminar'"
                  @click="
                    () => (dataFields = dataFields.filter((x) => x.name !== slotProps.data.name))
                  "
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
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && dataFields.length === 0"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
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
      <Button
        :label="typeEquipmentsStore.dialogMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="typeEquipmentsStore.loadingDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
  <data-field-type-equipments-modal :data-fields="dataFields" @create="addDataField" @update="updateDataField" />
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useToastService } from '@/helpers/toast.helper.ts'
import { useTypeEquipmentsStore } from '@/stores/typeEquipments.ts'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import DataFieldTypeEquipmentsModal from '@/components/type-equipments/DataFieldTypeEquipmentsModal.vue'
import { Button, Column, DataTable, Dialog, IconField, InputIcon, InputText, Tag } from 'primevue'
import TypeField from '@/api/common/interfaces/typeFields.ts'
import { FilterMatchMode } from '@primevue/core/api'

const typeEquipmentsStore = useTypeEquipmentsStore()

const name = ref('')
const description = ref('')
const dataFields = ref<DataFieldInputDto[]>([])
const wasSubmitted = ref(false)

const initTypeEquipment = () => {
  const te = typeEquipmentsStore.selectedTypeEquipment
  if (te) {
    name.value = te.name
    description.value = te.description ?? ''
    dataFields.value = te.dataFields
  } else {
    name.value = ''
    description.value = ''
    dataFields.value = []
  }
}

watch(
  () => typeEquipmentsStore.selectedTypeEquipment,
  () => {
    initTypeEquipment()
  },
)

const close = () => {
  wasSubmitted.value = false
  typeEquipmentsStore.selectedTypeEquipment = null
  initTypeEquipment()
  typeEquipmentsStore.isDialogOpen = false
}

function onDialogVisibilityChange(val: boolean) {
  typeEquipmentsStore.isDialogOpen = val
  if (!val) {
    close()
  }
}

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    name: name.value.trim() === '',
    nameExists:
      typeEquipmentsStore.typeEquipments.find((x) => x.name === name.value.trim()) &&
      typeEquipmentsStore.selectedTypeEquipment?.name !== name.value.trim(),
    dataFields: dataFields.value.length === 0,
  }
  return !Object.values(errors).some(Boolean)
}

const save = async () => {
  if (!validateForm()) {
    useToastService({
      severity: 'error',
      summary: 'Campos incorrectos',
      detail: 'Revise los campos y vuelva a intentarlo.',
    }).showToast()
    return
  }
  try {
    switch (typeEquipmentsStore.dialogMode) {
      case 'create':
        await typeEquipmentsStore.createTypeEquipment({
          name: name.value,
          description: description.value,
          dataFields: dataFields.value,
        })
        break
      case 'edit':
        await typeEquipmentsStore.updateTypeEquipment({
          name: name.value,
          description: description.value,
          dataFields: dataFields.value,
        })
        break
    }
    close()
  } catch {
  } finally {
  }
}

const getTypeFieldsName = (typeField: TypeField) => {
  switch (typeField) {
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
    format: (val: string | null | undefined | any) => (val ? val : '---'),
    exportable: true,
  },
  {
    field: 'typeField',
    header: 'Tipo de campo',
    sortable: true,
    format: (val: TypeField) => getTypeFieldsName(val),
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
    format: (val: number | null | undefined | any) =>
      val ? new Intl.NumberFormat('en-US').format(val) : '---',
    exportable: true,
  },
  {
    field: 'maximum',
    header: 'Máximo',
    sortable: true,
    format: (val: number | null | undefined | any) =>
      val ? new Intl.NumberFormat('en-US').format(val) : '---',
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

const createDialog = () => {
  typeEquipmentsStore.openDialogDataField('create')
}

const updateDialog = (name: string) => {
  typeEquipmentsStore.openDialogDataField(
    'edit',
    dataFields.value.find((x) => x.name === name),
  )
}

const addDataField = (dataField: DataFieldInputDto) => {
  dataFields.value.push(dataField)
}

const updateDataField = (dataField: DataFieldInputDto) => {
  dataFields.value[
    dataFields.value.findIndex((x) => x.name === typeEquipmentsStore.selectedDataField!.name)
  ] = dataField
}
</script>

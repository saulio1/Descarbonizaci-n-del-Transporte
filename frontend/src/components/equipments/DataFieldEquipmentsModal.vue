<template>
  <Dialog
    v-model:visible="equipmentsStore.isDialogDataFieldOpen"
    :style="{ width: '30rem' }"
    :header="
      equipmentsStore.dialogDataFieldMode === 'create'
        ? 'Agregar campo de dato'
        : 'Actualizar campo de dato'
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
        <InputText size="small" aria-autocomplete="none" variant="filled" v-model="name" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && name.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="
              dataFields.find((x) => x.name === name.trim()) &&
              equipmentsStore.selectedDataField?.name !== name.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El campo de dato ya existe
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Descripción
        </label>
        <InputText
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="description"
          fluid
        />
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Requerido <span class="text-error-500">*</span>
        </label>
        <div class="flex items-center gap-2">
          <Checkbox size="small" v-model="required" binary name="required" input-id="required" />
          <label for="required">Es requerido</label>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Tipo de campo <span class="text-error-500">*</span>
        </label>
        <Select
          size="small"
          v-model="typeField"
          :options="typeFieldsOptions"
          optionLabel="name"
          filter
          placeholder="Seleccione el tipo de campo"
          fluid
          optionValue="code"
        />
      </div>
      <div v-if="typeField === TypeField.SELECT || typeField === TypeField.MULTIPLESELECT">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Opciones <span class="text-error-500">*</span>
        </label>
        <InputChips separator="," size="small" aria-autocomplete="none" v-model="options" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="
              wasSubmitted &&
              (typeField === TypeField.SELECT || typeField === TypeField.MULTIPLESELECT) &&
              options.length === 0
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.NUMBER">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Mínimo
        </label>
        <InputNumber
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="minimum"
          fluid
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="notNone(minimum) && notNone(maximum) && minimum! > maximum!"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser menor o igual que el máximo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.NUMBER">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Máximo
        </label>
        <InputNumber
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="maximum"
          fluid
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="notNone(minimum) && notNone(maximum) && minimum! > maximum!"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser mayor o igual que el mínimo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.DATE">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Fecha Mínima
        </label>
        <DatePicker
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="minimumDateTime"
          fluid
          show-icon
          dateFormat="dd/mm/yy"
          iconDisplay="input"
          show-button-bar
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="
              notNone(minimumDateTime) &&
              notNone(maximumDateTime) &&
              minimumDateTime! > maximumDateTime!
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser menor o igual que el máximo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.DATE">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Fecha Máxima
        </label>
        <DatePicker
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="maximumDateTime"
          fluid
          show-icon
          dateFormat="dd/mm/yy"
          iconDisplay="input"
          show-button-bar
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="
              notNone(minimumDateTime) &&
              notNone(maximumDateTime) &&
              minimumDateTime! > maximumDateTime!
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser mayor o igual que el mínimo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.DATETIME">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Fecha y Hora Mínima
        </label>
        <DatePicker
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="minimumDateTime"
          fluid
          show-icon
          dateFormat="dd/mm/yy"
          iconDisplay="input"
          show-button-bar
          show-time
          hour-format="12"
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="
              notNone(minimumDateTime) &&
              notNone(maximumDateTime) &&
              minimumDateTime! > maximumDateTime!
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser menor o igual que el máximo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.DATETIME">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Fecha y Hora Máxima
        </label>
        <DatePicker
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="maximumDateTime"
          fluid
          show-icon
          dateFormat="dd/mm/yy"
          iconDisplay="input"
          show-button-bar
          show-time
          hour-format="12"
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="
              notNone(minimumDateTime) &&
              notNone(maximumDateTime) &&
              minimumDateTime! > maximumDateTime!
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser mayor o igual que el mínimo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.TIME">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Hora Mínima
        </label>
        <DatePicker
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="minimumDateTime"
          fluid
          show-icon
          iconDisplay="input"
          show-button-bar
          time-only
          hour-format="12"
        >
          <template #inputicon="slotProps">
            <i class="pi pi-clock" @click="slotProps.clickCallback" />
          </template>
        </DatePicker>
        <div class="flex flex-col gap-1">
          <span
            v-if="
              notNone(minimumDateTime) &&
              notNone(maximumDateTime) &&
              minimumDateTime! > maximumDateTime!
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser menor o igual que el máximo
          </span>
        </div>
      </div>
      <div v-if="typeField === TypeField.TIME">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Hora Máxima
        </label>
        <DatePicker
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="maximumDateTime"
          fluid
          show-icon
          iconDisplay="input"
          show-button-bar
          time-only
          hour-format="12"
        >
          <template #inputicon="slotProps">
            <i class="pi pi-clock" @click="slotProps.clickCallback" />
          </template>
        </DatePicker>
        <div class="flex flex-col gap-1">
          <span
            v-if="
              notNone(minimumDateTime) &&
              notNone(maximumDateTime) &&
              minimumDateTime! > maximumDateTime!
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser mayor o igual que el mínimo
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
        :label="equipmentsStore.dialogMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="equipmentsStore.loadingDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import TypeField from '@/api/common/interfaces/typeFields.ts'
import { ref, watch } from 'vue'
import { useToastService } from '@/helpers/toast.helper.ts'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import {
  Button,
  Checkbox,
  DatePicker,
  Dialog,
  InputChips,
  InputNumber,
  InputText,
  Select,
} from 'primevue'
import { useEquipmentsStore } from '@/stores/equipments.ts'

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

const typeFieldsOptions = [
  { name: getTypeFieldsName(TypeField.TIME), code: TypeField.TIME },
  { name: getTypeFieldsName(TypeField.TEXTFIELD), code: TypeField.TEXTFIELD },
  { name: getTypeFieldsName(TypeField.EMAIL), code: TypeField.EMAIL },
  { name: getTypeFieldsName(TypeField.DATE), code: TypeField.DATE },
  { name: getTypeFieldsName(TypeField.COLOR), code: TypeField.COLOR },
  { name: getTypeFieldsName(TypeField.DATETIME), code: TypeField.DATETIME },
  { name: getTypeFieldsName(TypeField.MULTIPLESELECT), code: TypeField.MULTIPLESELECT },
  { name: getTypeFieldsName(TypeField.NUMBER), code: TypeField.NUMBER },
  { name: getTypeFieldsName(TypeField.SELECT), code: TypeField.SELECT },
  { name: getTypeFieldsName(TypeField.TEXTAREA), code: TypeField.TEXTAREA },
].sort((a, b) => a.name.localeCompare(b.name))

const emit = defineEmits(['create', 'update'])

const equipmentsStore = useEquipmentsStore()

const name = ref('')
const description = ref('')
const minimum = ref<number | null>(null)
const maximum = ref<number | null>(null)
const minimumDateTime = ref<Date | null>(null)
const maximumDateTime = ref<Date | null>(null)
const options = ref<string[]>([])
const required = ref(true)
const typeField = ref(TypeField.TEXTFIELD)
const wasSubmitted = ref(false)

const initDataField = () => {
  const df = equipmentsStore.selectedDataField
  if (df) {
    name.value = df.name
    description.value = df.description ?? ''
    minimum.value = df.minimum ?? null
    maximum.value = df.maximum ?? null
    minimumDateTime.value = df.minimumDateTime ?? null
    maximumDateTime.value = df.maximumDateTime ?? null
    options.value = df.options ?? []
    required.value = df.required
    typeField.value = df.typeField
  } else {
    name.value = ''
    description.value = ''
    minimum.value = null
    maximum.value = null
    minimumDateTime.value = null
    maximumDateTime.value = null
    options.value = []
    required.value = true
    typeField.value = TypeField.TEXTFIELD
  }
}

watch(
  () => equipmentsStore.selectedDataField,
  () => {
    initDataField()
  },
)

const close = () => {
  wasSubmitted.value = false
  initDataField()
  equipmentsStore.closeDialogDataField()
}

function onDialogVisibilityChange(val: boolean) {
  equipmentsStore.isDialogDataFieldOpen = val
  if (!val) {
    close()
  }
}

function notNone(val: any) {
  return val !== null && val !== undefined
}

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    name: name.value.trim() === '',
    nameExists:
      props.dataFields.find((x) => x.name === name.value.trim()) &&
      equipmentsStore.selectedDataField?.name !== name.value.trim(),
    options:
      (typeField.value === TypeField.SELECT || typeField.value === TypeField.MULTIPLESELECT) &&
      options.value.length === 0,
    minimumMaximumValid:
      notNone(minimum.value) && notNone(maximum.value) && minimum.value! > maximum.value!,
    minimumDateTimeMaximumDateTimeValid:
      notNone(minimumDateTime.value) &&
      notNone(maximumDateTime.value) &&
      minimumDateTime.value! > maximumDateTime.value!,
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
  switch (equipmentsStore.dialogDataFieldMode) {
    case 'create':
      emit('create', {
        name: name.value,
        description: description.value,
        required: required.value,
        typeField: typeField.value,
        minimum: minimum.value,
        maximum: maximum.value,
        minimumDateTime: minimumDateTime.value,
        maximumDateTime: maximumDateTime.value,
        options: options.value,
      })
      break
    case 'edit':
      emit('update', {
        name: name.value,
        description: description.value,
        required: required.value,
        typeField: typeField.value,
        minimum: minimum.value,
        maximum: maximum.value,
        minimumDateTime: minimumDateTime.value,
        maximumDateTime: maximumDateTime.value,
        options: options.value,
      })
      break
  }
  close()
}

const props = defineProps<{
  dataFields: DataFieldInputDto[]
}>()
</script>

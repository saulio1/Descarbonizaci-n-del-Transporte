<template>
  <div class="mb-4">
    <label class="mb-1.5 flex items-center gap-2 text-sm font-medium text-gray-700 dark:text-gray-400">
      {{ field.name }}
      <span v-if="field.required" class="text-error-500">*</span>
      <Button
        v-if="field.description"
        icon="pi pi-info-circle"
        size="small"
        text
        class="p-0"
        v-tooltip.bottom="field.description"
        aria-label="Info"
        tabindex="-1"
      />

      <template v-if="isOther">
        <Button
          v-if="equipmentsStore.dialogMode !== 'view'"
          severity="warn"
          icon="pi pi-pencil"
          size="small"
          text
          class="p-0"
          @click="$emit('edit', field.name)"
          aria-label="Editar"
          v-tooltip.bottom="'Editar'"
        />
        <Button
          v-if="equipmentsStore.dialogMode !== 'view'"
          severity="danger"
          icon="pi pi-trash"
          size="small"
          text
          class="p-0"
          @click="$emit('delete', field.name)"
          aria-label="Eliminar"
          v-tooltip.bottom="'Eliminar'"
        />
      </template>
    </label>

    <component
      :is="inputComponent"
      :disabled="equipmentsStore.dialogMode === 'view'"
      v-model="proxyValue"
      :options="field.options"
      :min="Number(field.minimum)"
      :max="Number(field.maximum)"
      :placeholder="field.description"
      size="small"
      :rows="field.typeField === 'TEXTAREA' ? 5 : undefined"
      fluid
      display="chip"
      class="w-full"
      filter
      locale="en-US"
      show-icon
      show-buttons
      iconDisplay="input"
      show-button-bar
      :dateFormat="'dd/MM/yy'"
      hour-format="12"
      :show-time="field.typeField === 'TIME' || field.typeField === 'DATETIME'"
      :min-date="field.minimumDateTime ? new Date(field.minimumDateTime) : undefined"
      :max-date="field.minimumDateTime ? new Date(field.minimumDateTime) : undefined"
      :type="field.typeField === 'EMAIL' ? 'email' : field.typeField === 'NUMBER' ? 'number' : undefined"
      variant="filled"
      :inline="field.typeField === 'COLOR'"
    />
    <span
      v-if="wasSubmitted && field.required && (proxyValue === null || proxyValue === '')"
      class="text-red-400 dark:text-red-400/80 text-sm"
    >
      Este campo es requerido
    </span>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import DatePicker from 'primevue/datepicker'
import Select from 'primevue/select'
import MultiSelect from 'primevue/multiselect'
import ColorPicker from 'primevue/colorpicker'
import Button from 'primevue/button'
import TextArea from 'primevue/textarea'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import TypeField from '@/api/common/interfaces/typeFields.ts'
import { useAuthStore } from '@/stores/auth.ts'
import { useEquipmentsStore } from '@/stores/equipments.ts'

const props = defineProps<{
  field: DataFieldInputDto,
  modelValue: any,
  wasSubmitted: boolean,
  isOther?: boolean
}>()

const equipmentsStore = useEquipmentsStore()

const emit = defineEmits(['update:modelValue', 'edit', 'delete'])

const inputComponent = computed(() => {
  switch (props.field.typeField) {
    case TypeField.TEXTFIELD: return InputText
    case TypeField.TEXTAREA: return TextArea
    case TypeField.EMAIL: return InputText
    case TypeField.NUMBER: return InputNumber
    case TypeField.DATE: return DatePicker
    case TypeField.TIME: return DatePicker
    case TypeField.DATETIME: return DatePicker
    case TypeField.SELECT: return Select
    case TypeField.MULTIPLESELECT: return MultiSelect
    case TypeField.COLOR: return ColorPicker
    default: return InputText
  }
})

const proxyValue = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>

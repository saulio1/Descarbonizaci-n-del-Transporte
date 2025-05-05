<template>
  <Dialog
    v-model:visible="equipmentsStore.isDialogUsageDataOpen"
    :style="{ width: '40rem' }"
    :header="
      equipmentsStore.dialogUsageDataMode === 'create' ? 'Crear dato de uso' : 'Editar dato de uso'
    "
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Inicio del uso <span class="text-error-500">*</span>
        </label>
        <DatePicker
          :max-date="new Date()"
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="usageStart"
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
            v-if="wasSubmitted && isNone(usageStart)"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="!isNone(usageStart) && !isNone(usageEnd) && usageStart! > usageEnd!"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser menor o igual que el fin del uso
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Fin del uso <span class="text-error-500">*</span>
        </label>
        <DatePicker
          :max-date="new Date()"
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="usageEnd"
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
            v-if="wasSubmitted && isNone(usageEnd)"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="!isNone(usageEnd) && !isNone(usageEnd) && usageStart! > usageEnd!"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Tiene que ser mayor o igual que el inicio del uso
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Horas de operabilidad ({{equipmentsStore.selectedEquipment?.usageDataLoadOrCapacityUsedMeasurementUnit ?? ''}}) <span class="text-error-500">*</span>
        </label>
        <InputNumber show-buttons :min="0" locale="en-US" aria-autocomplete="none" variant="filled" v-model="operatingHours" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && isNone(operatingHours)"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Energía consumida ({{equipmentsStore.selectedEquipment?.usageDataEnergyConsumedMeasurementUnit ?? ''}}) <span class="text-error-500">*</span>
        </label>
        <InputNumber show-buttons :min="0" locale="en-US" aria-autocomplete="none" variant="filled" v-model="energyConsumed" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && isNone(operatingHours)"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Operador <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="operator" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && isNone(operator) || operator.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Localización <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="location" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && isNone(location) || location.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Notas
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="notes" fluid />
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
        :label="equipmentsStore.dialogUsageDataMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="equipmentsStore.loadingUsageDataDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { v4 as uuidv4 } from 'uuid'
import { ref, watch } from 'vue'
import { useToastService } from '@/helpers/toast.helper.ts'
import { useEquipmentsStore } from '@/stores/equipments.ts'
import { Button, DatePicker, Dialog, InputText, InputNumber } from 'primevue'

const equipmentsStore = useEquipmentsStore()

const date = ref<Date | null>(new Date())
const usageStart = ref<Date | null>(new Date())
const usageEnd = ref<Date | null>(new Date())
const operatingHours = ref(0)
const energyConsumed = ref(0)
const loadOrCapacityUsed = ref(0)
const operator = ref('')
const notes = ref('')
const location = ref('')
const wasSubmitted = ref(false)

const initUsageData = () => {
  const usageData = equipmentsStore.selectedUsageData
  if (usageData) {
    date.value = new Date(usageData.date)
    usageStart.value = new Date(usageData.usageStart)
    usageEnd.value = new Date(usageData.usageEnd)
    operatingHours.value = usageData.operatingHours
    energyConsumed.value = usageData.energyConsumed
    loadOrCapacityUsed.value = usageData.loadOrCapacityUsed
    operator.value = usageData.operator
    notes.value = usageData.notes
    location.value = usageData.location
  } else {
    location.value = ''
    date.value = new Date()
    usageStart.value = new Date()
    usageEnd.value = new Date()
    operatingHours.value = 0
    energyConsumed.value = 0
    loadOrCapacityUsed.value = 0
    operator.value = ''
    notes.value = ''
  }
}

watch(
  () => equipmentsStore.selectedUsageData,
  () => {
    initUsageData()
  },
)

const close = () => {
  wasSubmitted.value = false
  initUsageData()
  equipmentsStore.closeUsageDataDialog()
}

function onDialogVisibilityChange(val: boolean) {
  equipmentsStore.isDialogUsageDataOpen = val
  if (!val) {
    close()
  }
}

const isNone = (val: any) => val === null || val === undefined

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    usageStart: isNone(usageStart.value),
    usageEnd: isNone(usageEnd.value),
    operatingHours: isNone(operatingHours.value),
    energyConsumed: isNone(energyConsumed.value),
    loadOrCapacityUsed: isNone(loadOrCapacityUsed.value),
    operator: isNone(operator.value) || operator.value.trim() === '',
    location: isNone(location.value) || location.value.trim() === '',
    validDates:
      !isNone(usageStart.value) && !isNone(usageEnd.value) && usageStart.value! > usageEnd.value!,
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
    switch (equipmentsStore.dialogUsageDataMode) {
      case 'create':
        await equipmentsStore.createUsageData({
          usageStart: usageStart.value!,
          usageEnd: usageEnd.value!,
          energyConsumed: energyConsumed.value,
          loadOrCapacityUsed: loadOrCapacityUsed.value,
          location: location.value,
          notes: notes.value,
          operatingHours: operatingHours.value,
          operator: operator.value,
          id: uuidv4(),
        })
        break
      case 'edit':
        await equipmentsStore.updateUsageData({
          usageStart: usageStart.value!,
          usageEnd: usageEnd.value!,
          energyConsumed: energyConsumed.value,
          loadOrCapacityUsed: loadOrCapacityUsed.value,
          location: location.value,
          notes: notes.value,
          operatingHours: operatingHours.value,
          operator: operator.value,
          id: equipmentsStore.selectedUsageData?.id ?? uuidv4(),
        })
        break
    }
    close()
  } catch {
  } finally {
  }
}
</script>

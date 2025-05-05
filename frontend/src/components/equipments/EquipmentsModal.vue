<template>
  <Dialog
    v-model:visible="equipmentsStore.isDialogOpen"
    :style="{ width: '40rem' }"
    :header="
      equipmentsStore.dialogMode === 'create'
        ? 'Crear equipamiento'
        : equipmentsStore.dialogMode === 'edit'
          ? 'Editar equipamiento'
          : 'Detalle del equipamiento'
    "
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div v-if="equipmentsStore.dialogMode !== 'view'">
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
              equipmentsStore.equipments.find((x) => x.name === name.trim()) &&
              equipmentsStore.selectedEquipment?.name !== name.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El equipamiento ya existe
          </span>
        </div>
      </div>
      <div v-if="equipmentsStore.dialogMode !== 'view'">
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
      <div v-if="equipmentsStore.dialogMode !== 'view'">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          UM de la energía consumida (DU) <span class="text-error-500">*</span>
        </label>
        <InputText
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="usageDataEnergyConsumedMeasurementUnit"
          fluid
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && usageDataEnergyConsumedMeasurementUnit.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div v-if="equipmentsStore.dialogMode !== 'view'">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          UM de la carga o capacidad usada (DU) <span class="text-error-500">*</span>
        </label>
        <InputText
          size="small"
          aria-autocomplete="none"
          variant="filled"
          v-model="usageDataLoadOrCapacityUsedMeasurementUnit"
          fluid
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && usageDataLoadOrCapacityUsedMeasurementUnit.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div v-if="equipmentsStore.dialogMode !== 'view'">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Tipo de equipamiento <span class="text-error-500">*</span>
        </label>
        <Select
          :disabled="equipmentsStore.dialogMode === 'edit'"
          size="small"
          v-model="typeEquipmentId"
          :options="typeEquipmentsOptions"
          optionLabel="name"
          filter
          placeholder="Seleccione el tipo de equipamiento"
          fluid
          optionValue="code"
        />
      </div>
    </div>

    <Divider v-if="typeEquipmentId" align="center" type="solid">
      <b>Campos de datos</b>
    </Divider>
    <DynamicDataField
      v-for="field in typeEquipments.find(x => x.id === typeEquipmentId)!.dataFields"
      :key="field.name"
      :field="field"
      v-model="data[field.name]"
      :wasSubmitted="wasSubmitted"
      :isOther="false"
    />


    <Divider v-if="otherDataFields.length > 0" align="center" type="solid">
      <b>Otros campos de datos</b>
    </Divider>
    <DynamicDataField
      v-for="field in otherDataFields"
      :key="field.name"
      :field="field"
      v-model="data[field.name]"
      :wasSubmitted="wasSubmitted"
      :isOther="true"
      @edit="updateDialog"
      @delete="deleteOtherDataField"
    />


    <div v-if="equipmentsStore.dialogMode !== 'view'" class="p-3">
      <Button
        v-if="useAuthStore().isModerator"
        @click="createDialog()"
        label="Agregar otro campo de dato"
        fluid
        severity="secondary"
        text
        size="small"
        icon="pi pi-plus"
      />
    </div>
    <template #footer>
      <Button
        :label="equipmentsStore.dialogMode === 'view' ? 'Cerrar' : 'Cancelar'"
        icon="pi pi-times"
        variant="outlined"
        severity="secondary"
        text
        @click="close()"
      />
      <Button
        v-if="equipmentsStore.dialogMode !== 'view'"
        :label="equipmentsStore.dialogMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="equipmentsStore.loadingDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
  <data-field-equipments-modal
    :data-fields="combinedDataFields"
    @create="addDataField"
    @update="updateDataField"
  />
</template>

<script setup lang="ts">
import { computed, ref, watch, watchEffect } from 'vue'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import { useToastService } from '@/helpers/toast.helper.ts'
import { useEquipmentsStore } from '@/stores/equipments.ts'
import type EquipmentDataFieldInputDto from '@/api/equipments/interfaces/input/equipment-data-field.input.dto.ts'
import type TypeEquipmentOutputDto from '@/api/typeEquipments/interfaces/output/type-equipment.output.dto.ts'
import DataFieldEquipmentsModal from '@/components/equipments/DataFieldEquipmentsModal.vue'
import { Button, Dialog, InputText, Select, Divider } from 'primevue'
import DynamicDataField from '@/components/equipments/DynamicDataField.vue'
import { useAuthStore } from '@/stores/auth.ts'

const props = defineProps<{
  typeEquipments: TypeEquipmentOutputDto[]
}>()
const equipmentsStore = useEquipmentsStore()

const name = ref('')
const description = ref('')
const usageDataEnergyConsumedMeasurementUnit = ref('')
const usageDataLoadOrCapacityUsedMeasurementUnit = ref('')
const typeEquipmentId = ref<string | null>(null)
const data = ref<Record<string, any>>({})
const otherDataFields = ref<DataFieldInputDto[]>([])
const wasSubmitted = ref(false)

const combinedDataFields = computed(() => {
  const te = props.typeEquipments.find((x) => x.id === typeEquipmentId.value)
  const data = [...otherDataFields.value]
  if (te) {
    data.push(...te.dataFields)
  }
  return data
})

const initEquipment = () => {
  const e = equipmentsStore.selectedEquipment
  if (e) {
    name.value = e.name
    description.value = e.description ?? ''
    otherDataFields.value = e.otherDataFields ?? []
    data.value = {}
    for (const d of e.data) {
      data.value[d.fieldName] = d.data
    }
    usageDataLoadOrCapacityUsedMeasurementUnit.value = e.usageDataLoadOrCapacityUsedMeasurementUnit
    usageDataEnergyConsumedMeasurementUnit.value = e.usageDataEnergyConsumedMeasurementUnit
    typeEquipmentId.value = e.typeEquipment.id
  } else {
    name.value = ''
    data.value = {}
    description.value = ''
    otherDataFields.value = []
    usageDataLoadOrCapacityUsedMeasurementUnit.value = ''
    usageDataEnergyConsumedMeasurementUnit.value = ''
    typeEquipmentId.value =
      [...props.typeEquipments].sort((a, b) => a.name.localeCompare(b.name)).find((x) => x)?.id ??
      null
  }
}

watchEffect(() => {
  const fields = [
    ...otherDataFields.value,
    ...(props.typeEquipments.find(x => x.id === typeEquipmentId.value)?.dataFields ?? [])
  ]
  const newData: Record<string, any> = {}
  for (const field of fields) {
    newData[field.name] = data.value[field.name] ?? null
  }
  data.value = newData
})

watch(
  () => equipmentsStore.selectedEquipment,
  () => {
    initEquipment()
  },
)

watch(
  () => props.typeEquipments,
  (x: any) => {
    if (!equipmentsStore.selectedEquipment)
      typeEquipmentId.value =
        [...x].sort((a, b) => a.name.localeCompare(b.name)).find((x) => x)?.id ?? null
  },
)

const typeEquipmentsOptions = computed(() => {
  const dat = props.typeEquipments.map((x) => ({
    name: x.name,
    code: x.id,
  }))
  dat.sort((a, b) => a.name.localeCompare(b.name))
  return dat
})

const close = () => {
  wasSubmitted.value = false
  equipmentsStore.selectedEquipment = null
  initEquipment()
  equipmentsStore.isDialogOpen = false
}

function onDialogVisibilityChange(val: boolean) {
  equipmentsStore.isDialogOpen = val
  if (!val) {
    close()
  }
}

const isNone = (val: any) => {
  return val === undefined || val === null
}

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    name: name.value.trim() === '',
    nameExists:
      equipmentsStore.equipments.find((x) => x.name === name.value.trim()) &&
      equipmentsStore.selectedEquipment?.name !== name.value.trim(),
    usageDataEnergyConsumedMeasurementUnit:
      usageDataEnergyConsumedMeasurementUnit.value.trim() === '',
    usageDataLoadOrCapacityUsedMeasurementUnit:
      usageDataLoadOrCapacityUsedMeasurementUnit.value.trim() === '',
  }

  if (Object.values(errors).some(Boolean)) return false

  const requiredFields = combinedDataFields.value.filter((x) => x.required).map((x) => x.name)
  for (const dat of data.value) {
    if (requiredFields.includes(dat.fieldName) && isNone(dat.data)) {
      return false
    }
  }
  return true
}

const getDataArray = () => {
  return Object.entries(data.value).map(([fieldName, value]) => ({
    fieldName,
    data: value
  }))
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
    switch (equipmentsStore.dialogMode) {
      case 'create':
        await equipmentsStore.createEquipment({
          name: name.value,
          description: description.value,
          usageDataLoadOrCapacityUsedMeasurementUnit:
            usageDataLoadOrCapacityUsedMeasurementUnit.value,
          data: getDataArray(),
          typeEquipmentId: typeEquipmentId.value!,
          otherDataFields: otherDataFields.value,
          usageDataEnergyConsumedMeasurementUnit: usageDataEnergyConsumedMeasurementUnit.value,
        })
        break
      case 'edit':
        await equipmentsStore.updateEquipment({
          name: name.value,
          description: description.value,
          usageDataLoadOrCapacityUsedMeasurementUnit:
            usageDataLoadOrCapacityUsedMeasurementUnit.value,
          data: getDataArray(),
          typeEquipmentId: typeEquipmentId.value!,
          otherDataFields: otherDataFields.value,
          usageDataEnergyConsumedMeasurementUnit: usageDataEnergyConsumedMeasurementUnit.value,
        })
        break
    }
    close()
  } catch {
  } finally {
  }
}

const createDialog = () => {
  equipmentsStore.openDialogDataField('create')
}

const updateDialog = (name: string) => {
  equipmentsStore.openDialogDataField(
    'edit',
    otherDataFields.value.find((x) => x.name === name),
  )
}

const addDataField = (dataField: DataFieldInputDto) => {
  otherDataFields.value.push(dataField)
}

const updateDataField = (dataField: DataFieldInputDto) => {
  otherDataFields.value[
    otherDataFields.value.findIndex((x) => x.name === equipmentsStore.selectedDataField!.name)
  ] = dataField
}

const deleteOtherDataField = (name: string) => {
  otherDataFields.value = otherDataFields.value.filter(f => f.name !== name)
}
</script>

<template>
  <Dialog
    v-model:visible="positionsStore.isDialogOpen"
    :style="{ width: '25rem' }"
    :header="positionsStore.dialogMode === 'create' ? 'Crear ocupación' : 'Editar ocupación'"
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
              positionsStore.positions.find((x) => x.name === name.trim()) &&
              positionsStore.selectedPosition?.name !== name.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            La ocupación ya existe
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
        :label="positionsStore.dialogMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="positionsStore.loadingDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useToastService } from '@/helpers/toast.helper.ts'
import { usePositionsStore } from '@/stores/workerPositions.ts'
import { Button, Dialog, InputText } from 'primevue'

const positionsStore = usePositionsStore()

const name = ref('')
const wasSubmitted = ref(false)

const initPosition = () => {
  const position = positionsStore.selectedPosition
  if (position) {
    name.value = position.name
  } else {
    name.value = ''
  }
}

watch(
  () => positionsStore.selectedPosition,
  () => {
    initPosition()
  },
)

const close = () => {
  wasSubmitted.value = false
  positionsStore.selectedPosition = null
  initPosition()
  positionsStore.isDialogOpen = false
}

function onDialogVisibilityChange(val: boolean) {
  positionsStore.isDialogOpen = val
  if (!val) {
    close()
  }
}

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    name: name.value.trim() === '',
    nameExists:
      positionsStore.positions.find((x) => x.name === name.value.trim()) &&
      positionsStore.selectedPosition?.name !== name.value.trim(),
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
    switch (positionsStore.dialogMode) {
      case 'create':
        await positionsStore.createPosition({
          name: name.value,
        })
        break
      case 'edit':
        await positionsStore.updatePosition({
          name: name.value,
        })
        break
    }
    close()
  } catch {
  } finally {
  }
}
</script>

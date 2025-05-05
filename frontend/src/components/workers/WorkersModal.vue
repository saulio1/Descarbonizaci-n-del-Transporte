<template>
  <Dialog
    v-model:visible="workersStore.isDialogOpen"
    :style="{ width: '30rem' }"
    :header="workersStore.dialogMode === 'create' ? 'Crear trabajador' : 'Editar trabajador'"
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Nombre <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="names" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && names.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Apellidos <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="lastNames" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && lastNames.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Carné de identidad <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="cid" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && cid.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="!!cid && !validateCi(cid.trim())"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El carné de identidad no es válido
          </span>
          <span
            v-if="
              workersStore.workers.find((x) => x.cid === cid.trim()) &&
              workersStore.selectedWorker?.cid !== cid.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El carné de identidad ya existe
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Número de teléfono <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="phoneNumber" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && phoneNumber.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="!!phoneNumber && !/^(\+53)?\s?\d{8}$/.test(phoneNumber.trim())"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El número de teléfono no es válido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Correo <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="email" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && email.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="
              workersStore.workers.find((x) => x.email === email.trim()) &&
              workersStore.selectedWorker?.email !== email.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El correo ya existe
          </span>

          <span
            v-if="!!email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El correo no es válido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Ocupación <span class="text-error-500">*</span>
        </label>
        <Select
          v-model="position"
          :options="positionOptions"
          optionLabel="name"
          filter
          :loading="loadingPositions"
          placeholder="Seleccione la ocupación"
          fluid
          optionValue="code"
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && position.trim() === ''"
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
        :label="workersStore.dialogMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="workersStore.loadingDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useToastService } from '@/helpers/toast.helper.ts'
import { useWorkersStore } from '@/stores/workers.ts'
import { usePositionsStore } from '@/stores/workerPositions.ts'
import { Button, Dialog, InputText, Select } from 'primevue'

function validateCi(ci: string): boolean {
  if (!/^\d{11}$/.test(ci)) return false;

  const aa = ci.slice(0, 2);
  const mm = ci.slice(2, 4);
  const dd = ci.slice(4, 6);
  const siglo = ci.charAt(6);

  let yyyy: number;
  if (siglo === '9') {
    yyyy = 1800 + Number(aa);
  } else if ('012345'.includes(siglo)) {
    yyyy = 1900 + Number(aa);
  } else if ('678'.includes(siglo)) {
    yyyy = 2000 + Number(aa);
  } else {
    return false;
  }

  const mes = Number(mm);
  const dia = Number(dd);

  if (mes < 1 || mes > 12) return false;

  const fecha = new Date(yyyy, mes - 1, dia);
  if (
    fecha.getFullYear() !== yyyy ||
    fecha.getMonth() + 1 !== mes ||
    fecha.getDate() !== dia
  ) {
    return false;
  }

  const hoy = new Date();
  fecha.setHours(0, 0, 0, 0);
  hoy.setHours(0, 0, 0, 0);
  return fecha <= hoy;
}


const workersStore = useWorkersStore()

const names = ref('')
const lastNames = ref('')
const cid = ref('')
const phoneNumber = ref('')
const email = ref('')
const position = ref('')
const loadingPositions = ref(false)
const wasSubmitted = ref(false)

async function getPositions(){
  loadingPositions.value = true
  const positionsStore = usePositionsStore()
  await positionsStore.fetchPositions()
  const data = positionsStore.positions.map(x => ({
    name: x.name,
    code: x.id
  }))
  loadingPositions.value = false
  return data
}

const positionOptions = ref<{ name: string; code: string }[]>([])

const initWorker = () => {
  const worker = workersStore.selectedWorker
  if (worker) {
    names.value = worker.names
    lastNames.value = worker.lastNames
    cid.value = worker.cid
    phoneNumber.value = worker.phoneNumber
    email.value = worker.email
    position.value = worker.position.id
  } else {
    names.value = ''
    lastNames.value = ''
    cid.value = ''
    phoneNumber.value = ''
    email.value = ''
    position.value = ''
  }
}

watch(
  () => workersStore.selectedWorker,
  () => {
    initWorker()
  },
)

const close = () => {
  wasSubmitted.value = false
  workersStore.selectedWorker = null
  initWorker()
  workersStore.isDialogOpen = false
}

function onDialogVisibilityChange(val: boolean) {
  workersStore.isDialogOpen = val
  if (!val) {
    close()
  }
}

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    names: names.value.trim() === '',
    lastNames: lastNames.value.trim() === '',
    phoneNumber: phoneNumber.value === '',
    phoneNumberValid: !/^(\+53)?\s?\d{8}$/.test(phoneNumber.value.trim()),
    position: position.value.trim() === '',
    cid: cid.value.trim() === '',
    cidValid: !validateCi(cid.value),
    cidExists: workersStore.workers.find(x => x.cid === cid.value.trim()) &&
      workersStore.selectedWorker?.cid !== cid.value.trim(),
    email: email.value.trim() === '',
    emailValid: !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim()),
    emailExists:
      workersStore.workers.find((x) => x.email === email.value.trim()) &&
      workersStore.selectedWorker?.email !== email.value.trim(),
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
    switch (workersStore.dialogMode) {
      case 'create':
        await workersStore.createWorker({
          email: email.value,
          names: names.value,
          phoneNumber: phoneNumber.value,
          cid: cid.value,
          lastNames: lastNames.value,
          positionId: position.value,
        })
        break
      case 'edit':
        await workersStore.updateWorker({
          email: email.value,
          names: names.value,
          phoneNumber: phoneNumber.value,
          cid: cid.value,
          lastNames: lastNames.value,
          positionId: position.value,
        })
        break
    }
    close()
  } catch {
  } finally {
  }
}

onMounted(async () => {
  positionOptions.value = await getPositions()
})

</script>

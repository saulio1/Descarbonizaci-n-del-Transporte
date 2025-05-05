<template>
  <Dialog
    v-model:visible="auth.isChangePasswordOpen"
    @hide="close()"
    :style="{ width: '30rem' }"
    header="Cambiar contraseña"
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Contraseña anterior <span class="text-error-500">*</span>
        </label>
        <Password
          aria-autocomplete="none"
          variant="filled"
          :feedback="false"
          v-model="currentPassword"
          toggle-mask
          fluid
        />
        <span
          v-if="wasSubmitted && currentPassword.trim() === ''"
          class="text-red-400 dark:text-red-400/80 text-sm"
        >
          Este campo es requerido
        </span>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Nueva contraseña <span class="text-error-500">*</span>
        </label>
        <Password
          aria-autocomplete="none"
          variant="filled"
          v-model="newPassword"
          toggle-mask
          fluid
          prompt-label="Introduce tu nueva contraseña"
          weakLabel="Demasiado simple"
          mediumLabel="Complejidad media"
          strongLabel="Contraseña fuerte"
        >
          <template #header>
            <div class="font-semibold text-xm mb-4">Medidor de contraseñas</div>
          </template>
          <template #footer>
            <Divider />
            <ul class="pl-2 my-0 leading-normal text-sm">
              <li :class="getRuleClass(passwordRules.hasUpperCase)">
                Al menos una letra mayúscula
              </li>
              <li :class="getRuleClass(passwordRules.hasLowerCase)">
                Al menos una letra minúscula
              </li>
              <li :class="getRuleClass(passwordRules.hasNumber)">Al menos un número</li>
              <li :class="getRuleClass(passwordRules.hasMinLength)">Mínimo 8 caracteres</li>
              <li :class="getRuleClass(passwordRules.hasMaxLength)">Máximo 120 caracteres</li>
            </ul>
          </template>
        </Password>
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && !isPasswordStrong"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            La contraseña debe cumplir con los requisitos
          </span>

          <span
            v-if="wasSubmitted && newPassword.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Confirmar contraseña <span class="text-error-500">*</span>
        </label>
        <Password
          aria-autocomplete="none"
          variant="filled"
          :feedback="false"
          v-model="repeatPassword"
          toggle-mask
          fluid
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="repeatPassword !== newPassword"
            class="text-red-400 dark:text-red-400/80 text-sm"
            >Debe coincidir con la nueva contraseña</span
          >
          <span
            v-if="wasSubmitted && repeatPassword.trim() === ''"
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
        label="Guardar Cambios"
        type="button"
        severity="warn"
        :loading="loading"
        icon="pi pi-check"
        @click="saveProfile()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { Button, Dialog, Divider, Password } from 'primevue'
import { computed, ref } from 'vue'
import { useToastService } from '@/helpers/toast.helper.ts'
import { useAuthStore } from '@/stores/auth.ts'

const auth = useAuthStore()
const currentPassword = ref('')
const newPassword = ref('')
const repeatPassword = ref('')
const wasSubmitted = ref(false)
const loading = ref(false)

const close = () => {
  wasSubmitted.value = false
  currentPassword.value = ''
  newPassword.value = ''
  repeatPassword.value = ''
  auth.isChangePasswordOpen = false
}

function onDialogVisibilityChange(val: boolean) {
  auth.isChangePasswordOpen = val
  if (!val) {
    close()
  }
}

const passwordRules = {
  hasUpperCase: (pwd: string) => /[A-Z]/.test(pwd),
  hasLowerCase: (pwd: string) => /[a-z]/.test(pwd),
  hasNumber: (pwd: string) => /[0-9]/.test(pwd),
  hasMinLength: (pwd: string) => pwd.length >= 8,
  hasMaxLength: (pwd: string) => pwd.length <= 120,
}

const validateForm = () => {
  wasSubmitted.value = true

  const errors = {
    current: currentPassword.value.trim() === '',
    new: newPassword.value.trim() === '',
    repeat: repeatPassword.value.trim() === '' || repeatPassword.value !== newPassword.value,
    strength: !Object.values(passwordRules).every((rule) => rule(newPassword.value)),
  }

  return !Object.values(errors).some(Boolean)
}

const saveProfile = async () => {
  loading.value = true
  if (!validateForm()) {
    useToastService({
      severity: 'error',
      summary: 'Campos incorrectos',
      detail: 'Revise los campos y vuelva a intentarlo.',
    }).showToast()
    loading.value = false
    return
  }
  try {
    await auth.changePassword(currentPassword.value, newPassword.value)
    close()
  } catch {
  } finally {
    loading.value = false
  }
}

const isPasswordStrong = computed(() =>
  Object.values(passwordRules).every((rule) => rule(newPassword.value)),
)

const getRuleClass = (rule: (pwd: string) => boolean) => {
  return rule(newPassword.value)
    ? 'text-green-500 dark:text-green-400'
    : 'text-red-400 dark:text-red-400/80'
}
</script>

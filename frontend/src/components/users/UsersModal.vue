<template>
  <Dialog
    v-model:visible="usersStore.isDialogOpen"
    :style="{ width: '30rem' }"
    :header="usersStore.dialogMode === 'create' ? 'Crear usuario' : 'Editar usuario'"
    :modal="true"
    @update:visible="onDialogVisibilityChange"
    :dismissableMask="true"
  >
    <div class="flex flex-col gap-6">
      <div>
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Nombre de usuario <span class="text-error-500">*</span>
        </label>
        <InputText aria-autocomplete="none" variant="filled" v-model="username" fluid />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && username.trim() === ''"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
          <span
            v-if="
              usersStore.users.find((x) => x.username === username.trim()) &&
              usersStore.selectedUser?.username !== username.trim()
            "
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            El nombre de usuario ya existe
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
              usersStore.users.find((x) => x.email === email.trim()) &&
              usersStore.selectedUser?.email !== email.trim()
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
          Roles <span class="text-error-500">*</span>
        </label>
        <MultiSelect
          v-model="roles"
          display="chip"
          :options="roleOptions"
          optionLabel="name"
          filter
          placeholder="Seleccione los roles"
          :maxSelectedLabels="3"
          fluid
          optionValue="code"
        />
        <div class="flex flex-col gap-1">
          <span
            v-if="wasSubmitted && roles.length === 0"
            class="text-red-400 dark:text-red-400/80 text-sm"
          >
            Este campo es requerido
          </span>
        </div>
      </div>
      <div v-if="usersStore.dialogMode === 'create'">
        <label class="mb-1.5 block text-sm font-medium text-gray-700 dark:text-gray-400">
          Contraseña <span class="text-error-500">*</span>
        </label>
        <Password
          aria-autocomplete="none"
          variant="filled"
          v-model="password"
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
            v-if="wasSubmitted && password.trim() === ''"
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
        :label="usersStore.dialogMode === 'create' ? 'Crear' : 'Guardar Cambios'"
        type="button"
        severity="warn"
        :loading="usersStore.loadingDialog"
        icon="pi pi-check"
        @click="save()"
      />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { useToastService } from '@/helpers/toast.helper.ts'
import { computed, ref, watch } from 'vue'
import { useUsersStore } from '@/stores/users.ts'
import { Button, Dialog, Divider, InputText, MultiSelect, Password } from 'primevue'
import Role from '@/api/common/interfaces/roles.ts'

const usersStore = useUsersStore()

const email = ref('')
const username = ref('')
const roles = ref([])
const password = ref('')
const wasSubmitted = ref(false)

const roleOptions = [
  { name: 'Admin', code: Role.ROLE_ADMIN },
  { name: 'Moderador', code: Role.ROLE_MODERATOR },
  { name: 'Visitante', code: Role.ROLE_USER },
]

const initUser = () => {
  const user = usersStore.selectedUser
  if (user) {
    email.value = user.email
    username.value = user.username
    roles.value = user.roles
    password.value = ''
  } else {
    email.value = ''
    username.value = ''
    roles.value = ''
    password.value = ''
  }
}

watch(
  () => usersStore.selectedUser,
  () => {
    initUser()
  },
)

const close = () => {
  wasSubmitted.value = false
  usersStore.selectedUser = null
  initUser()
  usersStore.isDialogOpen = false
}

function onDialogVisibilityChange(val: boolean) {
  usersStore.isDialogOpen = val
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
    username: username.value.trim() === '',
    usernameExists:
      usersStore.users.find((x) => x.username === username.value.trim()) &&
      usersStore.selectedUser?.username !== username.value.trim(),
    email: email.value.trim() === '',
    emailValid: !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim()),
    emailExists:
      usersStore.users.find((x) => x.email === email.value.trim()) &&
      usersStore.selectedUser?.email !== email.value.trim(),
    password: password.value.trim() === '' && usersStore.dialogMode === 'create',
    roles: roles.value.length === 0,
    strength:
      !Object.values(passwordRules).every((rule) => rule(password.value)) &&
      usersStore.dialogMode === 'create',
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
    switch (usersStore.dialogMode) {
      case 'create':
        await usersStore.createUser({
          email: email.value,
          password: password.value,
          roles: roles.value,
          username: username.value,
        })
        break
      case 'edit':
        await usersStore.updateUser({
          email: email.value,
          username: username.value,
          roles: roles.value,
        })
        break
    }
    close()
  } catch {
  } finally {
  }
}

const isPasswordStrong = computed(() =>
  Object.values(passwordRules).every((rule) => rule(password.value)),
)

const getRuleClass = (rule: (pwd: string) => boolean) => {
  return rule(password.value)
    ? 'text-green-500 dark:text-green-400'
    : 'text-red-400 dark:text-red-400/80'
}
</script>

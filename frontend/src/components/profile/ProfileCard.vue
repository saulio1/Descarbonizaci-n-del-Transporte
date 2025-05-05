<template>
  <div>
    <div class="p-5 mb-6 border border-gray-200 rounded-2xl dark:border-gray-800 lg:p-6">
      <div class="flex flex-col gap-5 xl:flex-row xl:items-center xl:justify-between">
        <div class="flex flex-col items-center w-full gap-6 xl:flex-row">
          <div
            class="w-20 h-20 overflow-hidden border border-gray-200 rounded-full dark:border-gray-800"
          >
            <img src="/images/user/owner.jpeg" alt="user" />
          </div>
          <div class="order-3 xl:order-2">
            <h4
              class="mb-2 text-lg font-semibold text-center text-gray-800 dark:text-white/90 xl:text-left"
            >
              {{ auth.username }}
            </h4>
            <div
              class="flex flex-col items-center gap-1 text-center xl:flex-row xl:gap-3 xl:text-left"
            >
              <template v-for="(role, index) in getRoles()" :key="role">
                <p class="text-sm text-gray-500 dark:text-gray-400">{{ role }}</p>
                <div
                  v-if="index < getRoles().length - 1"
                  class="hidden h-3.5 w-px bg-gray-300 dark:bg-gray-700 xl:block"
                ></div>
              </template>
            </div>
          </div>
        </div>
        <button @click="auth.isChangePasswordOpen = true" class="edit-button">
          <SettingsIcon />
          Cambiar contraseña
        </button>
      </div>
    </div>
    <ChangePasswordModal />
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.ts'
import { SettingsIcon } from '@/icons'
import ChangePasswordModal from '@/components/profile/ChangePasswordModal.vue'
import Role from '@/api/common/interfaces/roles.ts'

const auth = useAuthStore()

const getRoles = () => {
  const roles = []
  if (auth.roles!.includes(Role.ROLE_ADMIN)) roles.push('Administrador')
  if (auth.roles!.includes(Role.ROLE_MODERATOR)) roles.push('Moderador')
  if (auth.roles!.includes(Role.ROLE_USER)) roles.push('Visitante')

  return roles
}

auth.isChangePasswordOpen = false
</script>

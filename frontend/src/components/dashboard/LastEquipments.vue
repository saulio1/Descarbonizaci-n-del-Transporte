<template>
  <div
    class="overflow-hidden rounded-2xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6"
  >
    <div class="flex flex-col gap-2 mb-4 sm:flex-row sm:items-center sm:justify-between">
      <div>
        <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90">Equipamientos Recientes</h3>
      </div>
    </div>

    <div class="max-w-full overflow-x-auto custom-scrollbar">
      <div v-if="reportsStore.loadingLastEquipments" class="flex justify-center py-6">
        <svg class="animate-spin h-6 w-6 text-gray-500 dark:text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
        </svg>
      </div>

      <div v-else-if="!reportsStore.equipments.length" class="py-6 text-center text-gray-500 dark:text-gray-400">
        No hay equipamientos recientes para mostrar.
      </div>

      <table v-else class="min-w-full">
        <thead>
        <tr class="border-t border-gray-100 dark:border-gray-800">
          <th class="py-3 text-left">
            <p class="font-extrabold text-gray-500 text-theme-sm dark:text-gray-400">Nombre</p>
          </th>
          <th class="py-3 text-left">
            <p class="font-extrabold text-gray-500 text-theme-sm dark:text-gray-400">Descripción</p>
          </th>
          <th class="py-3 text-left">
            <p class="font-extrabold text-gray-500 text-theme-sm dark:text-gray-400">Tipo de Equipamiento</p>
          </th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="(item, index) in reportsStore.lastEquipments"
          :key="index"
          class="border-t border-gray-100 dark:border-gray-800"
        >
          <td class="py-3 whitespace-nowrap">
            <p class="text-gray-500 text-theme-sm dark:text-gray-400">{{ item.name }}</p>
          </td>
          <td class="py-3 whitespace-nowrap">
            <p class="text-gray-500 text-theme-sm dark:text-gray-400">{{ item.description || '---' }}</p>
          </td>
          <td class="py-3 whitespace-nowrap">
            <p class="text-gray-500 text-theme-sm dark:text-gray-400">{{ item.typeEquipment }}</p>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useReportsStore } from '@/stores/reports.ts'

const reportsStore = useReportsStore();
</script>

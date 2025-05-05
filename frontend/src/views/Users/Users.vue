<template>
  <admin-layout>
    <PageBreadcrumb :pageTitle="currentPageTitle" />

    <div
      class="rounded-2xl border border-gray-200 bg-white p-5 dark:border-gray-800 dark:bg-white/[0.03] lg:p-6"
    >
      <h3 class="mb-5 text-lg font-semibold text-gray-800 dark:text-white/90 lg:mb-7">
        Listado de Usuarios
      </h3>
      <users-table />
    </div>
  </admin-layout>
</template>

<script setup lang="ts">
import PageBreadcrumb from '@/components/common/PageBreadcrumb.vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { ref } from 'vue'
import UsersTable from '@/components/users/UsersTable.vue'

const currentPageTitle = ref('Usuarios')
</script>

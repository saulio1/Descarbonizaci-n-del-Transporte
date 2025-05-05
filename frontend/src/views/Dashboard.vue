<template>
  <admin-layout>
    <div class="grid grid-cols-1 xl:grid-cols-12 gap-6 min-h-[340px]">
      <div class="flex flex-col gap-6 xl:col-span-3 h-full">
        <metrics class="flex-1" />
      </div>
      <div class="xl:col-span-9 h-full flex">
        <equipment-by-type class="flex-1 h-full" />
      </div>
    </div>

    <div class="grid grid-cols-1 xl:grid-cols-2 gap-6 mt-6">
      <recent-equipments />
      <recent-workers />
    </div>

    <div class="mt-6">
      <use-data-chart />
    </div>
  </admin-layout>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import RecentWorkers from '@/components/dashboard/LastWorkers.vue'
import RecentEquipments from '@/components/dashboard/LastEquipments.vue'
import EquipmentByType from '@/components/dashboard/EquipmentByType.vue'
import Metrics from '@/components/dashboard/Metrics.vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useReportsStore } from '@/stores/reports.ts'
import UseDataChart from '@/components/dashboard/UseDataChart.vue'

onMounted(async () => {
  await useReportsStore().fetchAll()
})
</script>

<template>
  <admin-layout>
    <PageBreadcrumb :pageTitle="currentPageTitle" />
    <div
      class="rounded-2xl border border-gray-200 bg-white p-5 dark:border-gray-800 dark:bg-white/[0.03] lg:p-6"
    >
      <h3 class="mb-5 text-lg font-semibold text-gray-800 dark:text-white/90 lg:mb-7">
        Datos de uso de los equipamientos
      </h3>
      <div class="flex items-center justify-between p-6">
        <IftaLabel>
          <Select
            variant="filled"
            v-model="selectedEquipmentId"
            :options="equipmentOptions"
            optionLabel="name"
            filter
            :loading="loadingEquipments"
            placeholder="Seleccione el equipamiento"
            optionValue="code"
            inputId="equipment"
          />
          <label for="equipment">Equipamiento</label>
        </IftaLabel>
        <Button
          severity="primary"
          :loading="loadingEquipments"
          icon="pi pi-external-link"
          label="Exportar datos"
          @click="equipmentsStore.openExportDialog()"
        />
      </div>
      <usage-data-table />
      <export-usage-modal />
    </div>
  </admin-layout>
</template>

<script setup lang="ts">
import AdminLayout from '@/components/layout/AdminLayout.vue'
import PageBreadcrumb from '@/components/common/PageBreadcrumb.vue'
import { onBeforeUnmount, onMounted, ref, watch, watchEffect } from 'vue'
import UsageDataTable from '@/components/usage-data/UsageDataTable.vue'
import ExportUsageModal from '@/components/usage-data/ExportUsageModal.vue'
import { useEquipmentsStore } from '@/stores/equipments.ts'
import { IftaLabel, Select } from 'primevue'

const currentPageTitle = ref('Datos de uso')
const selectedEquipmentId = ref<string>('')
const equipmentsStore = useEquipmentsStore()

const loadingEquipments = ref(false)
const equipmentOptions = ref<{ name: string; code: string }[]>([])

const stopWatch = watchEffect(
  () => {
    equipmentsStore.selectedEquipment = equipmentsStore.equipments.find(x => x.id === selectedEquipmentId.value)!
    equipmentsStore.fetchUsageData()
  },
)

onMounted(async () => {
  loadingEquipments.value = true
  await equipmentsStore.fetchEquipments()
  equipmentOptions.value = equipmentsStore.equipments
    .map((x) => ({
      name: x.name,
      code: x.id,
    }))
    .sort((a, b) => a.name.localeCompare(b.name))
  loadingEquipments.value = false
})

onBeforeUnmount(() => stopWatch());
</script>

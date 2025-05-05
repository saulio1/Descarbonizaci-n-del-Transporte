<template>
  <div
    class="rounded-2xl border border-gray-200 bg-white px-5 pb-5 pt-5 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6"
  >
    <div class="flex flex-col gap-5 mb-6 sm:flex-row sm:justify-between">
      <div class="w-full flex items-center">
        <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90 flex-1">
          Análisis de los datos de uso de los equipamientos
        </h3>
        <Menu
          :model="menuItems"
          popup
          ref="menu"
          class="ml-2"
        >
          <template #item="{ item }">
            <button
              class="flex items-center w-full px-3 py-2 hover:bg-gray-100 dark:hover:bg-gray-800"
              @click="item.command"
            >
              <span class="mr-2">
                <i :class="item.icon"></i>
              </span>
              {{ item.label }}
            </button>
          </template>
        </Menu>
        <button
          class="ml-2 px-2 py-1 rounded hover:bg-gray-100 dark:hover:bg-gray-800"
          @click="toggleMenu"
          aria-label="Descargar"
        >
          <svg width="20" height="20" fill="currentColor" class="text-gray-400"><circle cx="4" cy="10" r="2"/><circle cx="10" cy="10" r="2"/><circle cx="16" cy="10" r="2"/></svg>
        </button>
      </div>
      <div class="w-full sm:w-72">
        <Select
          v-model="selectedEquipment"
          :options="equipmentOptions"
          optionLabel="name"
          optionValue="id"
          placeholder="Seleccionar equipamiento"
          :loading="loadingEquipments"
          variant="filled"
          class="w-full"
          @change="onEquipmentChange"
        />
      </div>
    </div>

    <div v-if="loadingUsageData" class="flex justify-center items-center min-h-[250px]">
      <LoadingSpinner />
    </div>
    <div v-else-if="!selectedEquipment">
      <div class="flex flex-col items-center justify-center min-h-[250px] text-gray-400 dark:text-gray-500">
        <span>Seleccione un equipamiento para ver los datos</span>
      </div>
    </div>
    <div v-else-if="!usageData || usageData.length === 0">
      <div class="flex flex-col items-center justify-center min-h-[250px] text-gray-400 dark:text-gray-500">
        <span>No hay datos de uso para este equipamiento.</span>
      </div>
    </div>
    <div v-else class="max-w-full overflow-x-auto custom-scrollbar">
      <div id="chartThree" class="min-w-[1000px] xl:min-w-full pl-2">
        <VueApexCharts
          ref="chartRef"
          type="area"
          height="310"
          :options="chartOptions"
          :series="chartSeries"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useReportsStore } from '@/stores/reports.ts'
import Select from 'primevue/select'
import Menu from 'primevue/menu'
import VueApexCharts from 'vue3-apexcharts'

const LoadingSpinner = {
  template: `
    <svg class="animate-spin h-8 w-8 text-gray-400 dark:text-gray-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"></path>
    </svg>
  `
}

const reportsStore = useReportsStore()

const chartRef = ref<any>(null)
const menu = ref()

const equipmentOptions = computed(() => reportsStore.equipments)
const loadingEquipments = computed(() => reportsStore.loadingEquipments)

const selectedEquipment = ref<string | null>(null)
const usageData = computed(() => reportsStore.usageData)
const loadingUsageData = computed(() => reportsStore.loadingUsageData)

const selectedEquipmentObj = computed(() =>
  reportsStore.equipments.find(e => e.id === selectedEquipment.value)
)
const energyUnit = computed(() => selectedEquipmentObj.value?.usageDataEnergyConsumedMeasurementUnit || '')
const capacityUnit = computed(() => selectedEquipmentObj.value?.usageDataLoadOrCapacityUsedMeasurementUnit || '')

const chartSeries = computed(() => {
  if (!usageData.value) return []
  return [
    {
      name: 'Horas de operabilidad',
      data: usageData.value.map(d => d.operatingHours)
    },
    {
      name: `Energía consumida (${energyUnit.value})`,
      data: usageData.value.map(d => d.energyConsumed)
    },
    {
      name: `Carga/capacidad usada (${capacityUnit.value})`,
      data: usageData.value.map(d => d.loadOrCapacityUsed)
    }
  ]
})

const categories = computed(() =>
  usageData.value ? usageData.value.map(d => new Date(d.date).toLocaleDateString()) : []
)

const chartOptions = computed(() => ({
  legend: {
    show: true,
    position: 'top',
    horizontalAlign: 'left',
  },
  colors: ['#465FFF', '#9CB9FF', '#00C49A'],
  chart: {
    fontFamily: 'Outfit, sans-serif',
    type: 'area',
    toolbar: { show: false },
  },
  fill: {
    gradient: {
      enabled: true,
      opacityFrom: 0.55,
      opacityTo: 0,
    },
  },
  stroke: {
    curve: 'straight',
    width: [2, 2, 2],
  },
  dataLabels: {
    enabled: true,
    style: {
      fontSize: '13px',
      fontWeight: 'bold',
    },
    formatter: (val: number, opts: any) => {
      if (opts.seriesIndex === 1) return `${val} ${energyUnit.value}`
      if (opts.seriesIndex === 2) return `${val} ${capacityUnit.value}`
      return val
    }
  },
  grid: {
    xaxis: { lines: { show: false } },
    yaxis: { lines: { show: true } },
  },
  tooltip: {
    shared: true,
    intersect: false,
    y: {
      formatter: (val: number, opts: any) => {
        if (opts.seriesIndex === 1) return `${val} ${energyUnit.value}`
        if (opts.seriesIndex === 2) return `${val} ${capacityUnit.value}`
        return val
      }
    }
  },
  xaxis: {
    type: 'category',
    categories: categories.value,
    axisBorder: { show: false },
    axisTicks: { show: false },
    tooltip: { enabled: false },
  },
  yaxis: {
    title: { style: { fontSize: '0px' } },
  },
}))

onMounted(() => {
  if (!equipmentOptions.value.length) {
    reportsStore.fetchEquipments()
  }
})

async function onEquipmentChange() {
  const eq = reportsStore.equipments.find(e => e.id === selectedEquipment.value)
  reportsStore.selectedEquipment = eq || null
  if (eq) await reportsStore.fetchUsageData()
}

watch(selectedEquipment, async (newVal) => {
  if (newVal) await onEquipmentChange()
})

function downloadPNG() {
  if (!chartRef.value) return
  chartRef.value.dataURI().then(({ imgURI }) => {
    const link = document.createElement('a')
    link.href = imgURI
    link.download = 'equipamiento-uso.png'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  })
}

function downloadCSV() {
  if (!usageData.value || !selectedEquipmentObj.value) return
  const headers = [
    'Fecha',
    'Horas de operabilidad',
    `Energía consumida (${energyUnit.value})`,
    `Carga/capacidad usada (${capacityUnit.value})`
  ]
  const rows = usageData.value.map(d => [
    new Date(d.date).toLocaleDateString(),
    d.operatingHours,
    d.energyConsumed,
    d.loadOrCapacityUsed
  ])
  const csvContent =
    [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = 'equipamiento-uso.csv'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

const menuItems = [
  {
    label: 'Descargar PNG',
    icon: 'pi pi-image',
    command: downloadPNG
  },
  {
    label: 'Descargar CSV',
    icon: 'pi pi-file-excel',
    command: downloadCSV
  }
]

function toggleMenu(event: MouseEvent) {
  menu.value.toggle(event)
}
</script>

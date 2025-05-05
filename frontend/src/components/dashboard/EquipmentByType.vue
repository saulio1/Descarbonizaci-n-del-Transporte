<template>
  <div
    class="overflow-hidden rounded-2xl border border-gray-200 bg-white px-5 pt-5 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6"
  >
    <div class="flex items-center justify-between">
      <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90">
        Equipamientos por tipo de equipamiento
      </h3>
      <div class="flex items-center">
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
          <svg width="20" height="20" fill="currentColor" class="text-gray-400">
            <circle cx="4" cy="10" r="2"/>
            <circle cx="10" cy="10" r="2"/>
            <circle cx="16" cy="10" r="2"/>
          </svg>
        </button>
      </div>
    </div>

    <div class="max-w-full overflow-x-auto custom-scrollbar">
      <div id="chartOne" class="-ml-5 min-w-[650px] xl:min-w-full pl-2 relative">
        <template v-if="loadingCountEquipments">
          <div class="absolute inset-0 flex items-center justify-center bg-white/70 dark:bg-black/50 z-10 rounded-2xl">
            <svg
              class="animate-spin h-8 w-8 text-gray-600 dark:text-gray-300"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                class="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                stroke-width="4"
              ></circle>
              <path
                class="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"
              ></path>
            </svg>
          </div>
        </template>
        <VueApexCharts
          ref="chartRef"
          type="bar"
          height="360"
          :options="chartOptions"
          :series="series"
          v-else
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import Menu from 'primevue/menu'
import VueApexCharts from 'vue3-apexcharts'
import { useReportsStore } from '@/stores/reports.ts'

const reportsService = useReportsStore()
const chartRef = ref<any | null>(null)
const menu = ref()

const loadingCountEquipments = computed(() => reportsService.loadingCountEquipments)

const series = computed(() => [
  {
    name: 'Cantidad',
    data: reportsService.countEquipments.map((item) => item.quantity),
  },
])

const chartOptions = computed(() => ({
  colors: ['#465fff'],
  chart: {
    fontFamily: 'Outfit, sans-serif',
    type: 'bar',
    toolbar: { show: false },
  },
  plotOptions: {
    bar: {
      horizontal: false,
      columnWidth: '39%',
      borderRadius: 5,
      borderRadiusApplication: 'end',
    },
  },
  dataLabels: { enabled: false },
  stroke: { show: true, width: 4, colors: ['transparent'] },
  xaxis: {
    categories: reportsService.countEquipments.map((item) => item.typeEquipment),
    axisBorder: { show: false },
    axisTicks: { show: false },
  },
  legend: {
    show: true,
    position: 'top',
    horizontalAlign: 'left',
    fontFamily: 'Outfit',
    markers: { radius: 99 },
  },
  yaxis: { title: false },
  grid: { yaxis: { lines: { show: true } } },
  fill: { opacity: 1 },
  tooltip: {
    x: { show: false },
    y: {
      formatter: (val: number) => val.toString(),
    },
  },
}))

function downloadPNG() {
  if (!chartRef.value) return
  chartRef.value.dataURI().then(({ imgURI }) => {
    const link = document.createElement('a')
    link.href = imgURI
    link.download = 'equipments-by-type.png'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  })
}

function downloadCSV() {
  const headers = ['Tipo de Equipamiento', 'Cantidad']
  const rows = reportsService.countEquipments.map((item) => [
    `"${item.typeEquipment}"`,
    item.quantity,
  ])
  const csvContent =
    [headers.join(','), ...rows.map((r) => r.join(','))].join('\n')

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = 'equipments-by-type.csv'
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

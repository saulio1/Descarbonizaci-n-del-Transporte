import type EquipmentOutputDto from '@/api/equipments/interfaces/output/equipment.output.dto.ts'
import type TotalWorkersReportOutputDto from '@/api/reports/interfaces/output/total-workers.report.output.dto.ts'
import type TotalEquipmentsReportOutputDto from '@/api/reports/interfaces/output/total-equipments.report.output.dto.ts'
import type WorkerOutputDto from '@/api/workers/interfaces/output/worker.output.dto.ts'
import type EquipmentByTypeReportOutputDto from '@/api/reports/interfaces/output/equipment-by-type.report.output.dto.ts'
import type EquipmentUsageDataOutputDto from '@/api/equipments/interfaces/output/equipment-usage-data.output.dto.ts'
import { defineStore } from 'pinia'
import { getAllEquipments } from '@/api/equipments/services/equipments.service.ts'
import {
  reportCountEquipments,
  reportLastEquipments,
  reportLastWorkers,
  reportTotalEquipments,
  reportTotalWorkers,
  reportUsageData,
} from '@/api/reports/services/reports.service.ts'
import type LastEquipmentsReportOutputDto
  from '@/api/reports/interfaces/output/last-equipments.report.output.dto.ts'
import type LastWorkersReportOutputDto
  from '@/api/reports/interfaces/output/last-workers.report.output.dto.ts'
import type EquipmentUsageDataItemOutputDto
  from '@/api/equipments/interfaces/output/equipment-usage-data-item.output.dto.ts'

interface ReportsState {
  totalWorkers: TotalWorkersReportOutputDto | null
  loadingTotalWorkers: boolean
  totalEquipments: TotalEquipmentsReportOutputDto | null
  loadingTotalEquipments: boolean
  lastWorkers: LastWorkersReportOutputDto[]
  loadingLastWorkers: boolean
  lastEquipments: LastEquipmentsReportOutputDto[]
  loadingLastEquipments: boolean
  countEquipments: EquipmentByTypeReportOutputDto[]
  loadingCountEquipments: boolean
  usageData: EquipmentUsageDataItemOutputDto[] | null
  loadingUsageData: boolean
  equipments: EquipmentOutputDto[]
  loadingEquipments: boolean
  selectedEquipment: EquipmentOutputDto | null
}

export const useReportsStore = defineStore('reports', {
  state: (): ReportsState => ({
    equipments: [],
    loadingEquipments: false,
    selectedEquipment: null,
    totalWorkers: null,
    loadingTotalWorkers: false,
    totalEquipments: null,
    loadingTotalEquipments: false,
    lastWorkers: [],
    loadingLastWorkers: false,
    lastEquipments: [],
    loadingLastEquipments: false,
    countEquipments: [],
    loadingCountEquipments: false,
    usageData: null,
    loadingUsageData: false,
  }),

  actions: {
    async fetchAll() {
      try {
        await Promise.all([
          this.fetchTotalEquipments(),
          this.fetchTotalWorkers(),
          this.fetchTotalEquipments(),
          this.fetchTotalWorkers(),
          this.fetchLastWorkers(),
          this.fetchLastEquipments(),
          this.fetchCountEquipments(),
          this.fetchEquipments()
        ]);

        await this.fetchUsageData()
      } catch (error) {
        throw error
      }
    },

    async fetchEquipments() {
      try {
        this.loadingEquipments = true
        const equipments = await getAllEquipments()
        this.equipments = equipments!
      } catch (error) {
        throw error
      } finally {
        this.loadingEquipments = false
      }
    },

    async fetchTotalEquipments() {
      try {
        this.loadingTotalEquipments = true
        const total = await reportTotalEquipments()
        this.totalEquipments = total!
      } catch (error) {
        throw error
      } finally {
        this.loadingTotalEquipments = false
      }
    },

    async fetchTotalWorkers() {
      try {
        this.loadingTotalWorkers = true
        const total = await reportTotalWorkers()
        this.totalWorkers = total!
      } catch (error) {
        throw error
      } finally {
        this.loadingTotalWorkers = false
      }
    },

    async fetchLastWorkers() {
      try {
        this.loadingLastWorkers = true
        const last = await reportLastWorkers()
        this.lastWorkers = last!
      } catch (error) {
        throw error
      } finally {
        this.loadingLastWorkers = false
      }
    },

    async fetchLastEquipments() {
      try {
        this.loadingLastEquipments = true
        const last = await reportLastEquipments()
        this.lastEquipments = last!
      } catch (error) {
        throw error
      } finally {
        this.loadingLastEquipments = false
      }
    },

    async fetchCountEquipments() {
      try {
        this.loadingCountEquipments = true
        const c = await reportCountEquipments()
        this.countEquipments = c!
      } catch (error) {
        throw error
      } finally {
        this.loadingCountEquipments = false
      }
    },

    async fetchUsageData() {
      if (this.selectedEquipment) {
        try {
          this.loadingUsageData = true
          const u = await reportUsageData(this.selectedEquipment.id)
          this.usageData = u!.usageData
        } catch (error) {
          throw error
        } finally {
          this.loadingUsageData = false
        }
      }
    },

    async selectEquipment(selectedEquipment: EquipmentOutputDto) {
      this.selectedEquipment = selectedEquipment
      try {
        await this.fetchTotalEquipments()
      } catch (error) {
        throw error
      }
    },
  },
})

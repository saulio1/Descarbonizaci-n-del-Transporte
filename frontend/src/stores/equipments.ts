import { defineStore } from 'pinia'
import type EquipmentOutputDto from '@/api/equipments/interfaces/output/equipment.output.dto.ts'
import {
  createEquipment,
  deleteEquipment,
  getAllEquipments,
  getEquipmentUsageDataById,
  putEquipmentUsageData,
  updateEquipment,
} from '@/api/equipments/services/equipments.service.ts'
import type EquipmentInputDto from '@/api/equipments/interfaces/input/equipment.input.dto.ts'
import { reportExportUsageData } from '@/api/reports/services/reports.service.ts'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import type EquipmentUsageDataItemOutputDto from '@/api/equipments/interfaces/output/equipment-usage-data-item.output.dto.ts'
import type EquipmentUsageDataItemInputDto from '@/api/equipments/interfaces/input/equipment-usage-data-item.input.dto.ts'

interface EquipmentsState {
  equipments: EquipmentOutputDto[]
  usageDatas: EquipmentUsageDataItemOutputDto[]
  loading: boolean
  isDialogOpen: boolean
  isDialogDataFieldOpen: boolean
  isDialogExportOpen: boolean
  isDialogUsageDataOpen: boolean
  dialogDataFieldMode: 'create' | 'edit'
  dialogMode: 'create' | 'edit' | 'view'
  dialogUsageDataMode: 'create' | 'edit'
  selectedEquipment: EquipmentOutputDto | null
  selectedDataField: DataFieldInputDto | null
  selectedUsageData: EquipmentUsageDataItemOutputDto | null
  loadingDialog: boolean
  loadingExport: boolean
  loadingUsageData: boolean
  loadingUsageDataDialog: boolean
}

export const useEquipmentsStore = defineStore('equipments', {
  state: (): EquipmentsState => ({
    equipments: [],
    usageDatas: [],
    loading: false,
    isDialogOpen: false,
    isDialogDataFieldOpen: false,
    isDialogExportOpen: false,
    isDialogUsageDataOpen: false,
    dialogDataFieldMode: 'create',
    dialogMode: 'create',
    dialogUsageDataMode: 'create',
    selectedEquipment: null,
    selectedDataField: null,
    loadingDialog: false,
    loadingExport: false,
    loadingUsageDataDialog: false,
    selectedUsageData: null,
    loadingUsageData: false,
  }),

  actions: {
    async fetchEquipments() {
      this.loading = true
      try {
        const equipments = await getAllEquipments()
        this.equipments = equipments!
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchUsageData() {
      if (this.selectedEquipment) {
        this.loadingUsageData = true
        try {
          const usageData = await getEquipmentUsageDataById(this.selectedEquipment.id)
          this.usageDatas = usageData!.usageData
        } catch (error) {
          throw error
        } finally {
          this.loadingUsageData = false
        }
      }
    },

    openDialog(mode: 'create' | 'edit' | 'view', equipment?: EquipmentOutputDto) {
      this.dialogMode = mode
      this.selectedEquipment = equipment || null
      this.isDialogOpen = true
    },

    closeDialog() {
      this.isDialogOpen = false
      this.selectedEquipment = null
    },

    openUsageDataDialog(mode: 'create' | 'edit', usageData?: EquipmentUsageDataItemOutputDto) {
      this.dialogUsageDataMode = mode
      this.selectedUsageData = usageData || null
      this.isDialogUsageDataOpen = true
    },

    closeUsageDataDialog() {
      this.isDialogUsageDataOpen = false
      this.selectedUsageData = null
    },

    openExportDialog() {
      this.isDialogExportOpen = true
    },

    closeExportDialog() {
      this.isDialogExportOpen = false
    },

    openDialogDataField(mode: 'create' | 'edit', dataField?: DataFieldInputDto) {
      this.dialogDataFieldMode = mode
      this.selectedDataField = dataField || null
      this.isDialogDataFieldOpen = true
    },

    closeDialogDataField() {
      this.isDialogDataFieldOpen = false
      this.selectedDataField = null
    },

    async createEquipment(equipment: EquipmentInputDto) {
      try {
        this.loadingDialog = true
        await createEquipment(equipment)
        await this.fetchEquipments()
        this.closeDialog()
      } catch (error) {
        throw error
      } finally {
        this.loadingDialog = false
      }
    },

    async updateEquipment(equipment: EquipmentInputDto) {
      if (this.selectedEquipment) {
        try {
          this.loadingDialog = true
          await updateEquipment(this.selectedEquipment.id, equipment)
          await this.fetchEquipments()
          this.closeDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingDialog = false
        }
      }
    },

    async deleteEquipment(id: string) {
      try {
        this.loading = true
        await deleteEquipment(id)
        await this.fetchEquipments()
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    async export(ids?: string[]) {
      try {
        this.loadingExport = true
        const data = await reportExportUsageData(ids)
        if (data) {
          const jsonString = JSON.stringify(data, null, 2)

          const blob = new Blob([jsonString], { type: 'application/json' })
          const url = URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = 'equipments-with-usage-data.json'
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
          URL.revokeObjectURL(url)
        }
      } catch (error) {
        throw error
      } finally {
        this.loadingExport = false
      }
    },

    async createUsageData(usageData: EquipmentUsageDataItemInputDto) {
      if (this.selectedEquipment) {
        try {
          this.loadingUsageDataDialog = true
          const data = [...this.usageDatas].map((x) => ({
            usageStart: x.usageStart,
            usageEnd: x.usageEnd,
            operatingHours: x.operatingHours,
            energyConsumed: x.energyConsumed,
            loadOrCapacityUsed: x.loadOrCapacityUsed,
            location: x.location,
            operator: x.operator,
            notes: x.notes,
            id: x.id
          }))
          data.push(usageData)
          await putEquipmentUsageData(this.selectedEquipment.id, {
            usageData: data,
          })
          await this.fetchUsageData()
          this.closeUsageDataDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingUsageDataDialog = false
        }
      }
    },

    async updateUsageData(usageData: EquipmentUsageDataItemInputDto) {
      console.log(1)
      if (this.selectedEquipment && this.selectedUsageData) {
        try {
          this.loadingUsageDataDialog = true
          const data = [...this.usageDatas].map((x) => ({
            usageStart: x.usageStart,
            usageEnd: x.usageEnd,
            operatingHours: x.operatingHours,
            energyConsumed: x.energyConsumed,
            loadOrCapacityUsed: x.loadOrCapacityUsed,
            location: x.location,
            operator: x.operator,
            notes: x.notes,
            id: x.id
          }))

          const index = this.usageDatas.findIndex((x) => x.id === this.selectedUsageData!.id)
          if (index > -1) {
            data[index] = usageData
          }
          await putEquipmentUsageData(this.selectedEquipment.id, {
            usageData: data,
          })
          await this.fetchUsageData()
          this.closeUsageDataDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingUsageDataDialog = false
        }
      }
    },

    async deleteUsageData(id: String) {
      if (this.selectedEquipment) {
        this.loadingUsageData = true
        try {
          const data = [...this.usageDatas]
            .filter((x) => x.id !== id)
            .map((x) => ({
              usageStart: x.usageStart,
              usageEnd: x.usageEnd,
              operatingHours: x.operatingHours,
              energyConsumed: x.energyConsumed,
              loadOrCapacityUsed: x.loadOrCapacityUsed,
              location: x.location,
              operator: x.operator,
              notes: x.notes,
              id: x.id
            }))

          await putEquipmentUsageData(this.selectedEquipment.id, {
            usageData: data,
          })
          await this.fetchUsageData()
        } catch (error) {
          throw error
        } finally {
          this.loadingUsageData = false
        }
      }
    },
  },
})

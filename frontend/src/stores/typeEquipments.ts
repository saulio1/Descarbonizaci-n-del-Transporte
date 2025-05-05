import { defineStore } from 'pinia'
import type TypeEquipmentOutputDto from '@/api/typeEquipments/interfaces/output/type-equipment.output.dto.ts'
import {
  createTypeEquipment,
  deleteTypeEquipment,
  getAllTypeEquipments,
  updateTypeEquipment,
} from '@/api/typeEquipments/services/type-equipments.service.ts'
import type TypeEquipmentInputDto from '@/api/typeEquipments/interfaces/input/type-equipment.input.dto.ts'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'

interface TypeEquipmentsState {
  typeEquipments: TypeEquipmentOutputDto[]
  loading: boolean
  isDialogOpen: boolean
  dialogMode: 'create' | 'edit'
  isDialogDataFieldOpen: boolean
  dialogDataFieldMode: 'create' | 'edit'
  selectedTypeEquipment: TypeEquipmentOutputDto | null
  selectedDataField: DataFieldInputDto | null
  loadingDialog: boolean
}

export const useTypeEquipmentsStore = defineStore('typeEquipments', {
  state: (): TypeEquipmentsState => ({
    typeEquipments: [],
    loading: false,
    isDialogOpen: false,
    dialogMode: 'create',
    isDialogDataFieldOpen: false,
    dialogDataFieldMode: 'create',
    selectedTypeEquipment: null,
    selectedDataField: null,
    loadingDialog: false,
  }),

  actions: {
    async fetchTypeEquipments() {
      this.loading = true
      try {
        const types = await getAllTypeEquipments()
        this.typeEquipments = types!
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    openDialog(mode: 'create' | 'edit', typeEquipment?: TypeEquipmentOutputDto) {
      this.dialogMode = mode
      this.selectedTypeEquipment = typeEquipment || null
      this.isDialogOpen = true
    },

    closeDialog() {
      this.isDialogOpen = false
      this.selectedTypeEquipment = null
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

    async createTypeEquipment(typeEquipment: TypeEquipmentInputDto) {
      try {
        this.loadingDialog = true
        await createTypeEquipment(typeEquipment)
        await this.fetchTypeEquipments()
        this.closeDialog()
      } catch (error) {
        throw error
      } finally {
        this.loadingDialog = false
      }
    },

    async updateTypeEquipment(typeEquipment: TypeEquipmentInputDto) {
      if (this.selectedTypeEquipment) {
        try {
          this.loadingDialog = true
          await updateTypeEquipment(this.selectedTypeEquipment.id, typeEquipment)
          await this.fetchTypeEquipments()
          this.closeDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingDialog = false
        }
      }
    },

    async deleteTypeEquipment(id: string) {
      try {
        this.loading = true
        await deleteTypeEquipment(id)
        await this.fetchTypeEquipments()
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})

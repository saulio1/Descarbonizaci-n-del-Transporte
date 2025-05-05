import { defineStore } from 'pinia'
import type WorkerPositionOutputDto from '@/api/worker-positions/interfaces/output/worker-position.output.dto.ts'
import {
  createPosition,
  deletePosition,
  getAllPositions,
  updatePosition,
} from '@/api/worker-positions/services/worker-positions.service.ts'
import type WorkerPositionInputDto from '@/api/worker-positions/interfaces/input/worker-position.input.dto.ts'

interface PositionsState {
  positions: WorkerPositionOutputDto[]
  loading: boolean
  isDialogOpen: boolean
  dialogMode: 'create' | 'edit'
  selectedPosition: WorkerPositionOutputDto | null
  loadingDialog: boolean
}

export const usePositionsStore = defineStore('workersPositions', {
  state: (): PositionsState => ({
    positions: [],
    loading: false,
    isDialogOpen: false,
    dialogMode: 'create',
    selectedPosition: null,
    loadingDialog: false,
  }),

  actions: {
    async fetchPositions() {
      this.loading = true
      try {
        const positions = await getAllPositions()
        this.positions = positions!
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    openDialog(mode: 'create' | 'edit', position?: WorkerPositionOutputDto) {
      this.dialogMode = mode
      this.selectedPosition = position || null
      this.isDialogOpen = true
    },

    closeDialog() {
      this.isDialogOpen = false
      this.selectedPosition = null
    },

    async createPosition(position: WorkerPositionInputDto) {
      try {
        this.loadingDialog = true
        await createPosition(position)
        await this.fetchPositions()
        this.closeDialog()
      } catch (error) {
        throw error
      } finally {
        this.loadingDialog = false
      }
    },

    async updatePosition(position: WorkerPositionInputDto) {
      if (this.selectedPosition) {
        try {
          this.loadingDialog = true
          await updatePosition(this.selectedPosition.id, position)
          await this.fetchPositions()
          this.closeDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingDialog = false
        }
      }
    },

    async deletePosition(id: string) {
      try {
        this.loading = true
        await deletePosition(id)
        await this.fetchPositions()
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})

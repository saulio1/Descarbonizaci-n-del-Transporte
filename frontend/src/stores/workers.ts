import type WorkerOutputDto from '@/api/workers/interfaces/output/worker.output.dto.ts'
import { defineStore } from 'pinia'
import {
  createWorker,
  deleteWorker,
  getAllWorkers,
  updateWorker,
} from '@/api/workers/services/workers.service.ts'
import type WorkerInputDto from '@/api/workers/interfaces/input/worker.input.dto.ts'
import type WorkerPatchInputDto from '@/api/workers/interfaces/input/worker-patch.input.dto.ts'

interface WorkersState {
  workers: WorkerOutputDto[]
  loading: boolean
  isDialogOpen: boolean
  dialogMode: 'create' | 'edit'
  selectedWorker: WorkerOutputDto | null
  loadingDialog: boolean
}

export const useWorkersStore = defineStore('workers', {
  state: (): WorkersState => ({
    workers: [],
    loading: false,
    isDialogOpen: false,
    dialogMode: 'create',
    selectedWorker: null,
    loadingDialog: false,
  }),

  actions: {
    async fetchWorkers() {
      this.loading = true
      try {
        const workers = await getAllWorkers()
        this.workers = workers!
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    openDialog(mode: 'create' | 'edit', worker?: WorkerOutputDto) {
      this.dialogMode = mode
      this.selectedWorker = worker || null
      this.isDialogOpen = true
    },

    closeDialog() {
      this.isDialogOpen = false
      this.selectedWorker = null
    },

    async createWorker(worker: WorkerInputDto) {
      try {
        this.loadingDialog = true
        await createWorker(worker)
        await this.fetchWorkers()
        this.closeDialog()
      } catch (error) {
        throw error
      } finally {
        this.loadingDialog = false
      }
    },

    async updateWorker(worker: WorkerPatchInputDto) {
      if (this.selectedWorker) {
        try {
          this.loadingDialog = true
          await updateWorker(this.selectedWorker.id, worker)
          await this.fetchWorkers()
          this.closeDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingDialog = false
        }
      }
    },

    async deleteWorker(id: string) {
      try {
        this.loading = true
        await deleteWorker(id)
        await this.fetchWorkers()
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})

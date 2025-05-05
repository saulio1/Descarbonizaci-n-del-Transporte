import handleError from '@/api/common/utils/error.handler.ts'
import api from '@/api/common/utils/base.axios.ts'
import { useToastService } from '@/helpers/toast.helper.ts'
import type WorkerOutputDto from '@/api/workers/interfaces/output/worker.output.dto.ts'
import type WorkerPatchInputDto from '@/api/workers/interfaces/input/worker-patch.input.dto.ts'
import type WorkerInputDto from '@/api/workers/interfaces/input/worker.input.dto.ts'

export const getAllWorkers = async (): Promise<WorkerOutputDto[] | void> => {
  try {
    const response = await api.get<WorkerOutputDto[]>(`workers`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getWorkerById = async (id: string): Promise<WorkerOutputDto | void> => {
  try {
    const response = await api.get<WorkerOutputDto>(`workers/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const updateWorker = async (
  id: string,
  data: WorkerPatchInputDto,
): Promise<WorkerOutputDto | void> => {
  try {
    const response = await api.patch<WorkerOutputDto>(`workers/${id}`, data)
    useToastService({
      severity: 'success',
      summary: 'Trabajador actualizado',
      detail: 'El trabajador fue actualizado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const createWorker = async (data: WorkerInputDto): Promise<WorkerOutputDto | void> => {
  try {
    const response = await api.post<WorkerOutputDto>(`workers`, data)
    useToastService({
      severity: 'success',
      summary: 'Trabajador creado',
      detail: 'El trabajador fue creado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const deleteWorker = async (id: string): Promise<void> => {
  try {
    await api.delete(`workers/${id}/physical`)
    useToastService({
      severity: 'success',
      summary: 'Trabajador eliminado',
      detail: 'El trabajador fue eliminado con éxito.',
    }).showToast()
  } catch (error: any) {
    await handleError(error)
  }
}

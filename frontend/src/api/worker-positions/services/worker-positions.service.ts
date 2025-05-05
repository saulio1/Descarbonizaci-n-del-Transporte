import handleError from '@/api/common/utils/error.handler.ts'
import api from '@/api/common/utils/base.axios.ts'
import { useToastService } from '@/helpers/toast.helper.ts'
import type WorkerPositionOutputDto from '@/api/worker-positions/interfaces/output/worker-position.output.dto.ts'
import type WorkerPositionInputDto from '@/api/worker-positions/interfaces/input/worker-position.input.dto.ts'

export const getAllPositions = async (): Promise<WorkerPositionOutputDto[] | void> => {
  try {
    const response = await api.get<WorkerPositionOutputDto[]>(`positions`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getPositionById = async (id: string): Promise<WorkerPositionOutputDto | void> => {
  try {
    const response = await api.get<WorkerPositionOutputDto>(`positions/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const updatePosition = async (
  id: string,
  data: WorkerPositionInputDto,
): Promise<WorkerPositionOutputDto | void> => {
  try {
    const response = await api.put<WorkerPositionOutputDto>(`positions/${id}`, data)
    useToastService({
      severity: 'success',
      summary: 'Ocupación actualizada',
      detail: 'La ocupación de trabajador fue actualizada con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const createPosition = async (
  data: WorkerPositionInputDto,
): Promise<WorkerPositionOutputDto | void> => {
  try {
    const response = await api.post<WorkerPositionOutputDto>(`positions`, data)
    useToastService({
      severity: 'success',
      summary: 'Ocupación creado',
      detail: 'La ocupación de trabajador fue creada con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const deletePosition = async (id: string): Promise<void> => {
  try {
    await api.delete(`positions/${id}/logical`)
    useToastService({
      severity: 'success',
      summary: 'Ocupación eliminada',
      detail: 'La ocupación de trabajador fue eliminada con éxito.',
    }).showToast()
  } catch (error: any) {
    await handleError(error)
  }
}

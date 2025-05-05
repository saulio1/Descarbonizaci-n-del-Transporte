import api from '@/api/common/utils/base.axios.ts'
import handleError from '@/api/common/utils/error.handler.ts'
import { useToastService } from '@/helpers/toast.helper.ts'
import type EquipmentOutputDto from '@/api/equipments/interfaces/output/equipment.output.dto.ts'
import type EquipmentInputDto from '@/api/equipments/interfaces/input/equipment.input.dto.ts'
import type EquipmentUsageDataOutputDto from '@/api/equipments/interfaces/output/equipment-usage-data.output.dto.ts'
import type EquipmentUsageDataInputDto from '@/api/equipments/interfaces/input/equipment-usage-data.input.dto.ts'

export const getAllEquipments = async (): Promise<EquipmentOutputDto[] | void> => {
  try {
    const response = await api.get<EquipmentOutputDto[]>(`equipments`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getEquipmentById = async (id: string): Promise<EquipmentOutputDto | void> => {
  try {
    const response = await api.get<EquipmentOutputDto>(`equipments/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getEquipmentUsageDataById = async (
  id: string,
): Promise<EquipmentUsageDataOutputDto | void> => {
  try {
    const response = await api.get<EquipmentUsageDataOutputDto>(`equipments/usage-data/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const putEquipmentUsageData = async (
  id: string,
  data: EquipmentUsageDataInputDto,
): Promise<EquipmentUsageDataOutputDto | void> => {
  try {
    const response = await api.put<EquipmentUsageDataOutputDto>(`equipments/usage-data/${id}`, data)
    useToastService({
      severity: 'success',
      summary: 'Datos de uso de equipamiento actualizados',
      detail: 'Los datos de uso del equipamiento fueron actualizados con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const updateEquipment = async (
  id: string,
  data: EquipmentInputDto,
): Promise<EquipmentOutputDto | void> => {
  try {
    const response = await api.put<EquipmentOutputDto>(`equipments/${id}`, data)
    useToastService({
      severity: 'success',
      summary: 'Equipamiento actualizado',
      detail: 'El equipamiento fue actualizado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const createEquipment = async (
  data: EquipmentInputDto,
): Promise<EquipmentOutputDto | void> => {
  try {
    const response = await api.post<EquipmentOutputDto>(`equipments`, data)
    useToastService({
      severity: 'success',
      summary: 'Equipamiento creado',
      detail: 'El equipamiento fue creado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const deleteEquipment = async (id: string): Promise<void> => {
  try {
    await api.delete(`equipments/${id}/physical`)
    useToastService({
      severity: 'success',
      summary: 'Equipamiento eliminado',
      detail: 'El equipamiento fue eliminado con éxito.',
    }).showToast()
  } catch (error: any) {
    await handleError(error)
  }
}

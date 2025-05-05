import api from '@/api/common/utils/base.axios.ts'
import handleError from '@/api/common/utils/error.handler.ts'
import { useToastService } from '@/helpers/toast.helper.ts'
import type TypeEquipmentOutputDto from '@/api/typeEquipments/interfaces/output/type-equipment.output.dto.ts'
import type TypeEquipmentInputDto from '@/api/typeEquipments/interfaces/input/type-equipment.input.dto.ts'

export const getAllTypeEquipments = async (): Promise<TypeEquipmentOutputDto[] | void> => {
  try {
    const response = await api.get<TypeEquipmentOutputDto[]>(`typeEquipments`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getTypeEquipmentById = async (id: string): Promise<TypeEquipmentOutputDto | void> => {
  try {
    const response = await api.get<TypeEquipmentOutputDto>(`typeEquipments/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const updateTypeEquipment = async (
  id: string,
  data: TypeEquipmentInputDto,
): Promise<TypeEquipmentOutputDto | void> => {
  try {
    const response = await api.put<TypeEquipmentOutputDto>(`typeEquipments/${id}`, data)
    useToastService({
      severity: 'success',
      summary: 'Tipo de equipamiento actualizado',
      detail: 'El tipo de equipamiento fue actualizado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const createTypeEquipment = async (
  data: TypeEquipmentInputDto,
): Promise<TypeEquipmentOutputDto | void> => {
  try {
    const response = await api.post<TypeEquipmentOutputDto>(`typeEquipments`, data)
    useToastService({
      severity: 'success',
      summary: 'Tipo de equipamiento creado',
      detail: 'El tipo de equipamiento fue creado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const deleteTypeEquipment = async (id: string): Promise<void> => {
  try {
    await api.delete(`typeEquipments/${id}/logical`)
    useToastService({
      severity: 'success',
      summary: 'Tipo de equipamiento eliminado',
      detail: 'El tipo de equipamiento fue eliminado con éxito.',
    }).showToast()
  } catch (error: any) {
    await handleError(error)
  }
}

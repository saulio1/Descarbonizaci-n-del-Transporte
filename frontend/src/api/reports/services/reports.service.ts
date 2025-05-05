import type EquipmentUsageReportOutputDto from '@/api/reports/interfaces/output/equipment-usage.report.output.dto.ts'
import api from '@/api/common/utils/base.axios.ts'
import handleError from '@/api/common/utils/error.handler.ts'
import type TotalWorkersReportOutputDto from '@/api/reports/interfaces/output/total-workers.report.output.dto.ts'
import type TotalEquipmentsReportOutputDto from '@/api/reports/interfaces/output/total-equipments.report.output.dto.ts'
import type EquipmentByTypeReportOutputDto from '@/api/reports/interfaces/output/equipment-by-type.report.output.dto.ts'
import type EquipmentUsageDataOutputDto from '@/api/equipments/interfaces/output/equipment-usage-data.output.dto.ts'
import type LastWorkersReportOutputDto from '@/api/reports/interfaces/output/last-workers.report.output.dto.ts'
import type LastEquipmentsReportOutputDto from '@/api/reports/interfaces/output/last-equipments.report.output.dto.ts'
import { useToastService } from '@/helpers/toast.helper.ts'

export const reportExportUsageData = async (
  ids?: string[],
): Promise<EquipmentUsageReportOutputDto | void> => {
  try {
    let url = 'reports/usage-data'
    if (ids && ids.length > 0) {
      const params = new URLSearchParams()
      ids.forEach(id => params.append('ids', id))
      url += `?${params.toString()}`
    }
    url = url.replace('[]', '')
    const response = await api.get<EquipmentUsageReportOutputDto>(url)
    useToastService({
      severity: 'success',
      summary: 'Datos exportados',
      detail: 'Los datos seleccionados fueron exportados con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}



export const reportTotalWorkers = async (): Promise<TotalWorkersReportOutputDto | void> => {
  try {
    const response = await api.get<TotalWorkersReportOutputDto>(`reports/total/workers`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const reportTotalEquipments = async (): Promise<TotalEquipmentsReportOutputDto | void> => {
  try {
    const response = await api.get<TotalEquipmentsReportOutputDto>(`reports/total/equipments`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const reportLastWorkers = async (): Promise<LastWorkersReportOutputDto[] | void> => {
  try {
    const response = await api.get<LastWorkersReportOutputDto[]>(`reports/last-workers`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const reportLastEquipments = async (): Promise<LastEquipmentsReportOutputDto[] | void> => {
  try {
    const response = await api.get<LastEquipmentsReportOutputDto[]>(`reports/last-equipments`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const reportCountEquipments = async (): Promise<EquipmentByTypeReportOutputDto[] | void> => {
  try {
    const response = await api.get<EquipmentByTypeReportOutputDto[]>(`reports/count-equipments`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const reportUsageData = async (id: string): Promise<EquipmentUsageDataOutputDto | void> => {
  try {
    const response = await api.get<EquipmentUsageDataOutputDto>(`equipments/usage-data/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

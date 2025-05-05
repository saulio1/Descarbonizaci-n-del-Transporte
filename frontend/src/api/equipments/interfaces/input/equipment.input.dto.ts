import type EquipmentDataFieldInputDto from '@/api/equipments/interfaces/input/equipment-data-field.input.dto.ts'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'

export default interface EquipmentInputDto {
  name: string
  description?: string
  data: EquipmentDataFieldInputDto[]
  otherDataFields: DataFieldInputDto[]
  typeEquipmentId: string
  usageDataEnergyConsumedMeasurementUnit: string
  usageDataLoadOrCapacityUsedMeasurementUnit: string
}

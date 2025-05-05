import type EquipmentDataFieldInputDto from '@/api/equipments/interfaces/input/equipment-data-field.input.dto.ts'
import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'
import type TypeEquipmentOutputDto from '@/api/typeEquipments/interfaces/output/type-equipment.output.dto.ts'

export default interface EquipmentOutputDto {
  id: string
  name: string
  description: string
  data: EquipmentDataFieldInputDto[]
  otherDataFields: DataFieldInputDto[]
  typeEquipment: TypeEquipmentOutputDto
  usageDataEnergyConsumedMeasurementUnit: string
  usageDataLoadOrCapacityUsedMeasurementUnit: string
}

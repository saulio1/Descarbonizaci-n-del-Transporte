import type EquipmentUsageDataItemOutputDto from '@/api/equipments/interfaces/output/equipment-usage-data-item.output.dto.ts'

export default interface EquipmentUsageDataOutputDto {
  usageData: EquipmentUsageDataItemOutputDto[]
}

import type EquipmentOutputDto from '@/api/equipments/interfaces/output/equipment.output.dto.ts'
import type EquipmentUsageDataItemOutputDto from '@/api/equipments/interfaces/output/equipment-usage-data-item.output.dto.ts'

export default interface EquipmentUsageReportDto extends EquipmentOutputDto {
  usageData: EquipmentUsageDataItemOutputDto
}

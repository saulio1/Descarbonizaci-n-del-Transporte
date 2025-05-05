import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'

export default interface TypeEquipmentInputDto {
  name: string
  description?: string
  dataFields: DataFieldInputDto[]
}

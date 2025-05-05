import type DataFieldInputDto from '@/api/typeEquipments/interfaces/input/data-field.input.dto.ts'

export default interface TypeEquipmentOutputDto {
  id: string
  name: string
  description?: string
  dataFields: DataFieldInputDto[]
  editable: boolean
}

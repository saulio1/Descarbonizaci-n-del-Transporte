import TypeField from '@/api/common/interfaces/typeFields.ts'

export default interface DataFieldInputDto {
  name: string
  description?: string
  minimum?: number
  maximum?: number
  minimumDateTime?: Date
  maximumDateTime?: Date
  options: string[]
  required: boolean
  typeField: TypeField
}

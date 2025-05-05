import type Role from '@/api/common/interfaces/roles.ts'

export default interface UserInputDto {
  username: string
  email: string
  password: string
  roles: Role
}

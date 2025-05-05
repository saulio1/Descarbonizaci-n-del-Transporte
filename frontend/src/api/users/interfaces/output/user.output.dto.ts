import type Role from '@/api/common/interfaces/roles.ts'

export default interface UserOutputDto {
  id: string
  username: string
  email: string
  roles: Role[]
}

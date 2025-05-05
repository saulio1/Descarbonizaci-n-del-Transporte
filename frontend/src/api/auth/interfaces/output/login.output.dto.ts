import type Role from '@/api/common/interfaces/roles.ts'

export interface LoginOutputDto {
  id: string
  email: string
  username: string
  token: string
  roles: Role[]
}

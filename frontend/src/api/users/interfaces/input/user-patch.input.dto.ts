import type Role from '@/api/common/interfaces/roles.ts'

export default interface UserPatchInputDto {
  username?: string
  email?: string
  roles?: Role[]
}

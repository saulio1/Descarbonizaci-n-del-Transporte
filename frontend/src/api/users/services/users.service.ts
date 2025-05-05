import type UserOutputDto from '@/api/users/interfaces/output/user.output.dto.ts'
import handleError from '@/api/common/utils/error.handler.ts'
import api from '@/api/common/utils/base.axios.ts'
import type UserPatchInputDto from '@/api/users/interfaces/input/user-patch.input.dto.ts'
import type UserInputDto from '@/api/users/interfaces/input/user.input.dto.ts'
import { useToastService } from '@/helpers/toast.helper.ts'

export const getAllUsers = async (): Promise<UserOutputDto[] | void> => {
  try {
    const response = await api.get<UserOutputDto[]>(`users`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getUserById = async (id: string): Promise<UserOutputDto | void> => {
  try {
    const response = await api.get<UserOutputDto>(`users/${id}`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const getCurrentUser = async (): Promise<UserOutputDto | void> => {
  try {
    const response = await api.get<UserOutputDto>(`users/me`)
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const updateUser = async (
  id: string,
  userData: UserPatchInputDto,
): Promise<UserOutputDto | void> => {
  try {
    const response = await api.patch<UserOutputDto>(`users/${id}`, userData)
    useToastService({
      severity: 'success',
      summary: 'Usuario actualizado',
      detail: 'El usuario fue actualizado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const createUser = async (userData: UserInputDto): Promise<UserOutputDto | void> => {
  try {
    const response = await api.post<UserOutputDto>(`users`, userData)
    useToastService({
      severity: 'success',
      summary: 'Usuario creado',
      detail: 'El usuario fue creado con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(error)
  }
}

export const deleteUser = async (id: string): Promise<void> => {
  try {
    await api.delete(`users/${id}/physical`)
    useToastService({
      severity: 'success',
      summary: 'Usuario eliminado',
      detail: 'El usuario fue eliminado con éxito.',
    }).showToast()
  } catch (error: any) {
    await handleError(error)
  }
}

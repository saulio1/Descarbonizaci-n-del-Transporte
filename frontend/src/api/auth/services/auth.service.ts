import type LoginInputDto from '@/api/auth/interfaces/input/login.input.dto.ts'
import type { LoginOutputDto } from '@/api/auth/interfaces/output/login.output.dto.ts'
import api from '@/api/common/utils/base.axios.ts'
import type ChangePasswordInputDto from '@/api/auth/interfaces/input/change-password.input.dto.ts'
import handleError from '@/api/common/utils/error.handler.ts'
import { useToastService } from '@/helpers/toast.helper.ts'

export const login = async (loginData: LoginInputDto): Promise<LoginOutputDto> => {
  try {
    const response = await api.post<LoginOutputDto>(`auth/login`, loginData)
    return response.data
  } catch (error) {
    useToastService({
      severity: 'error',
      summary: 'Error de inicio de sesión',
      detail: 'Revise sus credenciales e intente nuevamente.',
    }).showToast()
    throw error
  }
}

export const changePassword = async (changePasswordInputDto: ChangePasswordInputDto) => {
  try {
    const response = await api.post(`auth/change-password`, changePasswordInputDto)
    useToastService({
      severity: 'success',
      summary: 'Contraseña cambiada',
      detail: 'Su contraseña fue cambiada con éxito.',
    }).showToast()
    return response.data
  } catch (error: any) {
    await handleError(
      error,
      'Contraseña incorrecta',
      'Revise su contraseña anterior y vuelva a intentarlo.',
    )
  }
}

import router from '@/router'
import { useToastService } from '@/helpers/toast.helper.ts'
import { useAuthStore } from '@/stores/auth.ts'

const handleErrorInternal = async (
  error: any,
  badRequestSummary: string,
  badRequestDetail: string,
) => {
  if (error.response) {
    const status = error.response.status

    if (status === 401) {
      useToastService({
        severity: 'error',
        summary: 'No autorizado',
        detail: 'Usted no se encuentra autorizado. Por favor, inicie sesión nuevamente.',
      }).showToast()
      await useAuthStore().logout()
      await router.push('/login')
    } else if (status === 403) {
      useToastService({
        severity: 'error',
        summary: 'Acceso prohibido',
        detail: 'Usted no tiene acceso al recurso solicitado.',
      }).showToast()
      await router.push('/error-403')
    } else if (status === 400) {
      console.error('Bad Request error: ', error)
      useToastService({
        severity: 'error',
        summary: badRequestSummary,
        detail: badRequestDetail,
      }).showToast()
    } else {
      console.error('Some error: ', error)
      useToastService({
        severity: 'error',
        summary: 'Mensaje de error',
        detail: 'Ocurrió un error. Por favor, inténtalo más tarde.',
      }).showToast()
    }
  } else {
    console.error('Unknown error: ', error)
    useToastService({
      severity: 'error',
      summary: 'Mensaje de error',
      detail: 'Ocurrió un error desconocido. Por favor, inténtalo más tarde.',
    }).showToast()
  }
}

const handleError = async (
  error: any,
  badRequestSummary: string = 'Datos incorrectos',
  badRequestDetail: string = 'Datos incorrectos.',
) => {
  await handleErrorInternal(error, badRequestSummary, badRequestDetail)
  if (error.response.status !== 401 && error.response.status === 403) {
    throw error
  }
}

export default handleError

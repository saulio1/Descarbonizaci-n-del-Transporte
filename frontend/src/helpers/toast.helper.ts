import type { ToastServiceMethods } from 'primevue/toastservice'
import { ToastEventBus } from 'primevue'

export const useToastService = (options: Parameters<ToastServiceMethods['add']>[0]) => {
  if (!options.life) {
    options.life = 5000
  }
  const showToast = () => {
    ToastEventBus.emit('add', options)
  }

  return { showToast }
}

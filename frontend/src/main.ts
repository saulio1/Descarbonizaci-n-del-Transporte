import './assets/main.css'
// Import Swiper styles
import 'swiper/css'
import 'swiper/css/navigation'
import 'swiper/css/pagination'
import 'jsvectormap/dist/jsvectormap.css'
import 'flatpickr/dist/flatpickr.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import VueApexCharts from 'vue3-apexcharts'
import PrimeVue from 'primevue/config'
import Aura from '@primeuix/themes/aura'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { ConfirmationService, ToastService, Tooltip } from 'primevue'
import { definePreset } from '@primevue/themes'
import 'primeicons/primeicons.css'

const app = createApp(App)

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)

app.use(router)
app.use(VueApexCharts)

const CustomAura = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{blue.50}',
      100: '{blue.100}',
      200: '{blue.200}',
      300: '{blue.300}',
      400: '{blue.400}',
      500: '{blue.500}',
      600: '{blue.600}',
      700: '{blue.700}',
      800: '{blue.800}',
      900: '{blue.900}',
      950: '{blue.950}',
    },
    colorScheme: {
      dark: {
        surface: {
          0: '{slate.0}',
          50: '{slate.50}',
          100: '{slate.100}',
          200: '{slate.200}',
          300: '{slate.300}',
          400: '{slate.400}',
          500: '{slate.500}',
          600: '{slate.600}',
          700: '{slate.700}',
          800: '{slate.800}',
          900: '{slate.900}',
          950: '{slate.950}',
        },
      },
    },
  },
})

// app.config.errorHandler = (err, vm, info) => {
//   location.reload()
// }

app.use(PrimeVue, {
  theme: {
    preset: CustomAura,
    options: {
      prefix: 'p',
      darkModeSelector: '.my-app-dark',
    },
  },
})
app.directive('tooltip', Tooltip)
app.use(ToastService)
app.use(ConfirmationService)
app.mount('#app')

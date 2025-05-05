<template>
  <ThemeProvider>
    <SidebarProvider>
      <RouterView />
      <Toast />
    </SidebarProvider>
  </ThemeProvider>
</template>

<script setup lang="ts">
import ThemeProvider from './components/layout/ThemeProvider.vue'
import SidebarProvider from './components/layout/SidebarProvider.vue'
import Toast from 'primevue/toast'
import { useAuthStore } from '@/stores/auth.ts'
import { onMounted, onUnmounted } from 'vue'

const authStore = useAuthStore()

onMounted(() => {
  authStore.startAutoUpdate()
})

onUnmounted(() => {
  authStore.stopAutoUpdate()
})
</script>

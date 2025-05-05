import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth.ts'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { left: 0, top: 0 }
  },
  routes: [
    {
      path: '/',
      name: 'Inicio',
      component: () => import('../views/Dashboard.vue'),
      meta: {
        title: 'Inicio',
      },
    },
    {
      path: '/users',
      name: 'Usuarios',
      component: () => import('../views/Users/Users.vue'),
      meta: {
        title: 'Usuarios',
      },
    },
    {
      path: '/workers',
      name: 'Trabajadores',
      component: () => import('../views/Workers/Workers.vue'),
      meta: {
        title: 'Trabajadores',
      },
    },
    {
      path: '/workers/positions',
      name: 'Ocupaciones de trabajadores',
      component: () => import('../views/Workers/WorkerPositions.vue'),
      meta: {
        title: 'Ocupaciones de trabajadores',
      },
    },
    {
      path: '/equipments',
      name: 'Equipamientos',
      component: () => import('../views/Equipments/Equipments.vue'),
      meta: {
        title: 'Equipamientos',
      },
    },
    {
      path: '/equipments/usage-data',
      name: 'Datos de uso',
      component: () => import('../views/Equipments/UsageData.vue'),
      meta: {
        title: 'Datos de uso',
      },
    },
    {
      path: '/equipments/types',
      name: 'Tipos de equipamientos',
      component: () => import('../views/Equipments/TypesEquipment.vue'),
      meta: {
        title: 'Tipos de equipamientos',
      },
    },
    {
      path: '/profile',
      name: 'Perfil',
      component: () => import('../views/Configuration/UserProfile.vue'),
      meta: {
        title: 'Perfil de Usuario',
      },
    },
    {
      path: '/404',
      name: '404 Error',
      component: () => import('../views/Errors/FourZeroFour.vue'),
      meta: {
        title: 'Página no encontrada',
      },
    },
    {
      path: '/403',
      name: '403 Error',
      component: () => import('../views/Errors/Forbidden.vue'),
      meta: {
        title: 'Sin permisos necesarios',
      },
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Auth/LogIn.vue'),
      meta: {
        title: 'Inicio de Sesión',
      },
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404',
    },
  ],
})

export default router

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title} | EcoFlow`

  const auth = useAuthStore()

  if (!auth.isAuthenticated && !['/login', '/403', '/404', '500', '503'].includes(to.path)) {
    return next('/login')
  }

  if (auth.isAuthenticated && to.path === '/login') {
    return next('/')
  }

  if (!auth.isAdmin && to.path === '/users') {
    return next('/403')
  }

  if (
    !auth.isUser &&
    !auth.isModerator &&
    ['/workers', '/workers/positions', 'equipments', 'equipments/types', '/equipments/usage-data'].includes(to.path)
  ) {
    return next('/403')
  }

  next()
})

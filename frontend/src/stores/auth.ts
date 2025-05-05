import Role from '@/api/common/interfaces/roles.ts'
import { defineStore } from 'pinia'
import type LoginInputDto from '@/api/auth/interfaces/input/login.input.dto.ts'
import { changePassword, login } from '@/api/auth/services/auth.service.ts'
import router from '@/router'
import type ChangePasswordInputDto from '@/api/auth/interfaces/input/change-password.input.dto.ts'
import { getCurrentUser } from '@/api/users/services/users.service.ts'

interface AuthState {
  id: string | null
  email: string | null
  username: string | null
  token: string | null
  roles: Role[] | null
  _intervalId?: ReturnType<typeof setInterval> | null
  isChangePasswordOpen: boolean
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    id: null,
    email: null,
    username: null,
    roles: null,
    token: null,
    _intervalId: null,
    isChangePasswordOpen: false,
  }),

  persist: true,

  getters: {
    isAdmin: (state) => state.roles?.includes(Role.ROLE_ADMIN),
    isModerator: (state) => state.roles?.includes(Role.ROLE_MODERATOR),
    isUser: (state) => state.roles?.includes(Role.ROLE_USER),
    isAuthenticated: (state) => state.token,
  },

  actions: {
    async login(usernameOrEmail: string, password: string) {
      const loginData: LoginInputDto = { usernameOrEmail, password }
      try {
        const data = await login(loginData)
        this.token = data.token
        this.roles = data.roles
        this.id = data.id
        this.username = data.username
        this.email = data.email
        await router.push('/')
      } catch (error) {
        throw error
      }
    },

    async logout() {
      this.id = null
      this.email = null
      this.username = null
      this.token = null
      this.roles = null
      this.token = null
      await router.push('/login')
    },

    async changePassword(oldPassword: string, newPassword: string) {
      const dto: ChangePasswordInputDto = { newPassword, currentPassword: oldPassword }
      try {
        await changePassword(dto)
      } catch (error) {
        throw error
      }
    },

    async updateCurrentUser() {
      if(this.isAuthenticated){
        try {
          const userData = await getCurrentUser()
          this.email = userData!.email
          this.username = userData!.username
          this.roles = userData!.roles
          this.id = userData!.id
        } catch {}
      }
    },

    async startAutoUpdate() {
      await this.updateCurrentUser()
      this._intervalId = setInterval(() => {
        this.updateCurrentUser()
      }, 300000) // cada 5 minutos
    },

    stopAutoUpdate() {
      if (this._intervalId) {
        clearInterval(this._intervalId)
      }
    },
  },
})

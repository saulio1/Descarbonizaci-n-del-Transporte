import type UserOutputDto from '@/api/users/interfaces/output/user.output.dto.ts'
import { defineStore } from 'pinia'
import {
  createUser,
  deleteUser,
  getAllUsers,
  updateUser,
} from '@/api/users/services/users.service.ts'
import type UserInputDto from '@/api/users/interfaces/input/user.input.dto.ts'
import type UserPatchInputDto from '@/api/users/interfaces/input/user-patch.input.dto.ts'

interface UsersState {
  users: UserOutputDto[]
  loading: boolean
  isDialogOpen: boolean
  dialogMode: 'create' | 'edit'
  selectedUser: UserOutputDto | null
  loadingDialog: boolean
}

export const useUsersStore = defineStore('users', {
  state: (): UsersState => ({
    users: [],
    loading: false,
    isDialogOpen: false,
    dialogMode: 'create',
    selectedUser: null,
    loadingDialog: false,
  }),

  actions: {
    async fetchUsers() {
      this.loading = true
      try {
        const users = await getAllUsers()
        this.users = users!
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    openDialog(mode: 'create' | 'edit', user?: UserOutputDto) {
      this.dialogMode = mode
      this.selectedUser = user || null
      this.isDialogOpen = true
    },

    closeDialog() {
      this.isDialogOpen = false
      this.selectedUser = null
    },

    async createUser(user: UserInputDto) {
      try {
        this.loadingDialog = true
        await createUser(user)
        await this.fetchUsers()
        this.closeDialog()
      } catch (error) {
        throw error
      } finally {
        this.loadingDialog = false
      }
    },

    async updateUser(user: UserPatchInputDto) {
      if (this.selectedUser) {
        try {
          this.loadingDialog = true
          await updateUser(this.selectedUser.id, user)
          await this.fetchUsers()
          this.closeDialog()
        } catch (error) {
          throw error
        } finally {
          this.loadingDialog = false
        }
      }
    },

    async deleteUser(id: string) {
      try {
        this.loading = true
        await deleteUser(id)
        await this.fetchUsers()
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})

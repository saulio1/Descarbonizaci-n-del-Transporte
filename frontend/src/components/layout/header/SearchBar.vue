<template>
  <div
    class="hidden lg:block relative"
    @keydown.down.prevent="onArrowDown"
    @keydown.up.prevent="onArrowUp"
    @keydown.enter.prevent="onEnter"
  >
    <form @submit.prevent>
      <div class="relative">
        <button type="button" class="absolute -translate-y-1/2 left-4 top-1/2" tabindex="-1">
          <svg
            class="fill-gray-500 dark:fill-gray-400"
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M3.04175 9.37363C3.04175 5.87693 5.87711 3.04199 9.37508 3.04199C12.8731 3.04199 15.7084 5.87693 15.7084 9.37363C15.7084 12.8703 12.8731 15.7053 9.37508 15.7053C5.87711 15.7053 3.04175 12.8703 3.04175 9.37363ZM9.37508 1.54199C5.04902 1.54199 1.54175 5.04817 1.54175 9.37363C1.54175 13.6991 5.04902 17.2053 9.37508 17.2053C11.2674 17.2053 13.003 16.5344 14.357 15.4176L17.177 18.238C17.4699 18.5309 17.9448 18.5309 18.2377 18.238C18.5306 17.9451 18.5306 17.4703 18.2377 17.1774L15.418 14.3573C16.5365 13.0033 17.2084 11.2669 17.2084 9.37363C17.2084 5.04817 13.7011 1.54199 9.37508 1.54199Z"
              fill=""
            />
          </svg>
        </button>
        <input
          ref="searchInput"
          type="text"
          v-model="query"
          placeholder="Buscar una sección..."
          class="dark:bg-dark-900 h-11 w-full rounded-lg border border-gray-200 bg-transparent py-2.5 pl-12 pr-14 text-sm text-gray-800 shadow-theme-xs placeholder:text-gray-400 focus:border-brand-300 focus:outline-hidden focus:ring-3 focus:ring-brand-500/10 dark:border-gray-800 dark:bg-gray-900 dark:bg-white/[0.03] dark:text-white/90 dark:placeholder:text-white/30 dark:focus:border-brand-800 xl:w-[430px]"
          @focus="showDropdown = true"
          @blur="onBlur"
          autocomplete="off"
          aria-autocomplete="list"
          aria-controls="autocomplete-list"
          :aria-expanded="showDropdown"
          aria-activedescendant="activeOptionId"
        />

        <div
          class="absolute right-2.5 top-1/2 inline-flex -translate-y-1/2 items-center gap-0.5 rounded-lg border border-gray-200 bg-gray-50 px-[7px] py-[4.5px] text-xs -tracking-[0.2px] text-gray-500 dark:border-gray-800 dark:bg-white/[0.03] dark:text-gray-400"
        >
          <span> Ctrl + </span>
          <span> K </span>
        </div>

        <!-- Dropdown -->
        <ul
          v-if="showDropdown && filteredRoutes.length > 0"
          id="autocomplete-list"
          class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-lg border border-gray-100/70 bg-white py-1 text-sm shadow-lg ring-1 ring-black ring-opacity-5 dark:border-gray-700 dark:bg-gray-800 scrollbar-thin scrollbar-thumb-gray-300 dark:scrollbar-thumb-gray-600 scrollbar-thumb-rounded"
          role="listbox"
        >
          <li
            v-for="(route, index) in filteredRoutes"
            :key="route.path"
            :id="index === highlightedIndex ? 'activeOptionId' : undefined"
            @mousedown.prevent="selectRoute(route)"
            @mouseenter="highlightedIndex = index"
            class="cursor-pointer px-4 py-2 transition-colors duration-150 ease-in-out hover:bg-brand-100 dark:hover:bg-brand-700 focus:bg-brand-200 dark:focus:bg-brand-600 rounded-md"
            :class="{ 'bg-brand-100 dark:bg-brand-700 font-semibold': index === highlightedIndex }"
            role="option"
            :aria-selected="index === highlightedIndex"
          >
            {{ route.name }}
          </li>
        </ul>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.ts'

const searchInput = ref<HTMLInputElement | null>(null)
const router = useRouter()
const auth = useAuthStore()

const query = ref('')
const showDropdown = ref(false)
const highlightedIndex = ref(-1)

const allRoutes = [
  { path: '/equipments', name: 'Equipamientos' },
  { path: '/', name: 'Inicio' },
  { path: '/users', name: 'Usuarios' },
  { path: '/workers/positions', name: 'Ocupaciones de trabajadores' },
  { path: '/profile', name: 'Perfil' },
  { path: '/equipments/types', name: 'Tipos de equipamientos' },
  { path: '/workers', name: 'Trabajadores' },
  { path: '/equipments/usage-data', name: 'Datos de uso'}
]

function canAccess(path: string): boolean {
  if (!auth.isAuthenticated) return false
  if (['/404', '/403', '/login'].includes(path)) return false

  if (!auth.isAdmin && path === '/users') return false

  if (
    !auth.isUser &&
    !auth.isModerator &&
    ['/workers', '/workers/positions', '/equipments', '/equipments/types', '/equipments/usage-data'].includes(path)
  )
    return false

  return true
}

const accessibleRoutes = computed(() => {
  return allRoutes.filter((route) => canAccess(route.path))
})

const filteredRoutes = computed(() => {
  if (!query.value.trim()) return accessibleRoutes.value
  return accessibleRoutes.value.filter((route) =>
    route.name.toLowerCase().includes(query.value.toLowerCase()),
  )
})

function selectRoute(route: { path: string; name: string }) {
  router.push(route.path)
  query.value = ''
  showDropdown.value = false
  highlightedIndex.value = -1
  searchInput.value?.blur()
}

function onArrowDown() {
  if (!showDropdown.value) {
    showDropdown.value = true
    return
  }
  if (highlightedIndex.value < filteredRoutes.value.length - 1) {
    highlightedIndex.value++
  } else {
    highlightedIndex.value = 0
  }
}

function onArrowUp() {
  if (!showDropdown.value) {
    showDropdown.value = true
    return
  }
  if (highlightedIndex.value > 0) {
    highlightedIndex.value--
  } else {
    highlightedIndex.value = filteredRoutes.value.length - 1
  }
}

function onEnter() {
  if (highlightedIndex.value >= 0 && highlightedIndex.value < filteredRoutes.value.length) {
    selectRoute(filteredRoutes.value[highlightedIndex.value])
  }
}

function onBlur() {
  setTimeout(() => {
    showDropdown.value = false
    highlightedIndex.value = -1
  }, 100)
}

function focusSearchInput(e: KeyboardEvent) {
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
    e.preventDefault()
    searchInput.value?.focus()
    showDropdown.value = true
  }
}

onMounted(() => {
  window.addEventListener('keydown', focusSearchInput)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', focusSearchInput)
})

watch(filteredRoutes, () => {
  highlightedIndex.value = -1
})
</script>

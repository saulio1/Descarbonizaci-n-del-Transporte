<template>
  <router-link to="/" class="lg:hidden">
    <img class="dark:hidden max-lg:w-sm" src="/images/logo/logo.png" alt="Logo" />
    <img class="hidden dark:block max-lg:w-sm" src="/images/logo/logo-dark.png" alt="Logo" />
  </router-link>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

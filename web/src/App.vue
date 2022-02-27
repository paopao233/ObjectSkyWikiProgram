<template>
  <!-- 不变化的部分写在app.vue这个页面 变化的就写在各自的页面即可 -->
  <a-layout>
    <the-header></the-header>
    <router-view/>
    <the-footer></the-footer>
  </a-layout>
</template>

<script lang="ts">
import TheHeader from '@/components/the-header.vue';
import TheFooter from '@/components/the-footer.vue';
import {defineComponent} from "vue"; // @ is an alias to /src

export default defineComponent({
  name:'app',
  components: {
    TheHeader,
    TheFooter,
  },
})

</script>



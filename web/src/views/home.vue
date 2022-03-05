<template>
  <!-- 这里是每个页面都变动都内容 -->
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="submenuHandleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
              <span>
                <user-outlined/>
                {{ item.name }}
              </span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">{{ child.name }}</a-menu-item>
        </a-sub-menu>

      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用万物天空知识库</h1>
      </div>
      <!--   data-source是一个列表 会被循环   -->
      <a-list v-show="!isShowWelcome"
              item-layout="vertical" size="large"
              :grid="{ gutter: 20,  xs:1, sm: 2, md: 3, lg: 4, xl:4, xxl: 4}"
              :data-source="ebooks"

      >
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
                <span v-for="{ type, text } in actions" :key="type">
                  <component v-bind:is="type" style="margin-right: 8px"/>
                  {{ text }}
                </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>

    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import axios from 'axios';
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import {StarOutlined, LikeOutlined, MessageOutlined} from '@ant-design/icons-vue';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool';

const listData: Record<string, string>[] = [];
export default defineComponent({
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },


  /* vue3 新增的 */
  setup() {
    const isShowWelcome = ref(true);
    let categoryId2 = 0; // 二级分类的id
    const level1 = ref(); // 一级分类树，children属性就是二级分类
    level1.value = [];

    const ebooks = ref(); // 响应式的数据：可实时刷新到界面上
    // const ebooks1 = reactive({books: []}); // vue3新增的一个函数 books是一个json对象 这里先放一个空数组 后面得到数据后再添加

    /**
     * 查询分类列表数据
     **/
    const categorysQueryHandle = (params: any) => {
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get('/category/allList', {
            //params:{} 是axios固定写法 而且一般不会把整个params传入进去 都是写好要哪些
            params: {
              name: params.name ? params.name : 'list',
            }
          }
      ).then((res) => {
        const data = res.data;
        if (data.success) {
          const categorys = data.data;

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);// 递归 初始条件是找到父亲

        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 查询电子书列表数据
     */
    const bookListHandle = () => {
      axios.get("/ebook/list",{
        params:{
          categoryId2: categoryId2,
        }
      }).then((res) => {
        const data = res.data;
        if(data.success){
          ebooks.value = data.data.list;
          // ebooks1.books = data.data;
        }else{
          if (data.message === "查询后列表为空，请检查参数"){
            message.error("该分类下没有电子书哦～换一个分类吧");
            ebooks.value = [];
          }else{
            message.error(data.message);
          }
        }


      });
    }

    /**
     * 侧边栏菜单点击事件
     */
    const submenuHandleClick = (value: any) => {
      // isShowWelcome.value = value === "welcome";

      if (value.key == "welcome") {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        bookListHandle();
      }
    };

    /**
     * onMounted是指页面加载的时候会加载的函数 跟微信小城西onload一样
     */
    onMounted(() => {
      // 查询电子书
      // bookListHandle(),

      // 查询分类列表数据
      categorysQueryHandle({})

    });

    /**
     * list component
     */
    const pagination = {
      onChange: (page: number) => {
        // console.log(page);
      },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];


    /**
     * html代码要拿到响应式的变量，所以要在setup最后return出去
     */
    return {
      ebooks,
      // books: toRef(ebooks1, "books"),// 这里需要把他转为ref对象 再return出去
      listData,
      pagination,
      actions,

      //category menu
      categorysQueryHandle,
      level1,
      submenuHandleClick,
      isShowWelcome,


    }
  }
})
;
</script>

<!-- scoped: 表示只在当前组件起作用 -->
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
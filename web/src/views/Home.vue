<template>
  <!-- 这里是每个页面都变动都内容 -->
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <template #title>
              <span>
                <user-outlined/>
                subnav 1
              </span>
          </template>
          <a-menu-item key="1">option1</a-menu-item>
          <a-menu-item key="2">option2</a-menu-item>
          <a-menu-item key="3">option3</a-menu-item>
          <a-menu-item key="4">option4</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
              <span>
                <laptop-outlined/>
                subnav 2
              </span>
          </template>
          <a-menu-item key="5">option5</a-menu-item>
          <a-menu-item key="6">option6</a-menu-item>
          <a-menu-item key="7">option7</a-menu-item>
          <a-menu-item key="8">option8</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <template #title>
              <span>
                <notification-outlined/>
                subnav 3
              </span>
          </template>
          <a-menu-item key="9">option9</a-menu-item>
          <a-menu-item key="10">option10</a-menu-item>
          <a-menu-item key="11">option11</a-menu-item>
          <a-menu-item key="12">option12</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="listData">
        <template #footer>
          <div>
            <b>ant design vue</b>
            footer part
          </div>
        </template>
        <template #renderItem="{ item }">
          <a-list-item key="item.title">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px"/>
            {{ text }}
          </span>
            </template>
            <template #extra>
              <img
                  width="272"
                  alt="logo"
                  src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
              />
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.title }}</a>
              </template>
              <template #avatar>
                <a-avatar :src="item.avatar"/>
              </template>
            </a-list-item-meta>
            {{ item.content }}
          </a-list-item>
        </template>
      </a-list>

    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import axios from 'axios';
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
// import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';

const listData: Record<string, string>[] = [];
for (let i = 0; i < 23; i++) {
  listData.push({
    href: 'https://www.antdv.com/',
    title: `ant design vue part ${i}`,
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    description:
        'Ant Design, a design language for background applications, is refined by Ant UED Team.',
    content:
        'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
  });
}

export default defineComponent({
  // components: {
  //   StarOutlined,
  //   LikeOutlined,
  //   MessageOutlined,
  // },

  /* vue3 新增的 */
  setup() {
    const ebooks = ref(); // 响应式的数据：可实时刷新到界面上
    const ebooks1 = reactive({books: []}); // vue3新增的一个函数 books是一个json对象 这里先放一个空数组 后面得到数据后再添加

    /**
     * onMounted是指页面加载的时候会加载的函数 跟微信小城西onload一样
     */
    onMounted(() => {
      axios.get("http://localhost:8080/ebook/list?name=spring").then((res) => {
        const data = res.data;
        ebooks.value = data.data;
        ebooks1.books = data.data;

        console.log(res)
      });
    });

    /**
     * list component
     */
    const pagination = {
      onChange: (page: number) => {
        console.log(page);
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
      books: toRef(ebooks1, "books"),// 这里需要把他转为ref对象 再return出去
      listData,
      pagination,
      actions,
    }
  }
})
;
</script>

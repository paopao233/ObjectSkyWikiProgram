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

      <!--   data-source是一个列表 会被循环   -->
      <a-table
          :dataSource="ebooks"
          :columns="columns"
          :row-key="record => record.id"
          :pagination:="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{text:cover}">
          <img v-if="cover" :src="cover" alt="avatar">
        </template>
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">
import axios from 'axios';
import {defineComponent, onMounted, ref} from 'vue';

export default defineComponent({

  /* setup() vue3 新增的 */
  name: 'AdminEbook',
  setup() {
    const ebooks = ref(); // 响应式的数据：可实时刷新到界面上
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: '操作',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get('/ebook/list', params).then((res) => {
        loading.value = false;
        const data = res.data;
        ebooks.value = data.data;
        // 重置分页按钮
        pagination.value.current = params.page;
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    // const categoryIds = ref();
    // const ebook = ref();
    // const modalVisible = ref(false);
    // const modalLoading = ref(false);
    // const handleModalOk = () => {
    //   modalLoading.value = true;
    //   ebook.value.category1Id = categoryIds.value[0];
    //   ebook.value.category2Id = categoryIds.value[1];
    //   axios.post("/ebook/save", ebook.value).then((response) => {
    //     modalLoading.value = false;
    //     const data = response.data; // data = commonResp
    //     if (data.success) {
    //       modalVisible.value = false;
    //
    //       // 重新加载列表
    //       handleQuery({
    //         page: pagination.value.current,
    //         size: pagination.value.pageSize,
    //       });
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };

    // /**
    //  * 编辑
    //  */
    // const edit = (record: any) => {
    //   modalVisible.value = true;
    //   ebook.value = Tool.copy(record);
    //   categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    // };
    //
    // /**
    //  * 新增
    //  */
    // const add = () => {
    //   modalVisible.value = true;
    //   ebook.value = {};
    // };
    //
    // const handleDelete = (id: number) => {
    //   axios.delete("/ebook/delete/" + id).then((response) => {
    //     const data = response.data; // data = commonResp
    //     if (data.success) {
    //       // 重新加载列表
    //       handleQuery({
    //         page: pagination.value.current,
    //         size: pagination.value.pageSize,
    //       });
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };
    //
    // const level1 =  ref();
    // let categorys: any;
    // /**
    //  * 查询所有分类
    //  **/
    // const handleQueryCategory = () => {
    //   loading.value = true;
    //   axios.get("/category/all").then((response) => {
    //     loading.value = false;
    //     const data = response.data;
    //     if (data.success) {
    //       categorys = data.content;
    //       console.log("原始数组：", categorys);
    //
    //       level1.value = [];
    //       level1.value = Tool.array2Tree(categorys, 0);
    //       console.log("树形结构：", level1.value);
    //
    //       // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
    //       handleQuery({
    //         page: 1,
    //         size: pagination.value.pageSize,
    //       });
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };
    //
    // const getCategoryName = (cid: number) => {
    //   // console.log(cid)
    //   let result = "";
    //   categorys.forEach((item: any) => {
    //     if (item.id === cid) {
    //       // return item.name; // 注意，这里直接return不起作用
    //       result = item.name;
    //     }
    //   });
    //   return result;
    // };

    onMounted(() => {
      handleQuery({});
    });

    return {
      //--- 方法内调用的不需要return
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange

    }
  }
});
</script>

<style scoped>
  img {
    width: 50px;
    height: 50px;
  }
</style>

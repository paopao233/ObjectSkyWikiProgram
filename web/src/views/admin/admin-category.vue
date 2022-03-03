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
      <a-button type="primary" @click="add" size="large">
        新增
      </a-button>
      <!--   data-source是一个列表 会被循环   -->
      <a-table
          :dataSource="categorys"
          :columns="columns"
          :row-key="record => record.id"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{text:cover}">
          <img v-if="cover" :src="cover" alt="avatar">
        </template>
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
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

  <!-- vue2的template下只能有一个节点，但是vue3是支持多个节点的 -->
  <a-modal
      title="分类表单"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
  >
    <!--  category这里是新的响应式  -->
    <a-form :model="category" :label-col="{span: 6}" :wrapper-col="wrapperCol">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>



    </a-form>

  </a-modal>
</template>


<script lang="ts">
import axios from 'axios';
import {defineComponent, onMounted, ref} from 'vue';

export default defineComponent({

  /* setup() vue3 新增的 */
  name: 'AdminCategory',
  setup() {
    const categorys = ref(); // 响应式的数据：可实时刷新到界面上
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get('/category/list', {
            //params:{} 是axios固定写法 而且一般不会把整个params传入进去 都是写好要哪些
            params: {
              page: params.page,
              size: params.size,
            }
          }
      ).then((res) => {
        loading.value = false;
        const data = res.data;
        categorys.value = data.data.list;
        // 重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.data.total;
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
    const category = ref({});
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    /**
     * 编辑
     * @param record
     */
    const edit = (record: any) => {
      visible.value = true;
      category.value = record; // 响应式的变量都是得用value

    };

    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      category.value = {}; // 清空category
    };

    /**
     * 点击修改保存数据
     */
    const handleOk = () => {
      confirmLoading.value = true;
      axios.post('/category/save', category.value).then((res) => {
        const data = res.data;
        if (data.success) {
          visible.value = false;
          confirmLoading.value = false;
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      })
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      //--- 方法内调用的不需要return

      categorys,
      // table list
      pagination,
      columns,
      loading,
      handleTableChange,

      // modal
      visible,
      confirmLoading,
      edit,
      handleOk,

      // form
      category,

      // add
      add
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

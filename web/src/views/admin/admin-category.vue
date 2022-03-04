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
      <a-input-search
          v-model:value="value"
          placeholder="请输入要查询的名称"
          enter-button="搜索"
          style="width: 400px;margin-right: 10px"
          @search="onSearch"
          size="large"
      />
      <a-button type="primary" @click="add" size="large" style="margin-right: 10px">
        新增
      </a-button>
      <a-button type="primary" @click="handleQuery" size="large">
        查询所有
      </a-button>
      <!--   data-source是一个列表 会被循环   -->
      <a-table
          :dataSource="categorys"
          :columns="columns"
          :row-key="record => record.id"
          :pagination="false"
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
            <a-popconfirm
                title="你确定要删除吗？该操作不可逆。"
                ok-text="确定"
                cancel-text="取消"
                @confirm="deleteHandle(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>

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
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({

  /* setup() vue3 新增的 */
  name: 'AdminCategory',
  setup() {
    const value = ref<string>(''); // 搜索框的值
    const categorys = ref(); // 响应式的数据：可实时刷新到界面上

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
     * 一级分类树，children属性就是二级分类 支持无限级
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级分类树，children属性就是二级分类
    // level1.value = [];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get('/category/allList', {
            //params:{} 是axios固定写法 而且一般不会把整个params传入进去 都是写好要哪些
            params: {
              name: params.name ? params.name : 'list',
            }
          }
      ).then((res) => {
        loading.value = false;
        const data = res.data;
        if (data.success) {
          categorys.value = data.data;
          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value,0);// 递归 初始条件是找到父亲
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      // console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({});
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
      // category.value = record; // 响应式的变量都是得用value ； 如果把值直接给category 会有响应式问题
      category.value = Tool.copy(record);
    };

    /**
     * 删除
     * @param record
     */
    const deleteHandle = (id: number) => {
      axios.post('/category/delete/' + id).then((res) => {
        const data = res.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({});
          message.success("分类删除成功～");
        } else {
          message.error(data.message);
        }
      });
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
          handleQuery({});
          message.success("信息保存成功～");
        } else {
          message.error(data.message);
          confirmLoading.value = false;
        }
      })
    };

    /**
     * onSearch
     */
    const onSearch = (searchValue: string) => {
      //
      searchValue = searchValue.trim();
      if (searchValue !== "") {
        handleQuery({
          name: searchValue,
        });

      } else {
        message.error("内容不能为空～");
      }
    };

    /**
     * 初始化函数
     */
    onMounted(() => {
      handleQuery({});
    });

    return {
      //--- 方法内调用的不需要return
      handleQuery,
      categorys,
      // table list
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
      add,

      // delete
      deleteHandle,

      // search
      value,
      onSearch,

      // table
      level1,

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

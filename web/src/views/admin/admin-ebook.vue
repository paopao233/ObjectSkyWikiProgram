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
      <a-button type="primary" @click="add" size="large">
        新增
      </a-button>
      <!--   data-source是一个列表 会被循环   -->
      <a-table
          :dataSource="ebooks"
          :columns="columns"
          :row-key="record => record.id"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{text:cover}">
          <img v-if="cover" :src="cover" alt="avatar">
        </template>
        <template v-slot:category="{text,record}">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="default">
                文档管理
              </a-button>
            </router-link>
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
      title="电子书表单"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
  >
    <!--  ebook这里是新的响应式  -->
    <a-form :model="ebook" :label-col="{span: 6}" :wrapper-col="wrapperCol">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{label:'name',value:'id',children:'children'}"
            :options="level1"
        />
      </a-form-item>
      <a-form-item label="文档数">
        <a-input v-model:value="ebook.docCount"/>
      </a-form-item>
      <a-form-item label="阅读数">
        <a-input v-model:value="ebook.viewCount"/>
      </a-form-item>
      <a-form-item label="点赞数">
        <a-input v-model:value="ebook.voteCount"/>
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description"/>
      </a-form-item>

    </a-form>

  </a-modal>
</template>


<script lang="ts">
import axios from 'axios';
import {defineComponent, onMounted, ref} from 'vue';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool';

export default defineComponent({

  /* setup() vue3 新增的 */
  name: 'AdminEbook',
  setup() {
    const value = ref<string>(''); // 搜索框的值
    const ebook = ref();
    const ebooks = ref(); // 响应式的数据：可实时刷新到界面上
    const pagination = ref({
      current: 1,
      pageSize: 4,
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
        title: '描述',
        dataIndex: 'description'
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
    level1.value = [];
    let categorys: any; // 给分类显示名称用


    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get('/ebook/list', {
            //params:{} 是axios固定写法 而且一般不会把整个params传入进去 都是写好要哪些
            params: {
              page: params.page,
              size: params.size,
              name: params.name ? params.name : "list",
            }
          }
      ).then((res) => {
        loading.value = false;
        const data = res.data;

        if (data.success) {
          ebooks.value = data.data.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.data.total;
        } else {
          if (data.message == "查询后列表为空，请检查参数") {
            handleQuery({
              page: params.page - 1,
              size: params.size,
            });
          } else {
            message.error(data.message);
          }

        }
      });
    };

    /**
     * 查询分类列表数据 异步操作：相当于到了get方法，就重新开启一个线程去获取数据 不会等到获取数据再去执行其他方法的
     **/
    const categorysQueryHandle = (params: any) => {
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
          categorys = data.data;

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);// 递归 初始条件是找到父亲

          // 查询电子书列表
          // 加载完分类后 再加载电子书 ，否则如果分类数加载的很慢，则电子书渲染就会出错！
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 通过查询category列表 拼接分类名
     */
    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id == cid) {
          result = item.name;
        }
      });
      return result;
    }

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      // console.log("看看自带的分页参数都有啥：" + pagination);
      // 传递页码
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref(); // 级联
    // const ebook = ref({});
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    /**
     * 编辑
     * @param record
     */
    const edit = (record: any) => {
      visible.value = true;
      // ebook.value = record; // 响应式的变量都是得用value;
      // 如果把值直接给ebook 会有响应式问题；因为改动时，改的是record 所以 record的值也会更着改变
      ebook.value = Tool.copy(record);
      // console.log(ebook.value);
      // 将分类的id映射出去
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };

    /**
     * 删除
     * @param record
     */
    const deleteHandle = (id: number) => {
      axios.post('/ebook/delete/' + id).then((res) => {
        const data = res.data;
        if (data.success) {
          message.success("删除成功～");
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
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
      ebook.value = {}; // 清空ebook

    };

    /**
     * 点击修改保存数据
     */
    const handleOk = () => {
      confirmLoading.value = true;
      // 将分页的id保存到电子书的去
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];

      // 发送请求
      axios.post('/ebook/save', ebook.value).then((res) => {
        const data = res.data;
        if (data.success) {
          visible.value = false;
          confirmLoading.value = false;
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
          // message
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
          page: 1,
          size: pagination.value.pageSize,
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
      // 查询分类列表
      categorysQueryHandle({});

    });

    return {
      //--- 方法内调用的不需要return

      ebooks,
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
      ebook,

      // add
      add,

      // delete
      deleteHandle,

      // search
      value,
      onSearch,

      // category
      categorysQueryHandle,
      categoryIds,
      level1,
      getCategoryName,
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

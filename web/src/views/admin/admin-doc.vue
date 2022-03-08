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
      <a-row :gutter="24">
        <a-col :span="8">
          <a-input-search
              v-model:value="value"
              placeholder="请输入要查询的名称"
              enter-button="搜索"
              style="width: 300px;margin-right: 10px"
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
              v-if="level1.length > 0"
              size="small"
              :dataSource="level1"
              :columns="columns"
              :row-key="record => record.id"
              :pagination="false"
              :loading="loading"
              :defaultExpandAllRows="true"
              @change="handleTableChange"
          >
            <template #name="{text,record}">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{text,record}">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="你确定要删除吗？该操作不可逆。"
                    ok-text="确定"
                    cancel-text="取消"
                    @confirm="deleteHandle(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>

              </a-space>
            </template>
          </a-table>
        </a-col>

        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <!--  level1这里是新的响应式  -->
          <a-form :model="level1" :label-col="{span: 6}" :wrapper-col="wrapperCol" layout="vertical">
            <a-form-item label="名称">
              <a-input v-model:value="doc.name" placeholder="请输入名称" />
            </a-form-item>
            <a-form-item label="父文档">
              <a-select-option value="0">
                无
              </a-select-option>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :tree-data="treeSelectData"
                  :replaceFields="{title:'name',key:'id',value:'id'}"
              >
              </a-tree-select>
            </a-form-item>

            <a-form-item label="排序">
              <a-input v-model:value="doc.sort" placeholder="请输入顺序" />
            </a-form-item>

            <a-form-item label="内容">
              <div id="content">

              </div>
            </a-form-item>


          </a-form>

        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>

  <!-- vue2的template下只能有一个节点，但是vue3是支持多个节点的 -->
  <!--  <a-modal-->
  <!--      title="文档表单"-->
  <!--      v-model:visible="visible"-->
  <!--      :confirm-loading="confirmLoading"-->
  <!--      @ok="handleOk"-->
  <!--  >-->
  <!--  </a-modal>-->

</template>


<script lang="ts">
import axios from 'axios';
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import {message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from '@ant-design/icons-vue';
import E from "wangeditor"

export default defineComponent({

  /* setup() vue3 新增的 */
  name: 'AdminDoc',
  setup() {
    // 富文本
    const editor = new E("#content")
    editor.config.zIndex = 0;// 父文本的权重

    // 接收来自路由传递过来的参数
    const route = useRoute();

    const value = ref<string>(''); // 搜索框的值
    const docs = ref(); // 响应式的数据：可实时刷新到界面上

    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 使用递归方法，将某节点及其子节点的id存储起来
     */
    let idList: Array<String> = [];
    const getDeleteIdList = (treeSelectData: any, id: number) => {
      //遍历数组

      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          idList.push(node.id);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIdList(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIdList(children, id);
          }
        }

      }
    }

    /**
     * 使用递归方法，将某节点及其子节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: number) => {
      //遍历数组
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          node.disabled = "disabled";

          // 遍历所有子节点 将所有子节点都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }

      }
    }

    /**
     * 一级文档树，children属性就是二级文档 支持无限级
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];
    const treeSelectData = ref(); // 树形下拉框的响应式数据

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      level1.value = [];
      axios.get('/doc/allList', {
            //params:{} 是axios固定写法 而且一般不会把整个params传入进去 都是写好要哪些
            params: {
              name: params.name ? params.name : 'list',
            }
          }
      ).then((res) => {
        loading.value = false;
        const data = res.data;
        if (data.success) {
          docs.value = data.data;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);// 递归 初始条件是找到父亲

        } else {
          message.error(data.message);
        }
      });
    };

    // -------- 表单 ---------
    const doc = ref({});
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    /**
     * 编辑
     * @param record
     */
    const edit = (record: any) => {
      visible.value = true;

      // doc.value = record; // 响应式的变量都是得用value ； 如果把值直接给doc 会有响应式问题
      doc.value = Tool.copy(record);

      // 把level1复制一份 重新定义treeData
      treeSelectData.value = Tool.copy(level1.value);

      // 将该节点下的子节点页包括自己 都disabled
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无" unshift是指在数组前面添加一个节点
      treeSelectData.value.unshift({id: 0, name: '无'})

      // 由于modal初始化需要时间 而富文本框很快就初始化好了 所以要有一个时间等待
      // 取消modal  就不需要了
      // setTimeout(function () {
      //   editor.create();// 初始化富文本
      // }, 100);
    };


    const showModal = () => {
      visible.value = true;
    };

    /**
     * 删除
     * @param record
     */
    const deleteHandle = (id: number) => {
      idList = []; // 没有这个会把其他id也带进来的！
      getDeleteIdList(level1.value, id); // 响应式变量 ！

      const axiosDelete = () => {
        getDeleteIdList(level1.value, id); // 响应式变量 ！
        axios.post('/doc/delete/' + idList).then((res) => {
          const data = res.data;
          if (data.success) {
            // 重新加载列表
            handleQuery({});
            message.success("文档删除成功～");
          } else {
            message.error(data.message);
          }
        });
      }

      const showDeleteConfirm = () => {
        Modal.confirm({
          title: () => '你真的要删除吗？',
          icon: () => createVNode(ExclamationCircleOutlined),
          content: () => '要删除的文档还包括其他文档，你确定要一起删除吗？（该操作不可逆，请谨慎操作。）',
          okText: () => '确定',
          okType: 'danger',
          cancelText: () => '取消',
          onOk() {
            axiosDelete();
          },
          onCancel() {
            console.log('Cancel');
          },
        });
      };

      // 父文档有孩子才提示
      if (idList.length > 1) {
        showDeleteConfirm();
      } else {
        axiosDelete();
      }


    };

    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      doc.value = {
        ebookId: route.query.ebookId,
      }; // 清空doc

      // 把level1复制一份 重新定义treeData
      treeSelectData.value = Tool.copy(level1.value);

      // 为选择树添加一个"无" unshift是指在数组前面添加一个节点
      treeSelectData.value.unshift({id: 0, name: '无'})

      // 由于modal初始化需要时间 而富文本框很快就初始化好了 所以要有一个时间等待
      // setTimeout(function () {
      //   editor.create();// 初始化富文本
      // }, 200);
    };

    /**
     * 点击修改保存数据
     */
    const handleSave = () => {
      confirmLoading.value = true;
      axios.post('/doc/save', doc.value).then((res) => {
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
      editor.create();// 初始化富文本
    });

    return {

      //--- 方法内调用的不需要return
      handleQuery,
      // docs,
      // table list
      columns,
      loading,

      // modal
      visible,
      confirmLoading,
      edit,
      handleSave,

      // form
      doc,

      // add
      add,

      // delete
      deleteHandle,

      // search
      value,
      onSearch,

      // table
      level1,
      treeSelectData,
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

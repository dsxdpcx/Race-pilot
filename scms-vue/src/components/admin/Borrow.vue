<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>器材管理</el-breadcrumb-item>
      <el-breadcrumb-item>借用信息</el-breadcrumb-item>
    </el-breadcrumb>

    <!--项目列表主体-->
    <el-card>
      <!--搜索区域-->
      <el-row :gutter="25">
        <el-col :span="5">
          <!--搜索添加-->
          <el-input
              v-model="queryInfo.query"
              clearable
              placeholder="请输入器材名称"
              @clear="page"
              @keyup.enter.native="page"
          >
            <!--搜索按钮-->
            <el-button
                slot="append"
                icon="el-icon-search"
                @click="page"
            ></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!--项目列表 stripe隔行变色-->
      <el-table :data="borrowlist" border stripe>
        <!--索引列-->

        <el-table-column type="index"></el-table-column>
        <el-table-column label="借用ID" prop="boId"></el-table-column>

        <el-table-column label="器材" prop="eqId"></el-table-column>

        <el-table-column label="借用数量" prop="boNum"></el-table-column>
        <el-table-column label="借用时间" prop="boStarttime"></el-table-column>
        <el-table-column label="归还时间" prop="boEndtime"></el-table-column>
        <el-table-column label="借用状态" prop="boState"></el-table-column>
        <el-table-column label="借用原因" prop="boDescription"></el-table-column>
        <el-table-column label="操作" prop="state">
          <template slot-scope="scope">
            <!--取消报名-->
            <el-button
                :disabled="scope.row.userIds!=null"
                size="mini"
                type="primary"
                @click="editBorrow(scope.row)"
            >归还器材
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <div>
        <el-pagination
            :current-page="queryInfo.currentPage"
            :page-size="queryInfo.pageSize"
            :page-sizes="[5, 10, 20, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
    </el-card>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Borrow",
  data() {
    return {
      borrowlist: [],
      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
      //select远程搜索加载中
      loading: false,
      addForm: {
        userId: "",
        eqId: "",
        boNum: "",
        boStarttime: "",
        boState: "0",
        boDescription: "",
      },
      editForm: {
        boId: "",
        userId: "",
        eqId: "",
        boNum: "",
        boStarttime: "",
        boState: "0",
        boDescription: "",
      },
      addDialogVisible: false,
      editDialogVisible: false,
      total: 0,
      // 对话框状态
      dialogTableVisible: false,
      dialogFormVisible: false,
    };
  },
  created() {
    this.page();
  },
  methods: {
    async page() {
      const _this = this;
      axios
          .get(
              "/borrow/queryall"
          )
          .then((res) => {
            let data = res.data.data;
            _this.borrowlist = data;
            _this.borrowlist.forEach((borrow, index) => {
              if (borrow.boState == 0) {
                borrow.boState = "未归还";
              } else {
                borrow.boState = "已归还";
              }

            });
            // _this.queryInfo.currentPage = data.current;
            // _this.total = data.total;
            // _this.queryInfo.pageSize = data.size;
          });
    },




    async getItemDetail(id) {
      const _this = this;
      axios.get("/item/getItem?itemId=" + id).then((res) => {
        let data = res.data.data;
        _this.itemDetail = [];
        _this.itemDetail.push(data);
      });
    },

    handleSizeChange(newSize) {
      const _this = this;
      _this.queryInfo.pageSize = newSize;
      _this.page();
    },
    handleCurrentChange(newPage) {
      const _this = this;
      _this.queryInfo.currentPage = newPage;
      _this.page();
    },



    async showEditDialog(id) {
      const _this = this;
      axios.get("/e/getSeason?seasonId=" + id).then((res) => {
        let data = res.data.data;
        _this.editForm = data;


        _this.editDialogVisible = true;
      });
    },
    addDialogClosed() {
      const _this = this;
      _this.$refs.addFormRef.resetFields();
    },

    async searchUser(queryName) {
      const _this = this;
      _this.loading = true;
      axios
          .get("/user/queryUser?currentPage=1&pageSize=999999999&query=" + queryName)
          .then((res) => {
            let data = res.data.data.records;
            data.forEach((item, index) => {
              item.nickname = item.nickname + "      " + item.team.teamName;
            });
            _this.userList = data;
            this.loading = false;
          });
    },


  },
};
</script>

<style lang="less" scoped>
.el-breadcrumb {
  margin-bottom: 15px;
  font-size: 17px;
}

.myTable {
  border-collapse: collapse;
  margin: 0 auto;
  text-align: center;
}

.myTable td,
.myTable th {
  border: 1px solid #cad9ea;
  color: #666;
  height: 40px;
}
</style>
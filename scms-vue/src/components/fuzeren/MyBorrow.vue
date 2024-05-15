<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>器材管理</el-breadcrumb-item>
      <el-breadcrumb-item>我的借用</el-breadcrumb-item>
    </el-breadcrumb>

    <!--项目列表主体-->
    <el-card>
      <!--项目列表 stripe隔行变色-->
      <!--参数运动员列表 stripe隔行变色-->
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
  name: "MyBorrow",
  data() {
    return {
      borrowlist: [],

      userId: JSON.parse(localStorage.getItem("user")).userId,

      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
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
              "/borrow/queryborrow?userId=" +this.userId
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


    async editBorrow(row) {
      const _this = this;
      const confirmResult = await _this
          .$confirm("是否确定归还？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .catch((err) => err);
      if (confirmResult !== "confirm") {
        return _this.$message.info("已取消操作");
      }
      row.userId=_this.userId;
      if (row.boState == "未归还") {
        row.boState = 0;
      } else {
        row.boState = 1;
      }
      axios
          .put("/borrow/returnequipment",row)
          .then((res) => {
            if (res.data.status == 200) {
              _this.$message.success("已归还");
              _this.addDialogVisible = false;
              _this.page();
            } else {
              _this.$message.error(res.data.msg);
            }
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
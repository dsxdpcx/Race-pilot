<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的器材</el-breadcrumb-item>
      <el-breadcrumb-item>器材借用</el-breadcrumb-item>
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
              placeholder="请输入项目名称"
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
        <el-table :data="equipmentList" border stripe>
          <!--索引列-->
          <el-table-column type="index"></el-table-column>
          <el-table-column label="器材名称" prop="eqName"></el-table-column>
          <el-table-column label="器材型号" prop="eqModel"></el-table-column>
          <el-table-column label="器材放置地点" prop="eqPlace"></el-table-column>
          <el-table-column label="器材数量" prop="eqSum"></el-table-column>
          <el-table-column label="器材购买时间" prop="eqBuytime"></el-table-column>

          <el-table-column label="操作" prop="state">
          <template slot-scope="scope">
            <!--详情-->
            <el-button
                icon="el-icon-tickets"
                size="mini"
                type="primary"
                @click="
                showAddDialog(scope.row.eqId);
              "
            >借用器材
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
    <el-dialog
        :visible.sync="addDialogVisible"
        title="填写借用信息"
        width="40%"
        @close="addDialogClosed"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="180px"
      >
        <el-form-item label="借出数量">
          <el-input v-model="addForm.boNum" placeholder="第一届运动会"></el-input>
        </el-form-item>
        <el-form-item label="借出理由">
          <el-input v-model="addForm.boDescription" placeholder="友谊第一比赛第二" type="textarea"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="borrowEquipment">确定</el-button>


      </span>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "BorrowList",
  data() {
    return {
      equipmentList: [],

      addForm: {
        userId: "",
        eqId: "",
        boNum: "",
        boStarttime: "",
        boEndtime: "",
        boState: "",
        boDescription: ""
      },
      //select远程搜索加载中
      loading: false,

      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
      total: 0,
      // 对话框状态
      addDialogVisible: false,
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
          .get("/equipment/queryequipment")
          .then((res) => {
            let data = res.data.data;
            _this.equipmentList = data;
            // _this.queryInfo.currentPage = data.current;
            // _this.total = data;
            // _this.queryInfo.pageSize = data.size;
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

    async showAddDialog(id) {
      const _this = this;
      axios.get("/equipment/queryone?id=" + id).then((res) => {
        let data = res.data.data;
        _this.addForm.eqId = id;
        _this.addDialogVisible = true;
      });
    },
    addDialogClosed() {
      const _this = this;
      _this.$refs.addFormRef.resetFields();
    },
    borrowEquipment() {
      const _this = this;
      _this.addForm.userId = JSON.parse(
          localStorage.getItem("user")
      ).userId;
      _this.$refs.addFormRef.validate(async (valid) => {
        if (!valid) {return;}
        axios.post("/borrow/borrow", _this.addForm).then((res) => {
          if (res.data.status != 200) {
            return _this.$message.error(res.data.msg);
          }
          _this.$message.success("借用成功");
          _this.addDialogVisible = false;
          _this.page();
        });
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
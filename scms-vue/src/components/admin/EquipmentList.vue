<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>器材管理</el-breadcrumb-item>
      <el-breadcrumb-item>器材管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!--届时列表主体-->
    <el-card>
      <!--搜索区域-->
      <el-row :gutter="25">
        <el-col :span="5">
          <!--搜索添加-->
          <el-input
              v-model="queryInfo.query"
              clearable
              placeholder="请输入运动会名称"
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


        <!--添加按钮-->
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true"
          >添加器材
          </el-button
          >
        </el-col>
      </el-row>
      <!--届时列表 stripe隔行变色-->
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
            <!--修改-->
            <el-button
                icon="el-icon-edit"
                size="mini"
                type="primary"
                @click="showEditDialog(scope.row.eqId)"
            ></el-button>
            <!--删除-->
            <el-button
                icon="el-icon-delete"
                size="mini"
                type="danger"
                @click="deleteEquipment(scope.row.eqId)"
            ></el-button>
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
        title="添加器材"
        width="40%"
        @close="addDialogClosed"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="180px"

      >
        <el-form-item label="器材名称">
          <el-input v-model="addForm.eqName" placeholder="第一届运动会"></el-input>
        </el-form-item>
        <el-form-item label="器材型号">
          <el-input v-model="addForm.eqModel" placeholder="友谊第一比赛第二" type="textarea"></el-input>
        </el-form-item>

        <el-form-item label="器材放置地点">
          <el-input v-model="addForm.eqPlace"></el-input>
        </el-form-item>

        <el-form-item label="器材数量">
          <el-input v-model="addForm.eqSum"></el-input>
        </el-form-item>
        <el-form-item label="器材采购时间">
          <el-date-picker
              v-model="addForm.eqBuytime"
              placeholder="选择日期时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addEquipment">确定</el-button>


      </span>
    </el-dialog>
    <el-dialog
        :visible.sync="editDialogVisible"
        title="修改器材"
        width="40%"
        @close="editDialogClosed"
    >
      <el-form
          ref="editFormRef"
          :model="editForm"
          class="demo-ruleForm"
          label-width="180px"
      >

        <el-form-item label="器材名称">
          <el-input v-model="editForm.eqName"></el-input>
        </el-form-item>
        <el-form-item label="器材型号" type="textarea">
          <el-input v-model="editForm.eqModel"></el-input>
        </el-form-item>

        <el-form-item label="器材采购时间">
          <el-date-picker
              v-model="editForm.eqBuytime"
              placeholder="选择日期时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="器材放置地点">
          <el-input v-model="editForm.eqPlace"></el-input>
        </el-form-item>
        <el-form-item label="器材数量">
          <el-input v-model="editForm.eqSum"></el-input>
        </el-form-item>


      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="editEquipment">确定</el-button>


      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "EquipmentList",
  data() {
    return {
      equipmentList: [],
      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
      total: 0,
      editForm: {
        eqId: "",
        eqName: "",
        eqModel: "",
        eqPlace: "",
        eqSum: "",
        eqBuytime: "",
      },
      addForm: {
        eqId: "",
        eqName: "",
        eqModel: "",
        eqPlace: "",
        eqSum: "",
        eqBuytime: "",
      },
      // 对话框状态
      addDialogVisible: false,

      editDialogVisible: false,
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

    async showEditDialog(id) {
      const _this = this;
      axios.get("/equipment/queryone?id=" + id).then((res) => {
        let data = res.data.data;
        _this.editForm = data;
        _this.editDialogVisible = true;
      });
    },
    addDialogClosed() {
      const _this = this;
      _this.$refs.addFormRef.resetFields();
    },
    editDialogClosed() {
      const _this = this;
      _this.$refs.editFormRef.resetFields();
    },
    addEquipment() {
      const _this = this;
      _this.$refs.addFormRef.validate(async (valid) => {
        if (!valid) return;
        axios.post("/equipment/addequipment", _this.addForm).then((res) => {
          if (res.data.status != 200) {
            return _this.$message.error(res.data.msg);
          }
          _this.$message.success("操作成功");
          _this.addDialogVisible = false;
          _this.page();
        });
      });
    },
    async deleteEquipment(id) {
      const _this = this;
      const confirmResult = await _this
          .$confirm("此操作将永久删除器材，是否继续？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .catch((err) => err);
      if (confirmResult !== "confirm") {
        return _this.$message.info("已取消删除");
      }
      axios.delete("/equipment/removeequipment?eqId=" + id).then((res) => {
        if (res.data.status == 200) {
          _this.$message.success("删除成功");
          _this.addDialogVisible = false;
          _this.page();
        } else {
          _this.$message.error(res.data.msg);
        }
      });
    },
    editEquipment() {
      const _this = this;
      _this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) {
          return;
        }
        axios.put("/equipment/updateequipment", _this.editForm).then((res) => {
          if (res.status != 200) {
            return _this.$message.error(res.msg);
          }
          _this.$message.success("操作成功");
          _this.editDialogVisible = false;
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
</style>
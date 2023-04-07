<template>
  <div class="app-container">
    <!--查询表单-->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称">
              <el-input
                style="width: 100%"
                v-model="search.roleName"
                placeholder="角色名称"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            :loading="loading"
            @click="fetchData()"
            >搜索</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetData"
            >重置</el-button
          >
          <el-button type="success" icon="el-icon-plus" size="mini" @click="add"
            >添 加</el-button
          >
          <el-button type="danger" size="mini" @click="batchRemove()"
            >批量删除</el-button
          >
        </el-row>
      </el-form>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%; margin-top: 10px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" />

      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="edit(scope.row.id)"
            title="修改"
          />
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="removeDataById(scope.row.id)"
            title="删除"
          />
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />

    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form
        ref="dataForm"
        :model="sysRole"
        label-width="150px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="角色名称">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="sysRole.roleCode" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="dialogVisible = false"
          size="small"
          icon="el-icon-refresh-right"
          >取 消</el-button
        >
        <el-button
          type="primary"
          icon="el-icon-check"
          @click="saveOrUpdate()"
          size="small"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api/system/sysRole";

export default {
  data() {
    return {
      list: [],
      page: 1,
      limit: 10,
      total: 0,
      sysRole: {
        roleName: "",
        roleCode: "",
      },
      dialogVisible: false,
      search: {},
      selected: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    handleSelectionChange(selection) {
      this.selected = selection;
      console.log(this.selected);
    },
    edit(id) {
      this.getRoleById(id);
      this.dialogVisible = true;
    },
    getRoleById(id) {
      api.getById(id).then((res) => {
        this.sysRole = res.data;
      });
    },
    add() {
      this.dialogVisible = true;
    },
    saveOrUpdate() {
      if (!this.sysRole.id) {
        this.save();
      } else {
        this.update();
      }
    },
    save() {
      api.saveRole(this.sysRole).then((res) => {
        this.dialogVisible = false;
        this.$message.success("操作成功");
        this.fetchData();
      });
    },
    update() {
      api.updateRole(this.sysRole).then((res) => {
        this.dialogVisible = false;
        this.$message.success("操作成功");
        this.fetchData();
      });
    },
    fetchData(current = 1) {
      this.page = current;
      api.getPageList(this.page, this.limit, this.search).then((res) => {
        this.list = res.data.records;
        this.total = res.data.total;
      });
    },
    removeDataById(id) {
      this.$confirm("是否确认删除", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return api.removeById(id);
        })
        .then((res) => {
          this.fetchData();
          this.$message.success("删除成功");
        });
    },

    batchRemove(id) {
      if (this.selected.length == 0) {
        this.$message.warning("请至少选择一条记录");
        return;
      }
      this.$confirm("请确认是否删除", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          var idList = [];
          this.selected.forEach((element) => {
            idList.push(element.id);
          });
          return api.batchRemove(idList);
        })
        .then((res) => {
          this.fetchData();
          this.$message.success("删除成功");
        });
    },
  },
};
</script>

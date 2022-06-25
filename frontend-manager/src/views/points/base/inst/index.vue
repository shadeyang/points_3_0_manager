<template>
   <div class="app-container" :key="once">
      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="handleAdd"
               v-hasPermi="['points:inst:add']"
            >新增</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table
         v-if="refreshTable"
         v-loading="loading"
         :data="instList"
         row-key="institutionId"
         :default-expand-all="isExpandAll"
         lazy
         :load=getChildrenList
         :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
         <el-table-column prop="institutionId" label="机构ID"></el-table-column>
         <el-table-column prop="institutionNo" label="机构编号"></el-table-column>
         <el-table-column prop="institutionName" label="机构名称"></el-table-column>
         <el-table-column prop="description" label="机构描述" :show-overflow-tooltip="true"></el-table-column>
         <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button
                  type="text"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['points:inst:edit']"
               >修改</el-button>
               <el-button
                  type="text"
                  icon="Plus"
                  @click="handleAdd(scope.row)"
                  v-hasPermi="['points:inst:add']"
               >新增</el-button>
               <el-button
                  type="text"
                  icon="Delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['points:inst:remove']"
               >删除</el-button>
            </template>
         </el-table-column>
      </el-table>

      <!-- 添加或修改菜单对话框 -->
      <el-dialog :title="title" v-model="open" width="680px" :before-close="handleClose" append-to-body>
         <el-form ref="instRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="24">
                    <el-form-item label="机构编号" prop="institutionNo">
                       <el-input v-model="form.institutionNo" />
                    </el-form-item>
               </el-col>
               <el-col :span="24">
                   <el-form-item label="机构名称" prop="institutionName">
                      <el-input v-model="form.institutionName" />
                   </el-form-item>
              </el-col>
              <el-col :span="24">
                  <el-form-item label="描述" prop="description">
                     <el-input v-model="form.description" />
                  </el-form-item>
             </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Inst">
import {queryByParentInst, addInst, updateInst, delInst} from "@/api/points/institution";

const { proxy } = getCurrentInstance();

const instList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(false);
const refreshTable = ref(true);

const data = reactive({
  form: {},
  rules: {
    institutionNo: [{ required: true, message: "机构编号不能为空", trigger: "blur" }],
    institutionName: [{ required: true, message: "机构名称不能为空", trigger: "blur" }]
  },
  once: 0,
});

const { form, rules, once } = toRefs(data);

/** 查询菜单列表 */
function getList() {
  loading.value = true;
  queryByParentInst('0').then(response => {
    instList.value = response;
	once.value +=1
    loading.value = false;
  });
}

async function getChildrenList(inst,treeNode,resolve) {
	console.info(instList)
   queryByParentInst(inst.institutionId).then(response => {
    resolve(response);
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 表单重置 */
function reset() {
  form.value = {
    description: undefined,
    institutionId: undefined,
    institutionNo: undefined,
    institutionName: undefined,
    parentInstitutionId: '0',
    topInstitutionId: '0'
  };
  proxy.resetForm("instRef");
}

/** 关闭弹窗隐藏图标选择 */
function handleClose() {
  cancel();
}
/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}
/** 新增按钮操作 */
async function handleAdd(row) {
  reset();
  if (row != null && row.institutionId) {
    form.value.parentInstitutionId = row.institutionId;
    if(row.parentInstitutionId == '0'){
        form.value.topInstitutionId = row.institutionId;
    }else{
        form.value.topInstitutionId = row.topInstitutionId;
    }
    title.value = "添加【"+row.institutionName+"】机构下的子机构";
  } else {
    form.value.parentInstitutionId = '0';
    form.value.topInstitutionId = '0';
    title.value = "添加顶级机构";
  }
  open.value = true;
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
    form.value = row;
    open.value = true;
    title.value = "修改【"+row.institutionName+"】的信息";
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["instRef"].validate(valid => {
    if (valid) {
      if (form.value.institutionId != undefined) {
        updateInst(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addInst(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除名称为"' + row.institutionName + '"的数据项?').then(function() {
    return delInst(row.institutionId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
</script>

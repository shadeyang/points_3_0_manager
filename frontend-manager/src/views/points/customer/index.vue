<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
      <el-form-item label="客户姓名" prop="customerName">
        <el-input
            v-model="queryParams.customerName"
            placeholder="请输入客户姓名"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="客户编号" prop="customerId">-->
<!--        <el-input-->
<!--            v-model="queryParams.customerId"-->
<!--            placeholder="请输入客户编号"-->
<!--            clearable-->
<!--            style="width: 240px"-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="凭证号码" prop="voucherNo">
        <el-input
              v-model="queryParams.voucherNo"
              placeholder="请输入凭证号码"
              style="width: 400px"
            >
              <template #prepend>
                <el-select v-model="queryParams.voucherTypeNo" placeholder="选择凭证类型" style="width: 160px">
                  <el-option
                      v-for="item in points_voucher_type"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                  />
                </el-select>
              </template>
            </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['points:customer:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['points:customer:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['points:customer:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="客户编号" prop="customerId" width="250"/>
      <el-table-column label="客户姓名" prop="customerName" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="性别" prop="gender" :show-overflow-tooltip="true" width="50">
        <template #default="scope">
          <dict-tag :options="sys_user_sex" :value="scope.row.gender"/>
        </template>
      </el-table-column>
      <el-table-column label="联系号码" prop="phoneNumber" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="Email" prop="email" :show-overflow-tooltip="true" width="200"/>
      <el-table-column label="地址" prop="address" :show-overflow-tooltip="true" width="400"/>
      <el-table-column label="生日" prop="birthdate" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="开户机构" prop="institutionId" :show-overflow-tooltip="true" width="150">
        <template #default="scope">
          <el-tag v-for="item in instList" :value="item.institutionId" :key="item.institutionId"
                  v-show="scope.row.institutionId === item.institutionId">{{ item.institutionName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
              type="text"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['points:customer:edit']"
          >修改
          </el-button>
          <el-button type="text" icon="CreditCard" @click="handleVoucherShow(scope.row.customerId)"
                     v-hasPermi="['points:voucher:query']">凭证
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="customerRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户编号" prop="customerId" v-if="!addNew">
          <el-input v-model="form.customerId" placeholder="客户编号" readonly/>
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称"/>
        </el-form-item>
        <el-form-item label="客户等级" prop="customerLvl">
          <div>
            <el-radio-group v-model="form.customerLvl">
              <el-radio-button label="0">普通</el-radio-button>
              <el-radio-button label="1">VIP</el-radio-button>
              <el-radio-button label="2">超VIP</el-radio-button>
            </el-radio-group>
          </div>
        </el-form-item>
        <el-form-item label="手机号码" prop="phoneNumber">
          <el-input v-model="form.phoneNumber" placeholder="请输入手机号码"/>
        </el-form-item>
        <el-form-item label="邮箱地址" prop="email">
          <el-input v-model="form.email" placeholder="邮箱地址"/>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="form.address" placeholder="联系地址"/>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthdate">
          <el-date-picker
              v-model="form.birthdate"
              type="date"
              placeholder="出生日期"
              value-format="YYYYMMDD"
          />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio-button
                v-for="dict in sys_user_sex"
                :key="dict.value"
                :label="dict.value"
            >{{ dict.label }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="开户机构" prop="institutionId">
          <el-select v-model="form.institutionId">
            <el-option
                v-for="item in instList"
                :key="item.institutionId"
                :label="item.institutionName" :value="item.institutionId"
            >{{ item.institutionName }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="openVoucher" :title="voucherTitle" direction="rtl" size="50%">
      <el-button type="text" icon="Plus" @click="handleVoucherAdd(voucherCustomerId)"
                 v-hasPermi="['points:voucher:add']">新增
      </el-button>
      <el-table :data="voucherList">
        <el-table-column type="index" width="50"/>
        <el-table-column property="voucherNo" label="凭证号码">
          <template #default="scope">
            <div class="input-box">
              <el-input
                  v-show="currentRowId == scope.$index && isEdit && scope.row.add"
                  size="small"
                  v-model="scope.row.voucherNo"
              ></el-input>
              <span
                  v-show="currentRowId != scope.$index || !isEdit || !scope.row.add"
              >{{ scope.row.voucherNo }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column property="voucherTypeNo" label="凭证类型" width="150px">
          <template #default="scope">
            <div class="input-box">
              <el-select v-show="currentRowId == scope.$index && isEdit && scope.row.add"
                         v-model="scope.row.voucherTypeNo" placeholder="选择凭证类型" size="small">
                <el-option
                    v-for="item in points_voucher_type"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    :disabled="item.value === 'cust'"
                />
              </el-select>
              <dict-tag :options="points_voucher_type" :value="scope.row.voucherTypeNo"
                        v-show="currentRowId != scope.$index || !isEdit || !scope.row.add"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column property="voucherOpenDate" label="开户日期" width="150px">
          <template #default="scope">
            <div class="input-box" v-show="currentRowId == scope.$index && isEdit">
              <el-date-picker
                  v-model="scope.row.voucherOpenDate"
                  type="date"
                  placeholder="开户日期"
                  value-format="YYYYMMDD"
                  format="YYYYMMDD"
                  size="small"
              />
            </div>
            <span v-show="currentRowId != scope.$index || !isEdit"
            >{{ scope.row.voucherOpenDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150px">
          <template #default="scope">
            <el-button type="text" icon="Edit" @click="handleVoucherEdit(scope.row, scope.$index)"
                       v-hasPermi="['points:voucher:edit']" v-show="currentRowId != scope.$index || !isEdit">修改
            </el-button>
            <el-button type="text" icon="Back" @click="handleVoucherCancel(scope.row, scope.$index)"
                       v-hasPermi="['points:voucher:edit']" v-show="currentRowId == scope.$index && isEdit">取消
            </el-button>
            <el-button type="text" icon="Check" @click="handleVoucherSave(scope.row, scope.$index)"
                       v-hasPermi="['points:voucher:edit']" v-show="currentRowId == scope.$index && isEdit">保存
            </el-button>
            <el-button type="text" icon="Delete" @click="handleVoucherDelete(scope.row)"
                       v-hasPermi="['points:voucher:remove']" v-show="!isEdit">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>
  </div>
</template>

<script setup name="Customer">
import {addCustomer, delCustomer, getCustomer, listCustomer, updateCustomer} from "@/api/points/customer";
import {queryByTop} from "@/api/points/institution";
import {addVoucher, delVoucher, getVoucher, updateVoucher} from "@/api/points/voucher";

const router = useRouter();
const {proxy} = getCurrentInstance();
const {
  sys_user_sex,
  sys_normal_disable,
  points_voucher_type
} = proxy.useDict("sys_user_sex", "sys_normal_disable", "points_voucher_type");

const customerList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const addNew = ref(true);
const dateRange = ref([]);
const menuOptions = ref([]);
const menuExpand = ref(false);
const menuNodeAll = ref(false);
const deptExpand = ref(true);
const deptNodeAll = ref(false);
const deptOptions = ref([]);
const openDataScope = ref(false);
const menuRef = ref(null);
const deptRef = ref(null);
const instList = ref([]);
const voucherList = ref([]);
const openVoucher = ref(false);
const voucherTitle = ref(null);
const currentRowId = ref(null);
const isEdit = ref(false);
const voucherCustomerId = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10
  },
  rules: {
    customerName: [{required: true, message: "客户名称不能为空", trigger: "blur"}],
    customerLvl: [{required: true, message: "客户等级必选", trigger: "blur"}],
    phoneNumber: [{required: true, message: "手机号码不能为空", trigger: "blur"}],
    email: [{required: true, message: "邮箱地址不能为空", trigger: "blur"}],
    address: [{required: true, message: "联系地址不能为空", trigger: "blur"}],
    gender: [{required: true, message: "客户性别必选", trigger: "blur"}],
    institutionId: [{required: true, message: "开户机构必选", trigger: "blur"}]
  },
});

const {queryParams, form, rules} = toRefs(data);

/** 查询角色列表 */
function getList() {
  loading.value = true;
  listCustomer(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    customerList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}


/** 查询机构列表 */
function getInst() {
  queryByTop().then(response => {
    instList.value = response;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.customerId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 重置新增的表单以及其他数据  */
function reset() {
  form.value = {
    address: undefined,
    birthdate: undefined,
    customerId: undefined,
    customerLvl: "0",
    customerName: undefined,
    email: undefined,
    gender: "0",
    institutionId: undefined,
    phoneNumber: undefined
  };
  proxy.resetForm("customerRef");
}

/** 添加角色 */
function handleAdd() {
  reset();
  addNew.value = true;
  open.value = true;
  title.value = "添加角色";
}

/** 修改角色 */
function handleUpdate(row) {
  reset();
  const customerId = row.customerId || ids.value;
  getCustomer(customerId).then(response => {
    form.value = response.data;
    addNew.value = false;
    open.value = true;
    title.value = "修改客户";
    form.password = "";
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const customerId = row.customerId || ids.value;
  proxy.$modal.confirm('是否确认删除客户编号为"' + customerId + '"的数据项?').then(function () {
    return delCustomer(customerId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 凭证操作 */
function handleVoucherShow(customerId) {
  getVoucher(customerId).then(response => {
    voucherTitle.value = "客户编号【" + customerId + "】的凭证";
    voucherCustomerId.value = customerId;
    voucherList.value = response.data;
    openVoucher.value = true
  });
}

function handleVoucherEdit(row, index) {
  if (isEdit.value) {
    proxy.$modal.msgError("请先保存正在编辑数据");
    return
  }
  currentRowId.value = index;
  isEdit.value = true;
}

function handleVoucherSave(row) {
  console.log(row)
  if (row.add) {
    addVoucher(row).then(response => {
      form.value = response.data;
      proxy.$modal.msgSuccess("保存成功");
      handleVoucherShow(row.customerId)
      isEdit.value = false;
    });
  } else {
    updateVoucher(row).then(response => {
      form.value = response.data;
      proxy.$modal.msgSuccess("保存成功");
      handleVoucherShow(row.customerId)
      isEdit.value = false;
    });
  }
}

function handleVoucherCancel(row, index) {
  currentRowId.value = null;
  handleVoucherShow(row.customerId)
  isEdit.value = false;
}

function handleVoucherAdd(customerId) {
  if (isEdit.value) {
    proxy.$modal.msgError("请先保存正在编辑数据");
    return
  }
  var voucher = {
    "customerId": customerId,
    "add": true
  }
  currentRowId.value = 0;
  isEdit.value = true;
  voucherList.value.unshift(voucher)
}

function handleVoucherDelete(row) {
  delVoucher(row).then(response => {
    proxy.$modal.msgSuccess("删除成功");
    handleVoucherShow(row.customerId)
    isEdit.value = false;
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["customerRef"].validate(valid => {
    if (valid) {
      if (form.value.customerId != undefined) {
        updateCustomer(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCustomer(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

getInst();
getList();
</script>
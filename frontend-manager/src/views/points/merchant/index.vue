<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
		<el-form-item label="商户编号" prop="merchantNo">
		  <el-input
		      v-model="queryParams.merchantNo"
		      placeholder="请输入商户编号"
		      clearable
		      style="width: 240px"
		      @keyup.enter="handleQuery"
		  />
		</el-form-item>
		<el-form-item label="商户名称" prop="merchantName">
		  <el-input
		      v-model="queryParams.merchantName"
		      placeholder="请输入商户名称"
		      clearable
		      style="width: 240px"
		      @keyup.enter="handleQuery"
		  />
		</el-form-item>
      <el-form-item label="归属机构" prop="institutionId">
        <el-select v-model="queryParams.institutionId" clearable placeholder="选择归属机构">
          <el-option
              v-for="item in instList"
              :key="item.institutionId"
              :label="item.institutionName"
              :value="item.institutionId"
          />
        </el-select>
      </el-form-item>
	  <el-form-item label="状态" prop="status">
	    <el-select v-model="queryParams.status" clearable placeholder="选择商户状态">
	      <el-option
	          v-for="item in points_merchant_status"
	          :key="item.value"
	          :label="item.label"
	          :value="item.value"
	      />
	    </el-select>
	  </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Refresh" @click="getList">刷新</el-button>
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['points:merchant:add']"> 新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="merchantList">
      <el-table-column label="商户编号" prop="merchantNo" width="100"/>
      <el-table-column label="商户名称" prop="merchantName" width="200"/>
	  <el-table-column label="归属机构" prop="institutionId" width="150">
		  <template #default="scope">
			<el-tag v-for="item in instList" :value="item.institutionId" :key="item.institutionId"
					v-show="scope.row.institutionId === item.institutionId">{{ item.institutionName }}
			</el-tag>
		  </template>
	  </el-table-column>
	  <el-table-column label="联系人" prop="contacts" width="100"/>
	  <el-table-column label="联系号码" prop="phone"/>
	  <el-table-column label="联系地址" prop="address" show-overflow-tooltip/>
      <el-table-column label="状态" prop="status" width="150">
        <template #default="scope">
          <el-tag v-for="item in points_merchant_status" :type="item.elTagType" :value="item.value" :key="item.value"
                  v-show="scope.row.status === item.value">{{ item.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button type="text" icon="Edit" @click="handleShow(scope.row)" v-hasPermi="['points:merchant:edit']" >修改</el-button>
		  <el-button type="text" icon="Delete" @click="handleRemove(scope.row)" v-hasPermi="['points:merchant:remove']" >删除</el-button>
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
	
	<el-drawer v-model="openMerchantDetails" title="收单商户详情" direction="rtl" size="50%">
	<el-form ref="merchantRef" :model="form" :rules="rules" label-width="100px">
	  <el-form-item label="商户编号" prop="merchantNo">
		  <span v-show="!addNew">{{ form.merchantNo }}</span>
	    <el-input v-model="form.merchantNo" placeholder="商户编号" v-show="addNew"></el-input>
	  </el-form-item>
	  <el-form-item label="商户名称" prop="merchantName">
	    <el-input v-model="form.merchantName" show-word-limit maxlength="10" minlength="1"></el-input>
	  </el-form-item>
	  <el-form-item label="归属机构" prop="institutionId">
	    <el-tag v-for="item in instList" :value="item.institutionId" :key="item.institutionId"
	    		v-show="!addNew && form.institutionId === item.institutionId">{{ item.institutionName }}
	    </el-tag>
		<el-select v-model="form.institutionId" placeholder="选择归属机构" v-show="addNew">
		  <el-option
		      v-for="item in instList"
		      :key="item.institutionId"
		      :label="item.institutionName"
		      :value="item.institutionId"
		  />
		</el-select>
	  </el-form-item>
	  <el-form-item label="联系人" prop="contacts">
	    <el-input v-model="form.contacts" show-word-limit maxlength="10"></el-input>
	  </el-form-item>
	  <el-form-item label="联系号码" prop="phone">
	    <el-input v-model="form.phone" show-word-limit maxlength="20"></el-input>
	  </el-form-item>
	  <el-form-item label="联系地址" prop="address">
	    <el-input v-model="form.address" show-word-limit maxlength="50"></el-input>
	  </el-form-item>
	  <el-form-item label="商户状态" prop="status">
	    <el-radio-group v-model="form.status" >
	          <el-radio-button v-for="item in points_merchant_status" :label="item.value" :key="item.value" :value="item.value" >{{item.label}}</el-radio-button>
	    </el-radio-group>
	  </el-form-item>
	</el-form>
	
	<template #footer>
	      <div style="flex: auto">
	        <el-button @click="openMerchantDetails = false">取消</el-button>
	        <el-button type="primary" @click="confirm">确定</el-button>
	      </div>
	    </template>
	</el-drawer>
  </div>
</template>

<script setup name="Merchant">
import {queryByTop} from "@/api/points/institution";
import {listMerchant, updateMerchant, addMerchant, delMerchant} from "@/api/points/merchant";

const router = useRouter();
const {proxy} = getCurrentInstance();
const {
  points_merchant_status
} = proxy.useDict("points_merchant_status");

const merchantList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const instList = ref([]);
const merchant = ref(null);
const title = ref(null);
const openMerchantDetails = ref(false);
const addNew = ref(false);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    status: undefined,
	merchantNo: '',
	merchantName: '',
	institutionId: undefined
  },
  rules: {
    merchantNo: [{required: true, message: "商户编号不能为空", trigger: "blur"}],
    merchantName: [{required: true, message: "商户名称不能为空", trigger: "blur"}],
    institutionId: [{required: true, message: "商户归属机构必选", trigger: "blur"}],
    status: [{required: true, message: "商户状态必选", trigger: "blur"}]
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询角色列表 */
function getList() {
  loading.value = true;
  listMerchant(queryParams.value).then(response => {
    merchantList.value = response.rows;
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
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleShow(row) {
	proxy.resetForm("merchantRef");
	form.value.address=row.address;
	form.value.contacts=row.contacts;
	form.value.institutionId=row.institutionId;
	form.value.merchantName=row.merchantName;
	form.value.merchantNo=row.merchantNo;
	form.value.phone=row.phone;
	form.value.status=row.status;
	addNew.value = false
	openMerchantDetails.value = true;
}

function confirm(){
	proxy.$refs["merchantRef"].validate(valid => {
	  if (valid) {
	    if (!addNew.value) {
	      updateMerchant(form.value).then(response => {
	        proxy.$modal.msgSuccess("修改成功");
	        getList();
			openMerchantDetails.value = false;
	      });
	    } else {
	      addMerchant(form.value).then(response => {
	        proxy.$modal.msgSuccess("新增成功");
	        getList();
			openMerchantDetails.value = false;
	      });
	    }
	  }
	});
}

function handleAdd() {
	proxy.resetForm("merchantRef");
	form.value.address='';
	form.value.contacts='';
	form.value.institutionId=undefined;
	form.value.merchantName='';
	form.value.merchantNo='';
	form.value.phone='';
	form.value.status=undefined;
	addNew.value = true
	openMerchantDetails.value = true;
}

function handleRemove(row) {
	proxy.$modal.confirm('是否确认删除清算商户"' + row.merchantNo + '"的数据项?').then(function () {
	  return delMerchant(row);
	}).then(() => {
	  getList();
	  proxy.$modal.msgSuccess("删除成功");
	}).catch(() => {
	});
}

getInst();
getList();
</script>
<style>
.mr10 {
  margin-right: 10px;
}
</style>
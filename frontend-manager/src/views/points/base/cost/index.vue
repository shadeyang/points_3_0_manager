<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
      <el-form-item label="所属机构" prop="institutionId">
        <el-select v-model="queryParams.institutionId" clearable placeholder="选择类型所属机构" @change="getList">
          <el-option
              v-for="item in instList"
              :key="item.institutionId"
              :label="'('+item.institutionNo+')'+item.institutionName"
              :value="item.institutionId"
          >({{item.institutionNo}}){{item.institutionName}}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
	<el-row :gutter="12">
	    <el-col :span="8" v-for="item in costList">
	 <el-card class="box-card" >
		<template #header>
		  <div class="card-header">
			<span v-show="editNum != item.costLine">{{item.costName}}</span>
			<el-input size="small" v-show="editNum === item.costLine" v-model="form.costName" minlength="1" maxlength="10" show-word-limit style="margin-right: 10px;margin-top: -5px;"/>
			<el-row :gutter="5">
				<el-col :span="12"><el-icon v-show="editNum != item.costLine" @click="edit(item)"><Edit /></el-icon></el-col>
				<el-col :span="12"><el-icon v-show="editNum != item.costLine" @click="remove(item)"><Delete /></el-icon></el-col>
				
				<el-col :span="12"><el-icon v-show="editNum === item.costLine" @click="close()"><Close /></el-icon></el-col>
				<el-col :span="12"><el-icon v-show="editNum === item.costLine" @click="save()"><Check /></el-icon></el-col>
			</el-row>
		  </div>
		</template>
		<el-descriptions size="small" :column="1">
			<el-descriptions-item label="编号"><span>{{item.costLine}}</span></el-descriptions-item>
			<el-descriptions-item label="机构">
				<el-tag v-for="inst in instList" :value="inst.institutionId" :key="inst.institutionId"
						v-show=" item.institutionId === inst.institutionId">({{inst.institutionNo}}){{inst.institutionName}}
				</el-tag>
			</el-descriptions-item>
		  </el-descriptions>
	  </el-card>
	    </el-col>
		<el-col :span="8">
		<el-card class="box-card" >
				<template #header>
				  <div class="card-header">
					<span v-show="!adding">增加费用部门</span>
					<el-input size="small" v-show="adding" v-model="form.costName" minlength="1" maxlength="10" show-word-limit style="margin-right: 10px;margin-top: -5px;"/>
					
					<el-row :gutter="5">
						<el-icon v-show="!adding" @click="add()"><Edit /></el-icon>
						<el-col :span="12"><el-icon v-show="adding" @click="close()"><Close /></el-icon></el-col>
						<el-col :span="12"><el-icon v-show="adding" @click="save()"><Check /></el-icon></el-col>
					</el-row>
				  </div>
				</template>
				<el-descriptions size="small" :column="1">
					<el-descriptions-item label="编号">--</el-descriptions-item>
					<el-descriptions-item label="机构">
						<span v-show="!adding">--</span>
						<el-select v-show="adding" v-model="form.institutionId" placeholder="选择类型所属机构" size="small">
						  <el-option
						      v-for="inst in instList"
						      :key="inst.institutionId"
						      :label="'('+inst.institutionNo+')'+inst.institutionName"
						      :value="inst.institutionId"
						  >({{inst.institutionNo}}){{inst.institutionName}}</el-option>
						</el-select>
					</el-descriptions-item>
				  </el-descriptions>
		 </el-card>
		</el-col>
	</el-row>
	  
  </div>
</template>

<script setup name="PointsCost">
import {queryByParentInst} from "@/api/points/institution";
import {listCostByInst, addCost, updateCost, delCost} from "@/api/points/cost";

const router = useRouter();
const {proxy} = getCurrentInstance();

const loading = ref(true);
const showSearch = ref(true);
const instList = ref([]);
const costList = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    institutionId: undefined
  },
  editNum: undefined,
  adding: false
});

const {queryParams, form, editNum, adding} = toRefs(data);

/** 查询角色列表 */
function getList() {
  loading.value = true;
  listCostByInst(queryParams.value.institutionId).then(response => {
    costList.value = response;
    loading.value = false;
  });
}


/** 查询机构列表 */
function getInst() {
  queryByParentInst('0').then(response => {
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

function edit(cost){
	form.value = cost;
	editNum.value = cost.costLine;
	adding.value = false;
}

function remove(cost){
	proxy.$modal.confirm('是否确认删除成本部门"' + cost.costName + '"的数据项?').then(function () {
	  return delCost(cost);
	}).then(() => {
	  getList();
	  proxy.$modal.msgSuccess("删除成功");
	}).catch(() => {
	});
}

function save(){
	if (form.value.costLine != undefined) {
	  updateCost(form.value).then(response => {
	    proxy.$modal.msgSuccess("修改成功");
		editNum.value = undefined;
	    getList();
	  });
	} else {
	  if(!form.value.costName){
		  proxy.$modal.msgError("请填写成本部门的名称");
		  return false;
	  }
	  if(!form.value.institutionId){
		  proxy.$modal.msgError("请选择成本部门的所属机构");
		  return false;
	  }
	  addCost(form.value).then(response => {
	    proxy.$modal.msgSuccess("新增成功");
		adding.value = false;
	    getList();
	  });
	}
}

function add(){
	form.value = {
	  description: undefined,
	  institutionId: queryParams.value.institutionId,
	  pointsTypeNo: undefined,
	  pointsTypeName: undefined,
	  rate: '1'
	};
	editNum.value = undefined;
	adding.value = true;
}

function close(){
	editNum.value = undefined;
	adding.value = false;
	getList()
}

/**
 * @description 格式化⾦额
 * @param number：要格式化的数字
 * @param decimals：保留⼏位⼩数默认0位
 * @param decPoint：⼩数点符号默认.
 * @param thousandsSep：千分位符号默认为,
 */
const formatMoney = (number, decimals = 0, decPoint = '.', thousandsSep = ',') => {
  number = (number + '').replace(/[^0-9+-Ee.]/g, '')
  let n = !isFinite(+number) ? 0 : +number
  let prec = !isFinite(+decimals) ? 0 : Math.abs(decimals)
  let sep = (typeof thousandsSep === 'undefined') ? ',' : thousandsSep
  let dec = (typeof decPoint === 'undefined') ? '.' : decPoint
  let s = ''
  let toFixedFix = function (n, prec) {
    let k = Math.pow(10, prec)
    return '' + Math.ceil(n * k) / k
  }
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.')
  let re = /(-?\d+)(\d{3})/
  while (re.test(s[0])) {
    s[0] = s[0].replace(re, '$1' + sep + '$2')
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || ''
    s[1] += new Array(prec - s[1].length + 1).join('0')
  }
  return s.join(dec)
}
getInst();
getList();
</script>
<style>
.mr10 {
  margin-right: 10px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 100%;
  margin-bottom: 5px;
}
</style>
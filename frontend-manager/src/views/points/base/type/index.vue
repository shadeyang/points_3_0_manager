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
	    <el-col :span="8" v-for="item in typeList">
	 <el-card class="box-card" >
		<template #header>
		  <div class="card-header">
			<span v-show="editNum != item.pointsTypeNo">{{item.pointsTypeName}}</span>
			<el-input size="small" v-show="editNum === item.pointsTypeNo" v-model="form.pointsTypeName" minlength="1" maxlength="10" show-word-limit style="margin-right: 10px;margin-top: -5px;"/>
			<el-row :gutter="5">
				<el-col :span="12"><el-icon v-show="editNum != item.pointsTypeNo" @click="edit(item)"><Edit /></el-icon></el-col>
				<el-col :span="12"><el-icon v-show="editNum != item.pointsTypeNo" @click="remove(item)"><Delete /></el-icon></el-col>
				
				<el-col :span="12"><el-icon v-show="editNum === item.pointsTypeNo" @click="close()"><Close /></el-icon></el-col>
				<el-col :span="12"><el-icon v-show="editNum === item.pointsTypeNo" @click="save()"><Check /></el-icon></el-col>
			</el-row>
		  </div>
		</template>
		<el-descriptions size="small" :column="1">
			<el-descriptions-item label="编号"><span>{{item.pointsTypeNo}}</span></el-descriptions-item>
			<el-descriptions-item label="价值"><span v-show="editNum != item.pointsTypeNo">{{formatMoney(item.rate,2)}}</span><el-input-number size="small" v-model="form.rate" :min="1" v-show="editNum === item.pointsTypeNo"></el-input-number>积分=1.00元</el-descriptions-item>
			<el-descriptions-item label="机构">
				<el-tag v-for="inst in instList" :value="inst.institutionId" :key="inst.institutionId"
						v-show=" item.institutionId === inst.institutionId">({{inst.institutionNo}}){{inst.institutionName}}
				</el-tag>
				<!-- <el-select v-show="editNum === item.pointsTypeNo" v-model="form.institutionId" placeholder="选择类型所属机构" size="small">
				  <el-option
				      v-for="inst in instList"
				      :key="inst.institutionId"
				      :label="'('+inst.institutionNo+')'+inst.institutionName"
				      :value="inst.institutionId"
				  >({{inst.institutionNo}}){{inst.institutionName}}</el-option>
				</el-select> -->
			</el-descriptions-item>
			<el-descriptions-item label="描述">
				<span v-show="editNum != item.pointsTypeNo">{{item.description ? item.description : '无'}}</span>
				<el-input size="small" v-show="editNum === item.pointsTypeNo" v-model="form.description" maxlength="20" show-word-limit style="width: 80%;"/>
			</el-descriptions-item>
		  </el-descriptions>
	  </el-card>
	    </el-col>
		<el-col :span="8">
		<el-card class="box-card" >
				<template #header>
				  <div class="card-header">
					<span v-show="!adding">增加积分类型</span>
					<el-input size="small" v-show="adding" v-model="form.pointsTypeName" minlength="1" maxlength="10" show-word-limit style="margin-right: 10px;margin-top: -5px;"/>
					
					<el-row :gutter="5">
						<el-icon v-show="!adding" @click="add()"><Edit /></el-icon>
						<el-col :span="12"><el-icon v-show="adding" @click="close()"><Close /></el-icon></el-col>
						<el-col :span="12"><el-icon v-show="adding" @click="save()"><Check /></el-icon></el-col>
					</el-row>
				  </div>
				</template>
				<el-descriptions size="small" :column="1">
					<el-descriptions-item label="编号">--</el-descriptions-item>
					<el-descriptions-item label="价值">
						<span v-show="!adding">?</span>
					<el-input-number size="small" v-model="form.rate" :min="1" v-show="adding"></el-input-number>积分=1.00元</el-descriptions-item>
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
					<el-descriptions-item label="描述">
						<span v-show="!adding">--</span>
						<el-input size="small" v-show="adding" v-model="form.description" maxlength="20" show-word-limit style="width: 80%;"/>
					</el-descriptions-item>
				  </el-descriptions>
		 </el-card>
		</el-col>
	</el-row>
	  
  </div>
</template>

<script setup name="PointsType">
import {queryByParentInst} from "@/api/points/institution";
import {listTypeByInst, addType, updateType, delType} from "@/api/points/type";

const router = useRouter();
const {proxy} = getCurrentInstance();

const loading = ref(true);
const showSearch = ref(true);
const instList = ref([]);
const typeList = ref([]);

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
  listTypeByInst(queryParams.value.institutionId).then(response => {
    typeList.value = response;
    loading.value = false;
  });
}


/** 查询机构列表 */
function getInst() {
  queryByParentInst('0').then(response => {
    instList.value = response;
  });
}

/** 查询积分类型列表 */
function getType() {
  listTypeByInst().then(response => {
    typeList.value = response;
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

function edit(type){
	form.value = type;
	editNum.value = type.pointsTypeNo;
	adding.value = false;
}

function save(){
	if (form.value.pointsTypeNo != undefined) {
	  updateType(form.value).then(response => {
	    proxy.$modal.msgSuccess("修改成功");
		editNum.value = undefined;
	    getList();
	  });
	} else {
	  if(!form.value.pointsTypeName){
		  proxy.$modal.msgError("请填写积分类型名称");
		  return false;
	  }
	  if(!form.value.institutionId){
	  		  proxy.$modal.msgError("请选择积分类型所属机构");
	  		  return false;
	  }
	  addType(form.value).then(response => {
	    proxy.$modal.msgSuccess("新增成功");
		adding.value = false;
	    getList();
	  });
	}
}

function remove(type){
	proxy.$modal.confirm('是否确认删除积分类型"' + type.pointsTypeName + '"的数据项?').then(function () {
	  return delType(type);
	}).then(() => {
	  getList();
	  proxy.$modal.msgSuccess("删除成功");
	}).catch(() => {
	});
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
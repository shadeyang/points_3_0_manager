<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
		 <el-form-item label="交易流水" prop="transNo">
            <el-input
               v-model="queryParams.transNo"
               placeholder="请输入交易流水"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="客户编号" prop="customerId">
            <el-input
               v-model="queryParams.customerId"
               placeholder="请输入客户编号"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
		 <el-form-item label="积分类型" prop="pointsTypeNo">
			 <el-select v-model="queryParams.pointsTypeNo" clearable placeholder="选择积分类型">
			 	 <el-option
			 	   v-for="item in typeList"
			 	   :key="item.pointsTypeNo"
			 	   :label="item.pointsTypeName"
			 	   :value="item.pointsTypeNo"
			 	 />
			 </el-select>
		 </el-form-item>
		 <el-form-item label="交易类型" prop="transTypeNo">
			<el-select v-model="queryParams.transTypeNo" clearable placeholder="选择交易类型">
				 <el-option
				   v-for="item in points_trans_type"
				   :key="item.value"
				   :label="item.label"
				   :value="item.value"
				 />
			</el-select>
		 </el-form-item>
		 <el-form-item label="借贷标记" prop="debitOrCredit">
		   <el-select v-model="queryParams.debitOrCredit" clearable placeholder="选择借贷标记">
			   <el-option key="C" label="贷入" value="C"></el-option>
			   <el-option key="D" label="借出" value="D"></el-option>
		   </el-select>
		 </el-form-item>
		 <el-form-item label="冲正标记" prop="reversedFlag">
			<el-select v-model="queryParams.reversedFlag" clearable placeholder="选择冲正标记">
				 <el-option
				   v-for="item in points_trans_reversed"
				   :key="item.value"
				   :label="item.label"
				   :value="item.value"
				 />
			</el-select>
		 </el-form-item>
		 <el-form-item label="交易状态" prop="transStatus">
			 <el-select v-model="queryParams.transStatus" clearable placeholder="选择交易状态">
				 <el-option
				   v-for="item in points_trans_status"
				   :key="item.value"
				   :label="item.label"
				   :value="item.value"
				 />
			 </el-select>
		 </el-form-item>
		 <el-form-item label="前置流水" prop="sysTransNo">
		    <el-input
		       v-model="queryParams.sysTransNo"
		       placeholder="请输入前置流水"
		       clearable
		       style="width: 240px"
		       @keyup.enter="handleQuery"
		    />
		 </el-form-item>
		 <el-form-item label="交易凭证" prop="voucherNo">
			<el-input
			      v-model="queryParams.voucherNo"
			      placeholder="请输入交易凭证"
			      style="width: 400px"
				  @keyup.enter="handleQuery"
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
		 <el-form-item label="交易时间" prop="dateRange">
		    <el-date-picker v-model="dateRange" type="datetimerange" unlink-panels
			start-placeholder="开始时间" end-placeholder="结束时间" :clearable="false" value-format="x"></el-date-picker>
		 </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
		  <el-col :span="1.5">
		    <el-button type="primary" plain icon="Refresh" @click="getList">刷新</el-button>
		  </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="transList">
		 <el-table-column label="交易流水" prop="transNo" width="250" />
         <el-table-column label="客户编号" prop="customerId" width="250" />
		 <el-table-column label="交易时间" prop="transDateTime" width="200">
			<template #default="scope">
				{{scope.row.transDate+' '+scope.row.transTime}}
			</template>
		 </el-table-column>
		 <el-table-column label="交易积分" prop="pointsAmount" width="200" align="right">
			 <template #default="scope">
				<el-tag type="danger" v-for="item in points_trans_reversed" :value="item.value" :key="item.value" v-show="scope.row.reversedFlag === item.value && item.value!= 0 ">{{item.label}}</el-tag>
				{{formatMoney(scope.row.pointsAmount,2)}}
				<el-tag type="success" v-show="scope.row.debitOrCredit === 'C'">贷</el-tag>
				<el-tag type="warning" v-show="scope.row.debitOrCredit === 'D'">借</el-tag>
			 </template>
		 </el-table-column>
		 <el-table-column label="积分类型" prop="pointsTypeNo" width="100">
			 <template #default="scope">
				<el-tag v-for="item in typeList" :value="item.pointsTypeNo" :key="item.pointsTypeNo" v-show="scope.row.pointsTypeNo === item.pointsTypeNo">{{item.pointsTypeName}}</el-tag>
			 </template>
		 </el-table-column>
		 <el-table-column label="状态" prop="transStatus" width="100">
			 <template #default="scope">
			    <el-tag v-for="item in points_trans_status" :type="item.elTagType" :value="item.value" :key="item.value" v-show="scope.row.transStatus === item.value">{{item.label}}</el-tag>
			 </template>
		 </el-table-column>
         <el-table-column label="描述" prop="description" :show-overflow-tooltip="true"/>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
            <template #default="scope">
				<el-button
				   type="text"
				   icon="Tickets"
				   @click="handleShow(scope.row)"
				>详细</el-button>
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
	<el-drawer v-model="openPointsTrans" title="交易流水详情" direction="rtl" size="80%">
		<el-descriptions
		    class="margin-top"
		    :column="3"
		    size="small" border
		  >
		    <el-descriptions-item label="交易流水" :span="3">{{pointsTrans.transNo}}</el-descriptions-item>
		    <el-descriptions-item label="客户编号" :span="2">{{pointsTrans.customerId}}</el-descriptions-item>
			<el-descriptions-item label="客户姓名" >{{customerInfo.customerName}}</el-descriptions-item>
			<!-- 交易信息 -->
			<el-descriptions-item label="交易凭证">
				<el-tag>{{pointsTrans.voucherTypeNo}}</el-tag>{{pointsTrans.voucherNo}}
			</el-descriptions-item>
			<el-descriptions-item label="交易类型">
				<el-tag v-for="item in points_trans_type" :value="item.value" :key="item.value" v-show="pointsTrans.transTypeNo === item.value ">{{item.label}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item label="交易操作员">{{pointsTrans.operator}}</el-descriptions-item>
		    <el-descriptions-item label="借贷标志">{{pointsTrans.debitOrCredit}}</el-descriptions-item>
			<el-descriptions-item label="交易积分">{{formatMoney(pointsTrans.pointsAmount,2)}}</el-descriptions-item>
			<el-descriptions-item label="积分类型">
				<el-tag v-for="item in typeList" :value="item.pointsTypeNo" :key="item.pointsTypeNo" v-show="pointsTrans.pointsTypeNo === item.pointsTypeNo">{{item.pointsTypeName}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item label="交易时间">{{pointsTrans.transDate+' '+pointsTrans.transTime}}</el-descriptions-item>
		    <el-descriptions-item label="交易状态">
				<el-tag v-for="item in points_trans_status" :type="item.elTagType" :value="item.value" :key="item.value" v-show="pointsTrans.transStatus === item.value">{{item.label}}</el-tag>
			</el-descriptions-item>
		    <el-descriptions-item label="冲正标志">
				<el-tag v-for="item in points_trans_reversed" :value="item.value" :key="item.value" v-show="pointsTrans.reversedFlag === item.value ">{{item.label}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item label="交易渠道">{{pointsTrans.transChannel}}</el-descriptions-item>
			<el-descriptions-item label="发起流水">{{pointsTrans.sysTransNo}}</el-descriptions-item>
			<el-descriptions-item label="发起机构">
				<el-tag v-for="item in instList" :value="item.institutionId" :key="item.institutionId" v-show="pointsTrans.institutionId === item.institutionId">{{item.institutionName}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item label="来源规则">{{pointsTrans.rulesId}}</el-descriptions-item>
			<el-descriptions-item label="有效期">{{pointsTrans.endDate}}</el-descriptions-item>
			<el-descriptions-item label="成本部门">
            	<el-tag v-for="item in costList" :value="item.costLine" :key="item.costLine" v-show="pointsTrans.costLine === item.costLine">{{item.costName}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item label="清算商户">{{pointsTrans.merchantNo}}</el-descriptions-item>
			<el-descriptions-item label="清算金额">{{formatMoney(pointsTrans.clearingAmt,2)}}</el-descriptions-item>
			<el-descriptions-item label="原始流水">{{pointsTrans.oldTransNo}}</el-descriptions-item>
			
			<el-descriptions-item label="交易描述" :span="3">{{pointsTrans.description}}</el-descriptions-item>
		</el-descriptions>
		<br />
		<el-table v-loading="detailsLoading" :data="detailsList" border>
			<el-table-column label="来源流水" prop="sourceTransNo" width="250" />
			<el-table-column label="交易积分" prop="pointsAmount" width="150" align="right">
				<template #default="scope">
					{{formatMoney(scope.row.pointsAmount,2)}}
				</template>
			</el-table-column>
			<el-table-column label="有效时间" prop="endDate" />
			<el-table-column label="结算商户" prop="merchantNo" width="100" />
			<el-table-column label="成本部门" prop="costLine" width="100">
			    <template #default="scope">
			        <el-tag v-for="item in costList" :value="item.costLine" :key="item.costLine" v-show="scope.row.costLine === item.costLine">{{item.costName}}</el-tag>
                </template>
            </el-table-column>
			<el-table-column label="清算金额" prop="clearingAmt" width="100" align="right">
				<template #default="scope">
					{{formatMoney(scope.row.clearingAmt,2)}}
				</template>
			</el-table-column>
		</el-table>
		<pagination
		   v-show="detailTotal > 0"
		   :total="detailTotal"
		   :background="false"
		   v-model:page="queryDetailsParams.pageNum"
		   v-model:limit="queryDetailsParams.pageSize"
		   layout="total, prev, pager, next"
		   @pagination="getListTransDetails"
		/>
	</el-drawer>
   </div>
</template>

<script setup name="Customer">
import {getCustomer} from "@/api/points/customer";
import {queryByTop} from "@/api/points/institution";
import {listTrans, listTransDetails} from "@/api/points/trans";
import {listTypeByInst} from "@/api/points/type";
import {listCostByInst} from "@/api/points/cost";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { points_voucher_type, points_trans_status, points_trans_reversed, points_trans_type } = proxy.useDict("points_voucher_type", "points_trans_status", "points_trans_reversed", "points_trans_type");

const transList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const dateRange = ref([new Date().setHours(0, 0, 0, 0),new Date().setHours(23, 59, 59, 999)]);
const instList = ref([]);
const typeList = ref([]);
const costList = ref([]);
const openPointsTrans = ref(false);
const pointsTrans = ref(null);
const customerInfo = ref(null);
const detailsLoading = ref(true);
const detailsList = ref([]);
const detailTotal = ref(0);
const transNo = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    status: undefined
  },
  queryDetailsParams:{
	  pageNum: 1,
	  pageSize: 5,
  }
});

const { queryParams,queryDetailsParams, form } = toRefs(data);

/** 查询角色列表 */
function getList() {
  loading.value = true;
  listTrans(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    transList.value = response.rows;
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

/** 查询积分类型列表 */
function getType() {
  listTypeByInst().then(response => {
     typeList.value = response;
   });
}
/** 查询成本部门列表 */
function getCost() {
    listCostByInst().then(response => {
        costList.value = response;
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

function handleShow(row){
	pointsTrans.value = row;
	detailsLoading.value = true;
	getCustomer(row.customerId).then(response => {
	   customerInfo.value = response.data;
	   openPointsTrans.value = true;
	   transNo.value = row.transNo;
	   getListTransDetails();
	});
}

function getListTransDetails(){
	listTransDetails(queryDetailsParams.value,transNo.value).then(response => {
	   detailsList.value = response.rows;
	   detailTotal.value = response.total;
	   detailsLoading.value = false;
	});
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
getType();
getCost();
getList();
</script>

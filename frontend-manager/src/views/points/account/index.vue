<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
      <el-form-item label="凭证号码" prop="voucherNo">
		<el-input
		      v-model="queryParams.voucherNo"
		      placeholder="请输入凭证号码"
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
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Refresh" @click="getList">刷新</el-button>
<!--        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['points:account:add']"> 新增</el-button>-->
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="accoutList">
      <el-table-column label="客户编号" prop="customerId" width="250"/>
      <el-table-column label="积分类型" prop="pointsTypeNo" width="100">
        <template #default="scope">
          <el-tag v-for="item in typeList" :value="item.pointsTypeNo" :key="item.pointsTypeNo"
                  v-show="scope.row.pointsTypeNo === item.pointsTypeNo">{{ item.pointsTypeName }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="积分余额" prop="pointsBalance" width="200" align="right">
        <template #default="scope">
          {{ formatMoney(scope.row.pointsBalance, 2) }}
        </template>
      </el-table-column>
      <el-table-column label="冻结积分" prop="freezingPoints" width="200" align="right">
        <template #default="scope">
          {{ formatMoney(scope.row.freezingPoints, 2) }}
        </template>
      </el-table-column>
      <el-table-column label="在途积分" prop="inTransitPoints" width="200" align="right">
        <template #default="scope">
          {{ formatMoney(scope.row.inTransitPoints, 2) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="pointsAccountStatus" width="150">
        <template #default="scope">
          <el-switch
              v-model="scope.row.pointsAccountStatus"
              :loading="statusLoading"
              active-value="0"
              inactive-value="1"
              class="mr10"
              @change="changeStatus(scope.row)"
          />
          <el-tag v-for="item in points_account_status" :type="item.elTagType" :value="item.value" :key="item.value"
                  v-show="scope.row.pointsAccountStatus === item.value">{{ item.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button type="text" icon="Tickets" @click="handleShow(scope.row)" v-hasPermi="['points:account:details']">详细</el-button>
          <el-button type="text" icon="Switch" @click="freezeShow(scope.row)" v-hasPermi="['points:account:freeze']">冻结/解冻</el-button>
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
    <el-drawer v-model="openPointsAccountDetails" title="账户详情" direction="rtl" size="80%">
      <el-descriptions
          class="margin-top"
          :column="3"
          size="small" border
      >
        <el-descriptions-item label="客户编号" :span="2">{{ account.customerId }}</el-descriptions-item>
        <el-descriptions-item label="积分类型">
          <el-tag v-for="item in typeList" :value="item.pointsTypeNo" :key="item.pointsTypeNo"
                  v-show="account.pointsTypeNo === item.pointsTypeNo">{{ item.pointsTypeName }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <br/>
      <br/>
      <el-descriptions
          class="margin-top"
          :column="3"
          size="small" border
          direction="vertical"
      >
        <el-descriptions-item label="积分余额" align="right"> {{ formatMoney(account.pointsBalance, 2) }}
        </el-descriptions-item>
        <el-descriptions-item label="冻结积分" align="right"> {{ formatMoney(account.freezingPoints, 2) }}
        </el-descriptions-item>
        <el-descriptions-item label="在途积分" align="right"> {{ formatMoney(account.inTransitPoints, 2) }}
        </el-descriptions-item>
      </el-descriptions>
      <br/>
      <p>积分明细</p>
      <el-table v-loading="detailsLoading" :data="detailsPointsAccountDetailsList">
        <el-table-column type="index" width="50"/>
        <el-table-column label="积分余额(有效时间)" prop="pointsAmount" align="right">
          <template #default="scope">
            {{ formatMoney(scope.row.pointsAmount, 2) }}
            <el-tag>{{ scope.row.expirationTime }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <br/>
      <p>待记账明细</p>
      <el-table v-loading="detailsLoading" :data="detailsPointsTransList">
        <el-table-column type="index" width="50"/>
        <el-table-column label="交易流水" prop="transNo" width="300"></el-table-column>
        <el-table-column label="积分余额" prop="pointsAmount" align="right">
          <template #default="scope">
            {{ formatMoney(scope.row.pointsAmount, 2) }}
            <el-tag v-if="scope.row.endDate">{{ scope.row.endDate }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>

    <!-- 添加账户 -->
    <el-dialog :title="title" v-model="freezing" append-to-body>
      <el-form :model="account" label-width="100px">
      </el-form>
    </el-dialog>

    <!-- 冻结配置对话框 -->
    <el-dialog :title="title" v-model="freezing" append-to-body>
      <el-form :model="account" label-width="100px">
        <el-form-item label="客户编号" prop="customerId" >
          {{account.customerId}}
        </el-form-item>
        <el-form-item label="积分类型" prop="pointsTypeNo">
            <el-tag v-for="item in typeList" :value="item.pointsTypeNo" :key="item.pointsTypeNo"
                    v-show="account.pointsTypeNo === item.pointsTypeNo">{{ item.pointsTypeName }}
            </el-tag>
        </el-form-item>
        <el-form-item label="冻结积分" prop="freezingPoints" >
			<el-input-number size="small" v-model="account.freezingPoints" :min = "0"></el-input-number>
          <!-- <div v-if="account.pointsBalance<=0">
            无可用积分冻结
          </div>
          <el-slider v-if="account.pointsBalance > 0" v-model="account.freezingPoints" show-input :min = "0" :max = "freezingMax" /> -->
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="freeze">确 定</el-button>
          <el-button @click="freezing = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Customer">
import {queryByTop} from "@/api/points/institution";
import {listTransDetails} from "@/api/points/trans";
import {listTypeByInst} from "@/api/points/type";
import {changeAccountStatus, freezePoints, listAccount, queryDetails} from "@/api/points/account";

const router = useRouter();
const {proxy} = getCurrentInstance();
const {
  points_voucher_type,
  points_trans_status,
  points_trans_reversed,
  points_trans_type,
  points_account_status
} = proxy.useDict("points_voucher_type", "points_trans_status", "points_trans_reversed", "points_trans_type", "points_account_status");

const accoutList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const dateRange = ref([new Date().setHours(0, 0, 0, 0), new Date().setHours(23, 59, 59, 999)]);
const instList = ref([]);
const typeList = ref([]);
const openPointsAccountDetails = ref(false);
const account = ref(null);
const detailsPointsAccountDetailsList = ref([])
const detailsPointsTransList = ref([])
const detailsLoading = ref(true);
const detailsList = ref([]);
const detailTotal = ref(0);
const transNo = ref(null);
const statusLoading = ref(false);
const title = ref(null);
const freezing = ref(false);
const freezingMax = ref(0);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    status: undefined
  },
  queryDetailsParams: {
    pageNum: 1,
    pageSize: 5,
  }
});

const {queryParams, queryDetailsParams, form} = toRefs(data);

/** 查询角色列表 */
function getList() {
  loading.value = true;
  listAccount(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    accoutList.value = response.rows;
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

function changeStatus(row) {
  statusLoading.value = true;
  changeAccountStatus(row.customerId, row.pointsTypeNo).then((response) => {
    row = response;
    proxy.$modal.msgSuccess("修改状态成功");
    statusLoading.value = false;
  }, (error) => {
    proxy.$modal.msgError("修改状态失败");
    statusLoading.value = false;
  }).catch(() => {
    proxy.$modal.msgError("修改状态失败");
    statusLoading.value = false;
  });
}

function handleShow(row) {
  account.value = row;
  detailsLoading.value = true;
  queryDetails(row.customerId, row.pointsTypeNo).then(response => {
    detailsPointsTransList.value = response.pointsTransList;
    detailsPointsAccountDetailsList.value = response.pointsAccountDetailsList;
    openPointsAccountDetails.value = true;
    detailsLoading.value = false;
  });
}

function freezeShow(row) {
  account.value = row;
  freezingMax.value = row.pointsBalance;
  title.value = "冻结/解冻账户【"+row.customerId+"】操作";
  freezing.value = true;
}

function freeze(){
  freezePoints(account.value).then(response => {
    account.value = response;
    freezing.value  = false;
    getList()
  });
}
function handleAdd() {

}

function getListTransDetails() {
  listTransDetails(queryDetailsParams.value, transNo.value).then(response => {
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
getList();
</script>
<style>
.mr10 {
  margin-right: 10px;
}
</style>
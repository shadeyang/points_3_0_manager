import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询客户列表
export function listCostByInst(institutionId) {
  return request({
    url: '/points/cost/listByInst/'+ parseStrEmpty(institutionId),
    method: 'get'
  })
}

export function addCost(data){
return request({
    url: '/points/cost',
    method: 'post',
    data: data
  })
}

export function updateCost(data){
 return request({
    url: '/points/cost',
    method: 'put',
    data: data
  })
}

export function delCost(data){
	return request({
	   url: '/points/cost',
	   method: 'delete',
	   data: data
	 })
}
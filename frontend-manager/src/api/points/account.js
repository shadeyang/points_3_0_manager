import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi";

// 查询账户列表
export function listAccount(query) {
  return request({
    url: '/points/account/list',
    method: 'get',
    params: query
  })
}

export function changeAccountStatus(customerId, pointsType) {
	return request({
	  url: '/points/account/status/'+ parseStrEmpty(customerId)+"/"+parseStrEmpty(pointsType),
	  method: 'put'
	})
}

export function queryDetails(customerId, pointsType) {
	return request({
	  url: '/points/account/details/'+ parseStrEmpty(customerId)+"/"+parseStrEmpty(pointsType),
	  method: 'get'
	})
}

export function freezePoints(data){
	return request({
		url: '/points/account/freeze',
		method: 'post',
		params: data
	})
}
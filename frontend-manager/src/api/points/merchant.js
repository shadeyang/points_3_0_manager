import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";


export function listMerchant(query) {
  return request({
    url: '/points/merchant/list',
    method: 'get',
    params: query
  })
}

export function listMerchantByInst(institutionId) {
  return request({
    url: '/points/merchant/listByInst/'+ parseStrEmpty(institutionId),
    method: 'get'
  })
}

export function addMerchant(data){
return request({
    url: '/points/merchant',
    method: 'post',
    data: data
  })
}

export function updateMerchant(data){
 return request({
    url: '/points/merchant',
    method: 'put',
    data: data
  })
}

export function delMerchant(data){
	return request({
	   url: '/points/merchant',
	   method: 'delete',
	   data: data
	 })
}
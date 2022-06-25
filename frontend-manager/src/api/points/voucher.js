import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询客户
export function getVoucher(customerId) {
  return request({
    url: '/points/voucher/' + parseStrEmpty(customerId),
    method: 'get'
  })
}

// 新增客户
export function addVoucher(data) {
  return request({
    url: '/points/voucher',
    method: 'post',
    data: data
  })
}

// 修改客户
export function updateVoucher(data) {
  return request({
    url: '/points/voucher',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delVoucher(data) {
  return request({
    url: '/points/voucher',
    method: 'delete',
	data: data
  })
}
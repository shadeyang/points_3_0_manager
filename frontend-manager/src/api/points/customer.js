import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询客户列表
export function listCustomer(query) {
  return request({
    url: '/points/customer/list',
    method: 'get',
    params: query
  })
}

// 查询客户
export function getCustomer(customerId) {
  return request({
    url: '/points/customer/' + parseStrEmpty(customerId),
    method: 'get'
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/points/customer',
    method: 'post',
    data: data
  })
}

// 修改客户
export function updateCustomer(data) {
  return request({
    url: '/points/customer',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delCustomer(customerId) {
  return request({
    url: '/points/customer/' + customerId,
    method: 'delete'
  })
}

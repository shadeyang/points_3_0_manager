import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询客户列表
export function listTypeByInst(institutionId) {
  return request({
    url: '/points/type/listByInst/'+ parseStrEmpty(institutionId),
    method: 'get'
  })
}

export function addType(data){
return request({
    url: '/points/type',
    method: 'post',
    data: data
  })
}

export function updateType(data){
 return request({
    url: '/points/type',
    method: 'put',
    data: data
  })
}

export function delType(data){
 return request({
    url: '/points/type',
    method: 'delete',
    data: data
  })
}
import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询参数列表
export function queryByTop() {
  return request({
    url: '/points/inst/queryByTop/0',
    method: 'get'
  })
}

// 查询下级列表
export function queryByParentInst(parentInstitutionId) {
  return request({
    url: '/points/inst/list/'+parseStrEmpty(parentInstitutionId),
    method: 'get'
  })
}

export function addInst(data){
return request({
    url: '/points/inst',
    method: 'post',
    data: data
  })
}

export function updateInst(data){
 return request({
    url: '/points/inst',
    method: 'put',
    data: data
  })
}

export function delInst(institutionId) {
  return request({
    url: '/points/inst/'+parseStrEmpty(institutionId),
    method: 'delete',
  })
}
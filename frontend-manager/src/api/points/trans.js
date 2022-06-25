import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询交易流水列表
export function listTrans(query) {
  return request({
    url: '/points/trans/list',
    method: 'get',
    params: query
  })
}

export function listTransDetails(query,transNo) {
  return request({
    url: '/points/trans/detail/'+ parseStrEmpty(transNo),
    method: 'get',
    params: query
  })
}
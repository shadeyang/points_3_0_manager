package com.wt2024.points.repository;

import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.api.customer.domain.CustomerDTO;
import com.wt2024.points.repository.api.customer.repository.CustomerRepository;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 17:07
 * @project points3.0:com.wt2024.points.repository
 */
public interface CustomerAdminRepository extends CustomerRepository {

    PageInfo<CustomerDTO> queryCustomerListByPage(CustomerDTO customerDTO);
}

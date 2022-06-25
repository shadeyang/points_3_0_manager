package com.wt2024.points.repository.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.CustomerAdminRepository;
import com.wt2024.points.repository.api.customer.domain.CustomerDTO;
import com.wt2024.points.repository.customer.converter.ConverterConstants;
import com.wt2024.points.repository.customer.entity.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 17:07
 * @project points3.0:com.wt2024.points.repository
 */
@Primary
@Repository
public class CustomerAdminRepositoryImpl extends CustomerRepositoryImpl implements CustomerAdminRepository {

    @Override
    public PageInfo<CustomerDTO> queryCustomerListByPage(CustomerDTO customerDTO) {
        Customer customer = ConverterConstants.CUSTOMER_CONVERTER.toCustomer(customerDTO);
        PageInfo<Customer> source = PageHelper.getLocalPage().doSelectPageInfo(()-> customerMapper.selectByCustomer(customer));
        PageInfo<CustomerDTO> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        List<CustomerDTO> collect = source.getList().stream().map(ConverterConstants.CUSTOMER_CONVERTER::toCustomerDTO).collect(Collectors.toList());
        target.setList(collect);
        return target;
    }
}

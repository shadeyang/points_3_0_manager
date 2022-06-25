package com.wt2024.points.web.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.domain.CustomerAdminDTO;
import com.wt2024.points.exception.PointsIllegalArgumentException;
import com.wt2024.points.repository.CustomerAdminRepository;
import com.wt2024.points.repository.api.customer.domain.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/4/29 11:02
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/customer")
public class CustomerController extends PointsBaseController {

    @Autowired
    CustomerAdminRepository customerRepository;

    @PreAuthorize("@ss.hasPermi('points:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerAdminDTO customer)
    {
        String customerId;
        try {
            customerId = getCustomerId(customer.getVoucherNo(), customer.getVoucherTypeNo());
            if(Objects.isNull(customerId)){
                return getDataTable(Collections.emptyList());
            }
        } catch (PointsIllegalArgumentException e) {
            customerId = null;
        }
        startPage();
        customer.setCustomerId(customerId);
        PageInfo<CustomerDTO> list = customerRepository.queryCustomerListByPage(customer);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('points:customer:query')")
    @GetMapping(value = { "/", "/{customerId}" })
    public AjaxResult getInfo(@PathVariable(value = "customerId",required = false) String customerId){
        return AjaxResult.success(customerRepository.queryCustomerById(customerId));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('points:customer:add')")
    @Log(title = "客户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CustomerDTO customer)
    {
        customer.setOperator(getUsername());
        return AjaxResult.success(customerRepository.createCustomer(customer,null));
    }

    /**
     * 修改保存
     */
    @PreAuthorize("@ss.hasPermi('points:customer:edit')")
    @Log(title = "客户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CustomerDTO customer)
    {
        customer.setOperator(getUsername());
        if (customerRepository.updateCustomer(customer) > 0)
        {
            return AjaxResult.success();
        }
        return AjaxResult.error("修改客户'" + customer.getCustomerId() + "'失败，请联系管理员");
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('points:customer:remove')")
    @Log(title = "客户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable String[] customerIds)
    {
        return toAjax(customerRepository.deleteCustomerByIds(customerIds));
    }

}

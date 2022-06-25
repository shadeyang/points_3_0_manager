package com.wt2024.points.web.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.repository.api.voucher.domain.VoucherDTO;
import com.wt2024.points.repository.api.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 11:22
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/voucher")
public class VoucherController extends BaseController {

    @Autowired
    VoucherRepository voucherRepository;

    @PreAuthorize("@ss.hasPermi('points:voucher:query')")
    @GetMapping(value = {"/{customerId}"})
    public AjaxResult getInfo(@PathVariable(value = "customerId") String customerId) {
        return AjaxResult.success(voucherRepository.queryVoucherByCustomer(customerId));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('points:voucher:add')")
    @Log(title = "凭证管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody VoucherDTO voucher) {
        VoucherDTO voucherExists = voucherRepository.queryVoucher(voucher.getVoucherTypeNo(), voucher.getVoucherNo());
        if (Objects.nonNull(voucherExists)) {
            return AjaxResult.error("增加凭证'" + voucher.getVoucherTypeNo() + ":" + voucher.getVoucherNo() + "'失败，凭证已存在");
        }
        return toAjax(voucherRepository.addVoucher(voucher));
    }

    /**
     * 修改保存
     */
    @PreAuthorize("@ss.hasPermi('points:voucher:edit')")
    @Log(title = "凭证管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody VoucherDTO voucher) {
        VoucherDTO voucherExists = voucherRepository.queryVoucher(voucher.getVoucherTypeNo(), voucher.getVoucherNo());
        if (Objects.isNull(voucherExists)) {
            return AjaxResult.error("修改凭证'" + voucher.getVoucherTypeNo() + ":" + voucher.getVoucherNo() + "'失败，凭证不存在");
        }
        if(!voucherExists.getCustomerId().equals(voucher.getCustomerId())){
            return AjaxResult.error("修改凭证'" + voucher.getVoucherTypeNo() + ":" + voucher.getVoucherNo() + "'失败，凭证不在当前客户下");
        }
        return toAjax(voucherRepository.updateVoucher(voucher));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('points:voucher:remove')")
    @Log(title = "凭证管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@Validated @RequestBody VoucherDTO voucher) {
        return toAjax(voucherRepository.deleteVoucher(voucher));
    }
}

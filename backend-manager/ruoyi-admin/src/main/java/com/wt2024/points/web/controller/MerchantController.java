package com.wt2024.points.web.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.repository.MerchantAdminRepository;
import com.wt2024.points.repository.api.merchant.domain.MerchantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/8 10:22
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/merchant")
public class MerchantController extends BaseController {

    @Autowired
    MerchantAdminRepository merchantRepository;

    @PreAuthorize("@ss.hasPermi('points:merchant:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantDTO merchant) {
        startPage();
        PageInfo<MerchantDTO> list = merchantRepository.queryMerchantListByPage(merchant);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('points:merchant:listByInst')")
    @GetMapping(value = {"/listByInst", "/listByInst/{institutionId}"})
    public List<MerchantDTO> queryListByInst(@PathVariable(value = "institutionId", required = false) String institutionId) {
        return merchantRepository.queryMerchantByInstitutionId(institutionId);
    }

    @PreAuthorize("@ss.hasPermi('points:merchant:add')")
    @Log(title = "收单商户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MerchantDTO merchant) {
        try {
            return toAjax(merchantRepository.createMerchant(merchant, getUsername()));
        } catch (Exception e) {
            return AjaxResult.error("新增收单商户失败" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:merchant:edit')")
    @Log(title = "收单商户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MerchantDTO merchant) {
        try {
            return toAjax(merchantRepository.updateMerchant(merchant));
        } catch (Exception e) {
            return AjaxResult.error("修改收单商户'" + merchant.getMerchantNo() + "'失败，" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:merchant:remove')")
    @Log(title = "收单商户管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@Validated @RequestBody MerchantDTO merchant) {
        try {
            return toAjax(merchantRepository.deleteMerchant(merchant));
        } catch (Exception e) {
            return AjaxResult.error("删除收单商户'" + merchant.getMerchantNo() + "'失败，" + e.getMessage());
        }
    }
}

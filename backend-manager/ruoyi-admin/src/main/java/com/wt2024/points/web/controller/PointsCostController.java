package com.wt2024.points.web.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.repository.api.account.domain.PointsCostDTO;
import com.wt2024.points.repository.api.account.repository.PointsCostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/31 16:29
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/cost")
public class PointsCostController extends BaseController {

    @Autowired
    PointsCostRepository pointsCostRepository;

    @PreAuthorize("@ss.hasPermi('points:cost:listByInst')")
    @GetMapping(value = {"/listByInst", "/listByInst/{institutionId}"})
    public List<PointsCostDTO> queryListByInst(@PathVariable(value = "institutionId", required = false) String institutionId) {
        return pointsCostRepository.queryAllPointsCostByInstitutionId(institutionId);
    }

    @PreAuthorize("@ss.hasPermi('points:cost:add')")
    @Log(title = "成本部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PointsCostDTO pointsCost) {
        try {
            return toAjax(pointsCostRepository.createPointsCost(pointsCost));
        } catch (Exception e) {
            return AjaxResult.error("新增成本部门失败" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:cost:edit')")
    @Log(title = "成本部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PointsCostDTO pointsCost) {
        try {
            return toAjax(pointsCostRepository.updatePointsCost(pointsCost));
        } catch (Exception e) {
            return AjaxResult.error("修改成本部门'" + pointsCost.getCostLine() + "'失败，" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:cost:remove')")
    @Log(title = "成本部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@Validated @RequestBody PointsCostDTO pointsCost) {
        try {
            return toAjax(pointsCostRepository.deletePointsCost(pointsCost));
        } catch (Exception e) {
            return AjaxResult.error("删除成本部门'" + pointsCost.getCostLine() + "'失败，" + e.getMessage());
        }
    }
}

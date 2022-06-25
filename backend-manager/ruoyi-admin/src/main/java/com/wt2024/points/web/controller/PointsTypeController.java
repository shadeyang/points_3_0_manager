package com.wt2024.points.web.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.repository.api.account.domain.PointsCostDTO;
import com.wt2024.points.repository.api.account.domain.PointsTypeDTO;
import com.wt2024.points.repository.api.account.repository.PointsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/6 15:50
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/type")
public class PointsTypeController extends BaseController {

    @Autowired
    PointsTypeRepository pointsTypeRepository;

    @PreAuthorize("@ss.hasPermi('points:type:listByInst')")
    @GetMapping(value = {"/listByInst", "/listByInst/{institutionId}"})
    public List<PointsTypeDTO> queryListByInst(@PathVariable(value = "institutionId", required = false) String institutionId) {
        return pointsTypeRepository.queryPointsTypeListByInst(institutionId);
    }

    @PreAuthorize("@ss.hasPermi('points:type:add')")
    @Log(title = "积分类型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PointsTypeDTO pointsType) {
        try {
            return toAjax(pointsTypeRepository.createPointsType(pointsType));
        } catch (Exception e) {
            return AjaxResult.error("新增积分类型失败" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:type:edit')")
    @Log(title = "积分类型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PointsTypeDTO pointsType) {
        try {
            return toAjax(pointsTypeRepository.updatePointsType(pointsType));
        } catch (Exception e) {
            return AjaxResult.error("修改积分类型'" + pointsType.getPointsTypeNo() + "'失败，" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:type:remove')")
    @Log(title = "积分类型管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@Validated @RequestBody PointsTypeDTO pointsType) {
        try {
            return toAjax(pointsTypeRepository.deletePointsType(pointsType));
        } catch (Exception e) {
            return AjaxResult.error("删除积分类型'" + pointsType.getPointsTypeNo() + "'失败，" + e.getMessage());
        }
    }
}

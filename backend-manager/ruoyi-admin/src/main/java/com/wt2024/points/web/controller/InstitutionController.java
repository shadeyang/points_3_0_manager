package com.wt2024.points.web.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.converter.AdminConverterConstants;
import com.wt2024.points.domain.InstitutionAdminTreeDTO;
import com.wt2024.points.repository.InstitutionAdminRepository;
import com.wt2024.points.repository.api.account.domain.PointsTypeDTO;
import com.wt2024.points.repository.api.account.repository.PointsTypeRepository;
import com.wt2024.points.repository.api.merchant.domain.MerchantDTO;
import com.wt2024.points.repository.api.merchant.repository.MerchantRepository;
import com.wt2024.points.repository.api.system.domain.InstitutionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/4/29 16:17
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/inst")
public class InstitutionController extends BaseController {

    @Autowired
    InstitutionAdminRepository institutionRepository;
    @Autowired
    PointsTypeRepository pointsTypeRepository;
    @Autowired
    MerchantRepository merchantRepository;

    @PreAuthorize("@ss.hasPermi('points:inst:list')")
    @GetMapping("/list")
    public TableDataInfo list(InstitutionDTO institution) {
        startPage();
        PageInfo<InstitutionDTO> list = institutionRepository.queryInstitutionListByPage(institution);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('points:inst:list')")
    @GetMapping("/list/{parentInstitutionId}")
    public List<InstitutionAdminTreeDTO> list(@PathVariable("parentInstitutionId") String parentInstitutionId) {
        return institutionRepository.selectByParentInstitutionId(parentInstitutionId).stream().map(AdminConverterConstants.INSTITUTION_ADMIN_CONVERTER::toInstitutionAdminTreeDTO).collect(Collectors.toList());
    }

    @PreAuthorize("@ss.hasPermi('points:inst:query')")
    @GetMapping("/queryByTop/{topInstitutionId}")
    public List<InstitutionDTO> query(@PathVariable("topInstitutionId") String topInstitutionId) {
        return institutionRepository.queryInstitutionByTop(topInstitutionId);
    }

    @PreAuthorize("@ss.hasPermi('points:inst:add')")
    @Log(title = "机构管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody InstitutionDTO institution) {
        try {
            return toAjax(institutionRepository.createInstitution(institution));
        } catch (Exception e) {
            return AjaxResult.error("新增机构失败" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('points:inst:edit')")
    @Log(title = "机构管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody InstitutionDTO institution) {
        try {
            return toAjax(institutionRepository.updateInstitution(institution));
        } catch (Exception e) {
            return AjaxResult.error("修改机构'" + institution.getInstitutionNo() + "'失败，" + e.getMessage());
        }
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('points:inst:remove')")
    @Log(title = "机构管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{institutionId}")
    public AjaxResult remove(@PathVariable String institutionId) {
        List<PointsTypeDTO> pointsTypeDTOList = pointsTypeRepository.queryPointsTypeListByInst(institutionId);
        if (!CollectionUtils.isEmpty(pointsTypeDTOList)) {
            return AjaxResult.error("机构ID'" + institutionId + "'已被积分类型使用，不可删除");
        }
        List<MerchantDTO> merchantDTOList = merchantRepository.queryMerchantByInstitutionId(institutionId);
        if (!CollectionUtils.isEmpty(merchantDTOList)) {
            return AjaxResult.error("机构ID'" + institutionId + "'已被收单商户使用，不可删除");
        }
        List<InstitutionDTO> institutionChildrenList = institutionRepository.selectByParentInstitutionId(institutionId);
        if(!CollectionUtils.isEmpty(institutionChildrenList)){
            return AjaxResult.error("机构ID'" + institutionId + "'存在子机构，不可删除");
        }
        return toAjax(institutionRepository.deleteInstitutionId(institutionId));
    }
}

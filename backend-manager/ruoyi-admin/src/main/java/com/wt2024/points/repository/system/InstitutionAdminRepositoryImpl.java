package com.wt2024.points.repository.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.InstitutionAdminRepository;
import com.wt2024.points.repository.api.system.domain.InstitutionDTO;
import com.wt2024.points.repository.system.converter.ConverterConstants;
import com.wt2024.points.repository.system.entity.Institution;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/1 15:20
 * @project points3.0:com.wt2024.points.repository.account
 */
@Primary
@Repository
public class InstitutionAdminRepositoryImpl extends InstitutionRepositoryImpl implements InstitutionAdminRepository {
    @Override
    public PageInfo<InstitutionDTO> queryInstitutionListByPage(InstitutionDTO institutionDTO) {
        Institution institution = ConverterConstants.INSTITUTION_CONVERTER.toInstitution(institutionDTO);
        PageInfo<Institution> source = PageHelper.getLocalPage().doSelectPageInfo(()-> institutionMapper.selectByParentInstitutionId(institution.getParentInstitutionId()));
        PageInfo<InstitutionDTO> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        List<InstitutionDTO> collect = source.getList().stream().map(ConverterConstants.INSTITUTION_CONVERTER::toInstitutionDTO).collect(Collectors.toList());
        target.setList(collect);
        return target;
    }
}

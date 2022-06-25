package com.wt2024.points.converter;

import com.wt2024.points.domain.InstitutionAdminTreeDTO;
import com.wt2024.points.repository.api.system.domain.InstitutionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/2 10:20
 * @project points3.0:com.wt2024.points.converter
 */
@Mapper
public interface InstitutionAdminConverter {

    @Mapping(target = "hasChildren", ignore = true)
    @Mapping(target = "children", ignore = true)
    InstitutionAdminTreeDTO toInstitutionAdminTreeDTO(InstitutionDTO institutionDTO);
}

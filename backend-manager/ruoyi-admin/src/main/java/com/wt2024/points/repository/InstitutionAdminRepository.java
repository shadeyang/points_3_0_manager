package com.wt2024.points.repository;

import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.api.system.domain.InstitutionDTO;
import com.wt2024.points.repository.api.system.repository.InstitutionRepository;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/1 15:20
 * @project points3.0:com.wt2024.points.repository
 */
public interface InstitutionAdminRepository extends InstitutionRepository {
    PageInfo<InstitutionDTO> queryInstitutionListByPage(InstitutionDTO institution);
}

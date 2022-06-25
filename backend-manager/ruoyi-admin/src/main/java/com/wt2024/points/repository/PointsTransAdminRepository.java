package com.wt2024.points.repository;

import com.github.pagehelper.PageInfo;
import com.wt2024.points.domain.PointsTransAdminDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDetailsDTO;
import com.wt2024.points.repository.api.account.repository.PointsTransRepository;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 17:43
 * @project points3.0:com.wt2024.points.repository
 */
public interface PointsTransAdminRepository extends PointsTransRepository {

    PageInfo<PointsTransDTO> queryTransListByPage(PointsTransAdminDTO pointsTransAdminDTO);

    PageInfo<PointsTransDetailsDTO> queryPointsTransDetailsByTransNoPage(String transNo);

}

package com.wt2024.points.converter;

import com.wt2024.points.domain.PointsTransAdminDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDTO;
import org.mapstruct.Mapper;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 18:03
 * @project points3.0:com.wt2024.points.converter
 */
@Mapper
public interface PointsTransAdminConverter {

    PointsTransDTO toPointsTransDTO(PointsTransAdminDTO pointsTransAdminDTO);
}

package com.wt2024.points.converter;

import org.mapstruct.factory.Mappers;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2021/12/16 16:30
 * @project points3.0:com.wt2024.points.core.converter
 */
public class AdminConverterConstants {

    public static final PointsTransAdminConverter POINTS_TRANS_ADMIN_CONVERTER = Mappers.getMapper(PointsTransAdminConverter.class);

    public static final InstitutionAdminConverter INSTITUTION_ADMIN_CONVERTER = Mappers.getMapper(InstitutionAdminConverter.class);
}

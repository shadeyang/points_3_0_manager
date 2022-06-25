package com.wt2024.points.repository;

import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.api.account.domain.PointsAccountInfoDTO;
import com.wt2024.points.repository.api.account.repository.PointsAccountInfoRepository;

/**
 * @ClassName PointsAccountInfoAdminRepository
 * @Description: TODO
 * @Author shade.yang
 * @Date 2022/5/6
 * @Version V1.0
 **/
public interface PointsAccountInfoAdminRepository extends PointsAccountInfoRepository {
    PageInfo<PointsAccountInfoDTO> queryAccountListByPage(PointsAccountInfoDTO pointsAccountInfoDTO);
}

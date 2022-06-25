package com.wt2024.points.domain;

import com.wt2024.points.repository.api.account.domain.PointsAccountDetailsDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDTO;

import java.util.List;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/12 09:24
 * @project points3.0:com.wt2024.points.domain
 */
public class AccountDetailsAdminDTO {

    private List<PointsAccountDetailsDTO> pointsAccountDetailsList;

    private List<PointsTransDTO> pointsTransList;

    public List<PointsAccountDetailsDTO> getPointsAccountDetailsList() {
        return pointsAccountDetailsList;
    }

    public void setPointsAccountDetailsList(List<PointsAccountDetailsDTO> pointsAccountDetailsList) {
        this.pointsAccountDetailsList = pointsAccountDetailsList;
    }

    public List<PointsTransDTO> getPointsTransList() {
        return pointsTransList;
    }

    public void setPointsTransList(List<PointsTransDTO> pointsTransList) {
        this.pointsTransList = pointsTransList;
    }
}

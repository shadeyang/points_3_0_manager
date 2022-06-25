package com.wt2024.points.domain;

import com.wt2024.points.repository.api.account.domain.PointsAccountInfoDTO;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/7 15:22
 * @project points3.0:com.wt2024.points.domain
 */
public class PointsAccountInfoAdminDTO extends PointsAccountInfoDTO {

    private String voucherTypeNo;

    private String voucherNo;

    public String getVoucherTypeNo() {
        return voucherTypeNo;
    }

    public void setVoucherTypeNo(String voucherTypeNo) {
        this.voucherTypeNo = voucherTypeNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
}

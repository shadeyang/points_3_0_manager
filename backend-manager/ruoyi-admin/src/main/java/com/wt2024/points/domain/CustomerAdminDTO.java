package com.wt2024.points.domain;

import com.wt2024.points.repository.api.customer.domain.CustomerDTO;

/**
 * @ClassName CustomerAdminDTO
 * @Description: TODO
 * @Author shade.yang
 * @Date 2022/5/15
 * @Version V1.0
 **/
public class CustomerAdminDTO extends CustomerDTO {

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

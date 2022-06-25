package com.wt2024.points.web.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.StringUtils;
import com.wt2024.points.common.enums.VoucherType;
import com.wt2024.points.exception.PointsIllegalArgumentException;
import com.wt2024.points.repository.api.voucher.domain.VoucherDTO;
import com.wt2024.points.repository.api.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * @ClassName PointsBaseController
 * @Description: TODO
 * @Author shade.yang
 * @Date 2022/5/15
 * @Version V1.0
 **/
@Controller
public abstract class PointsBaseController extends BaseController {

    @Autowired
    VoucherRepository voucherRepository;

    protected String getCustomerId(String voucherNo,String voucherTypeNo){
        if (!StringUtils.isAnyEmpty(voucherNo, voucherTypeNo)) {
            VoucherType type = VoucherType.getEnum(voucherTypeNo);
            if (VoucherType.CUST.equals(type)) {
                return voucherNo;
            } else {
                VoucherDTO voucher = voucherRepository.queryVoucher(voucherTypeNo, voucherNo);
                if (Objects.nonNull(voucher)) {
                    return voucher.getCustomerId();
                }
            }
            return null;
        }else{
            throw new PointsIllegalArgumentException("凭证类型和号码数据不全");
        }
    }
}

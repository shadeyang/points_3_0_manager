package com.wt2024.points.web.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.wt2024.points.common.enums.AccountStatus;
import com.wt2024.points.domain.AccountDetailsAdminDTO;
import com.wt2024.points.domain.PointsAccountInfoAdminDTO;
import com.wt2024.points.exception.PointsIllegalArgumentException;
import com.wt2024.points.repository.PointsAccountInfoAdminRepository;
import com.wt2024.points.repository.PointsTransAdminRepository;
import com.wt2024.points.repository.api.account.domain.PointsAccountDetailsDTO;
import com.wt2024.points.repository.api.account.domain.PointsAccountInfoDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName AccountInfoController
 * @Description: TODO
 * @Author shade.yang
 * @Date 2022/5/6
 * @Version V1.0
 **/
@RestController
@RequestMapping("/points/account")
public class AccountInfoController extends PointsBaseController {

    @Autowired
    PointsAccountInfoAdminRepository pointsAccountInfoRepository;
    @Autowired
    PointsTransAdminRepository pointsTransAdminRepository;

    @PreAuthorize("@ss.hasPermi('points:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(PointsAccountInfoAdminDTO pointsAccountInfo) {
        String customerId;
        try {
            customerId = getCustomerId(pointsAccountInfo.getVoucherNo(), pointsAccountInfo.getVoucherTypeNo());
            if(Objects.isNull(customerId)){
                return getDataTable(Collections.emptyList());
            }
        } catch (PointsIllegalArgumentException e) {
            customerId = null;
        }
        startPage();
        pointsAccountInfo.setCustomerId(customerId);
        PageInfo<PointsAccountInfoDTO> list = pointsAccountInfoRepository.queryAccountListByPage(pointsAccountInfo);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('points:account:query')")
    @GetMapping(value = {"/{customerId}", "/{customerId}/{pointTypeNo}"})
    public List<PointsAccountInfoDTO> query(@PathVariable(value = "customerId") String customerId, @PathVariable(value = "pointTypeNo", required = false) String pointTypeNo) {
        return pointsAccountInfoRepository.queryPointsAccountInfo(customerId, pointTypeNo, null);
    }

    @PreAuthorize("@ss.hasPermi('points:account:update')")
    @PutMapping(value = {"/status/{customerId}/{pointTypeNo}"})
    @Log(title = "账户状态", businessType = BusinessType.UPDATE)
    public PointsAccountInfoDTO status(@PathVariable(value = "customerId") String customerId, @PathVariable(value = "pointTypeNo") String pointTypeNo) {
        PointsAccountInfoDTO pointsAccountInfoDTO = pointsAccountInfoRepository.queryPointsAccountInfoByType(customerId, pointTypeNo);
        AccountStatus status = AccountStatus.AVAILABLE.getStatus().equals(pointsAccountInfoDTO.getPointsAccountStatus()) ? AccountStatus.DISABLE : AccountStatus.AVAILABLE;
        return pointsAccountInfoRepository.changePointsAccountStatus(customerId, pointTypeNo, status);
    }

    @PreAuthorize("@ss.hasPermi('points:account:details')")
    @GetMapping(value = {"/details/{customerId}/{pointTypeNo}"})
    public AccountDetailsAdminDTO details(@PathVariable(value = "customerId") String customerId, @PathVariable(value = "pointTypeNo") String pointTypeNo) {
        List<PointsTransDTO> pointsTransTempList = pointsTransAdminRepository.queryAccountingTrans(customerId, pointTypeNo);
        List<String> accountingTransNo = pointsTransTempList.stream().map(PointsTransDTO::getTransNo).distinct().collect(Collectors.toList());
        List<PointsAccountDetailsDTO> pointsAccountDetailsList = pointsTransAdminRepository.queryPointsAccountDetails(customerId, pointTypeNo, accountingTransNo)
                .stream().filter(pointsAccountDetailsDTO -> !pointsAccountDetailsDTO.getPointsAmount().equals(BigDecimal.ZERO)).collect(Collectors.toList());

        AccountDetailsAdminDTO details = new AccountDetailsAdminDTO();
        details.setPointsAccountDetailsList(pointsAccountDetailsList);
        details.setPointsTransList(pointsTransTempList);
        return details;
    }

    @PreAuthorize("@ss.hasPermi('points:account:freeze')")
    @PostMapping(value = {"/freeze"})
    @Log(title = "账户冻结", businessType = BusinessType.UPDATE)
    public PointsAccountInfoDTO freeze(PointsAccountInfoDTO pointsAccountInfo) {
        return pointsAccountInfoRepository.freezePoints(pointsAccountInfo);
    }
}

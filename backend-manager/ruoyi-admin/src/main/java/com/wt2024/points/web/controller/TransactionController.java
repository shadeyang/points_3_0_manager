package com.wt2024.points.web.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.wt2024.points.domain.PointsTransAdminDTO;
import com.wt2024.points.repository.PointsTransAdminRepository;
import com.wt2024.points.repository.api.account.domain.PointsTransDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 17:39
 * @project points3.0:com.wt2024.points.web.controller
 */
@RestController
@RequestMapping("/points/trans")
public class TransactionController extends BaseController {

    @Autowired
    PointsTransAdminRepository pointsTransRepository;


    @PreAuthorize("@ss.hasPermi('points:transaction:list')")
    @GetMapping("/list")
    public TableDataInfo list(PointsTransAdminDTO pointsTransAdminDTO)
    {
        startPage();
        PageInfo<PointsTransDTO> list = pointsTransRepository.queryTransListByPage(pointsTransAdminDTO);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('points:transaction:detail')")
    @GetMapping("/detail/{transNo}")
    public TableDataInfo detail(@PathVariable("transNo")String transNo){
        startPage();
        PageInfo<PointsTransDetailsDTO> list = pointsTransRepository.queryPointsTransDetailsByTransNoPage(transNo);
        return getDataTable(list);
    }
}

package com.wt2024.points.repository.account;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt2024.points.converter.AdminConverterConstants;
import com.wt2024.points.domain.PointsTransAdminDTO;
import com.wt2024.points.repository.PointsTransAdminRepository;
import com.wt2024.points.repository.account.converter.ConverterConstants;
import com.wt2024.points.repository.account.entity.PointsTrans;
import com.wt2024.points.repository.account.entity.PointsTransDetails;
import com.wt2024.points.repository.api.account.domain.PointsTransDTO;
import com.wt2024.points.repository.api.account.domain.PointsTransDetailsDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 17:45
 * @project points3.0:com.wt2024.points.repository.account
 */
@Primary
@Repository
public class PointsTransAdminRepositoryImpl extends PointsTransRepositoryImpl implements PointsTransAdminRepository {

    @Override
    public PageInfo<PointsTransDTO> queryTransListByPage(PointsTransAdminDTO pointsTransAdminDTO) {
        PointsTransDTO pointsTransDTO = AdminConverterConstants.POINTS_TRANS_ADMIN_CONVERTER.toPointsTransDTO(pointsTransAdminDTO);
        PointsTrans pointsTrans = ConverterConstants.POINTS_TRANS_CONVERTER.toPointsTrans(pointsTransDTO);
        long startDateTime = Long.valueOf(String.valueOf(pointsTransAdminDTO.getParams().get("beginTime")));
        long stopDateTime = Long.valueOf(String.valueOf(pointsTransAdminDTO.getParams().get("endTime")));
        PageInfo<PointsTrans> source = PageHelper.getLocalPage().doSelectPageInfo(()-> pointsTransMapper.selectByTrans(pointsTrans,startDateTime,stopDateTime));
        PageInfo<PointsTransDTO> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        List<PointsTransDTO> collect = source.getList().stream().map(ConverterConstants.POINTS_TRANS_CONVERTER::toPointsTransDTO).collect(Collectors.toList());
        target.setList(collect);
        return target;
    }

    @Override
    public PageInfo<PointsTransDetailsDTO> queryPointsTransDetailsByTransNoPage(String transNo) {
        PageInfo<PointsTransDetails> source = PageHelper.getLocalPage().doSelectPageInfo(()-> pointsTransDetailsMapper.selectByTransNo(transNo));
        PageInfo<PointsTransDetailsDTO> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        List<PointsTransDetailsDTO> collect = source.getList().stream().map(ConverterConstants.POINTS_TRANS_CONVERTER::toPointsTransDetailsDTO).collect(Collectors.toList());
        target.setList(collect);
        return target;
    }
}

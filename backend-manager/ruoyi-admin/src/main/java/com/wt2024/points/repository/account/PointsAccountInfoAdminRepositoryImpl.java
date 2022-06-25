package com.wt2024.points.repository.account;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.PointsAccountInfoAdminRepository;
import com.wt2024.points.repository.account.converter.ConverterConstants;
import com.wt2024.points.repository.account.entity.PointsAccountInfo;
import com.wt2024.points.repository.api.account.domain.PointsAccountInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PointsAccountInfoAdminRepositoryImpl
 * @Description: TODO
 * @Author shade.yang
 * @Date 2022/5/6
 * @Version V1.0
 **/
@Primary
@Repository
public class PointsAccountInfoAdminRepositoryImpl extends PointsAccountInfoRepositoryImpl implements PointsAccountInfoAdminRepository {

    @Override
    public PageInfo<PointsAccountInfoDTO> queryAccountListByPage(PointsAccountInfoDTO pointsAccountInfoDTO) {
        PointsAccountInfo pointsAccountInfo = ConverterConstants.POINTS_ACCOUNT_INFO_CONVERTER.toPointsAccountInfo(pointsAccountInfoDTO);
        PageInfo<PointsAccountInfo> source = PageHelper.getLocalPage().doSelectPageInfo(()-> pointsAccountInfoMapper.selectPointsAccountInfo(pointsAccountInfo));
        PageInfo<PointsAccountInfoDTO> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        List<PointsAccountInfoDTO> collect = source.getList().stream().map(ConverterConstants.POINTS_ACCOUNT_INFO_CONVERTER::toPointsAccountInfoDTO).collect(Collectors.toList());
        target.setList(collect);
        return target;
    }
}

package com.wt2024.points.repository.merchant;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.MerchantAdminRepository;
import com.wt2024.points.repository.api.merchant.domain.MerchantDTO;
import com.wt2024.points.repository.merchant.converter.ConverterConstants;
import com.wt2024.points.repository.merchant.entity.Merchant;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/8 10:25
 * @project points3.0:com.wt2024.points.repository.merchant
 */
@Primary
@Repository
public class MerchantAdminRepositoryImpl extends MerchantRepositoryImpl implements MerchantAdminRepository {
    @Override
    public PageInfo<MerchantDTO> queryMerchantListByPage(MerchantDTO merchantDTO) {
        Merchant merchant = ConverterConstants.Merchant_CONVERTER.toMerchant(merchantDTO);
        PageInfo<Merchant> source = PageHelper.getLocalPage().doSelectPageInfo(()-> merchantMapper.selectByMerchant(merchant));
        PageInfo<MerchantDTO> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        List<MerchantDTO> collect = source.getList().stream().map(ConverterConstants.Merchant_CONVERTER::toMerchantDTO).collect(Collectors.toList());
        target.setList(collect);
        return target;
    }
}

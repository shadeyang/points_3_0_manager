package com.wt2024.points.repository;

import com.github.pagehelper.PageInfo;
import com.wt2024.points.repository.api.merchant.domain.MerchantDTO;
import com.wt2024.points.repository.api.merchant.repository.MerchantRepository;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/8 10:24
 * @project points3.0:com.wt2024.points.repository
 */
public interface MerchantAdminRepository extends MerchantRepository {

    public PageInfo<MerchantDTO> queryMerchantListByPage(MerchantDTO merchantDTO);
}

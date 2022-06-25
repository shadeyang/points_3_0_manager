package com.wt2024.points.domain;

import com.wt2024.points.repository.api.account.domain.PointsTransDTO;

import java.util.Map;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/5/5 17:54
 * @project points3.0:com.wt2024.points.domain
 */
public class PointsTransAdminDTO extends PointsTransDTO {

    /** 请求参数 */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}

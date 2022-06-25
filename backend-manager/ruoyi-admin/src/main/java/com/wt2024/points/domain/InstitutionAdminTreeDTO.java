package com.wt2024.points.domain;

import com.wt2024.points.repository.api.system.domain.InstitutionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shade.Yang [shade.yang@aliyun.com]
 * @date 2022/6/2 10:18
 * @project points3.0:com.wt2024.points.domain
 */
public class InstitutionAdminTreeDTO extends InstitutionDTO {

    private boolean hasChildren = true;

    private List<InstitutionAdminTreeDTO> children = new ArrayList<>();

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<InstitutionAdminTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<InstitutionAdminTreeDTO> children) {
        this.children = children;
    }
}

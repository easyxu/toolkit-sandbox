package com.phoenix.base.privilege.dao;

import com.phoenix.base.dal.dao.OperatorAdapter;
import com.phoenix.base.privilege.PrivilegeInfo;



public class OperatorAdapterImpl implements OperatorAdapter{
    PrivilegeInfo pvgInfo;

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public String getOperator() {
        return pvgInfo == null ? null : pvgInfo.getLoginId();
    }
}

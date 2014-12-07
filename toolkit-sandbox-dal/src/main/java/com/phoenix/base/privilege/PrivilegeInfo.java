package com.phoenix.base.privilege;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class PrivilegeInfo implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -3735668636403943350L;
    private static final String EMPTY                  = "";
    private String              actionName             = EMPTY;                    //操作
    private String              bizScope               = EMPTY;
    private String              ctrlFieldName          = EMPTY;                    //控制字段
    private String              deptPro                = EMPTY;                    //组织属性
    private String              language               = EMPTY;                    //登录者使用语言
    private String              loginId                = EMPTY;                    //登录者Id
    private String              managerPro             = EMPTY;                    //管理属性
    private String              orgExpand              = EMPTY;                    //组织访问扩展类型
    private String              orgId                  = EMPTY;                    //组织ID
    private String              remoteSite             = EMPTY;                    //如果是远程的，填远程系统名
    private String              resName                = EMPTY;                    //资源
    private Long				roleId					= null;						//登陆者角色ID
    private String              roleName               = EMPTY;                    //登录者角色名
    private String              userRoles              = EMPTY;                    //列表用户角色
    private String              token                  = EMPTY;                    //csrf token

//    private List<AppResControl> ctrlList;

    private static final String PVG_DT_ACTION_NAME     = "_PVG_DT_ACTION_NAME";
    private static final String PVG_DT_BIZ_SCOPE       = "_PVG_DT_BIZ_SCOPE";
    private static final String PVG_DT_CTRL_FIELD_NAME = "_PVG_DT_CTRL_FIELD_NAME";
    private static final String PVG_DT_DEPT_PRO        = "_PVG_DT_DEPT_PRO";
    private static final String PVG_DT_LANGUAGE        = "_PVG_DT_LANGUAGE";
    private static final String PVG_DT_LOGIN_ID        = "_PVG_DT_LOGIN_ID";
    private static final String PVG_DT_MANAGER_PRO     = "_PVG_DT_MANAGER_PRO";
    private static final String PVG_DT_ORG_EXPAND      = "_PVG_DT_ORG_EXPAND";
    private static final String PVG_DT_ORG_ID          = "_PVG_DT_ORG_ID";
    private static final String PVG_DT_REMOTE_SITE     = "_PVG_DT_REMOTE_SITE";
    private static final String PVG_DT_RES_NAME        = "_PVG_DT_RES_NAME";
    private static final String PVG_DT_ROLE_ID			= "_PVG_DT_ROLE_ID";
    private static final String PVG_DT_ROLE_NAME       = "_PVG_DT_ROLE_NAME";
    private static final String PVG_DT_USER_ROLES      = "_PVG_DT_USER_ROLES";
    private static final String PVG_DT_TOKEN           = "_PVG_DT_TOKEN";

    public String getRemoteSite() {
        return remoteSite;
    }

    public void setRemoteSite(String remoteSite) {
        this.remoteSite = remoteSite;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//    /**
//     * 获取控制规则列表.
//     * @return
//     */
//    public List<AppResControl> getCtrlList() {
//        return ctrlList;
//    }
//
//    /**
//     * 设置控制规则列表.
//     * @param ctrlList
//     */
//    public void setCtrlList(List<AppResControl> ctrlList) {
//        this.ctrlList = ctrlList;
//    }

    public String getDeptPro() {
        return deptPro;
    }

    public void setDeptPro(String deptPro) {
        this.deptPro = deptPro;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getManagerPro() {
        return managerPro;
    }

    public void setManagerPro(String managerPro) {
        this.managerPro = managerPro;
    }

    public String getOrgExpand() {
        return orgExpand;
    }

    public void setOrgExpand(String orgExpand) {
        this.orgExpand = orgExpand;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<?> getCtrlItem() {
        List<?> fields = new ArrayList<Object>();
        return fields;
    }

    public String getBizScope() {
        return bizScope;
    }

    public void setBizScope(String bizScope) {
        this.bizScope = bizScope;
    }

    public String getCtrlFieldName() {
        return ctrlFieldName;
    }

    public void setCtrlFieldName(String ctrlFieldName) {
        this.ctrlFieldName = ctrlFieldName;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    public PrivilegeInfo() {
        super();
    }

    public PrivilegeInfo(PrivilegeInfo info) {
        this.actionName = info.getActionName();
        this.bizScope = info.getBizScope();
        this.ctrlFieldName = info.getCtrlFieldName();
        this.deptPro = info.getDeptPro();
        this.language = info.getLanguage();
        this.loginId = info.getLoginId();
        this.managerPro = info.getManagerPro();
        this.orgExpand = info.getOrgExpand();
        this.orgId = info.getOrgId();
        this.remoteSite = info.getRemoteSite();
        this.resName = info.getResName();
        this.roleName = info.getRoleName();
        this.userRoles = info.getUserRoles();
        this.token = info.getToken();
    }

    /**
     * 清空所有属性
     * @param info
     */
    public void clear() {
        this.actionName = EMPTY;
        this.bizScope = EMPTY;
        this.ctrlFieldName = EMPTY;
        this.deptPro = EMPTY;
        this.language = EMPTY;
        this.loginId = EMPTY;
        this.managerPro = EMPTY;
        this.orgExpand = EMPTY;
        this.orgId = EMPTY;
        this.remoteSite = EMPTY;
        this.resName = EMPTY;
        this.roleName = EMPTY;
        this.userRoles = EMPTY;
        this.token = EMPTY;
//        ctrlList = null;
    }

    /**
     * 将信息保存到一个对象中
     * @param o
     * @return
     */
    public boolean saveIn(Object obj) {
        boolean result = true;
        if (obj != null) {
            if (obj instanceof Properties) {
                Properties o = (Properties) obj;
                o.setProperty(PVG_DT_ACTION_NAME, (this.actionName == null) ? EMPTY : this.actionName);
                o.setProperty(PVG_DT_BIZ_SCOPE, (this.bizScope == null) ? EMPTY : this.bizScope);
                o.setProperty(PVG_DT_CTRL_FIELD_NAME, (this.ctrlFieldName == null) ? EMPTY : this.ctrlFieldName);
                o.setProperty(PVG_DT_DEPT_PRO, (this.deptPro == null) ? EMPTY : this.deptPro);
                o.setProperty(PVG_DT_LANGUAGE, (this.language == null) ? EMPTY : this.language);
                o.setProperty(PVG_DT_LOGIN_ID, (this.loginId == null) ? EMPTY : this.loginId);
                o.setProperty(PVG_DT_MANAGER_PRO, (this.managerPro == null) ? EMPTY : this.managerPro);
                o.setProperty(PVG_DT_ORG_EXPAND, (this.orgExpand == null) ? EMPTY : this.orgExpand);
                o.setProperty(PVG_DT_ORG_ID, (this.orgId == null) ? EMPTY : this.orgId);
                o.setProperty(PVG_DT_REMOTE_SITE, (this.remoteSite == null) ? EMPTY : this.remoteSite);
                o.setProperty(PVG_DT_RES_NAME, (this.resName == null) ? EMPTY : this.resName);
                o.setProperty(PVG_DT_ROLE_ID, (this.roleId == null) ? "0" : this.roleId.toString());
                o.setProperty(PVG_DT_ROLE_NAME, (this.roleName == null) ? EMPTY : this.roleName);
                o.setProperty(PVG_DT_USER_ROLES, (this.userRoles == null) ? EMPTY : this.userRoles);
                o.setProperty(PVG_DT_TOKEN, (this.token == null) ? EMPTY : this.token);
                
            } else {
                new Exception("Unknow implement Object to get PRIVILEGE INFO").printStackTrace();
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 从一个对象中取出信息
     * @param o
     * @return
     */
    public static PrivilegeInfo receiveFrom(Object obj) {
        PrivilegeInfo info = null;
        if (obj != null) {
            info = new PrivilegeInfo();
            if (obj instanceof Properties) {
                Properties o = (Properties) obj;
                info.setActionName(o.getProperty(PVG_DT_ACTION_NAME));
                info.setBizScope(o.getProperty(PVG_DT_BIZ_SCOPE));
                info.setCtrlFieldName(o.getProperty(PVG_DT_CTRL_FIELD_NAME));
                info.setDeptPro(o.getProperty(PVG_DT_DEPT_PRO));
                info.setLanguage(o.getProperty(PVG_DT_LANGUAGE));
                info.setLoginId(o.getProperty(PVG_DT_LOGIN_ID));
                info.setManagerPro(o.getProperty(PVG_DT_MANAGER_PRO));
                info.setOrgExpand(o.getProperty(PVG_DT_ORG_EXPAND));
                info.setOrgId(o.getProperty(PVG_DT_ORG_ID));
                info.setRemoteSite(o.getProperty(PVG_DT_REMOTE_SITE));
                info.setResName(o.getProperty(PVG_DT_RES_NAME));
                info.setRoleId(Long.valueOf(o.getProperty(PVG_DT_ROLE_ID)));
                info.setRoleName(o.getProperty(PVG_DT_ROLE_NAME));
                info.setUserRoles(o.getProperty(PVG_DT_USER_ROLES));
                info.setToken(o.getProperty(PVG_DT_TOKEN));
            } else {
                new Exception("Unknow implement Object to get PRIVILEGE INFO").printStackTrace();
            }
        }
        return info;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}

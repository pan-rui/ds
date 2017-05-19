package com.pc.security;

import com.pc.base.BaseImpl;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.BaseDao;
import com.pc.dao.auth.AuthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-03-27 14:12)
 * @version: \$Rev: 1158 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-04-18 15:53:47 +0800 (周二, 18 4月 2017) $
 */
@Service
public class SecurityService {

    @Autowired
    private BaseImpl baseImpl;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private AuthDao authDao;

    /**
     * 查询用户功能角色
     *
     * @param ddBB
     * @param principals 用户名
     * @return
     */
    @Cacheable(value = "auth", cacheManager = "cacheManager", key = "'roles$'+#ddBB+'-'+#principalsr", sync = true)
    public List<Map<String, Object>> findFuncRoles(String ddBB, String principals) {
        return authDao.queryFuncRolesByUserTab(ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE, ddBB + TableConstants.SEPARATE + TableConstants.USER,
                ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE, principals);
    }

    /**
     * 根据角色列表(不能为空)查询用户权限
     *
     * @param ddBB
     * @param roles
     * @return
     */
    public List<Map<String, Object>> findFuncPermissByRoles(String ddBB, List<String> roles) {
        if (roles == null || roles.isEmpty()) return Collections.emptyList();
        return authDao.queryFuncPermissByRolesTab(ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES, roles);
    }

    /**
     * @param ddBB
     * @param roleId
     * @return
     */
    @Cacheable(value = "auth", cacheManager = "cacheManager", key = "'permissByRole$'+#ddBB+'-'+#roleId")
    public List<Map<String, Object>> findFuncPermissByRole(String ddBB, String roleId) {
        return authDao.queryFuncPermissByRoleTab(ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES, roleId);
    }

    /**
     * @param ddBB
     * @param userId
     * @return
     */
    @Cacheable(value = "auth", cacheManager = "cacheManager", key = "'permiss$'+#ddBB+'-'+#userId", sync = true)
    public List<Map<String, Object>> findFuncPermissByUser(String ddBB, String userId) {
        return baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE, ParamsMap.newMap("USER_ID", userId));
    }


}

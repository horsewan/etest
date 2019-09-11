package com.eningqu.common.shiro;

import com.eningqu.common.kit.SpringContextKit;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.modules.system.vo.ShiroUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/25 16:37
 * @version    1.0
 *
 **/
public class ShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 校验用户权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 查询用户菜单权限
        ISysUserService sysUserService = SpringContextKit.getBean(ISysUserService.class);
        Set<String> perms = sysUserService.selectPermsForUser();
        // 设置用户权限
        info.setStringPermissions(perms);
        return info;
    }

    /**
     * 校验用户信息
     * @param authToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        UsernamePasswordToken loginToken = (UsernamePasswordToken) authToken;
        // 获取bean实例
        ISysUserService sysUserService = SpringContextKit.getBean(ISysUserService.class);
        // 通过用户名获取用户信息
        SysUser sysUser = sysUserService.selectByLoginName(loginToken.getUsername());
        if (sysUser != null) {
            // 判断该用户是否可以登录
            if (StatusEnum.NO.getValue().equals(sysUser.getIsDisable())) {
                logger.error("账号["+sysUser.getLoginName()+"]已被锁定,请联系管理员");
                throw new LockedAccountException("账号["+sysUser.getLoginName()+"]已被锁定,请联系管理员");
            }
            byte[] salt = Hex.decode(sysUser.getLoginPwd().substring(0, 16));
            return new SimpleAuthenticationInfo(new ShiroUser(sysUser.getId(), sysUser.getLoginName(), sysUser.getUserType()),
                    sysUser.getLoginPwd().substring(16),
                    ByteSource.Util.bytes(salt),
                    getName());
        } else {
            logger.info("用户名[ "+loginToken.getUsername()+" ]不存在");
            throw new UnknownAccountException("用户名["+loginToken.getUsername()+"]不存在");
        }
    }
}

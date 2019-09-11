package com.eningqu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.enums.SysMenuTypeEnum;
import com.eningqu.common.enums.SysUserTypeEnum;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.kit.PasswordKit;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.api.mapper.UserMapper;
import com.eningqu.modules.system.mapper.SysUserMapper;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.modules.system.vo.MenuVo;
import com.eningqu.modules.system.vo.ShiroUser;
import com.eningqu.modules.system.vo.UserRolesVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @desc TODO  系统用户service接口实现
 * @author     Yanghuangping
 * @date       2018/4/18 15:42
 * @version    1.0
 *
 **/
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

//    @Autowired
//    private RedisKit redisKit;


    @Autowired
    private UserMapper userMapper;

    /**
     * TODO 登录用户名查询用户信息
     *
     * @param loginName
     * @return
     */
    @Override
    public SysUser selectByLoginName(String loginName) {
        return baseMapper.selectByLoginName(loginName, StatusEnum.YES.getValue());
    }

    @Override
    public SysUser selectByPhone(String phone) {
        return baseMapper.selectByPhone(phone, StatusEnum.YES.getValue());
    }

    /**
     * TODO 根据用户ID查询权限
     * @return
     */
    @Override
    public Set<String> selectPermsForUser() {

        ShiroUser sysUser = ShiroKit.loginInfo();

        // 从缓存中获取权限码
//        Set<String> redisPerms = (Set<String>) redisKit.get(Global.REDIS_PERMS_PREFIX + sysUser.getId());
//        if(redisPerms!= null && !redisPerms.isEmpty()){
//            return redisPerms;
//        }

        List<String> sources = null;

        // 若是超级管理员 则有所有权限
        if(StrUtil.equalsIgnoreCase(SysUserTypeEnum.SUPER.getValue(), sysUser.getUserType())){
            sources = baseMapper.selectSuperPerm(StatusEnum.YES.getValue());
        }else{
            sources = baseMapper.selectPermById(sysUser.getId(), StatusEnum.YES.getValue());
        }


        Set<String> set = Sets.newHashSet();
        for(String perm : sources){
            if(!StrUtil.isBlank(perm)){
                if(perm.indexOf(Global.PERM_DELIMITER) > -1){
                    String[] tempArr = perm.split(Global.PERM_DELIMITER);
                    for (String temp : tempArr) {
                        set.add(temp);
                    }
                }else {
                    set.add(perm);
                }
            }
        }
        // 把权限码设置到缓存中
//        redisKit.set(Global.REDIS_PERMS_PREFIX + sysUser.getId(), set, Global.REDIS_EXPIRE_TIME);
        return set;
    }

    /**
     * TODO 根据用户ID查询菜单
     *
     * @return
     */
    @Override
    public List<MenuVo> selectMenusForUser() {

        ShiroUser sysUser = ShiroKit.loginInfo();

        List<MenuVo> target = new ArrayList<>();//(List<MenuVo>) redisKit.get(Global.REDIS_MENUS_PREFIX + sysUser.getId());
//        if (target!= null && !target.isEmpty()) {
//            return target;
//        }

        List<MenuVo> sources = null;

        // 若是超级管理员 则有所有菜单
        if(StrUtil.equalsIgnoreCase(SysUserTypeEnum.SUPER.getValue(), sysUser.getUserType())){
            sources = baseMapper.selectMenusForSuperAdmin(StatusEnum.YES.getValue(), Lists.newArrayList(SysMenuTypeEnum.DIRECTORY.getValue(), SysMenuTypeEnum.PAGE.getValue()));
        }else{
            sources = baseMapper.selectMenusForUser(sysUser.getId(), StatusEnum.YES.getValue(),
                    Lists.newArrayList(SysMenuTypeEnum.DIRECTORY.getValue(), SysMenuTypeEnum.PAGE.getValue()));
        }

        target = Lists.newArrayList();
        for (MenuVo menuVo : sources) {
            if(menuVo.getPid().longValue() == Global.TOP_PID_ID.longValue()){
                convertMenus(sources, menuVo);
                target.add(menuVo);
            }
        }
        // 设置缓存
//        redisKit.set(Global.REDIS_MENUS_PREFIX + sysUser.getId(), target, Global.REDIS_EXPIRE_TIME);
        return target;
    }

    private void convertMenus(List<MenuVo> sources, MenuVo menu) {
        List<MenuVo> childs = Lists.newArrayList();
        for (MenuVo menuVo : sources) {
            if(menuVo.getPid().longValue() == menu.getId().longValue()){
                childs.add(menuVo);
            }
        }
        menu.setChildren(childs);
    }

    /**
     * TODO 根据用户ID查询 用户角色
     *
     * @param id
     * @return
     */
    @Override
    public List<UserRolesVO> selectRolesForUser(Long id) {
        return baseMapper.selectUserRoles(id, StatusEnum.YES.getValue());
    }

    /**
     * TODO 删除用户信息
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void deleteUser(Long id) throws ServiceException{
        SysUser sysUser = baseMapper.selectById(id);
        if(SysUserTypeEnum.SUPER.getValue().equalsIgnoreCase(sysUser.getUserType())){
            throw new ServiceException("超级管理员不能删除");
        }
        try {
            baseMapper.updateDelStatus(id, StatusEnum.NO.getValue());
//            redisKit.del(Global.REDIS_PERMS_PREFIX + id, Global.REDIS_MENUS_PREFIX + id);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * TODO 添加系统用户
     *
     * @param sysUser
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void insertUser(SysUser sysUser, List<Long> ids) throws ServiceException {
        try {
            sysUser.setLoginPwd(PasswordKit.entrypt(Global.DEFAULT_PWD));
            sysUser.setUserType(SysUserTypeEnum.ORDINARY.getValue());
            int count = baseMapper.insert(sysUser);
            if(count != 1){
                throw new ServiceException("系统用户添加异常");
            }
            baseMapper.batchInsertUserRoles(sysUser.getId(), ids);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * TODO 修改系统用户
     *
     * @param sysUser
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)

    @Override
    public void updateUser(Long ids) throws ServiceException {
        userMapper.updateSysUser(ids);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void updateUserPhone( SysUser sysUser) {

        this.updateById(sysUser);
        //修改手机号对应的普通用户的私人特助
        if(!StringUtils.isEmpty(sysUser.getPhone())){
            userMapper.updateAgentNoByPhone("A00000",sysUser.getPhone(),"0");
        }
    }

    @Override
    public SysUser selectByLoginNameSplit(String loginName) {
        return baseMapper.selectByLoginNameSplit(loginName);
    }
}

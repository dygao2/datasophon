/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.datasophon.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.enums.Status;
import com.datasophon.api.security.UserPermission;
import com.datasophon.api.utils.SecurityUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.EncryptionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.datasophon.dao.entity.UserInfoEntity;
import com.datasophon.api.service.UserInfoService;
import com.datasophon.common.utils.Result;


/**
 * 用户信息表
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-03-15 17:36:08
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("api/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 列表带分页
     */
    @ApiOperation(value = "列表带分页")
    @PostMapping("/list")
    public Result list(String username,Integer page,Integer pageSize) {
        return userInfoService.getUserListByPage(username,page,pageSize);
    }

    /**
     * 查询所有用户
     */
    @ApiOperation(value = "查询所有用户")
    @PostMapping("/all")
    public Result all() {
        List<UserInfoEntity> list = userInfoService.lambdaQuery().eq(UserInfoEntity::getId, 1).list();
        return Result.success(list);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        UserInfoEntity userInfo = userInfoService.getById(id);

        return Result.success().put(Constants.DATA, userInfo);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    @UserPermission
    public Result save(@RequestBody UserInfoEntity userInfo) {

        return userInfoService.createUser(userInfo);
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    @UserPermission
    public Result update(@RequestBody UserInfoEntity userInfo) {
        //用户名判重
        List<UserInfoEntity> list = userInfoService.list(new QueryWrapper<UserInfoEntity>().eq(Constants.USERNAME, userInfo.getUsername()));
        if (Objects.nonNull(list) && list.size() >= 1) {
            UserInfoEntity userInfoEntity = list.get(0);
            if(!userInfoEntity.getId().equals(userInfo.getId())){
                return Result.error(Status.USER_NAME_EXIST.getCode(), Status.USER_NAME_EXIST.getMsg());
            }
        }
        String password = userInfo.getPassword();
        userInfo.setPassword(EncryptionUtils.getMd5(password));
        userInfoService.updateById(userInfo);

        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    @UserPermission
    public Result delete(@RequestBody Integer[] ids) {
//        UserInfoEntity authUser = SecurityUtils.getAuthUser();
//        if (!SecurityUtils.isAdmin(authUser)) {
//            return Result.error(Status.USER_NO_OPERATION_PERM.getCode(), Status.USER_NO_OPERATION_PERM.getMsg());
//        }
        if(SecurityUtils.getAuthUser().getId() != 1 ){
            return Result.error(Status.USER_NO_OPERATION_PERM.getMsg());
        }
        userInfoService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}

package com.example.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Author:Niu
 * Date:2025/2/5 15:36
 * Description: nothing.
 */
public class UserDetail extends User {

    private UserDO userDO;

    public UserDetail(UserDO userDO,Collection<? extends GrantedAuthority> authorities) {
        super(userDO.getName(), userDO.getPassword(),authorities);
        this.userDO = userDO;
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }
}

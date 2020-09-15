package ru.erasko.forIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class UserInfoIterator implements Iterator<UserInfo.UserInfoPart> {

    private final UserInfo userInfo;
    private int userInfoPartsCount;

    public UserInfoIterator(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.userInfoPartsCount = userInfo.getPartCount();
    }

    @Override
    public boolean hasNext() {
        if (userInfoPartsCount == 4) {
            return userInfo.hasUserId() || userInfo.hasUserName() || userInfo.hasUserRole() || userInfo.hasUserAccount();
        } else if (userInfoPartsCount == 3) {
            return userInfo.hasUserId() || userInfo.hasUserName() || userInfo.hasUserRole();
        } else if (userInfoPartsCount == 2) {
            return userInfo.hasUserId() || userInfo.hasUserName();
        } else if (userInfoPartsCount == 1) {
            return userInfo.hasUserId();
        }
        return false;
    }

    @Override
    public UserInfo.UserInfoPart next() throws NoSuchElementException {
        if (userInfoPartsCount <= 0) {
            throw new NoSuchElementException("No more elements in this word!");
        }

        try {
            if (userInfoPartsCount == 4) {
                return userInfo.getUserAccount();
            }
            if (userInfoPartsCount == 3) {
                return userInfo.getUserRole();
            }
            if (userInfoPartsCount == 2) {
                return userInfo.getUserName();
            }
            return userInfo.getUserId();
        } finally {
            userInfoPartsCount--;
        }
    }
}

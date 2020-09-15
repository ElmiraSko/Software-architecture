package ru.erasko.forIterator;

import java.util.Iterator;

public class UserInfo implements Iterable<UserInfo.UserInfoPart>{
    private final UserId userId;
    private UserName userName;
    private UserRole userRole;
    private UserAccount userAccount;
    private final int partCount;

    public UserInfo(UserId userId) {
        this.userId = userId;
        partCount = 1;
    }

    public UserInfo(UserId userId, UserName userName) {
        this.userId = userId;
        this.userName = userName;
        partCount = 2;
    }

    public UserInfo(UserId userId, UserName userName, UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        partCount = 3;
    }

    public UserInfo(UserId userId, UserName userName, UserRole userRole, UserAccount userAccount) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.userAccount = userAccount;
        partCount = 4;
    }

    public UserId getUserId() {
        return userId;
    }

    public UserName getUserName() {
        return userName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public int getPartCount() {
        return partCount;
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasUserName() {
        return userId != null;
    }

    public boolean hasUserRole() {
        return userId != null;
    }

    public boolean hasUserAccount() {
        return userId != null;
    }

    @Override
    public Iterator<UserInfoPart> iterator() {
        return new UserInfoIterator(this);
    }

    //====================================================
    public interface UserInfoPart {

    String getPartInformationAboutUser();

    }

    public static class UserId implements UserInfoPart {

        private final String part;

        public UserId(String part) {
            this.part = part;
        }

        @Override
        public String getPartInformationAboutUser() {
            return part;
        }
    }

    public static class UserName implements UserInfoPart {

        private final String part;

        public UserName(String part) {
            this.part = part;
        }

        @Override
        public String getPartInformationAboutUser() {
            return part;
        }
    }

    public static class UserRole implements UserInfoPart {

        private final String part;

        public UserRole(String part) {
            this.part = part;
        }

        @Override
        public String getPartInformationAboutUser() {
            return part;
        }
    }

    public static class UserAccount implements UserInfoPart {
        private final String part;

        public UserAccount (String part) {
            this.part = part;
        }

        @Override
        public String getPartInformationAboutUser() {
            return part;
        }
    }




}

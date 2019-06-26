package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 8/1/18.
 */

public class userObject implements Serializable {
    public String ActiveStatus;
    public String userID;
    public   String inviteKey;
    public String userFullName;
    public String userProfilePic;
    public int notedPostsNumber;
    public long userRating;
    public long userAllowedAccessDate;
    public String userAccessLevel;
    public String userNecessaryAlertType;
    public String userNecessaryAlertType2;
    public String userUniverstiyVerificationEmail;
    public long userLastAccountResetDate;
    public long userLastAccountResetDateInverse;
    public int userStrikes;
    public String userName;
    public boolean showUserStrikeAlert;
/*
"ActiveStatus":"ACTIVE" as AnyObject,
                                "userID":Auth.auth().currentUser?.uid as AnyObject,
            "inviteKey": autoKomKey as AnyObject,
            "userFullName":Auth.auth().currentUser?.displayName as AnyObject,
            "userProfilePic":photoUrl as AnyObject,
            "notedPostsNumber": 0 as AnyObject,
                                "userRating": 0 as AnyObject,
                                "userAllowedAccessDate": 0 as AnyObject,
                                "userAccessLevel":"NORMAL" as AnyObject,
                                "userNecessaryAlertType":"none" as AnyObject,
                                "userNecessaryAlertType2":"none" as AnyObject,
                                "userUniverstiyVerificationEmail":"none" as AnyObject,
                                "userLastAccountResetDate": 0 as AnyObject,
                                "userLastAccountResetDateInverse":0 as AnyObject,
                                "userStrikes":0 as AnyObject,
                                "showUserStrikeAlert":false as AnyObject

*/
    public userObject() {
    }




    public userObject(String AActiveStatus, String UserID,
                      String InviteKey, String UserFullName, String UserProfilePic, int NotedPostsNumber
    ,long UserRating, long UserAllowedAccessDate, String UserAccessLevel, String UserNecessaryAlertType,
                      String UserNecessaryAlertType2, String UserUniverstiyVerificationEmail,
                      long UserLastAccountResetDate, long UserLastAccountResetDateInverse ,
                      int UserStrikes, boolean ShowUserStrikeAlert) {
        this.ActiveStatus = AActiveStatus;
        this.userID = UserID;
        this.inviteKey = InviteKey;
        this.userFullName = UserFullName;
        this.userProfilePic = UserProfilePic;
        this.notedPostsNumber = NotedPostsNumber;
        this.userRating = UserRating;
        this.userAllowedAccessDate = UserAllowedAccessDate;
        this.userAccessLevel = UserAccessLevel;
        this.userNecessaryAlertType = UserNecessaryAlertType;
        this.userNecessaryAlertType2 = UserNecessaryAlertType2;
        this.userUniverstiyVerificationEmail = UserUniverstiyVerificationEmail;
        this.userLastAccountResetDate = UserLastAccountResetDate ;
        this.userLastAccountResetDateInverse = UserLastAccountResetDateInverse;
        this.userStrikes = UserStrikes;
        this.showUserStrikeAlert = ShowUserStrikeAlert;



    }

    public userObject(String AActiveStatus, String UserID,
                      String InviteKey, String UserFullName, String UserProfilePic, int NotedPostsNumber
            ,long UserRating, long UserAllowedAccessDate, String UserAccessLevel, String UserNecessaryAlertType,
                      String UserNecessaryAlertType2, String UserUniverstiyVerificationEmail,
                      long UserLastAccountResetDate, long UserLastAccountResetDateInverse ,
                      int UserStrikes, String Username, boolean ShowUserStrikeAlert) {
        this.ActiveStatus = AActiveStatus;
        this.userID = UserID;
        this.inviteKey = InviteKey;
        this.userFullName = UserFullName;
        this.userProfilePic = UserProfilePic;
        this.notedPostsNumber = NotedPostsNumber;
        this.userRating = UserRating;
        this.userAllowedAccessDate = UserAllowedAccessDate;
        this.userAccessLevel = UserAccessLevel;
        this.userNecessaryAlertType = UserNecessaryAlertType;
        this.userNecessaryAlertType2 = UserNecessaryAlertType2;
        this.userUniverstiyVerificationEmail = UserUniverstiyVerificationEmail;
        this.userLastAccountResetDate = UserLastAccountResetDate ;
        this.userLastAccountResetDateInverse = UserLastAccountResetDateInverse;
        this.userStrikes = UserStrikes;
        this.userName = Username;

        this.showUserStrikeAlert = ShowUserStrikeAlert;


    }
}

package ru.shiler.likeit.model.user;

import ru.shiler.likeit.database.dao.Identified;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class User implements Identified<Integer> {

    private int id;
    private String userName;
    private String fullName;
    private String email;
    private String password;
    private Timestamp createTime;
    private UserRole userRole;
    private int status;
    private int age;
    private int answerAmount;

    public int getAnswerAmount() {
        return answerAmount;
    }

    public void setAnswerAmount(int answerAmount) {
        this.answerAmount = answerAmount;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", userRole=" + userRole +
                ", status=" + status +
                ", age=" + age +
                '}';
    }
}

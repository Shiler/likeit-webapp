package ru.shiler.likeit.model;

import ru.shiler.likeit.database.dao.Identified;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class User implements Identified<Integer> {

    private int id;
    private String username;
    private String email;
    private String password;
    private String createTime;
    private String updateTime;
    private int role;
    private int status;
    private int age;
    private String hobbies;
    private int avatar;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", age=" + age +
                ", hobbies='" + hobbies + '\'' +
                ", avatar=" + avatar +
                ", name='" + name + '\'' +
                '}';
    }
}

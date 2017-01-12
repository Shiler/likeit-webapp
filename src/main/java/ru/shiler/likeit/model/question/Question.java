package ru.shiler.likeit.model.question;

import ru.shiler.likeit.database.dao.Identified;
import ru.shiler.likeit.model.user.User;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class Question implements Identified<Integer> {

    private int id;
    private QuestionType questionType;
    private User creator;
    private int status;
    private String title;
    private String content;
    private Date createTime;
    private Timestamp updateTime;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}

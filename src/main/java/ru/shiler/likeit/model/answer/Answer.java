package ru.shiler.likeit.model.answer;

import ru.shiler.likeit.database.dao.Identified;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;

import java.util.Date;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class Answer implements Identified<Integer> {

    private int id;
    private User creator;
    private Question question;
    private Date createTime;
    private String text;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", creator=" + creator +
                ", question=" + question +
                ", createTime=" + createTime +
                ", text='" + text + '\'' +
                '}';
    }
}

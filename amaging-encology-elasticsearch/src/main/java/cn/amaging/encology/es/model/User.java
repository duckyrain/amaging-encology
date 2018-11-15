package cn.amaging.encology.es.model;

import cn.amaging.encology.es.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * Created by DuQiyu on 2018/6/19.
 */
@Document(indexName = "*", type = "doc", createIndex = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class User extends BaseModel implements Serializable {

    private String userName;

    private int sex;

    private int age;

    private String idCard;

    private String phoneNumber;

    private String mail;

    private String salaryCard;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSalaryCard() {
        return salaryCard;
    }

    public void setSalaryCard(String salaryCard) {
        this.salaryCard = salaryCard;
    }
}

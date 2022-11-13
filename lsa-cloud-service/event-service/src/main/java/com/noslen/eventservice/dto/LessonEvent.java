package com.noslen.eventservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "lesson_event")
public class LessonEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studentId;
    private LocalDateTime date;
    private String comments;

    public LessonEvent() {
    }

    public LessonEvent(String studentId, LocalDateTime date, String comments) {
        this.studentId = studentId;
        this.date = date;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonEvent that = (LessonEvent) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(studentId, that.studentId)) return false;
        if (!Objects.equals(date, that.date)) return false;
        return Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LessonEvent{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                '}';
    }
}

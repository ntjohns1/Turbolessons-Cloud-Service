package com.noslen.eventservice.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "lesson_event")
public class LessonEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String student;
    private String teacher;
    private LocalDateTime date;
    private String comments;

    public LessonEvent() {
    }

    public LessonEvent(Integer id, String student, String teacher, LocalDateTime date, String comments) {
        this.id = id;
        this.student = student;
        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
    }

    public LessonEvent(String student, String teacher, LocalDateTime date, String comments) {
        this.student = student;
        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
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
        if (!Objects.equals(student, that.student)) return false;
        if (!Objects.equals(teacher, that.teacher)) return false;
        if (!Objects.equals(date, that.date)) return false;
        return Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (student != null ? student.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LessonEvent{" +
                "id=" + id +
                ",x student='" + student + '\'' +
                ", teacher='" + teacher + '\'' +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                '}';
    }
}

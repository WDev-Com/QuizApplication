package com.alien.security.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignment_model")
public class StudentAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    
    @ManyToMany
    private List<UserModel> student = new ArrayList<>();
   
    @ManyToMany
    @JoinTable(
            name = "student_assignment_quiz", // Specify the join table name
            joinColumns = @JoinColumn(name = "student_assignment_id"), // Column name for StudentAssignment
            inverseJoinColumns = @JoinColumn(name = "quiz_id") // Column name for Quiz
        )
    private List<Quiz> quiz = new ArrayList<>();  

	private int marks;

	private boolean submit;
	@Override
	public String toString() {
	    return "StudentAssignment [id=" + id + ", quiz=" + quiz + ", marks=" + marks + "]";
	}
}

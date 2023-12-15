package com.alien.security.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.alien.security.entity.StudentAssignment;
import com.alien.security.model.AssignmentResponse;
import com.alien.security.model.QuestionWrapper;



public interface StudAssigementRepo extends CrudRepository<StudentAssignment, Long> {
 
    @Query(value = "SELECT * FROM assignment_model asm WHERE asm.studentID = :studentID", nativeQuery = true)
    Optional<StudentAssignment> findBystudentID(@Param("studentID") Long studentID);
    
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE assignment_model " +
                   "SET marks = :newMarks, submit = true " +
                   "WHERE assignment_model.id = (" +
                   "    SELECT subquery.id " +
                   "    FROM (" +
                   "        SELECT am.id " +
                   "        FROM assignment_model am " +
                   "        JOIN assignment_model_student ams ON am.id = ams.student_assignment_id " +
                   "        WHERE ams.student_id = :studentId " +
                   "        AND ams.student_assignment_id = :assignmentId " +
                   "    ) AS subquery" +
                   ")", nativeQuery = true)
    void updateMarksAndSubmitted(@Param("newMarks") int newMarks,
                                 @Param("studentId") long studentId,
                                 @Param("assignmentId") long assignmentId);

    @Query(value = "SELECT submit FROM assignment_model WHERE id = :assignmentId", nativeQuery = true)
    boolean findSubmittedByAssignmentId(@Param("assignmentId") Long assignmentId);

//
//  Complete Query    
//    @Query(value = "SELECT qd.id AS question_id, qd.category, qd.difficulty, qd.option1, qd.option2, qd.option3, qd.option4, qd.right_answer, qd.question_title " +
//            "FROM student_assignment_quiz saq " +
//            "JOIN quiz_model_questions qmq ON saq.quiz_id = qmq.quiz_id " +
//            "JOIN quizdata qd ON qmq.questions_id = qd.id " +
//            "WHERE saq.student_assignment_id = :studentAssignmentId", nativeQuery = true)
    
    @Query(value = "SELECT qd.id AS question_id, qd.option1, qd.option2, qd.option3, qd.option4, qd.question_title " +
          "FROM student_assignment_quiz saq " +
          "JOIN quiz_model_questions qmq ON saq.quiz_id = qmq.quiz_id " +
          "JOIN quizdata qd ON qmq.questions_id = qd.id " +
          "WHERE saq.student_assignment_id = :studentAssignmentId", nativeQuery = true)
    List<Object[]> findQuestionsByStudentAssignmentId(@Param("studentAssignmentId") Long studentAssignmentId);

    default List<QuestionWrapper> findQuestionResponsesByStudentAssignmentId(Long studentAssignmentId) {
        List<Object[]> result = findQuestionsByStudentAssignmentId(studentAssignmentId);
        return mapToQuestionResponses(result);
    }

    private List<QuestionWrapper> mapToQuestionResponses(List<Object[]> result) {
        List<QuestionWrapper> questionResponses = new ArrayList<>();

        for (Object[] row : result) {
        	QuestionWrapper questionResponse = QuestionWrapper.builder()
                    .question_id((Integer) row[0])
                    .option1((String) row[1])
                    .option2((String) row[2])
                    .option3((String) row[3])
                    .option4((String) row[4])                
                    .questionTitle((String) row[5])
                    .build();
            questionResponses.add(questionResponse);
        }
        return questionResponses;
    }

    
    @Query(value = "SELECT ams.student_id, saq.quiz_id, qm.title, ams.student_assignment_id AS assignment_id, am.marks " +
            "FROM assignment_model_student ams " +
            "JOIN student_assignment_quiz saq ON ams.student_assignment_id = saq.student_assignment_id " +
            "JOIN quiz_model qm ON saq.quiz_id = qm.id " +
            "JOIN assignment_model am ON ams.student_assignment_id = am.id " +
            "WHERE ams.student_id = :studentId", nativeQuery = true)
    List<Object[]> findQuizzesByStudentId(@Param("studentId") int studentId);
   
    default List<AssignmentResponse> findAssignmentResponsesByStudentId(int studentId) {
        List<Object[]> result = findQuizzesByStudentId(studentId);
        return mapToAssignmentResponses(result);
    }

    private List<AssignmentResponse> mapToAssignmentResponses(List<Object[]> result) {
        List<AssignmentResponse> assignmentResponses = new ArrayList<>();

        for (Object[] row : result) {
            AssignmentResponse assignmentResponse = AssignmentResponse.builder()
                    .student_id((Integer) row[0])
                    .quiz_id((Integer) row[1])
                    .title((String) row[2])
                    .assignment_id((Long) row[3])
                    .marks((Integer) row[4])
                    .build();
            assignmentResponses.add(assignmentResponse);
        }
        return assignmentResponses;
    }
}

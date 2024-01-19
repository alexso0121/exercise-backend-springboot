package com.Acerise.System_api.Service;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import com.Acerise.System_api.Model.Exercise;
import com.Acerise.System_api.Model.Tags;
import com.Acerise.System_api.Repository.ExerciseRepository;
import com.Acerise.System_api.dto.User.Preference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

@ExtendWith(MockitoExtension.class)

class ExerciseServiceTest {

    @InjectMocks
    ExerciseService exerciseService;

    @Mock
    ExerciseRepository exerciseRepository;

//    @Test
//    void getAllByFilter() {
//        String subjects="ENGLISH,MATHS";
//        String grades="HIGH_PRIMARY";
//        String tags="test,hello";
//
//        List<Exercise> dumpExercise=List.of(
//                Exercise.builder().subject(Subject.ENGLISH).build(),
//                Exercise.builder().subject(Subject.BIOLOGY).build(),
//                Exercise.builder().grade(Grade.UNIVERSITY).build(),
//                Exercise.builder().grade(Grade.HIGH_PRIMARY).subject(Subject.BIOLOGY).build(),
//                Exercise.builder().tags(List.of(new Tags(null,null,null,"test1"))).build(),
//                Exercise.builder().subject(Subject.BIOLOGY).tags(List.of(new Tags(null,null,null,"hello"),new Tags(null,null,null,"sdf"))).build(),
//                Exercise.builder().subject(Subject.ENGLISH).tags(List.of(new Tags(null,null,null,"helulo"),new Tags(null,null,null,"sdf"))).build()
//        );
//
//        when(exerciseRepository.findAll(Sort.by(Sort.Direction.DESC,"upload_time"))).thenReturn(dumpExercise);
//        List<Exercise> result=exerciseService.GetAllByFilter(subjects,grades,tags).getBody();
//        assertEquals(5,result.size());
//
//
//    }

    @Test
    void _getRecommendExercise() {
        List<Exercise> dumpExercise=List.of(
                Exercise.builder().subject(Subject.combined).avg_rating(5).title("1").grade(List.of(Grade.S1,Grade.S2)).tags(List.of("DSE")).build(),
                Exercise.builder().subject(Subject.combined).avg_rating(4).title("2").grade(List.of(Grade.P1)).tags(List.of("DSE")).build(),
                Exercise.builder().subject(Subject.combined).avg_rating(4).title("3").grade(List.of(Grade.P1)).tags(List.of()).build(),
                Exercise.builder().subject(Subject.reading).avg_rating(3).title("4").grade(List.of(Grade.P1)).tags(List.of("DssSE","DSE")).build(),
                Exercise.builder().subject(Subject.reading).avg_rating(3).title("5").grade(List.of(Grade.P1)).tags(List.of("DssSE","DSE")).build(),
                Exercise.builder().subject(Subject.reading).avg_rating(3).title("6").grade(List.of(Grade.P1)).tags(List.of("DssSE")).build(),
                Exercise.builder().subject(Subject.reading).avg_rating(3).title("6").grade(List.of()).tags(List.of()).build(),
                Exercise.builder().subject(Subject.listening).avg_rating(3).title("6").grade(List.of()).tags(List.of()).build(),
                Exercise.builder().subject(Subject.listening).avg_rating(3).title("6").grade(List.of()).tags(List.of()).build(),
                Exercise.builder().subject(Subject.listening).avg_rating(3).title("10").grade(List.of()).tags(List.of()).build(),
                Exercise.builder().subject(Subject.listening).avg_rating(3).title("6").grade(List.of()).tags(List.of()).build(),
                Exercise.builder().subject(Subject.listening).avg_rating(3).title("6").grade(List.of()).tags(List.of()).build()







        );


        Preference preference1=Preference.builder().prefer_grade(List.of(Grade.P1)).prefer_subj(List.of(Subject.combined)).prefer_tag(List.of("DSE")).build();
        Sort sort=Sort.by(Sort.Direction.DESC,"avg_rating");
        when(exerciseRepository.findAll(sort)).thenReturn(dumpExercise);
        List<Exercise> result=exerciseService._getRecommendExercise(preference1);
        assertEquals(6,result.size());

        Preference preference2=Preference.builder().prefer_grade(List.of(Grade.S1)).prefer_subj(List.of(Subject.combined)).prefer_tag(List.of("DSE")).build();
        List<Exercise> result2=exerciseService._getRecommendExercise(preference2);
        assertEquals(5,result2.size());

        Preference preference3=Preference.builder().prefer_grade(List.of(Grade.S1)).prefer_subj(List.of()).prefer_tag(List.of()).build();
        List<Exercise> result3=exerciseService._getRecommendExercise(preference3);
        assertEquals(10,result3.size());

        Preference preference4=Preference.builder().prefer_subj(List.of(Subject.combined,Subject.reading,Subject.listening)).prefer_grade(List.of()).prefer_tag(List.of()).build();
        List<Exercise> result4=exerciseService._getRecommendExercise(preference4);
        assertEquals(10,result4.size());
        assertEquals("10",result4.get(9).getTitle());


    }
}
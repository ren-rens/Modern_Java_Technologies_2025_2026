package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public abstract sealed class  AbstractSemesterPlanner implements SemesterPlannerAPI permits SoftwareEngineeringSemesterPlanner, ComputerScienceSemesterPlanner {
    
    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) throws IllegalArgumentException, DisappointmentException {
        if (subjects == null || subjects.length == 0) {
            throw new IllegalArgumentException("if the subjects are missing or null!");
        }

        if (maximumSlackTime < 0 || semesterDuration < 0) {
            throw new IllegalArgumentException("The maximumSlackTime/semesterDuration are not positive integers");
        }

        int jarsCount = 0;

        int neededBreakTime = 0;
        int studyTime = 0;

        for (UniversitySubject sub : subjects) {
            if (sub == null) {
                break; // we could have a null because of how i implement function calculateSubjectList()
            }

            double coef = switch (sub.category()) {
                case MATH -> 0.2;
                case PROGRAMMING -> 0.1;
                case THEORY -> 0.15;
                case PRACTICAL -> 0.05;
            };

            int neededStudyTime = sub.neededStudyTime();
            neededBreakTime += Math.round(coef * neededStudyTime);
            studyTime += neededStudyTime;
        }

        if (maximumSlackTime < neededBreakTime) {
            throw new DisappointmentException("Grandma is disappointed");
        }

        jarsCount = (studyTime / 5);

        if (semesterDuration < neededBreakTime + studyTime) {
            jarsCount *= 2;
        }

        return jarsCount;
    }
    
}

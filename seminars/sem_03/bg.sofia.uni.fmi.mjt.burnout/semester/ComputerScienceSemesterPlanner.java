package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public final class ComputerScienceSemesterPlanner extends AbstractSemesterPlanner{
    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) throws IllegalArgumentException, DisappointmentException {
        return super.calculateJarCount(subjects, maximumSlackTime, semesterDuration);
    }

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws CryToStudentsDepartmentException, IllegalArgumentException{
        if (semesterPlan == null) {
            throw new IllegalArgumentException("Semester plan is null!");
        }

        UniversitySubject[] subjects = semesterPlan.subjects();
        if (subjects == null) {
            throw new IllegalArgumentException("UniversitySubjects array is null!");
        }

        sortByRating(subjects); // the reference is copied but the place where it points changes

        int minimalAmountOfCredits = semesterPlan.minimalAmountOfCredits();
        UniversitySubject[] currentSubjects = new UniversitySubject[semesterPlan.subjects().length];

        int size = coverSubjects(minimalAmountOfCredits, subjects, currentSubjects); // the referance is copied but the value is changed and we need the number of covered subjects so we return it

        UniversitySubject[] res = new UniversitySubject[size];
        System.arraycopy(currentSubjects, 0, res, 0, size);

        return res;
    }

    private void sortByRating(UniversitySubject[] subjects) {
        int size = subjects.length;
        boolean swapped = false;

        for (int i = 0; i < size - 1; i++) {
            swapped = false;

            for (int j = 0; j < size - i - 1; j++) {
                if (subjects[j].rating() < subjects[j + 1].rating()) {
                    // swap
                    UniversitySubject temp = subjects[j];
                    subjects[j] = subjects[j + 1];
                    subjects[j + 1] = temp;

                    swapped = true;
                }
            }

            // if no swaps were made, the array is sorted
            if (!swapped) {
                break;
            }
        }
    }

    private int coverSubjects(int minimalAmountOfCredits, UniversitySubject[] subjects, UniversitySubject[] currentSubjects) throws CryToStudentsDepartmentException{
        int idx = 0;
        int currCredits = 0;

        for (UniversitySubject sub : subjects) {
            if (currCredits >= minimalAmountOfCredits) {
                break;
            }

            currCredits += sub.credits();
            currentSubjects[idx++] = sub;
        }

        if (currCredits < minimalAmountOfCredits) {
            throw new CryToStudentsDepartmentException("Computer science student cannot cover their semester credits!");
        }

        return idx;
    }
}

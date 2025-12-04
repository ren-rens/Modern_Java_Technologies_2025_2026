package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public final class SoftwareEngineeringSemesterPlanner extends AbstractSemesterPlanner {

    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) {
        return super.calculateJarCount(subjects, maximumSlackTime, semesterDuration);
    }

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan)
        throws InvalidSubjectRequirementsException {
        validateSemesterPlan(semesterPlan);

        UniversitySubject[] subjects = semesterPlan.subjects();
        sortForSE(subjects);

        SubjectRequirement[] requirements = semesterPlan.subjectRequirements();
        validateRequirements(requirements);

        UniversitySubject[] currentSubjects = new UniversitySubject[semesterPlan.subjects().length];
        int size = coverSubjects(subjects, semesterPlan, currentSubjects);

        return trimToSize(currentSubjects, size);
    }

    private void validateSemesterPlan(SemesterPlan semesterPlan) {
        if (semesterPlan == null || semesterPlan.subjects() == null || semesterPlan.subjects().length == 0) {
            throw new IllegalArgumentException("Semester plan is null!");
        }
    }

    private void sortForSE(UniversitySubject[] subjects) {
        int size = subjects.length;
        sortByCategories(subjects, size);
        sortByCreditsWithinCategories(subjects, size);
    }

    private void sortByCategories(UniversitySubject[] subjects, int size) {
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < size - i - 1; j++) {
                if (subjects[j + 1].category().compareTo(subjects[j].category()) < 0) {
                    swap(subjects, j, j + 1);
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    private void sortByCreditsWithinCategories(UniversitySubject[] subjects, int size) {
        int startIdx = 0;

        for (int j = 0; j < size - 1; j++) {
            if (subjects[j].category().compareTo(subjects[j + 1].category()) != 0) {
                sortByCredits(subjects, startIdx, j);
                startIdx = j + 1;
            }
        }

        sortByCredits(subjects, startIdx, size - 1);
    }

    private void sortByCredits(UniversitySubject[] subjects, int startIdx, int endIdx) {
        for (int i = startIdx; i <= endIdx; i++) {
            boolean swapped = false;
            int innerLimit = startIdx + (endIdx - startIdx) - (i - startIdx);

            for (int j = startIdx; j < innerLimit; j++) {
                if (subjects[j].credits() < subjects[j + 1].credits()) {
                    swap(subjects, j, j + 1);
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    private void swap(UniversitySubject[] subjects, int i, int j) {
        UniversitySubject temp = subjects[i];
        subjects[i] = subjects[j];
        subjects[j] = temp;
    }

    private void validateRequirements(SubjectRequirement[] requirements)
        throws InvalidSubjectRequirementsException {
        if (requirements == null) {
            throw new InvalidSubjectRequirementsException("The subjectRequirements is null");
        }

        checkForDuplicateCategories(requirements);
    }

    private void checkForDuplicateCategories(SubjectRequirement[] requirements)
        throws InvalidSubjectRequirementsException {
        boolean[] seen = new boolean[Category.values().length];
        for (SubjectRequirement req : requirements) {
            if (seen[req.category().getIdx()]) {
                throw new InvalidSubjectRequirementsException(
                    "The subjectRequirements contain duplicate categories");
            }
            seen[req.category().getIdx()] = true;
        }
    }

    private int coverSubjects(UniversitySubject[] subjects, SemesterPlan semesterPlan,
                              UniversitySubject[] currentSubjects) throws CryToStudentsDepartmentException {
        int[] coveredSubjectsCount = new int[Category.values().length];
        int currentCredits = 0;
        int idx = 0;

        for (UniversitySubject sub : subjects) {
            if (shouldAddSubjectForRequirement(sub, semesterPlan.subjectRequirements(), coveredSubjectsCount)) {
                idx = addSubject(currentSubjects, idx, sub);
                currentCredits += sub.credits();
                coveredSubjectsCount[sub.category().getIdx()]++;
            }
        }

        boolean allRequirementsCovered = areAllRequirementsCovered(
            semesterPlan.subjectRequirements(), coveredSubjectsCount);

        if (currentCredits < semesterPlan.minimalAmountOfCredits()) {
            currentCredits = addSubjectsForCredits(subjects, currentSubjects, idx, currentCredits,
                semesterPlan.minimalAmountOfCredits());
            idx = countAddedSubjects(currentSubjects);
        }

        if (!allRequirementsCovered || currentCredits < semesterPlan.minimalAmountOfCredits()) {
            throw new CryToStudentsDepartmentException(
                "Software engineering student cannot cover their semester credits!");
        }

        return idx;
    }

    private boolean shouldAddSubjectForRequirement(UniversitySubject subject,
                                                   SubjectRequirement[] requirements,
                                                   int[] coveredSubjectsCount) {
        for (SubjectRequirement req : requirements) {
            if (req.category().equals(subject.category())) {
                int coveredCount = coveredSubjectsCount[subject.category().getIdx()];
                return coveredCount < req.minAmountEnrolled();
            }
        }

        return false;
    }

    private int addSubject(UniversitySubject[] subjects, int idx, UniversitySubject subject) {
        subjects[idx] = subject;
        return idx + 1;
    }

    private boolean areAllRequirementsCovered(SubjectRequirement[] requirements,
                                              int[] coveredSubjectsCount) {
        for (SubjectRequirement req : requirements) {
            if (coveredSubjectsCount[req.category().getIdx()] < req.minAmountEnrolled()) {
                return false;
            }
        }
        return true;
    }

    private int addSubjectsForCredits(UniversitySubject[] subjects,
                                      UniversitySubject[] currentSubjects,
                                      int currentIdx, int currentCredits, int minimalCredits) {
        UniversitySubject[] sortedSubjects = subjects.clone();
        sortByCredits(sortedSubjects, 0, sortedSubjects.length - 1);

        for (UniversitySubject sub : sortedSubjects) {
            if (currentCredits >= minimalCredits) {
                break;
            }

            if (!isSubjectAlreadyAdded(sub, currentSubjects, currentIdx)) {
                currentSubjects[currentIdx++] = sub;
                currentCredits += sub.credits();
            }
        }

        return currentCredits;
    }

    private boolean isSubjectAlreadyAdded(UniversitySubject subject,
                                          UniversitySubject[] addedSubjects, int count) {
        for (int i = 0; i < count; i++) {
            if (addedSubjects[i] == subject) {
                return true;
            }
        }
        return false;
    }

    private int countAddedSubjects(UniversitySubject[] subjects) {
        int count = 0;
        for (UniversitySubject subject : subjects) {
            if (subject != null) {
                count++;
            }
        }

        return count;
    }

    private UniversitySubject[] trimToSize(UniversitySubject[] subjects, int size) {
        UniversitySubject[] result = new UniversitySubject[size];
        for (int i = 0; i < size; i++) {
            result[i] = subjects[i];
        }

        return result;
    }

}

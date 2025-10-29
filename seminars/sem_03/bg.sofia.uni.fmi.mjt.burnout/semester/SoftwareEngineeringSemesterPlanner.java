package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public final class SoftwareEngineeringSemesterPlanner extends AbstractSemesterPlanner{
    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) throws IllegalArgumentException, DisappointmentException {
        return super.calculateJarCount(subjects, maximumSlackTime, semesterDuration);
    }

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException, CryToStudentsDepartmentException {
        if (semesterPlan == null || semesterPlan.subjects() == null || semesterPlan.subjects().length == 0) {
            throw new IllegalArgumentException("Semester plan is null!");
        }

        UniversitySubject[] subjects = semesterPlan.subjects();

        sortForSE(subjects);

        SubjectRequirement[] req = semesterPlan.subjectRequirements();
        if (req == null || req.length > Category.values().length) {
            throw new InvalidSubjectRequirementsException("The subjectRequirements is null or contain duplicate categories");
        }

        int minimalAmountOfCredits = semesterPlan.minimalAmountOfCredits();
        int currentCredits = 0;
        int[] coveredSubjectsCredits = new int[req.length];
        boolean subjectsCreditsAreCovered = false;

        UniversitySubject[] currentSubjects = new UniversitySubject[semesterPlan.subjects().length];
        int idx = 0;

        for (UniversitySubject sub : subjects) {
            if (currentCredits >= minimalAmountOfCredits && subjectsCreditsAreCovered) {
                break;
            }

            for (SubjectRequirement r : req) {
                if (r.category().equals(sub.category())) {
                    // same category
                    int coveredSubjectsCredit = coveredSubjectsCredits[r.category().getIdx()];
                    int minAmountEnrolled = r.minAmountEnrolled();

                    if (minAmountEnrolled <= coveredSubjectsCredit) {
                        break;
                    }

                    coveredSubjectsCredit += minAmountEnrolled;
                    coveredSubjectsCredits[r.category().getIdx()] = coveredSubjectsCredit;
                    currentSubjects[idx++] = sub;

                    currentCredits += sub.credits();
                }
            }

            subjectsCreditsAreCovered = true;
            for (int coveredSubjectsCredit : coveredSubjectsCredits) {
                if (coveredSubjectsCredit == 0) {
                    subjectsCreditsAreCovered = false;
                    break;
                }
            }
        }

        if (currentCredits < minimalAmountOfCredits) {
            // should pass by all subjects again and sign more
            sortByCredits(subjects, 0, subjects.length);

            for (UniversitySubject sub : subjects) {
                if (currentCredits >= minimalAmountOfCredits) {
                    break;
                }

                boolean inResult = false;
                for (UniversitySubject subject : currentSubjects) {
                    if (sub == subject) {
                        inResult = true;
                        break;
                    }
                }

                if (inResult) {
                    continue;
                }

                currentCredits += sub.credits();
                currentSubjects[idx++] = sub;
            }
        }

        if (!subjectsCreditsAreCovered || currentCredits < minimalAmountOfCredits) {
            throw new CryToStudentsDepartmentException("Software engineering student cannot cover their semester credits!");
        }

        UniversitySubject[] res = new UniversitySubject[idx];
        System.arraycopy(currentSubjects, 0, res, 0, idx);

        return res;
        //return currentSubjects;
    }

    private void sortForSE(UniversitySubject[] subjects) {
        int size = subjects.length;

        sortByCategories(subjects, size);

        // are sorted by categories now should by credits
        sortByCreditsStartAndEndIdx(subjects, size);
    }

    private void sortByCategories(UniversitySubject[] subjects, int size) {
        boolean swapped = false;

        for (int i = 0; i < size - 1; i++) {
            swapped = false;

            for (int j = 0; j < size - i - 1; j++) {
                int shouldSwap = subjects[j + 1].category().compareTo(subjects[j].category());
                if (shouldSwap < 0) {
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

    private void sortByCreditsStartAndEndIdx(UniversitySubject[] subjects, int size) {
        int startIdx = 0;
        int endIdx = 0;

        for (int j = 0; j < size - 1; j++) {
            int shouldSwap = subjects[j + 1].category().compareTo(subjects[j].category());
            if (shouldSwap == 0) {
                // same category
                endIdx = j;
            }
            else {
                // we have reached a place where we end one category and start another one
                // should sort by credits now from start index until end index

                sortByCredits(subjects, startIdx, endIdx);
                startIdx = j + 1;
                endIdx = startIdx;
            }
        }
    }

    private void sortByCredits(UniversitySubject[] subjects, int startIdx, int endIdx) {
        boolean swapped = false;

        for (int i = startIdx; i < endIdx - 1; i++) {
            swapped = false;

            for (int j = startIdx; j < endIdx - i - 1; j++) {
                if (subjects[j].credits() < subjects[j + 1].credits()) {
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
}

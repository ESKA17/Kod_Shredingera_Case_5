package com.example.mycli.utils;

import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.model.SubjectType;

import java.util.ArrayList;
import java.util.List;

import static com.example.mycli.model.SubjectType.*;
import static com.example.mycli.model.SubjectType.HISTORY;

public class Utils {
    public static List<Integer> fromSubjectTypeToInteger (List<SubjectType> subjectTypeList) {
        List<Integer> out = new ArrayList<>();
        for (SubjectType in: subjectTypeList) {
            switch (in) {
                case MATH: {
                    out.add(0);
                    break;
                }
                case PHYSICS: {
                    out.add(1);
                    break;
                }
                case CHEMISTRY: {
                    out.add(2);
                    break;
                }
                case BIOLOGY: {
                    out.add(3);
                    break;
                }
                case INFORMATICS: {
                    out.add(4);
                    break;
                }
                case HISTORY: {
                    out.add(5);
                    break;
                }
                default:
                    throw new AccountBadRequest("major with id " + in + " does not match any subject");
            }
        }
        return out;
    }

    public static List<SubjectType> fromIntegerToSubjectType (List<Integer> intList,
                                                              List<SubjectType> subjectList) {
        for (int in: intList) {
            switch (in) {
                case 0: {
                    if (!subjectList.contains(MATH)) {subjectList.add(MATH);}
                    break;
                }
                case 1: {
                    if (!subjectList.contains(PHYSICS)) {subjectList.add(PHYSICS);}
                    break;
                }
                case 2: {
                    if (!subjectList.contains(CHEMISTRY)) {subjectList.add(CHEMISTRY);}
                    break;
                }
                case 3: {
                    if (!subjectList.contains(BIOLOGY)) {subjectList.add(BIOLOGY);}
                    break;
                }
                case 4: {
                    if (!subjectList.contains(INFORMATICS)) {subjectList.add(INFORMATICS);}
                    break;
                }
                case 5: {
                    if (!subjectList.contains(HISTORY)) {subjectList.add(HISTORY);}
                    break;
                }
                default: throw new AccountBadRequest("major with id " + in + " does not match any subject");
            }
        }
        return subjectList;
    }
    public static List<SubjectType> fromIntToSubjectType(List<Integer> subjects) {
        List<SubjectType> subjectList = new ArrayList<>();
        for (int digit: subjects) {
            switch (digit) {
                case 0: {
                    subjectList.add(SubjectType.MATH);
                    break;
                }
                case 1: {
                    subjectList.add(SubjectType.PHYSICS);
                    break;
                }
                case 2: {
                    subjectList.add(SubjectType.CHEMISTRY);
                    break;
                }
                case 3: {
                    subjectList.add(SubjectType.BIOLOGY);
                    break;
                }
                case 4: {
                    subjectList.add(SubjectType.INFORMATICS);
                    break;
                }
                case 5: {
                    subjectList.add(SubjectType.HISTORY);
                    break;
                }
            }
        }
        return subjectList;
    }

}

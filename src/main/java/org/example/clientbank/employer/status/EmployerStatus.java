package org.example.clientbank.employer.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmployerStatus {
    SUCCESS("Success operation."),
    EMPLOYER_NOT_FOUND("Employer not found."),
    NOTHING_TO_UPDATE("No changes detected for the employer."),
    EMPLOYER_UPDATED("Employer was successfully updated."),
    DELETED("Employer was successfully deleted."),
    UNEXPECTED("An unexpected error occurred.");

    private final String message;
}

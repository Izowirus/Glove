package pl.performace_tests.actor.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FailureClassificationMessage {
    public TestClassificatorMessage testClassificatorMessage;
    public String failureCause;
}

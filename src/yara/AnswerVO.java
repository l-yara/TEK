package yara;

import java.io.Serializable;
import java.util.Objects;

/**
 * Answer VO (Value Object) - a pair of <Integer, Boolean> which represent answer from {@linkplain Prime} service.
 */
public class AnswerVO implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int value;
    private final boolean isPrime;

    public AnswerVO(int value, boolean isPrime) {
        this.value = value;
        this.isPrime = isPrime;
    }

    public int getValue() {
        return value;
    }

    public boolean isPrime() {
        return isPrime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerVO)) return false;
        AnswerVO answerVO = (AnswerVO) o;
        return value == answerVO.value &&
                isPrime == answerVO.isPrime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isPrime);
    }

    @Override
    public String toString() {
        return "AnswerVO{" +
                "value " + value +
                " is " + (isPrime ? "" : "not ") + "prime";
    }
}

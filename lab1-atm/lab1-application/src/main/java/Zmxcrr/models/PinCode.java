package Zmxcrr.models;

import java.security.SecureRandom;
import java.util.Objects;

public class PinCode {
    private final String pin;
    private static final int pinLength = 4;

    public PinCode(String pin) {
        if (!isValid(pin)) {
            throw new IllegalArgumentException("PIN must be exactly " + pinLength + " digits.");
        }
        this.pin = pin;
    }
    private static boolean isValid(String pin) {
        return pin != null && pin.matches("\\d{" + pinLength + "}");
    }

    public static PinCode generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder pinBuilder = new StringBuilder();
        for (int i = 0; i < pinLength; i++) {
            pinBuilder.append(random.nextInt(10));
        }
        return new PinCode(pinBuilder.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PinCode pinCode = (PinCode) o;

        return Objects.equals(pin, pinCode.pin);
    }
}

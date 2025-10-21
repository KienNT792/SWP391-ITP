package fpt.swp.springmvctt.itp.validation;

import fpt.swp.springmvctt.itp.dto.request.UserCreationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserCreationRequest> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserCreationRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        if (password == null && confirmPassword == null) {
            return true;
        }

        boolean isValid = password != null && password.equals(confirmPassword);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password and confirm password do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}

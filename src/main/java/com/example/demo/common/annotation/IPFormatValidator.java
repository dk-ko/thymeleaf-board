package com.example.demo.common.annotation;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// TODO 작성해봤는데 동작하지 않아서 우선 보류
//@Component
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class IPFormatValidator implements ConstraintValidator<IPFormat, String> {
    @Override
    public boolean isValid(String inputIP, ConstraintValidatorContext constraintValidatorContext) {
        if (inputIP == null) return false;

        final String IP_ADDRESS_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(inputIP);
        return matcher.matches();
    }
}

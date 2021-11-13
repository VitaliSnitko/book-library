package com.itechart.library.servlet.validator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public class ReaderValidatorTest {

    public static final String VALID_EMAIL_1 = "abc@gmail.com";
    public static final String VALID_EMAIL_2 = "def@mail.ru";
    public static final String VALID_NAME_1 = "abc";
    public static final String VALID_NAME_2 = "def";
    public static final String VALID_NAME_3 = "aaa";
    public static final String EMAIL_PARAMETER = "email";
    public static final String NAME_PARAMETER = "name";
    @Mock
    private HttpServletRequest req;

    public ReaderValidatorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isValid() {
        //given
        String[] emails = {VALID_EMAIL_1, VALID_EMAIL_2};
        String[] names = {VALID_NAME_1, VALID_NAME_2};
        when(req.getParameterValues(EMAIL_PARAMETER)).thenReturn(emails);
        when(req.getParameterValues(NAME_PARAMETER)).thenReturn(names);

        //then
        Assert.assertTrue(new ReaderValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfEmailsAreNull() {
        //given
        String[] emails = null;
        String[] names = {VALID_NAME_1, VALID_NAME_2};
        when(req.getParameterValues(EMAIL_PARAMETER)).thenReturn(emails);
        when(req.getParameterValues(NAME_PARAMETER)).thenReturn(names);

        //then
        Assert.assertFalse(new ReaderValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfEmailsAndNamesHaveDifferentLength() {
        //given
        String[] emails = {VALID_EMAIL_1, VALID_EMAIL_2};
        String[] names = {VALID_NAME_1, VALID_NAME_2, VALID_NAME_3};
        when(req.getParameterValues(EMAIL_PARAMETER)).thenReturn(emails);
        when(req.getParameterValues(NAME_PARAMETER)).thenReturn(names);

        //then
        Assert.assertFalse(new ReaderValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfEmailsDontMatchPattern() {
        //given
        String[] emails = {" ", VALID_EMAIL_2};
        String[] names = {VALID_NAME_1, VALID_NAME_2};
        when(req.getParameterValues(EMAIL_PARAMETER)).thenReturn(emails);
        when(req.getParameterValues(NAME_PARAMETER)).thenReturn(names);

        //then
        Assert.assertFalse(new ReaderValidator().isValid(req));
    }
}
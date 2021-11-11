package com.itechart.library.servlet.validator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public class ReaderValidatorTest {

    @Mock
    private HttpServletRequest req;

    public ReaderValidatorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isValid() {
        //given
        String[] emails = {"abc@gmail.com", "def@mail.ru"};
        String[] names = {"abc", "def"};
        when(req.getParameterValues("email")).thenReturn(emails);
        when(req.getParameterValues("name")).thenReturn(names);

        //then
        Assert.assertTrue(new ReaderValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfEmailsAreNull() {
        //given
        String[] emails = null;
        String[] names = {"abc", "def"};
        when(req.getParameterValues("email")).thenReturn(emails);
        when(req.getParameterValues("name")).thenReturn(names);

        //then
        Assert.assertFalse(new ReaderValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfEmailsAndNamesHaveDifferentLength() {
        //given
        String[] emails = {"abc@gmail.com", "def@mail.ru"};
        String[] names = {"abc", "def", "aaa"};
        when(req.getParameterValues("email")).thenReturn(emails);
        when(req.getParameterValues("name")).thenReturn(names);

        //then
        Assert.assertFalse(new ReaderValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfEmailsDontMatchPattern() {
        //given
        String[] emails = {" ", "def@mail.ru"};
        String[] names = {"abc", "def"};
        when(req.getParameterValues("email")).thenReturn(emails);
        when(req.getParameterValues("name")).thenReturn(names);

        //then
        Assert.assertFalse(new ReaderValidator().isValid(req));
    }
}
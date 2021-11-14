package com.itechart.library.servlet.validator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public class BookFormValidatorTest {

    public static final String VALID_STRING_INPUT = "AAA";
    public static final String VALID_INT_INPUT = "123";
    public static final String INVALID_INT_INPUT = "AAA";

    @Mock
    private HttpServletRequest req;

    public BookFormValidatorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isValidPositiveCase() {
        //given
        when(req.getParameter("title")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("authors")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("genres")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("publisher")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("date")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("pageCount")).thenReturn(VALID_INT_INPUT);
        when(req.getParameter("ISBN")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("totalBookAmount")).thenReturn(VALID_INT_INPUT);

        //then
        Assert.assertTrue(new BookFormValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfStringCannotBeCastedToInt() {
        //given
        when(req.getParameter("title")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("authors")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("genres")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("publisher")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("date")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("pageCount")).thenReturn(INVALID_INT_INPUT);
        when(req.getParameter("ISBN")).thenReturn(VALID_STRING_INPUT);
        when(req.getParameter("totalBookAmount")).thenReturn(VALID_INT_INPUT);

        //then
        Assert.assertFalse(new BookFormValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfParameterNull() {
        //given
        when(req.getParameter("title")).thenReturn(null);

        //then
        Assert.assertFalse(new BookFormValidator().isValid(req));
    }
}
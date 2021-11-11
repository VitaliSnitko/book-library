package com.itechart.library.servlet.validator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public class BookFormValidatorTest {

    @Mock
    private HttpServletRequest req;

    public BookFormValidatorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isValidPositiveCase() {
        //given
        when(req.getParameter("title")).thenReturn("AAA");
        when(req.getParameter("authors")).thenReturn("AAA");
        when(req.getParameter("genres")).thenReturn("AAA");
        when(req.getParameter("publisher")).thenReturn("AAA");
        when(req.getParameter("date")).thenReturn("AAA");
        when(req.getParameter("pageCount")).thenReturn("123");
        when(req.getParameter("ISBN")).thenReturn("AAA");
        when(req.getParameter("totalBookAmount")).thenReturn("123");

        //then
        Assert.assertTrue(new BookFormValidator().isValid(req));
    }

    @Test
    public void isValidNegativeCaseIfStringCannotBeCastedToInt() {
        //given
        when(req.getParameter("title")).thenReturn("AAA");
        when(req.getParameter("authors")).thenReturn("AAA");
        when(req.getParameter("genres")).thenReturn("AAA");
        when(req.getParameter("publisher")).thenReturn("AAA");
        when(req.getParameter("date")).thenReturn("AAA");
        when(req.getParameter("pageCount")).thenReturn("AAA");
        when(req.getParameter("ISBN")).thenReturn("AAA");
        when(req.getParameter("totalBookAmount")).thenReturn("123");

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
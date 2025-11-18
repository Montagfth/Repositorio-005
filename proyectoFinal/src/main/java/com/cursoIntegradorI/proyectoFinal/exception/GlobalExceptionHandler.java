package com.cursoIntegradorI.proyectoFinal.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(NoHandlerFoundException ex) {
        ModelAndView mav = new ModelAndView("error/error-page");
        mav.addObject("errorCode", "404");
        mav.addObject("errorTitle", "Página No Encontrada");
        mav.addObject("errorMessage", "La página que buscas no existe o ha sido movida.");
        mav.addObject("errorIcon", "search_off");
        mav.addObject("errorUri", ex.getRequestURL());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/error-page");
        mav.addObject("errorCode", "500");
        mav.addObject("errorTitle", "Error Interno");
        mav.addObject("errorMessage", "Ha ocurrido un error inesperado. Por favor, intenta nuevamente.");
        mav.addObject("errorIcon", "error");
        mav.addObject("showDetails", false);
        return mav;
    }
}
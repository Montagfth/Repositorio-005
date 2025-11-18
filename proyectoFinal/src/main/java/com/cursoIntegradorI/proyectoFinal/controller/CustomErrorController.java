package com.cursoIntegradorI.proyectoFinal.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Obtener el código de estado del error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String errorTitle = "Error";
        String errorMessage = "Ha ocurrido un error inesperado";
        String errorCode = "500";
        String errorIcon = "error";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorCode = String.valueOf(statusCode);

            switch (statusCode) {
                case 400:
                    errorTitle = "Solicitud Incorrecta";
                    errorMessage = "La solicitud no pudo ser procesada debido a un error del cliente.";
                    errorIcon = "warning";
                    break;

                case 401:
                    errorTitle = "No Autorizado";
                    errorMessage = "Necesitas iniciar sesión para acceder a este recurso.";
                    errorIcon = "lock";
                    break;

                case 403:
                    errorTitle = "Acceso Denegado";
                    errorMessage = "No tienes permisos para acceder a este recurso.";
                    errorIcon = "block";
                    break;

                case 404:
                    errorTitle = "Página No Encontrada";
                    errorMessage = "La página que buscas no existe o ha sido movida.";
                    errorIcon = "search_off";
                    break;

                case 405:
                    errorTitle = "Método No Permitido";
                    errorMessage = "El método HTTP utilizado no está permitido para este recurso.";
                    errorIcon = "do_not_disturb";
                    break;

                case 500:
                    errorTitle = "Error Interno del Servidor";
                    errorMessage = "Algo salió mal en nuestro servidor. Estamos trabajando para solucionarlo.";
                    errorIcon = "error";
                    break;

                case 502:
                    errorTitle = "Bad Gateway";
                    errorMessage = "El servidor recibió una respuesta inválida.";
                    errorIcon = "cloud_off";
                    break;

                case 503:
                    errorTitle = "Servicio No Disponible";
                    errorMessage = "El servicio está temporalmente fuera de línea. Intenta nuevamente más tarde.";
                    errorIcon = "construction";
                    break;

                default:
                    errorTitle = "Error " + statusCode;
                    errorMessage = "Ha ocurrido un error inesperado.";
                    errorIcon = "error";
                    break;
            }
        }

        // Obtener información adicional del error
        String errorException = (String) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        String errorRequestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        // Agregar atributos al modelo
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorIcon", errorIcon);
        model.addAttribute("errorUri", errorRequestUri != null ? errorRequestUri : "Desconocido");
        model.addAttribute("showDetails", true); // Cambiar a true en desarrollo

        return "error/error-page";
    }
}
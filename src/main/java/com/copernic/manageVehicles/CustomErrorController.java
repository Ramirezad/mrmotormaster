/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collection;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;



@Controller
@ControllerAdvice
public class CustomErrorController implements ErrorController {
    
    private void addRolesToModel(Model model, Principal principal) {
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getAuthorities();
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
        model.addAttribute("isMecanico", isMecanico);
    }

    @ExceptionHandler({AccessDeniedException.class, NoHandlerFoundException.class})
    public String handleError(Model model, Exception ex, HttpServletResponse response, Principal principal) {
        // Obtener el nif del usuario actualmente autenticado
        String nif = principal.getName();
        model.addAttribute("nif", nif);

        // Añadir roles al modelo
        addRolesToModel(model, principal);

        if (ex instanceof NoHandlerFoundException) {
            // Si es un error 404, redirige a la página personalizada para el error 404
            return "error404";
        } else if (ex instanceof AccessDeniedException) {
            // Para errores de acceso denegado, muestra una página de error específica
            model.addAttribute("error", "No tienes permiso para acceder a este recurso");
            return "error";
        } else {
            // Para otros errores, muestra una página de error genérica
            model.addAttribute("error", "Se produjo un error inesperado");
            return "error";
        }
    }
    
     @RequestMapping("/error")
    public String handleError(Model model, Principal principal) {
    
    // Obtener el nif del usuario actualmente autenticado
    String nif = principal.getName();
    model.addAttribute("nif", nif);

    // Añadir roles al modelo
    addRolesToModel(model, principal);
    
    return "error"; // Redirige a error404.html en caso de error 404
    }
}


    
    
    
   
    
    



    

    


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import EJB.UserFacadeLocal;
import jadaxi.Email;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.User;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author jadaxi
 */
@Named
@ViewScoped
public class RegisterConroler implements Serializable {

    private User user;
    @EJB
    private UserFacadeLocal userEJB;

    private boolean skip;

    @PostConstruct
    public void inicio() {
        try {

            user = new User();

        } catch (Exception e) {
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private boolean checkUserName(String user) {

        List<User> users = null;
        try {
            users = userEJB.findAll();
        } catch (Exception e) {
            System.out.println("Fallo al traer los nombres de usuario: " + e.getMessage());
        }

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserName().equals(user)) {
                System.out.println("Se repiten " + user + " y " + users.get(i).getUserName());
                return false;
            }

        }

        return true;

    }

    private String genCode() {

        String alphabet = "1234567890";
        StringBuilder password = new StringBuilder();

        int i = 0;
        while (i < 6) {
            int rand = (int) ((Math.random() * ((10 - 0))));// Con el random generamos la contraseña sacando los
            // careacteres del array alphabet
            char char2 = (char) rand;
            System.out.println("cahr: " + char2);
            password.append(alphabet.charAt(char2));

            i++;
        }

        return password.toString();
    }

    public void insertUser() {

        if (checkUserName(user.getUserName())) {

            //user.setAcCode(genCode());
            user.setAcCode("000112");
            System.out.println("asdasd: " + user.getAcCode());
            try {

                userEJB.create(user);

            } catch (Exception e) {
                System.out.println("Error al insertar el usuario: " + e.getMessage());
            }
            Email email = new Email(user.getEmail(), "Confirmación de cuenta", "Introduce el sigueinte codigo en la pagina: \n " + user.getAcCode());
            try {
                email.send();
            } catch (IOException ex) {
                System.out.println("Error al enviar el email al usuario: " + ex.getMessage());
            }
        }

    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }
}

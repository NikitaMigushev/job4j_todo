package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        model = mock(Model.class);
    }

    @Test
    void testGetLoginPage() {
        String viewName = userController.getLoginPage();
        assertThat(viewName).isEqualTo("users/login");
    }

    @Test
    void testLoginUserValidCredentials() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        Model model = mock(Model.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        String viewName = userController.loginUser(user, model, request);
        verify(model, never()).addAttribute(eq("error"), anyString());
        verify(session, times(1)).setAttribute("user", user);
        assertThat(viewName).isEqualTo("redirect:/index");
    }

    @Test
    void testLoginUserInvalidCredentials() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        Model model = mock(Model.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());
        String viewName = userController.loginUser(user, model, request);
        verify(model, times(1)).addAttribute("error", "Почта или пароль введены неверно");
        verify(session, never()).setAttribute(eq("user"), any());
        assertThat(viewName).isEqualTo("users/login");
    }

    @Test
    void testLogout() {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        String viewName = userController.logout(session);
        verify(session, times(1)).invalidate();
        assertThat(viewName).isEqualTo("redirect:/users/login");
    }

    @Test
    void testGetRegistrationPage() {
        ModelAndView modelAndView = userController.getRegistrationPage(model);
        assertThat(modelAndView.getViewName()).isEqualTo("users/register");
    }

    @Test
    void testRegisterValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        Model model = mock(Model.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(userService.save(user)).thenReturn(Optional.of(user));
        String viewName = userController.register(model, user, request);
        verify(model, never()).addAttribute(eq("message"), anyString());
        assertThat(viewName).isEqualTo("redirect:/index");
    }

    @Test
    void testRegisterDuplicateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        Model model = mock(Model.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(userService.save(user)).thenReturn(Optional.empty());
        String viewName = userController.register(model, user, request);
        verify(model, times(1)).addAttribute("message", "Пользователь с такой почтой уже существует");
        assertThat(viewName).isEqualTo("users/register");
    }

}
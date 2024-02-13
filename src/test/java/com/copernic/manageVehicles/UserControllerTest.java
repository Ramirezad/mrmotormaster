package com.copernic.manageVehicles;
import com.copernic.manageVehicles.UserController;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.services.UserServiceImpl;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

@Test
public void testUserUpdate() throws Exception {
    User user = new User();
    user.setNif(user.getNif());

    when(userService.save(user)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/updateUser/update")
            .flashAttr("user", user))
            .andExpect(status().isOk())
            .andExpect(view().name("user-details"));
}

@Test
public void testShowRegistrationForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/signin"))
            .andExpect(status().isOk())
            .andExpect(view().name("signin"));
}

@Test
public void testSubmitUser() throws Exception {
    User user = new User();
    user.setNif(user.getNif());

    when(userService.save(user)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/signin")
            .flashAttr("user", user))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/login"));
}

@Test
public void testListUsers() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andExpect(status().isOk())
            .andExpect(view().name("user-list"));
}

@Test
public void testDelete() throws Exception {
    User user = new User();
    user.setNif(user.getNif());

    doNothing().when(userService).deleteById(user.getNif());

    mockMvc.perform(MockMvcRequestBuilders.get("/deleteUser/" + user.getNif()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/users"));
}
}
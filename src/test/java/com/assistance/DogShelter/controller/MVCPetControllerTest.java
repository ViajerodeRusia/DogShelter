package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.controller.dto.PetDto;
;
import com.assistance.DogShelter.db.repository.PetRepository;
import com.assistance.DogShelter.mapper.PetMapper;
import com.assistance.DogShelter.service.PetService;
import jakarta.ws.rs.core.MediaType;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class MVCPetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private PetMapper petMapper;
    @MockBean
    private PetService petService;
    @Autowired
    private PetController petController;

    @Test
    public void getPetByIdTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;
        String food = "Сухой корм";

        PetDto expectedPet = new PetDto();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setBreed(breed);
        expectedPet.setAge(age);
        expectedPet.setFood(food);

        when(petService.findPetById(id)).thenReturn(Optional.of(expectedPet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value(breed))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age))
                .andExpect(MockMvcResultMatchers.jsonPath("$.food").value(food));
    }

    @Test
    void getAllPetsTest() throws Exception {
        // Создаем и сохраняем питомцев в сервисе или базе данных
        List<PetDto> pets = Arrays.asList(
                new PetDto(1, "Рекс", "Лабрадор", 3, "Сухой корм", null, LocalDate.now()),
                new PetDto(2, "Вискас", "Немецкая овчарка", 2, "Сухой корм", null, LocalDate.now())
        );
        when(petService.getAllPets()).thenReturn(pets);

        mockMvc.perform(get("/pets/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))); // Проверяем, что вернулась коллекция из двух питомцев
    }

    @Test
    public void createPetTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;
        String food = "Сухой корм";

        JSONObject petObject = new JSONObject();
        petObject.put("name", name);
        petObject.put("breed", breed);
        petObject.put("age", age);
        petObject.put("food", food);

        PetDto newPet = new PetDto();
        newPet.setId(id);
        newPet.setName(name);
        newPet.setBreed(breed);
        newPet.setAge(age);
        newPet.setFood(food);

        when(petService.addPet(any(PetDto.class))).thenReturn(newPet);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pets/add")
                        .content(petObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value(breed))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age))
                .andExpect(MockMvcResultMatchers.jsonPath("$.food").value(food));
    }

    @Test
    public void deletePetTest() throws Exception {
        Long id = 1L;

        // Настройка моков
        PetDto mockPet = new PetDto();
        mockPet.setId(id);
        when(petService.findPetById(id)).thenReturn(Optional.of(mockPet));
        doNothing().when(petService).deletePet(id);

        // Вызов и проверка
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findPetByIdTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;

        PetDto expectedPet = new PetDto();
        expectedPet.setId(id);
        expectedPet.setName(name);
        expectedPet.setBreed(breed);
        expectedPet.setAge(age);

        when(petService.findPetById(id)).thenReturn(Optional.of(expectedPet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value(breed))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test
    public void editPetTest() throws Exception {
        Long id = 1L;
        String name = "Дружок";
        String breed = "Лабрадор";
        int age = 3;
        String food = "Сухой корм";

        JSONObject petObject = new JSONObject();
        petObject.put("name", name);
        petObject.put("breed", breed);
        petObject.put("age", age);
        petObject.put("food", food);

        PetDto updatedPet = new PetDto();
        updatedPet.setId(id);
        updatedPet.setName(name);
        updatedPet.setBreed(breed);
        updatedPet.setAge(age);
        updatedPet.setFood(food);

        when(petService.findPetById(id)).thenReturn(Optional.of(updatedPet));
        when(petService.editPet(any(PetDto.class))).thenReturn(updatedPet);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pets/{id}", id)
                        .content(petObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value(breed))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age))
                .andExpect(MockMvcResultMatchers.jsonPath("$.food").value(food));
    }
}
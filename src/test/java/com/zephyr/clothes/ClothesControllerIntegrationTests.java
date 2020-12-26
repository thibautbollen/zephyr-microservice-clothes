package com.zephyr.clothes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.clothes.model.Clothes;
import com.zephyr.clothes.repository.ClothesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClothesControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClothesRepository clothesRepository;

    private Clothes testClothes = new Clothes("Pijamama", "Fuchsia", "XXL", "Unisex", "RLX", 99.99, "Avondkledij");
    private Clothes testClothes1 = new Clothes("Shirtie", "White", "XL", "M", "Bateau", 29.99, "Shirt");
    private Clothes testClothes2 = new Clothes("RegenJas", "Black", "L", "F", "RJS", 39.99, "Jacket");
    private Clothes testdelete = new Clothes("Frakske", "Orange", "M", "F", "LMFAO",29.00,"Jacket");

    @BeforeEach
    public void beforeAllTests() {
        clothesRepository.deleteAll();
        clothesRepository.save(testClothes);
        clothesRepository.save(testClothes1);
        clothesRepository.save(testClothes2);
        clothesRepository.save(testdelete);
    }

    @AfterEach
    public void afterAllTests() {
        clothesRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenClothes_whenGetClothesByName_thenReturnJsonReviews() throws Exception {


        mockMvc.perform(get("/clothes/{name}", "Shirtie"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color",is("White")))
                .andExpect(jsonPath("$.size",is("XL")))
                .andExpect(jsonPath("$.gender",is("M")))
                .andExpect(jsonPath("$.brand",is("Bateau")))
                .andExpect(jsonPath("$.price",is(29.99)))
                .andExpect(jsonPath("$.type",is("Shirt")));

    }

    @Test
    public void givenClothes_whenGetClothesByUUID_thenReturnJsonReview() throws Exception {
        String uuid = testClothes1.getUuid();

        mockMvc.perform(get("/clothes/{uuid}",uuid))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Shirtie")))
                .andExpect(jsonPath("$.color",is("White")))
                .andExpect(jsonPath("$.size",is("XL")))
                .andExpect(jsonPath("$.gender",is("M")))
                .andExpect(jsonPath("$.brand",is("Bateau")))
                .andExpect(jsonPath("$.price",is(29.99)))
                .andExpect(jsonPath("$.type",is("Shirt")));
    }

    @Test
    public void whenPostClothes_thenReturnJsonReview() throws Exception {
        Clothes testClothes4 = new Clothes("Broek", "Blue", "S", "F", "off-white", 129.99, "Jeans");

        mockMvc.perform(post("/clothes")
                .content(mapper.writeValueAsString(testClothes4))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Broek")))
                .andExpect(jsonPath("$.color",is("Blue")))
                .andExpect(jsonPath("$.size",is("S")))
                .andExpect(jsonPath("$.gender",is("F")))
                .andExpect(jsonPath("$.brand",is("off-white")))
                .andExpect(jsonPath("$.price",is(129.99)))
                .andExpect(jsonPath("$.type",is("Jeans")));   }

    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception {

        Clothes updatedClothes =  new Clothes("RegenJas", "Black", "L", "F", "RJS", 59.99,"Jacket");
        String uuid = testClothes.getUuid();
        mockMvc.perform(put("/clothes/{uuid}", uuid)
                .content(mapper.writeValueAsString(updatedClothes))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("RegenJas")))
                .andExpect(jsonPath("$.color",is("Black")))
                .andExpect(jsonPath("$.size",is("L")))
                .andExpect(jsonPath("$.gender",is("F")))
                .andExpect(jsonPath("$.brand",is("RJS")))
                .andExpect(jsonPath("$.price",is(59.99)))
                .andExpect(jsonPath("$.type",is("Jacket")));
    }




    @Test
    public void givenNoClothes_whenDeleteClothes_thenStatusNotFound() throws Exception {
        String uuid = testdelete.getUuid();
        mockMvc.perform(delete("/users/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}
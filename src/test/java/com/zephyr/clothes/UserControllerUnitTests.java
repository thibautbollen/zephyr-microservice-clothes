package com.zephyr.clothes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.clothes.model.Clothes;
import com.zephyr.clothes.repository.ClothesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClothesRepository clothesRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenallClothes_thenReturnJsonReview() throws Exception {
        Clothes testClothes = new Clothes("Pijamama", "Fuchsia", "XXL", "Unisex", "RLX", 99.99, "Avondkledij");
        Clothes testClothes1 = new Clothes("Shirtie", "White", "XL", "M", "Bateau", 29.99, "Shirt");

        List<Clothes> clothesList = new ArrayList<>();
        clothesList.add(testClothes);
        clothesList.add(testClothes1);
        given(clothesRepository.findAll()).willReturn(clothesList);

        mockMvc.perform(get("/clothes"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name",is("Pijamama")))
                .andExpect(jsonPath("$[0].color",is("Fuchsia")))
                .andExpect(jsonPath("$[0].size",is("XXL")))
                .andExpect(jsonPath("$[0].gender",is("Unisex")))
                .andExpect(jsonPath("$[0].brand",is("RLX")))
                .andExpect(jsonPath("$[0].price",is(99.99)))
                .andExpect(jsonPath("$[0].type",is("Avondkledij")));
    }

    @Test
    public void givenClothes_whenGetClohtesByName_thenReturnJsonReview() throws Exception {
        Clothes clothes1 = new Clothes("Frakske", "Orange", "M", "F", "LMFAO",29.00,"Jacket");

        given(clothesRepository.findFirstByName("Frakske")).willReturn(clothes1);

        mockMvc.perform(get("/clothes/name/{name}","Frakske"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Frakske")))
                .andExpect(jsonPath("$.color",is("Orange")))
                .andExpect(jsonPath("$.size",is("M")))
                .andExpect(jsonPath("$.gender",is("F")))
                .andExpect(jsonPath("$.brand",is("LMFAO")))
                .andExpect(jsonPath("$.price",is(29.00)))
                .andExpect(jsonPath("$.type",is("Jacket")));
    }

    @Test
    public void givenClothes_whenGetClothesByUUID_thenReturnJsonReview() throws Exception {
        Clothes clothes1 = new Clothes("Frakske", "Orange", "M", "F", "LMFAO",29.00,"Jacket");
        clothes1.setUuid("hdiefgw-472348-jhdwjhgwr");
        given(clothesRepository.findClothesByUuid("hdiefgw-472348-jhdwjhgwr")).willReturn(clothes1);

        mockMvc.perform(get("/clothes/uuid/{uuid}","hdiefgw-472348-jhdwjhgwr"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Frakske")))
                .andExpect(jsonPath("$.color",is("Orange")))
                .andExpect(jsonPath("$.size",is("M")))
                .andExpect(jsonPath("$.gender",is("F")))
                .andExpect(jsonPath("$.brand",is("LMFAO")))
                .andExpect(jsonPath("$.price",is(29.00)))
                .andExpect(jsonPath("$.type",is("Jacket")));
    }





    @Test
    public void whenPostClothes_thenReturnJsonReview() throws Exception{
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
                .andExpect(jsonPath("$.type",is("Jeans")));
    }

    @Test
    public void givenUser_whenPutUser_thenReturnJsonReview() throws Exception{
        Clothes testClothes1 = new Clothes("Shirtie", "White", "XXS", "M", "Bateau", 29.99, "Shirt");
        testClothes1.setUuid("hdiefgw-472348-jhdwjhgwr");
        given(clothesRepository.findClothesByUuid("hdiefgw-472348-jhdwjhgwr")).willReturn(testClothes1);

        Clothes testClothes2 = new Clothes("Shirtie", "White", "XL", "M", "Bateau", 29.99, "Shirt");

        mockMvc.perform(put("/clothes/uuid/{uuid}", "hdiefgw-472348-jhdwjhgwr")
                .content(mapper.writeValueAsString(testClothes2))
                .contentType(MediaType.APPLICATION_JSON))
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
    public void givenClothes_whenDeleteClothes_thenStatusOk() throws Exception{
        Clothes testClothes1 = new Clothes("Shirtie", "White", "XXS", "M", "Bateau", 29.99, "Shirt");
        testClothes1.setUuid("hdiefgw-472348-jhdwjhgwr");
        String uuid =  "hdiefgw-472348-jhdwjhgwr";
        given(clothesRepository.findClothesByUuid("hdiefgw-472348-jhdwjhgwr")).willReturn(testClothes1);
        mockMvc.perform(delete("/clothes/uuid/{uuid}",uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoUser_whenDeleteUser_thenStatusNotFound() throws Exception{
        given(clothesRepository.findClothesByUuid("abcdefg")).willReturn(null);

        mockMvc.perform(delete("/clothes/{uuid}","abcdefg")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}
package com.zephyr.clothes.controller;

import com.zephyr.clothes.model.Clothes;
import com.zephyr.clothes.repository.ClothesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class ClothesController {
    @Autowired
    private ClothesRepository clothesRepository;

    @PostConstruct
    public void fillDB() {
        Clothes testClothes = new Clothes("Pijamama", "Fuchsia", "XXL", "Unisex", "RLX", 99.99, "Avondkledij");
        Clothes testClothes1 = new Clothes("Shirtie", "White", "XL", "M", "Bateau", 29.99, "Shirt");
        Clothes testClothes2 = new Clothes("RegenJas", "Black", "L", "F", "RJS", 39.99, "Jacket");

        if (clothesRepository.count() == 0) {
            clothesRepository.save(testClothes);
            clothesRepository.save(testClothes1);
            clothesRepository.save(testClothes2);

        }
        System.out.println("Clothes test: " + clothesRepository.count());
    }

    @GetMapping("/clothes")
    public List<Clothes> get() {
        return clothesRepository.findAll();
    }

    @GetMapping("/clothes/uuid/{uuid}")
    public Clothes findOrder(@PathVariable String uuid) {
        return clothesRepository.findFirstByUuid(uuid);
    }

    @GetMapping("/clothes/name/{name}")
    public Clothes findOrderName(@PathVariable String name){return clothesRepository.findFirstByName(name);}

    @PostMapping("/clothes")
    public Clothes addClothes(@RequestBody Clothes clothes) {
        Clothes newClothes = new Clothes(clothes.getName(), clothes.getColor(), clothes.getSize(), clothes.getGender(), clothes.getBrand(), clothes.getPrice(), clothes.getType());
        clothesRepository.save(newClothes);
        return newClothes;
    }

    @PutMapping("/clothes/{uuid}")
    public Clothes updatePrice(@PathVariable String uuid, @RequestBody Clothes updateClothes) {
        Clothes clothesUpdate = clothesRepository.findFirstByUuid(uuid);
        updateClothes.setPrice(updateClothes.getPrice());
        updateClothes.setBrand(updateClothes.getBrand());
        updateClothes.setColor(updateClothes.getColor());
        updateClothes.setGender(updateClothes.getGender());
        updateClothes.setName(updateClothes.getName());
        updateClothes.setSize(updateClothes.getSize());
        clothesRepository.save(clothesUpdate);
        return clothesUpdate;
    }

    @DeleteMapping("/clothes/{uuid}")
    public ResponseEntity deleteReview(@PathVariable String uuid) {
        Clothes clothes = clothesRepository.findFirstByUuid(uuid);
        if (clothes != null) {
            clothesRepository.delete(clothes);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

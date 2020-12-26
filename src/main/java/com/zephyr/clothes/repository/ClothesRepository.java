package com.zephyr.clothes.repository;

import com.zephyr.clothes.model.Clothes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesRepository extends MongoRepository<Clothes, String> {
   // Clothes findFirstByUuid(String uuid);
    Clothes findFirstByName(String name);
    Clothes findClothesByUuid(String uuid);
}

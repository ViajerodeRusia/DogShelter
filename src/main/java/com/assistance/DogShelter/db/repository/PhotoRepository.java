package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository <Photo, Long> {
}

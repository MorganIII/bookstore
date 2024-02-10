package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section,String> {



    // i want to get all the sections names

    @Query("SELECT s.name FROM Section s")
    List<String> getAllSectionsNames();
}

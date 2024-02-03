package org.morgan.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.dto.section.SectionRequest;
import org.morgan.bookstore.entity.Section;
import org.morgan.bookstore.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;
    @PostMapping("/section")
    @ResponseStatus(HttpStatus.CREATED)
    public String addSection(@RequestBody @Valid SectionRequest request) {
        return sectionService.addSection(request);
    }

    @PutMapping("/section/{name}")
    @ResponseStatus(HttpStatus.OK)
    public SectionRequest updateSection(@PathVariable(value = "name") String sectionName,
                                            @RequestBody @Valid SectionRequest request) {
        return sectionService.updateSection(sectionName,request);
    }

    @DeleteMapping("/section/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable("name") String sectionName) {
        sectionService.deleteSection(sectionName);
    }

    @GetMapping("/section/{name}")
    public Section getSection(@PathVariable("name") String name) {
        return sectionService.getSection(name);
    }

    @GetMapping("/section")
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }

 }

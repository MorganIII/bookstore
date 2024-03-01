package org.morgan.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.SectionRequest;
import org.morgan.bookstore.model.Section;
import org.morgan.bookstore.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addSection(@RequestBody @Valid SectionRequest request) {
        return sectionService.addSection(request);
    }

    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public SectionRequest updateSection(@PathVariable(value = "name") String sectionName,
                                        @RequestBody @Valid SectionRequest request) {
        return sectionService.updateSection(sectionName,request);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable("name") String sectionName) {
        sectionService.deleteSection(sectionName);
    }

    @GetMapping("/{name}")
    public Section getSection(@PathVariable("name") String name) {
        return sectionService.getSection(name);
    }

    @GetMapping("/all")
    public List<String> getAllSections() {
        return sectionService.getAllSections();
    }



 }

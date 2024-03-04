package org.morgan.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.SectionRequest;
import org.morgan.bookstore.model.Section;
import org.morgan.bookstore.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Section", description = "Section API")
@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @Operation(summary = "Add a new section", description = "Add a new section to the bookstore")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Section is added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Section already exists")
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addSection(@RequestBody @Valid SectionRequest request) {
        return sectionService.addSection(request);
    }

    @Operation(summary = "Update a section", description = "Update a section in the bookstore")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Section is updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Section not found")
            }
    )
    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public SectionRequest updateSection(@PathVariable(value = "name") String sectionName,
                                        @RequestBody @Valid SectionRequest request) {
        return sectionService.updateSection(sectionName,request);
    }

    @Operation(summary = "Delete a section", description = "Delete a section from the bookstore")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Section is deleted"),
                    @ApiResponse(responseCode = "404", description = "Section not found")
            }
    )
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable("name") String sectionName) {
        sectionService.deleteSection(sectionName);
    }

    @Operation(summary = "Get a section", description = "Get a section from the bookstore")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Section is found"),
                    @ApiResponse(responseCode = "404", description = "Section not found")
            }
    )
    @GetMapping("/{name}")
    public Section getSection(@PathVariable("name") String name) {
        return sectionService.getSection(name);
    }

    @Operation(summary = "Get all sections", description = "Get all sections from the bookstore")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Sections are returned successfully"),
            }
    )
    @GetMapping("/all")
    public List<String> getAllSections() {
        return sectionService.getAllSections();
    }



 }

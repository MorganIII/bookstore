package org.morgan.bookstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.request.SectionRequest;
import org.morgan.bookstore.model.Section;
import org.morgan.bookstore.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;

    public String addSection(SectionRequest request) {
        String sectionName = request.getName();
        String sectionDesc = request.getDescription();
        validateSectionDuplicate(sectionName);
        Section section = Section.builder()
                .name(sectionName)
                .description(sectionDesc)
                .build();
        sectionRepository.save(section);
        log.info("A section with name {} is added", sectionName);
        return sectionName;
    }


    public SectionRequest updateSection(String sectionName, SectionRequest request) {
        Section section = getSection(sectionName);
        if (!sectionName.equals(request.getName())) {
            validateSectionDuplicate(request.getName());
        }
        section.setName(request.getName());
        section.setDescription(request.getDescription());
        sectionRepository.save(section);
        log.info("A section with name {} has been updated", sectionName);
        return request;
    }

    public void deleteSection(String sectionName) {
        Section section = getSection(sectionName);
        sectionRepository.deleteById(sectionName);
        log.info("A section with name {} has been deleted", sectionName);
    }

    public Section getSection(String sectionName) {
        Optional<Section> checkSection = sectionRepository.findById(sectionName);
        return checkSection.orElseThrow(()->new EntityNotFoundException(String.format("The section with name %s is not found", sectionName)));
    }

    private void validateSectionDuplicate(String request) {
        Optional<Section> isPresent = sectionRepository.findById(request);
        if (isPresent.isPresent()) {
            throw new EntityExistsException(String.format("A section with name %s is already exist", request));
        }
    }

    public List<String> getAllSections() {
        return sectionRepository.getAllSectionsNames();
    }


}

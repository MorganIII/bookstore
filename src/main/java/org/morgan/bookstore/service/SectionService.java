package org.morgan.bookstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.dto.section.SectionRequest;
import org.morgan.bookstore.entity.Section;
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
        Optional<Section> checkSection = sectionRepository.findById(sectionName);
        if(checkSection.isPresent()) {
            throw new EntityExistsException(String.format("Section with name '%s' is already in the system.", sectionName));
        }
        Section section = Section.builder()
                .name(sectionName)
                .description(sectionDesc)
                .build();
        sectionRepository.save(section);
        log.info("A section with name {} is added", sectionName);
        return sectionName;
    }


    public SectionRequest updateSection(String sectionName, SectionRequest request) {
        Optional<Section> checkSection = sectionRepository.findById(sectionName);
        if(checkSection.isPresent()) {
            if(!sectionName.equals(request.getName())) {
                Optional<Section> isPresent = sectionRepository.findById(request.getName());
                if(isPresent.isPresent()) {
                    throw new EntityExistsException(String.format("A section with name %s is already exist", request.getName()));
                }
            }
            Section section = checkSection.get();
            section.setName(request.getName());
            section.setDescription(request.getDescription());
            sectionRepository.save(section);
            log.info("a section with name {} has been updated", sectionName);
            return request;
        }
        throw new EntityNotFoundException(String.format("The section with name %s is not found", sectionName));
    }

    public void deleteSection(String sectionName) {
        Optional<Section> checkSection = sectionRepository.findById(sectionName);
        if(checkSection.isPresent()) {
            sectionRepository.deleteById(sectionName);
        }
        else {
            throw new EntityNotFoundException(String.format("The section with name %s is not found", sectionName));
        }
    }

    public Section getSection(String name) {
        Optional<Section> checkSection = sectionRepository.findById(name);
        return checkSection.orElseThrow(()->new EntityNotFoundException(String.format("The section with name %s is not found", name)));
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }
}

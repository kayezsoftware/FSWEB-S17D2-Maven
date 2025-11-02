package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private final Taxable taxable;
    private Map<Integer, Developer> developers;

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }


    public Map<Integer, Developer> getDevelopers() {
        return developers;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer createDeveloper(@RequestBody Developer developer) {
        double salary = developer.getSalary();

        switch (developer.getExperience()) {
            case JUNIOR -> salary -= salary * (taxable.getSimpleTaxRate() / 100.0);
            case MID    -> salary -= salary * (taxable.getMiddleTaxRate() / 100.0);
            case SENIOR -> salary -= salary * (taxable.getUpperTaxRate() / 100.0);
        }

        developer.setSalary(salary);

        Developer toStore = switch (developer.getExperience()) {
            case JUNIOR -> new JuniorDeveloper(developer.getId(), developer.getName(), developer.getSalary());
            case MID    -> new MidDeveloper(developer.getId(), developer.getName(), developer.getSalary());
            case SENIOR -> new SeniorDeveloper(developer.getId(), developer.getName(), developer.getSalary());
        };

        developers.put(toStore.getId(), toStore);
        return toStore;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer updated) {
        developers.put(id, updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public String deleteDeveloper(@PathVariable int id) {
        developers.remove(id);
        return "Developer with id " + id + " removed.";
    }
}
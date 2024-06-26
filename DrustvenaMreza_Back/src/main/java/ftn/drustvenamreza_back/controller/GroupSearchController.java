package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.indexrepository.GroupElasticsearch;
import ftn.drustvenamreza_back.service.implementation.GroupSearchServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups/search")
public class GroupSearchController {

    private final GroupSearchServiceImpl groupSearchService;

    public GroupSearchController(GroupSearchServiceImpl groupSearchService){
        this.groupSearchService = groupSearchService;
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<GroupElasticsearch>> searchByName(@RequestParam String name) {
        List<GroupElasticsearch> results = groupSearchService.searchByName(name);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/by-description")
    public ResponseEntity<List<GroupElasticsearch>> searchByDescription(@RequestParam String description) {
        List<GroupElasticsearch> results = groupSearchService.searchByDescription(description);
        return ResponseEntity.ok(results);
    }
}

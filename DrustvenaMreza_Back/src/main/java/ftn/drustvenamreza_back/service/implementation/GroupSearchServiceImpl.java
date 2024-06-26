package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.indexrepository.GroupElasticsearch;
import ftn.drustvenamreza_back.indexrepository.GroupElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class GroupSearchServiceImpl {
    private final GroupElasticsearchRepository groupElasticsearchRepository;

    public List<GroupElasticsearch> searchByName(String name) {
        return groupElasticsearchRepository.findByNameContainingIgnoreCase(name);
    }

    public List<GroupElasticsearch> searchByDescription(String description) {
        return groupElasticsearchRepository.findByDescriptionContainingIgnoreCase(description);
    }
}

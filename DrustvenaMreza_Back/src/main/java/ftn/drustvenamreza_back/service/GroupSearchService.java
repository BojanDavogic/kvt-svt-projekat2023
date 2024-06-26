package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.indexrepository.GroupElasticsearch;

import java.util.List;

public interface GroupSearchService {
    List<GroupElasticsearch> searchByName(String name);
    List<GroupElasticsearch> searchByDescription(String description);
}

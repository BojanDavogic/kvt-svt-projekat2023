package ftn.drustvenamreza_back.indexservice;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import ftn.drustvenamreza_back.exceptionhandling.exception.MalformedQueryException;
import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import ftn.drustvenamreza_back.indexmodel.PostIndex;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl {
    private final ElasticsearchOperations elasticsearchTemplate;

    public Page<PostIndex> simpleSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildSimpleSearchPostQuery(keywords)).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    public Page<GroupIndex> simpleGroupSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildSimpleSearchGroupQuery(keywords)).withPageable(pageable);
        return  runGroupQuery(searchQueryBuilder.build());
    }

    public Page<PostIndex> advancedSearch(List<String> expression, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression)).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    public Page<GroupIndex> advancedGroupSearch(List<String> expression, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression)).withPageable(pageable);
        return runGroupQuery(searchQueryBuilder.build());
    }

    private Query buildSimpleSearchPostQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(m -> m.field("title").analyzer("serbian").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("full_content").analyzer("serbian").query(token)));
                b.should(sb -> sb.match(m -> m.field("file_content").analyzer("serbian").query(token)));
                b.should(sb -> sb.match(m -> m.field("comment_content").analyzer("serbian").query(token)));

                try {
                    String[] range = token.split("-");
                    if (range.length == 2) {
                        Long from = Long.parseLong(range[0].trim());
                        Long to = Long.parseLong(range[1].trim());
                        b.should(sb -> sb.range(r -> r.field("number_of_likes").gte(JsonData.of(from)).lte(JsonData.of(to))));
                    } else {
                        Long numberOfLikes = Long.parseLong(token.trim());
                        b.should(sb -> sb.range(r -> r.field("number_of_likes").gte(JsonData.of(numberOfLikes))));
                    }
                } catch (NumberFormatException e) {
                }
            });
            return b;
        })))._toQuery();
    }

    private Query buildSimpleSearchGroupQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(m -> m.field("name").analyzer("serbian").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("description").analyzer("serbian").query(token)));
                b.should(sb -> sb.match(m -> m.field("file_content").analyzer("serbian").query(token)));
                b.should(sb -> sb.match(m -> m.field("rules").analyzer("serbian").query(token)));

                try {
                    String[] range = token.split("-");
                    if (range.length == 2) {
                        Long from = Long.parseLong(range[0].trim());
                        Long to = Long.parseLong(range[1].trim());
                        b.should(sb -> sb.range(r -> r.field("number_of_posts").gte(JsonData.of(from)).lte(JsonData.of(to))));
                    } else {
                        Long numberOfPosts = Long.parseLong(token.trim());
                        b.should(sb -> sb.range(r -> r.field("number_of_posts").gte(JsonData.of(numberOfPosts))));
                    }
                } catch (NumberFormatException e) {
                }

            });
            return b;
        })))._toQuery();
    }

    private Query buildAdvancedSearchQuery(List<String> operands) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            for (String query : operands) {
                var parts = query.split(":");
                var field = parts[0];
                var value = parts[1];
                var operator = parts[2];

                switch (operator.toUpperCase()) {
                    case "AND":
                        if (value.contains("-")) {
                            String[] range = value.split("-");
                            Long from = Long.parseLong(range[0].trim());
                            Long to = Long.parseLong(range[1].trim());
                            b.must(sb -> sb.range(r -> r.field(field).gte(JsonData.of(from)).lte(JsonData.of(to))));
                        } else {
                            b.must(sb -> sb.match(m -> m.field(field).fuzziness(Fuzziness.ONE.asString()).query(value)));
                            b.should(sb -> sb.matchPhrase(p -> p.field(field).slop(1).query(value)));
                        }
                        break;
                    case "OR":
                        if (value.contains("-")) {
                            String[] range = value.split("-");
                            Long from = Long.parseLong(range[0].trim());
                            Long to = Long.parseLong(range[1].trim());
                            b.should(sb -> sb.range(r -> r.field(field).gte(JsonData.of(from)).lte(JsonData.of(to))));
                        } else {
                            b.should(sb -> sb.match(m -> m.field(field).fuzziness(Fuzziness.ONE.asString()).query(value)));
                            b.should(sb -> sb.matchPhrase(p -> p.field(field).slop(1).query(value)));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + operator);
                }
            }
            return b;
        })))._toQuery();
    }


    private Page<PostIndex> runQuery(NativeQuery searchQuery) {
        SearchHits<PostIndex> searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class, IndexCoordinates.of("posts_index"));
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        return (Page<PostIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }

    private Page<GroupIndex> runGroupQuery(NativeQuery searchQuery) {
        SearchHits<GroupIndex> searchHits = elasticsearchTemplate.search(searchQuery, GroupIndex.class, IndexCoordinates.of("group_index"));
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        return (Page<GroupIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}

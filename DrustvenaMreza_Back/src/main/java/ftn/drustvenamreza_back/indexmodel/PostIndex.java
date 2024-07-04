package ftn.drustvenamreza_back.indexmodel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "posts_index")
public class PostIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text, store = true, name = "title", analyzer = "serbian")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "full_content", analyzer = "serbian")
    private String fullContent;

    @Field(type = FieldType.Text, store = true, name = "file_content", analyzer = "serbian")
    private String fileContent;

    @Field(type = FieldType.Long, store = true, name = "number_of_likes", analyzer = "serbian")
    private Long numberOfLikes;

    @Field(type = FieldType.Text, store = true, name = "comment_content", analyzer = "serbian")
    private String commentContent;
}



package ftn.drustvenamreza_back.indexservice;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexrepository.PostIndexRepository;
import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Reaction;
import ftn.drustvenamreza_back.repository.CommentRepository;
import ftn.drustvenamreza_back.repository.PostRepository;
import ftn.drustvenamreza_back.repository.ReactionRepository;
import ftn.drustvenamreza_back.service.implementation.MinioServiceImpl;
import ftn.drustvenamreza_back.service.implementation.ReactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl {

    private final PostIndexRepository postIndexRepository;
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final MinioServiceImpl minioService;
    private final LanguageDetector languageDetector;
    private final ReactionServiceImpl reactionService;

    @Transactional
    public String indexPost(MultipartFile documentFile, Long postId) throws IOException {
        var postIndex = new PostIndex();
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " not found."));

//        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        postIndex.setTitle(post.getTitle());
        postIndex.setId(post.getId());

        var documentContent = extractDocumentContent(documentFile);
        var detectedLanguage = detectLanguage(documentContent);
        postIndex.setFullContent(post.getContent());

        var serverFilename = minioService.uploadFile(documentFile);
        post.setFileName(serverFilename);
        postIndex.setFileContent(documentContent);

        List<Reaction> reactions =  reactionRepository.findByPostIdAndIsDeletedFalse(postId);
        Long numberOfLikes = reactionService.calculateLikes(reactions);
        postIndex.setNumberOfLikes(numberOfLikes);

        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalse(postId);
        StringBuilder commentContent = new StringBuilder();
        for (Comment comment : comments) {
            commentContent.append(comment.getText()).append(" ");
        }
        postIndex.setCommentContent(commentContent.toString().trim());

        postIndexRepository.save(postIndex);

        return serverFilename;
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to load PDF file content.");
        }
        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }
        return detectedLanguage;
    }
}


